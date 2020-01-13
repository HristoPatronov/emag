package com.example.emag.controller;

import com.example.emag.model.dto.*;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.emag.utils.UserUtil.SESSION_KEY_LOGGED_USER;

@RestController
public class UserController extends AbstractController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    UserService userService;

    //register
    @SneakyThrows
    @PostMapping("/users")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto,
                                           HttpSession session) {
        User user = userService.registerUser(userDto);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //login
    @SneakyThrows
    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDto,
                                        HttpSession session) {
        User user = userService.loginUser(userDto);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //logout
    @SneakyThrows
    @PostMapping("/users/logout")
    public void logout(HttpSession session) {
        if (session.getAttribute("cart") != null) {
            Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
            userService.logoutUser(products);
        }
        session.invalidate();
    }

    //change subscription
    @SneakyThrows
    @PutMapping("/users/subscription")
    public UserWithoutPasswordDTO unsubscribe(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.changeSubscription(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return userWithoutPasswordDTO;
    }

    //get user info
    @SneakyThrows
    @GetMapping("/users")
    public UserWithoutPasswordDTO getInfo(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.getUserInfo(user);
    }

    //update user info
    @SneakyThrows
    @PutMapping("/users")
    public UserWithoutPasswordDTO updateUserInfo(@RequestBody UserWithoutPasswordDTO userDto,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.updateUserInfo(userDto, user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return userWithoutPasswordDTO;
    }

    //change password
    @SneakyThrows
    @PutMapping("/users/password")
    public UserWithoutPasswordDTO userChangePassword(@RequestBody UserPasswordDTO userPasswordDto,
                                     HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.changePassword(userPasswordDto,user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return userWithoutPasswordDTO;
    }

    //get all addresses
    @SneakyThrows
    @GetMapping("/users/addresses")
    public List<AddressDTO> allAdressesByUserId(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.getAllAddresses(user);
    }

    //add address
    @SneakyThrows
    @PostMapping("/users/addresses")
    public AddressDTO addAddress(@RequestBody Address address, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.addAddress(address, user);
    }

    //remove address
    @SneakyThrows
    @DeleteMapping("/users/addresses/{addressId}")
    public AddressDTO deleteAddress(@PathVariable(name="addressId") long addressId,
                                    HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.removeAddress(addressId, user);
    }

    //edit address
    @SneakyThrows
    @PutMapping("/users/addresses")
    public AddressDTO editAddress(@RequestBody Address address,
                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.editAddress(address, user);
    }

    //get orders
    @SneakyThrows
    @GetMapping("/users/orders")
    public List<Order> allOrders(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.getAllUserOrders(user);
    }

    //get products by order
    @SneakyThrows
    @GetMapping("/users/orders/{orderId}")
    public OrderWithProductsDTO productsByOrder(@PathVariable(name="orderId") long orderId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.getAllProductsByOrder(orderId, user);
    }

    //get favourite products
    @SneakyThrows
    @GetMapping("/users/favourites")
    public List<ProductDTO> getFavouriteProducts(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.getFavouriteProducts(user);
    }

    //add to favourite
    @SneakyThrows
    @PostMapping("/users/favourites/{productId}")
    public ProductDTO addFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.addFavouriteProduct(productId, user);
    }

    //delete from favourite
    @SneakyThrows
    @DeleteMapping("/users/favourites/{productId}")
    public ProductDTO deleteFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return userService.deleteProduct(productId, user);
    }
}
