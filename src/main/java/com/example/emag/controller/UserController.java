package com.example.emag.controller;

import com.example.emag.model.dto.*;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Order;
import com.example.emag.utils.UserUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class UserController extends AbstractController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    UserUtil userService;

    //register
    @SneakyThrows
    @PostMapping("/users")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto,
                                           HttpSession session) {
        return userService.registerUser(userDto, session);
    }

    //login
    @SneakyThrows
    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDto,
                                        HttpSession session) {
        return userService.loginUser(userDto, session);
    }

    //logout
    @SneakyThrows
    @PostMapping("/users/logout")
    public void logout(HttpSession session) {
        userService.logoutUser(session);
    }

    //change subscription
    @SneakyThrows
    @PutMapping("/users/subscription")
    public UserWithoutPasswordDTO unsubscribe(HttpSession session) {
        return userService.changeSubscription(session);
    }

    //get user info
    @SneakyThrows
    @GetMapping("/users")
    public UserWithoutPasswordDTO getInfo(HttpSession session) {
        return userService.getUserInfo(session);
    }

    //update user info
    @SneakyThrows
    @PutMapping("/users")
    public UserWithoutPasswordDTO updateUserInfo(@RequestBody UserWithoutPasswordDTO userDto,
                                                 HttpSession session) {
        return userService.updateUserInfo(userDto, session);
    }

    //change password
    @SneakyThrows
    @PutMapping("/users/password")
    public UserWithoutPasswordDTO userChangePassword(@RequestBody UserPasswordDTO userPasswordDto,
                                     HttpSession session) {
        return userService.changePassword(userPasswordDto,session);
    }

    //get all addresses
    @SneakyThrows
    @GetMapping("/users/addresses")
    public List<AddressDTO> allAdressesByUserId(HttpSession session) {
        return userService.getAllAddresses(session);
    }

    //add address
    @SneakyThrows
    @PostMapping("/users/addresses")
    public AddressDTO addAddress(@RequestBody Address address, HttpSession session) {
        return userService.addAddress(address, session);
    }

    //remove address
    @SneakyThrows
    @DeleteMapping("/users/addresses/{addressId}")
    public AddressDTO deleteAddress(@PathVariable(name="addressId") long addressId,
                                    HttpSession session) {
        return userService.removeAddress(addressId, session);
    }

    //edit address
    @SneakyThrows
    @PutMapping("/users/addresses")
    public AddressDTO editAddress(@RequestBody Address address,
                            HttpSession session) {
        return userService.editAddress(address, session);
    }

    //get orders
    @SneakyThrows
    @GetMapping("/users/orders")
    public List<Order> allOrders(HttpSession session) {
        return userService.getAllUserOrders(session);
    }

    //get products by order
    @SneakyThrows
    @GetMapping("/users/orders/{orderId}")
    public OrderWithProductsDTO productsByOrder(@PathVariable(name="orderId") long orderId,
                                                 HttpSession session) {
        return userService.getAllProductsByOrder(orderId, session);
    }

    //get favourite products
    @SneakyThrows
    @GetMapping("/users/favourites")
    public List<ProductDTO> getFavouriteProducts(HttpSession session) {

        return userService.getFavouriteProducts(session);
    }

    //add to favourite
    @SneakyThrows
    @PostMapping("/users/favourites/{productId}")
    public ProductDTO addFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        return userService.addFavouriteProduct(productId, session);
    }

    //delete from favourite
    @SneakyThrows
    @DeleteMapping("/users/favourites/{productId}")
    public ProductDTO deleteFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        return userService.deleteProduct(productId, session);
    }
}
