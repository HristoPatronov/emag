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
import java.sql.SQLException;
import java.time.LocalDate;
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
        return shoppingCartService.getProductsFromCart(session);
    }

    //add product to cart
    @SneakyThrows
    @PostMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO addProductToCart(@PathVariable(name = "productId") long productId,
                                                 HttpSession session) {
        return shoppingCartService.addProductToCart(productId, session);
    }

    //remove product from cart
    @SneakyThrows
    @DeleteMapping("/users/cart/products/{productId}")
    public ProductsWithPriceDTO removeProductFromCart(@PathVariable(name = "productId") long productId,
                                      HttpSession session) {
        return shoppingCartService.removeProductFromCart(productId, session);
    }

    //edit quantities in cart
    @SneakyThrows
    @PutMapping("/users/cart/products/{productId}/pieces/{quantity}")
    public ProductsWithPriceDTO editProductsInCart(@PathVariable(name = "productId") long productId,
                                   @PathVariable(name = "quantity") int quantity,
                                   HttpSession session) {
        return shoppingCartService.editProductsInCart(productId, quantity, session);
    }

    //checkout
    @SneakyThrows
    @PostMapping("/users/cart/checkout/{paymentType}")
    public Order checkout(@PathVariable long paymentType,
                         HttpSession session) {
        return shoppingCartService.checkout(paymentType, session);
    }
}
