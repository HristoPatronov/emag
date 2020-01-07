package com.example.emag.controller;

import com.example.emag.model.dao.DBManager;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ShoppingCartController {

    //get products in cart
    @GetMapping("/cart")
    public Map<Product, Integer> getProductsFromCart(HttpSession session,
                                             HttpServletResponse response,
                                             Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            model.addAttribute("error", "no products in cart");
            return null;
        }
        return products;
    }

    //add product to cart
    @PostMapping("/cart")
    public void addProductToCart(@RequestParam int id,
                                 HttpSession session,
                                 HttpServletResponse response,
                                 Model model){  //id = productId
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        Map<Product, Integer> products = new HashMap<>();
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        }
        Product fetchedProduct = null;
        try {
            fetchedProduct = ProductDAO.getInstance().getProductById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (fetchedProduct != null && (fetchedProduct.getStock() - fetchedProduct.getReservedQuantity()) > 0) {
            if (!products.containsKey(fetchedProduct)) {
                products.put(fetchedProduct, 1);
                session.setAttribute("cart", products);
                try {
                    ProductDAO.getInstance().addReservedQuantity(fetchedProduct.getId(), 1);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                model.addAttribute("error", "This product already in cart!");
            }
        } else {
            model.addAttribute("error", "This product doesn't exist");
        }
    }

    //remove product from cart
    @DeleteMapping("/cart")
    public void removeProductFromCart(@RequestParam int id,
                                      HttpSession session,
                                      HttpServletResponse response,
                                      Model model){   //id = productId
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
        Product fetchedProduct = null;
        try {
            fetchedProduct = ProductDAO.getInstance().getProductById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (fetchedProduct != null) {
            if (products.containsKey(fetchedProduct)) {
                try {
                    ProductDAO.getInstance().removeReservedQuantity(fetchedProduct.getId(),
                                                                    products.get(fetchedProduct));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                products.remove(fetchedProduct);
                session.setAttribute("cart", products);
            } else {
                model.addAttribute("error", "This product is not in cart!");
            }
        } else {
            model.addAttribute("error", "This product doesn't exist");
        }
    }

    //edit quantities in cart
    @PutMapping("/cart")
    public void editProductsInCart(@RequestParam int id,
                                   @RequestParam int quantity,
                                   HttpSession session,
                                   HttpServletResponse response,
                                   Model model) {
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
        Product fetchedProduct = null;
        try {
            fetchedProduct = ProductDAO.getInstance().getProductById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (quantity < 1 || quantity > 10) {
            model.addAttribute("error", "quantity should be in interval between 1 and 10");
            return;
        }
        if (fetchedProduct != null) {
            if (products.containsKey(fetchedProduct)) {
                if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity() +
                        products.get(fetchedProduct)) < quantity) {
                    model.addAttribute("error", "the quantity is not available on stock");
                    return;
                } else {
                    try {
                        if (quantity < products.get(fetchedProduct)) {
                            ProductDAO.getInstance().removeReservedQuantity(fetchedProduct.getId(),
                                    products.get(fetchedProduct) - quantity);
                        } else {
                            ProductDAO.getInstance().addReservedQuantity(fetchedProduct.getId(),
                                    quantity - products.get(fetchedProduct));
                        }
                        products.put(fetchedProduct, quantity);
                        session.setAttribute("cart", products);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                model.addAttribute("error", "This product is not in cart!");
            }
        } else {
            model.addAttribute("error", "This product doesn't exist");
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
            int orderId = OrderDAO.getInstance().addOrder(order);
            ProductDAO.getInstance().addProductsToOrder(products, orderId);
            // remove products from stock
            ProductDAO.getInstance().removeProducts(products);
            //remove reserved quantities
            ProductDAO.getInstance().removeReservedQuantities(products);
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
