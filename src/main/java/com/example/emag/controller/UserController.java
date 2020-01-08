package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.*;
import com.example.emag.model.dto.LoginUserDTO;
import com.example.emag.model.dto.RegisterUserDTO;
import com.example.emag.model.dto.UserWithoutPasswordDTO;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController extends AbstractController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private UserDAO userDao;
    @Autowired
    private AddressDAO addressDao;
    @Autowired
    private ProductDAO productDao;

    @PostMapping("/users")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto,
                                           HttpSession session) throws SQLException{
        //TODO validate data in userDto
        User user = new User(userDto);
        userDao.registerUser(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDto,
                                        HttpSession session) throws SQLException {
        //TODO validate data in userDto
        User user = userDao.getUserByEmail(userDto.getEMail());
        if(user == null){
            throw new AuthorizationException("Invalid credentials");
        }
        if(passwordValid(user, userDto)) {
            session.setAttribute(SESSION_KEY_LOGGED_USER, user);
            return new UserWithoutPasswordDTO(user);
        }
        else{
            throw new AuthorizationException("Invalid credentials");
        }
    }

    private boolean passwordValid(User user, LoginUserDTO userDTO) {
        if (user.getPassword().equals(userDTO.getPassword())) {
            return true;
        }
        return false;
    }

    @PostMapping("/users/logout")
    public void logout(HttpSession session) throws SQLException{
        if (session.getAttribute("cart") == null) {
            throw new NotFoundException("The cart is empty");
        }
        Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
        productDao.removeReservedQuantities(products);
        session.invalidate();
    }

    @PutMapping("/users/subscription")
    public void unsubscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        user.setSubscribed(!user.isSubscribed());
        userDao.changeSubscriptionStatus(user.getId(), user.isSubscribed());
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        //TODO return UserWithoutPasswordDTO
    }

    @GetMapping("/users")
    public UserWithoutPasswordDTO getInfo(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        return new UserWithoutPasswordDTO(user);
    }

    //update Personal info
    @PutMapping("/users")
    public UserWithoutPasswordDTO updateUserInfo(@RequestBody UserWithoutPasswordDTO userDto,
                                                 HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if (user == null) {
            throw new AuthorizationException();
        }
        //TODO validate input data
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setEMail(userDto.getEMail());
        userDao.updateUserInfo(user);
        return new UserWithoutPasswordDTO(user);
    }
//
//    @PutMapping("/user/changePassword")
//    public String userChangePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session, Model model, HttpServletResponse response){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return "login";
//        }
//        int userId = (int) session.getAttribute("userId");
//        try {
//            User user = UserDAO.getInstance().getUserById(userId);
//            if (user.getPassword().equals(oldPassword)){
//                UserDAO.getInstance().changePassword(userId, newPassword);
//                return "user/info";
//            } else {
//                model.addAttribute("error", " old password does not match");
//                return "user/changePassword";
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return "home";
//    }
//
   //get all addresses
    @GetMapping("/users/addresses")
    public List<Address> allAdressesByUserId(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        return addressDao.getAllAddresses(user.getId());
    }

    //add address
    @PostMapping("/users/addresses")
    public Address addAddress(@RequestBody Address address, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        //TODO validete input info
        address.setUser(user);
        addressDao.addAddress(user.getId(), address);
        return address;
    }

    //remove address
    @DeleteMapping("/users/addresses/{addressId}")
    public Address deleteAddress(@PathVariable(name="addressId") long addressId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Address address = addressDao.getAddress(addressId);
        if (address == null) {
            throw new NotFoundException("Address not found!");
        }
        addressDao.deleteAddress(addressId);
        return address;
    }

    //edit address
    @PutMapping("/users/addresses/{addressId}")
    public Address editAddress(@PathVariable(name="addressId") long addressId,
                            @RequestBody Address address,
                            HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Address currentAddress = addressDao.getAddress(addressId);
        if (address == null) {
            throw new NotFoundException("Address not found!");
        }
        //TODO validate input address
        if (currentAddress.getUser().getId() != user.getId()) {
            throw new AuthorizationException("This address belongs to other user!");
        }
        currentAddress.setCity(address.getCity());
        currentAddress.setDistrict(address.getDistrict());
        currentAddress.setStreet(address.getStreet());
        currentAddress.setZip(address.getZip());
        currentAddress.setPhoneNumber(address.getPhoneNumber());
        addressDao.updateAddress(currentAddress, currentAddress.getId());
        return currentAddress;
    }
//
//    //get orders
//    @GetMapping("/user/order")
//    public List<Order> allOrders(HttpSession session, HttpServletResponse response, Model model){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        int userId = (int) session.getAttribute("userId");
//        List<Order> list = new ArrayList<>();
//        try {
//            list = OrderDAO.getInstance().getOrdersByUserId(userId);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }
//
//    //get products by order
//    @GetMapping("/user/order/products")
//    public Map<Product, Integer> productsByOrder(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        Map<Product, Integer> products = new HashMap<>();
//        try {
//            products = ProductDAO.getInstance().getProductsByOrder(id);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return products;
//    }
//
//
//    //tested OK
//    //get favourite products
//    @GetMapping("/user/favouriteProducts")
//    public List<Product> getFavouriteProducts(HttpSession session, HttpServletResponse response, Model model) {
//        if (session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        List<Product> products = new ArrayList<>();
//        try {
//            products = ProductDAO.getInstance().getFavouriteProducts((Integer) session.getAttribute("userId"));
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return products;
//    }
//
//    //tested OK
//    //add to favourite
//    @PostMapping("/user/favouriteProducts/add")
//    public void addProductToFavourite(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model){   //id = productId
//        if (session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        List<Product> products;
//        int userId = (int) session.getAttribute("userId");
//        Product product = null;
//        try {
//            products = ProductDAO.getInstance().getFavouriteProducts(userId);
//            product = ProductDAO.getInstance().getProductById(id);
//            for (Product p : products) {
//                if (p.getId() == (product.getId())) {
//                    model.addAttribute("error", "this product already present in favourite list");
//                    return;
//                }
//            }
//            if (product != null){
//                ProductDAO.getInstance().addFavouriteProduct(userId, product.getId());
//            } else {
//                model.addAttribute("error", "this item doesn't exists");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    //tested OK
//    //remove favourite product
//    @DeleteMapping("/user/favouriteProducts/delete")
//    public void deleteFavouriteProduct(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model) {  //productId
//        if (session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        List<Product> products;
//        int userId = (int) session.getAttribute("userId");
//        try {
//            products = ProductDAO.getInstance().getFavouriteProducts(userId);
//            for (Product p : products) {
//                if(p.getId() == id) {
//                    ProductDAO.getInstance().removeFavouriteProduct(userId, id);
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
