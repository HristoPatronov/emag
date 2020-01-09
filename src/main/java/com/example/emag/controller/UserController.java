package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.*;
import com.example.emag.model.dto.*;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class UserController extends AbstractController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private UserDAO userDao;

    @Autowired
    private AddressDAO addressDao;

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private OrderDAO orderDao;

    //register
    @SneakyThrows
    @PostMapping("/users")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto,
                                           HttpSession session) {
        //TODO validate data in userDto
        if (userDao.getUserByEmail(userDto.getEMail()) != null) {
            throw new AuthorizationException("User with same e-mail already exist!");
        }
        User user = new User(userDto);
        user.setPassword(encryptPassword(userDto.getPassword()));
        userDao.registerUser(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    private String encryptPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    //login
    @SneakyThrows
    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDto,
                                        HttpSession session) {
        //TODO validate data in userDto
        User user = userDao.getUserByEmail(userDto.getEMail());
        if(user == null || userDto.getPassword().isEmpty()) {
            throw new AuthorizationException("Invalid credentials");
        }
        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password!");
        }
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //logout
    @SneakyThrows
    @PostMapping("/users/logout")
    public void logout(HttpSession session) {
        if (session.getAttribute("cart") != null) {
            Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
            productDao.removeReservedQuantities(products);
        }
        session.invalidate();
    }

    //change subscription
    @SneakyThrows
    @PutMapping("/users/subscription")
    public UserWithoutPasswordDTO unsubscribe(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        user.setSubscribed(!user.isSubscribed());
        userDao.changeSubscriptionStatus(user.getId(), user.isSubscribed());
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //get user info
    @SneakyThrows
    @GetMapping("/users")
    public UserWithoutPasswordDTO getInfo(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        return new UserWithoutPasswordDTO(user);
    }

    //update user info
    @SneakyThrows
    @PutMapping("/users")
    public UserWithoutPasswordDTO updateUserInfo(@RequestBody UserWithoutPasswordDTO userDto,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        //TODO validate input data
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setEMail(userDto.getEMail());
        userDao.updateUserInfo(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //change password
    @SneakyThrows
    @PutMapping("/users/password")
    public UserWithoutPasswordDTO userChangePassword(@RequestBody UserPasswordDTO userPasswordDto,
                                     HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        if (!BCrypt.checkpw(userPasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthorizationException("The old password does not match to the current password");
        }
        if (!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())) {
            throw new BadRequestException("The new password does not match to confirm password");
        }
        String hashedPassword = encryptPassword(userPasswordDto.getNewPassword());
        user.setPassword(hashedPassword);
        userDao.changePassword(user.getId(), hashedPassword);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    //get all addresses
    @SneakyThrows
    @GetMapping("/users/addresses")
    public List<AddressDTO> allAdressesByUserId(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Address> addresses = addressDao.getAllAddresses(user.getId());
        if (addresses == null) {
            throw new NotFoundException("There are no addresses found!");
        }
        List<AddressDTO> addressesDto = new ArrayList<>();
        for (Address address : addresses) {
            addressesDto.add(new AddressDTO(address));
        }
        return addressesDto;
    }

    //add address
    @SneakyThrows
    @PostMapping("/users/addresses")
    public AddressDTO addAddress(@RequestBody Address address, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        //TODO validete input info
        address.setUser(user);
        addressDao.addAddress(user.getId(), address);
        return new AddressDTO(address);
    }

    //remove address
    @SneakyThrows
    @DeleteMapping("/users/addresses/{addressId}")
    public AddressDTO deleteAddress(@PathVariable(name="addressId") long addressId,
                                    HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Address address = addressDao.getAddress(addressId);
        checkForAddressExistence(address);
        List<Address> addresses = addressDao.getAllAddresses(user.getId());
        if (!addresses.contains(address)) {
            throw new AuthorizationException("This address does not belong to this user!");
        }
        addressDao.deleteAddress(addressId);
        return new AddressDTO(address);
    }

    //edit address
    @SneakyThrows
    @PutMapping("/users/addresses")
    public AddressDTO editAddress(@RequestBody Address address,
                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Address currentAddress = addressDao.getAddress(address.getId());
        checkForAddressExistence(currentAddress);
        //TODO validate input address
        if (currentAddress.getUser().getId() != user.getId()) {
            throw new AuthorizationException("This address does not belong to this user!");
        }
        editAddressInfo(currentAddress, address);
        addressDao.updateAddress(currentAddress, currentAddress.getId());
        return new AddressDTO(currentAddress);
    }

    private void editAddressInfo(Address currentAddress, Address updatedAddress) {
        currentAddress.setCity(updatedAddress.getCity());
        currentAddress.setDistrict(updatedAddress.getDistrict());
        currentAddress.setStreet(updatedAddress.getStreet());
        currentAddress.setZip(updatedAddress.getZip());
        currentAddress.setPhoneNumber(updatedAddress.getPhoneNumber());
    }

    //get orders
    @SneakyThrows
    @GetMapping("/users/orders")
    public List<Order> allOrders(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Order> orders = orderDao.getOrdersByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found!");
        }
        return orders;
    }

    //get products by order
    @SneakyThrows
    @GetMapping("/users/orders/{orderId}")
    public Map<Product, Integer> productsByOrder(@PathVariable(name="orderId") long orderId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Order> orders = orderDao.getOrdersByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found!");
        }
        if (!checkIfOrderExist(orders, orderId)) {
            throw new NotFoundException("This order does not belong to the user!");
        }
        return productDao.getProductsByOrder(orderId);
    }

    private boolean checkIfOrderExist(List<Order> orders, long orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return true;
            }
        }
        return false;
    }

    //get favourite products
    @SneakyThrows
    @GetMapping("/users/favourites")
    public List<ProductDTO> getFavouriteProducts(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Product> products = productDao.getFavouriteProducts(user.getId());
        if (products.isEmpty()) {
            throw new NotFoundException("No favourite products found!");
        }
        List<ProductDTO> productDto = new ArrayList<>();
        for (Product product : products) {
            if (!product.isDeleted()) {
                continue;
            }
            productDto.add(new ProductDTO(product));
        }
        return productDto;
    }

    //add to favourite
    @SneakyThrows
    @PostMapping("/users/favourites/{productId}")
    public ProductDTO addFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        if (!product.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        List<Product> products = productDao.getFavouriteProducts(user.getId());
        if (products.contains(product)) {
            throw new BadRequestException("Product is already added to favourites!");
        }
        productDao.addFavouriteProduct(user.getId(), productId);
        return new ProductDTO(product);
    }

    //delete from favourite
    @SneakyThrows
    @DeleteMapping("/users/favourites/{productId}")
    public ProductDTO deleteFavouriteProduct(@PathVariable(name="productId") long productId,
                                            HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Product> products = productDao.getFavouriteProducts(user.getId());
        if (products.isEmpty()) {
            throw new NotFoundException("No favourite products found!");
        }
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        if (!products.contains(product)) {
            throw new BadRequestException("This product is not added to favourite products!");
        }
        productDao.removeFavouriteProduct(user.getId(), productId);
        return new ProductDTO(product);
    }
}
