package com.example.emag.controller;

import com.example.emag.dao.DBManager;
import com.example.emag.dao.OrderDAO;
import com.example.emag.dao.ProductDAO;
import com.example.emag.model.Order;
import com.example.emag.model.Product;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (fetchedProduct != null && fetchedProduct.getStock() > 0) {
            if (!products.containsKey(fetchedProduct)) {
                products.put(fetchedProduct, 1);
                session.setAttribute("cart", products);
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
                if (fetchedProduct.getStock() < quantity) {
                    model.addAttribute("error", "the quantity is not available on stock");
                    return;
                } else {
                    products.put(fetchedProduct, quantity);
                    session.setAttribute("cart", products);
                }
            } else {
                model.addAttribute("error", "This product is not in cart!");
            }
        } else {
            model.addAttribute("error", "This product doesn't exist");
        }
    }


    //checkout TODO
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
            totalPrice += product.getPrice()*products.get(product);
        }
        int userId = (int) session.getAttribute("userId");
        Order order = new Order(totalPrice, LocalDate.now(), userId, paymentType, 1);
        try {
            //check if products quantities is on stock
            if (ProductDAO.getInstance().checkIfProductsExist(products)) {
                int orderId = OrderDAO.getInstance().addOrder(order);
                ProductDAO.getInstance().addProductsToOrder(products, orderId);
                // remove products from stock
                ProductDAO.getInstance().removeProducts(products);
            } else {
                model.addAttribute("error", "products are not available for purchase!");
                return;
            }
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
