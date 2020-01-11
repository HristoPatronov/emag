package com.example.emag.controller;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.DBManager;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dto.ProductWithQuantityDTO;
import com.example.emag.model.dto.ProductsWithPriceDTO;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.utils.TransformationUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShoppingCartController extends AbstractController {

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private OrderDAO orderDao;

    //get products in cart
    @SneakyThrows
    @GetMapping("/users/cart")
    public ProductsWithPriceDTO getProductsFromCart(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        if (session.getAttribute("cart") == null) {
            return null;
        }
        return TransformationUtil.transformMap((Map<Product, Integer>) session.getAttribute("cart"));
    }

    //add product to cart
    @SneakyThrows
    @PostMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO addProductToCart(@PathVariable(name = "productId") long productId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        Map<Product, Integer> products = new HashMap<>();
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        }
        Product fetchedProduct = productDao.getProductById(productId);
        if (fetchedProduct == null) {
            throw new NotFoundException("No such product found");
        }
        if (fetchedProduct.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity()) > 0) {
            if (!products.containsKey(fetchedProduct)) {
                products.put(fetchedProduct, 1);
                session.setAttribute("cart", products);
                productDao.addReservedQuantity(fetchedProduct.getId(), 1);
            } else {
                throw new BadRequestException("This product already in cart!");
            }
        } else {
            throw new NotFoundException("The product is ot available!");
        }

        return TransformationUtil.transformMap(products);
    }

    //remove product from cart
    @SneakyThrows
    @DeleteMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO removeProductFromCart(@PathVariable(name = "productId") long productId,
                                      HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        Product fetchedProduct = productDao.getProductById(productId);
        if (fetchedProduct == null) {
            throw new NotFoundException("No such product found");
        }
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
        return TransformationUtil.transformMap(products);
    }

    //edit quantities in cart
    @SneakyThrows
    @PutMapping("/users/cart/products/{productId}/pieces/{quantity}")
    public ProductsWithPriceDTO editProductsInCart(@PathVariable(name = "productId") long productId,
                                   @PathVariable(name = "quantity") int quantity,
                                   HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        Product fetchedProduct = productDao.getProductById(productId);
        if (fetchedProduct == null) {
            throw new NotFoundException("No such product found");
        }
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Quantity should be in interval between 1 and 10");
        }
        if (products.containsKey(fetchedProduct)) {
            if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity() +
                    products.get(fetchedProduct)) < quantity) {
                throw new NotFoundException("The quantity is not available on stock");
            } else {
                if (quantity < products.get(fetchedProduct)) {
                    productDao.removeReservedQuantity(fetchedProduct.getId(),
                            products.get(fetchedProduct) - quantity);
                } else {
                    productDao.addReservedQuantity(fetchedProduct.getId(),
                            quantity - products.get(fetchedProduct));
                }
                products.put(fetchedProduct, quantity);
                session.setAttribute("cart", products);
            }
        } else {
            throw new NotFoundException("This product is not in cart!");
        }
        return TransformationUtil.transformMap(products);
    }

    //checkout
    @SneakyThrows
    @PostMapping("/users/cart/checkout/{paymentType}")
    public Order checkout(@PathVariable long paymentType,
                         HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        double totalPrice = 0;
        for (Product product : products.keySet()) {
            double discount = (double) product.getDiscount()/ 100;
            totalPrice += product.getPrice()*(1 - discount)*products.get(product);
        }
        Order order = new Order(totalPrice, LocalDate.now(), user, new Order.PaymentType(paymentType),
                new Order.Status(1));
        try {
            DBManager.getInstance().getConnection().setAutoCommit(false);
            orderDao.addOrder(order);
            productDao.addProductsToOrder(products, order.getId());
            productDao.removeProducts(products);
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
        order.setPaymentType(orderDao.getPaymentTypeById(paymentType));
        order.setStatus(orderDao.getStatusById(order.getStatus().getId()));
        return order;
    }
}
