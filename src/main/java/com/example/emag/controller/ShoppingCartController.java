package com.example.emag.controller;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.DBManager;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dto.ProductsWithPriceDTO;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.services.ShoppingCartService;
import com.example.emag.utils.TransformationUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.example.emag.controller.UserController.SESSION_KEY_LOGGED_USER;

@RestController
public class ShoppingCartController extends AbstractController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //get products in cart
    @SneakyThrows
    @GetMapping("/users/cart")
    public ProductsWithPriceDTO getProductsFromCart(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if (session.getAttribute("cart") == null) {
            return null;
        }
        Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
        products = shoppingCartService.getProductsFromCart(user, products);
        return TransformationUtil.transformMap(products);
    }

    //add product to cart
    @SneakyThrows
    @PostMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO addProductToCart(@PathVariable(name = "productId") long productId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Map<Product, Integer> products = new HashMap<>();
        if (session.getAttribute("cart") != null) {
            products = (Map<Product, Integer>) session.getAttribute("cart");
        }
        products = shoppingCartService.addProductToCart(productId, user, products);
        session.setAttribute("cart", products);
        return TransformationUtil.transformMap(products);
    }

    //remove product from cart
    @SneakyThrows
    @DeleteMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO removeProductFromCart(@PathVariable(name = "productId") long productId,
                                      HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        products = shoppingCartService.removeProductFromCart(productId,user, products);
        session.setAttribute("cart", products);
        return TransformationUtil.transformMap(products);
    }

    //edit quantities in cart
    @SneakyThrows
    @PutMapping("/users/cart/products/{productId}/pieces/{quantity}")
    public ProductsWithPriceDTO editProductsInCart(@PathVariable(name = "productId") long productId,
                                   @PathVariable(name = "quantity") int quantity,
                                   HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        products = shoppingCartService.editProductsInCart(productId, quantity, user, products);
        session.setAttribute("cart", products);
        return TransformationUtil.transformMap(products);
    }

    //checkout
    @SneakyThrows
    @PostMapping("/users/cart/checkout/{paymentType}")
    public Order checkout(@PathVariable long paymentType,
                         HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Map<Product, Integer> products;
        if (session.getAttribute("cart") != null){
            products = (Map<Product, Integer>) session.getAttribute("cart");
        } else {
            throw new BadRequestException("The cart is empty");
        }
        Order order = shoppingCartService.checkout(paymentType, user, products);
        session.setAttribute("cart", null);
        return order;
    }
}
