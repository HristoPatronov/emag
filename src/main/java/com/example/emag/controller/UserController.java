package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
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
    public void login(HttpSession session){
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
    }

    @GetMapping("/users")
    public UserWithoutPasswordDTO getInfo(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        return new UserWithoutPasswordDTO(user);
    }

//    @GetMapping("/index")
//    public String index() {
//        return "home";
//    }
//
//    @GetMapping("/login")
//    public String login(Model model) {
//        return "login";
//    }
//
//    //login
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password,
//                        Model model,
//                        HttpSession session) {
//
//        if(!UserUtil.isEMailValid(email)){
//            model.addAttribute("error", "E-mail address should be a valid one!");
//            return "login";
//        }
//        try {
//                User user = UserDAO.getInstance().getUserByEmail(email);
//                if (user != null && user.getPassword().equals(password)) {
//                    session.setAttribute("userId", user.getId());
//                    model.addAttribute("msg", "success");
//                    if(user.isAdmin()) {
//                        return "admin";
//                    }
//                    return "home";
//                } else {
//                    model.addAttribute("error", "Invalid credentials!"); //password does not match
//                    return "login";
//                }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        model.addAttribute("error", "Error");
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String register(Model model) {
//        return "register";
//    }
//
//    //register
//    @PostMapping("/register")
//    public String register(@RequestBody RegisterUserDTO userDto, HttpSession session) {
//
//        if(!UserUtil.isFirstNameValid(userDto.getFirst_name())){
//
//            return "register";
//        }
//
//        if(!UserUtil.isLastNameValid(userDto.getLast_name())){
//
//            return "register";
//        }
//
//        if(!UserUtil.isUsernameValid(userDto.getUserName())){
//
//            return "register";
//        }
//
//        if(!UserUtil.isEMailValid(userDto.getEMail())){
//
//            return "register";
//        }
//
//        if(!UserUtil.isPasswordValid(userDto.getPassword())) {
//
//            return "register";
//        }
//
//        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
//
//            return "register";
//        }
//
//        try {
//            if (!UserDAO.getInstance().checkIfUserExists(userDto.getEMail())) {
//                User user = new User(userDto.getFirst_name(), userDto.getLast_name(), userDto.getUserName(), userDto.getPassword(), userDto.getEMail(), userDto.isSubscribed());
//                UserDAO.getInstance().registerUser(user);
//                return "login";
//            } else {
//                return "register";
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return "register";
//    }
//
//    //logout
//    @GetMapping("/logout")
//    public String logout(HttpSession session, HttpServletResponse response, Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return "login";
//        }
//        if (session.getAttribute("cart") != null){
//            Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
//            try {
//                ProductDAO.getInstance().removeReservedQuantities(products);
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//            session.setAttribute("cart", null);
//        }
//        session.setAttribute("userId", null);
//        model.addAttribute("msg", "success");
//        return "login";
//    }
//
//    //unsubscribe
//    @PutMapping("user/subscription")
//    public String unsubscribe(@RequestParam(defaultValue = "false") boolean subscribed,
//                              HttpSession session,
//                              HttpServletResponse response,
//                              Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return "login";
//        }
//        int userId = (int) session.getAttribute("userId");
//        try {
//            UserDAO.getInstance().changeSubscriptionStatus(userId, subscribed);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return "home";
//    }
//
//    //get Personal info
//    @GetMapping("/user/info")
//    public User getUserInfoById(HttpSession session, HttpServletResponse response, Model model){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        int userId = (int) session.getAttribute("userId");
//        User user = null;
//        try {
//            user = UserDAO.getInstance().getUserById(userId);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return user;
//    }
//
//    //update Personal info
//    @PutMapping("/user/info/edit")
//    public void updateUserInfo(@RequestBody User user, HttpSession session, HttpServletResponse response, Model model){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        user.setId((int) session.getAttribute("userId"));
//        try {
//            UserDAO.getInstance().updateUserInfo(user);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
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
//    //get all addresses
//    @GetMapping("/user/address")
//    public List<Address> allAdressesByUserId(HttpSession session, HttpServletResponse response, Model model){
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        int userId = (int) session.getAttribute("userId");
//        List<Address> list = new ArrayList<>();
//        try {
//            list = AddressDAO.getInstance().getAllAddresses(userId);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }
//
//    //add address
//    @PostMapping("/user/address/add")
//    public void addAddress(@RequestBody Address address,
//                           HttpServletResponse response,
//                           HttpSession session,
//                           Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        int userId = (int) session.getAttribute("userId");
//        try {
//            AddressDAO.getInstance().addAddress(userId, address);
//            model.addAttribute("msg", "success");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    //remove address TODO
//    @DeleteMapping("/user/address/delete")
//    public void deleteAddress(@RequestParam int addressId,
//                              HttpServletResponse response,
//                              HttpSession session,
//                              Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        int userId = (int) session.getAttribute("userId");
//        try {
//            List<Address> addresses = AddressDAO.getInstance().getAllAddresses(userId);
//            for (Address address : addresses) {
//                if (addressId == address.getId()) {
//                    AddressDAO.getInstance().deleteAddress(addressId);
//                    model.addAttribute("msg", "success");
//                    return;
//                }
//            }
//            model.addAttribute("error", "no such address");
//            response.setStatus(405);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    //edit address TODO
//    @PutMapping("/user/address/edit")
//    public void editAddress(@RequestBody Address address,
//                            @RequestParam int addressId,
//                            HttpServletResponse response,
//                            HttpSession session,
//                            Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        int userId = (int) session.getAttribute("userId");
//
//        try {
//            List<Address> addresses = AddressDAO.getInstance().getAllAddresses(userId);
//            for (Address currentAddress : addresses) {
//                if (addressId == currentAddress.getId()) {
//                    AddressDAO.getInstance().updateAddress(address, addressId);
//                    model.addAttribute("msg", "success");
//                    return;
//                }
//            }
//            model.addAttribute("error", "no such address");
//            response.setStatus(405);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
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
