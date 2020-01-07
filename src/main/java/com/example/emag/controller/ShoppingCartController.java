package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.DBManager;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ShoppingCartController extends AbstractController {

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private OrderDAO orderDao;

    //get products in cart
    @GetMapping("/cart")
    public Map<Product, Integer> getProductsFromCart(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new NotFoundException("The cart is empty");
        }
        return products;
    }

    //add product to cart
    @PostMapping("/cart/products/{productId}")
    public void addProductToCart(@PathVariable(name = "productId") long productId,
                                 HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Map<Product, Integer> products = new HashMap<>();
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        }
        Product fetchedProduct = null;
        try {
            fetchedProduct = productDao.getProductById(productId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (fetchedProduct != null && (fetchedProduct.getStock() - fetchedProduct.getReservedQuantity()) > 0) {
            if (!products.containsKey(fetchedProduct)) {
                products.put(fetchedProduct, 1);
                session.setAttribute("cart", products);
                try {
                    productDao.addReservedQuantity(fetchedProduct.getId(), 1);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                throw new BadRequestException("This product already in cart!");
            }
        } else {
            throw new NotFoundException("This product doesn't exist");
        }
    }

    //remove product from cart
    @DeleteMapping("/cart/products/{productId}")
    public void removeProductFromCart(@PathVariable(name = "productId") long productId,
                                      HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        Product fetchedProduct = null;
        try {
            fetchedProduct = productDao.getProductById(productId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (fetchedProduct != null) {
            if (products.containsKey(fetchedProduct)) {
                try {
                    productDao.removeReservedQuantity(fetchedProduct.getId(),
                                                                    products.get(fetchedProduct));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                products.remove(fetchedProduct);
                session.setAttribute("cart", products);
            } else {
                throw new NotFoundException("This product is not in cart!");
            }
        } else {
            throw new NotFoundException("This product doesn't exist");
        }
    }

    //edit quantities in cart
    @PutMapping("/cart/products/{productId}/pieces/{quantity}")
    public void editProductsInCart(@PathVariable(name = "productId") long productId,
                                   @PathVariable(name = "quantity") int quantity,
                                   HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        Product fetchedProduct = null;
        try {
            fetchedProduct = productDao.getProductById(productId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Quantity should be in interval between 1 and 10");
        }
        if (fetchedProduct != null) {
            if (products.containsKey(fetchedProduct)) {
                if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity() +
                        products.get(fetchedProduct)) < quantity) {
                    throw new NotFoundException("The quantity is not available on stock");
                } else {
                    try {
                        if (quantity < products.get(fetchedProduct)) {
                            productDao.removeReservedQuantity(fetchedProduct.getId(),
                                    products.get(fetchedProduct) - quantity);
                        } else {
                            productDao.addReservedQuantity(fetchedProduct.getId(),
                                    quantity - products.get(fetchedProduct));
                        }
                        products.put(fetchedProduct, quantity);
                        session.setAttribute("cart", products);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                throw new NotFoundException("This product is not in cart!");
            }
        } else {
            throw  new NotFoundException("This product doesn't exist");
        }
    }

    //checkout
    @GetMapping("/checkout")
    public void checkout(@RequestParam int paymentType,
                         HttpSession session,
                         HttpServletResponse response,
                         Model model) {
        //TODO check quantity sold and remove product quantity from DB
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            model.addAttribute("error", "The cart is empty");
            return;
        }
        double totalPrice = 0;
        for (Product product : products.keySet()) {
            double discount = (double) product.getDiscount()/ 100;
            totalPrice += product.getPrice()*(1 - discount)*products.get(product);
        }
        int userId = (int) session.getAttribute("userId");
        Order order = new Order(totalPrice, LocalDate.now(), userId, paymentType, 1);
        try {
            DBManager.getInstance().getConnection().setAutoCommit(false);
            int orderId = orderDao.addOrder(order);
            productDao.addProductsToOrder(products, orderId);
            // remove products from stock
            productDao.removeProducts(products);
            //remove reserved quantities
            productDao.removeReservedQuantities(products);
            DBManager.getInstance().getConnection().commit();
            session.setAttribute("cart", null);
        } catch (SQLException e) {
            try {
                DBManager.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                DBManager.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
