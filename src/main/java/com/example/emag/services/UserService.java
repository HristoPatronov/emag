package com.example.emag.services;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.AddressDAO;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.dto.*;
import com.example.emag.model.pojo.Address;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends AbstractService {

    @Autowired
    private UserDAO userDao;
    @Autowired
    private ProductDAO productDao;
    @Autowired
    private AddressDAO addressDao;
    @Autowired
    private OrderDAO orderDao;

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    private static final String FIRST_NAME_PATTERN = "([A-Z][a-zA-Z]*).{2,45}";
    private static final String LAST_NAME_PATTERN = "([a-zA-z]+([ '-][a-zA-Z]+)*).{2,45}";
    private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,225})";
    private static final String CITY_PATTERN = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private static final String DISTRICT_PATTERN = "^[0-9a-zA-Z\\s,-]+$";
    private static final String STREET_PATTERN = "^[#.0-9a-zA-Z\\s,-]+$";
    private static final String ZIP_PATTERN = "^[0-9\\-\\+]{4,4}$";
    private static final String PHONE_NUMBER_PATTERN = "^[0-9\\-\\+]{6,15}$";

    private static boolean isFirstNameValid(String firstName){
        return firstName.matches(FIRST_NAME_PATTERN);
    }

    private static boolean isLastNameValid(String lastName){
        return lastName.matches(LAST_NAME_PATTERN);
    }

    private static boolean isEMailValid(String email){
        return email.matches(EMAIL_PATTERN);
    }

    private static boolean isPasswordValid(String password){
        return password.matches(PASSWORD_PATTERN);
    }

    private static boolean isCityValid(String city) {
        return city.matches(CITY_PATTERN);
    }

    private static boolean isDistrictValid(String district) {
        return district.matches(DISTRICT_PATTERN);
    }

    private static boolean isStreetValid(String street) {
        return street.matches(STREET_PATTERN);
    }

    private static boolean isZipValid(String ZIP) {
        return ZIP.matches(ZIP_PATTERN);
    }

    private static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    private static void trimSpacesRegisterUser(RegisterUserDTO registerUserDTO) {
        registerUserDTO.setFirst_name(registerUserDTO.getFirst_name().trim());
        registerUserDTO.setLast_name(registerUserDTO.getLast_name().trim());
        registerUserDTO.setEMail(registerUserDTO.getEMail().trim());
        registerUserDTO.setPassword(registerUserDTO.getPassword().trim());
        registerUserDTO.setConfirmPassword(registerUserDTO.getConfirmPassword().trim());
    }

    private static void validateRegisterDTO(RegisterUserDTO registerUserDto) {
        trimSpacesRegisterUser(registerUserDto);
        if (!isFirstNameValid(registerUserDto.getFirst_name())) {
            throw new BadRequestException("First name should not be empty and should contains at least 2 characters " +
                    "and starts with uppercase letter!");
        }
        if (!isLastNameValid(registerUserDto.getLast_name())) {
            throw new BadRequestException("Last name should not be empty and should contains at least 2 characters!");
        }
        if (!isEMailValid(registerUserDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
        if (!isPasswordValid(registerUserDto.getPassword())) {
            throw new BadRequestException("Password should not be empty and should contains at least 6 characters, " +
                    "at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!isPasswordValid(registerUserDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password should not be empty and should contains at " +
                    "least 6 characters, at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!registerUserDto.getPassword().trim().equals(registerUserDto.getConfirmPassword().trim())) {
            throw new BadRequestException("Password and confirm password does not match each other!");
        }
    }

    public UserWithoutPasswordDTO registerUser(RegisterUserDTO userDto, HttpSession session) throws SQLException {
        UserService.validateRegisterDTO(userDto);
        if (userDao.getUserByEmail(userDto.getEMail()) != null) {
            throw new AuthorizationException("User with same e-mail already exist!");
        }
        User user = new User(userDto);
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        userDao.registerUser(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    private static void trimSpacesLogin(LoginUserDTO loginUserDto) {
        loginUserDto.setEMail(loginUserDto.getEMail().trim());
        loginUserDto.setPassword(loginUserDto.getPassword().trim());
    }

    private static void validateLogin(LoginUserDTO loginUserDto) {
        trimSpacesLogin(loginUserDto);
        if (!isEMailValid(loginUserDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
    }

    private static void trimSpacesEditProfile(UserWithoutPasswordDTO userWithoutPasswordDto) {
        userWithoutPasswordDto.setFirst_name(userWithoutPasswordDto.getFirst_name().trim());
        userWithoutPasswordDto.setLast_name(userWithoutPasswordDto.getLast_name().trim());
        userWithoutPasswordDto.setEMail(userWithoutPasswordDto.getEMail().trim());
    }

    private static void validateEditProfile(UserWithoutPasswordDTO userWithoutPasswordDto) {
        trimSpacesEditProfile(userWithoutPasswordDto);
        if (!isFirstNameValid(userWithoutPasswordDto.getFirst_name())) {
            throw new BadRequestException("First name should not be empty and should contains at least 2 characters " +
                    "and starts with uppercase letter!");
        }
        if (!isLastNameValid(userWithoutPasswordDto.getLast_name())) {
            throw new BadRequestException("Last name should not be empty and should contains at least 2 characters!");
        }
        if (!isEMailValid(userWithoutPasswordDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
    }

    private static void trimSpacesChangePassword(UserPasswordDTO userPasswordDto) {
        userPasswordDto.setOldPassword(userPasswordDto.getOldPassword().trim());
        userPasswordDto.setNewPassword(userPasswordDto.getNewPassword().trim());
        userPasswordDto.setConfirmPassword(userPasswordDto.getConfirmPassword().trim());
    }

    private static void validateChangePassword(UserPasswordDTO userPasswordDto) {
        trimSpacesChangePassword(userPasswordDto);
        if (!isPasswordValid(userPasswordDto.getNewPassword())) {
            throw new BadRequestException("New password should not be empty and should contains at least 6 characters, " +
                    "at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!isPasswordValid(userPasswordDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password should not be empty and should contains " +
                    "at least 6 characters, at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
    }

    public UserWithoutPasswordDTO loginUser(LoginUserDTO userDto, HttpSession session) throws SQLException {
        validateLogin(userDto);
        User user = userDao.getUserByEmail(userDto.getEMail());
        if(user == null || userDto.getPassword().isEmpty()) {
            throw new AuthorizationException("Invalid credentials");
        }
        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password!");
        }
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return  new UserWithoutPasswordDTO(user);
    }

    public void logoutUser(HttpSession session) throws SQLException {
        if (session.getAttribute("cart") != null) {
            Map<Product, Integer> products = (Map<Product, Integer>) session.getAttribute("cart");
            productDao.removeReservedQuantities(products);
        }
        session.invalidate();
    }

    public UserWithoutPasswordDTO changeSubscription(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        user.setSubscribed(!user.isSubscribed());
        userDao.changeSubscriptionStatus(user.getId(), user.isSubscribed());
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO getUserInfo(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO updateUserInfo(UserWithoutPasswordDTO userDto, HttpSession session) 
                                                                                        throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        UserService.validateEditProfile(userDto);
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setEMail(userDto.getEMail());
        userDao.updateUserInfo(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO changePassword(UserPasswordDTO userPasswordDto, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        UserService.validateChangePassword(userPasswordDto);
        if (!BCrypt.checkpw(userPasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthorizationException("The old password does not match to the current password");
        }
        if (!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())) {
            throw new BadRequestException("The new password does not match to confirm password");
        }
        String hashedPassword = BCrypt.hashpw(userPasswordDto.getNewPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userDao.changePassword(user.getId(), hashedPassword);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(user);
    }

    public List<AddressDTO> getAllAddresses(HttpSession session) throws SQLException {
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

    public AddressDTO addAddress(Address address, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        validateAddress(address);
        address.setUser(user);
        addressDao.addAddress(user.getId(), address);
        return new AddressDTO(address);
    }

    private static void trimSpacesAddress(Address address) {
        address.setCity(address.getCity().trim());
        address.setDistrict(address.getDistrict().trim());
        address.setStreet(address.getStreet().trim());
        address.setZip(address.getZip().trim());
        address.setPhoneNumber(address.getPhoneNumber().trim());
    }

    private static void validateAddress(Address address) {
        trimSpacesAddress(address);
        if (!isCityValid(address.getCity())) {
            throw new BadRequestException("City should not be empty!");
        }
        if (!isDistrictValid(address.getDistrict())) {
            throw new BadRequestException("District should not be empty!");
        }
        if (!isStreetValid(address.getStreet())) {
            throw new BadRequestException("Street should not be empty!");
        }
        if (!isZipValid(address.getZip())) {
            throw new BadRequestException("ZIP invalid!");
        }
        if (!isPhoneNumberValid(address.getPhoneNumber())) {
            throw new BadRequestException("Phone number invalid!");
        }
    }

    public AddressDTO removeAddress(long addressId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Address address = addressDao.getAddress(addressId);
        checkForAddressExistence(address);
        if (address.getUser().getId() != user.getId()) {
            throw new AuthorizationException("This address does not belong to this user!");
        }
        addressDao.deleteAddress(addressId);
        return new AddressDTO(address);
    }

    private void checkForAddressExistence(Address address) throws SQLException {
        if (address == null) throw new NotFoundException("Address not found");
    }

    public AddressDTO editAddress(Address address, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Address currentAddress = addressDao.getAddress(address.getId());
        checkForAddressExistence(currentAddress);
        validateAddress(address);
        if (currentAddress.getUser().getId() != user.getId()) {
            throw new AuthorizationException("This address does not belong to this user!");
        }
        editAddressInfo(currentAddress, address);
        addressDao.updateAddress(currentAddress, currentAddress.getId());
        return new AddressDTO(address);
    }

    private void editAddressInfo(Address currentAddress, Address updatedAddress) {
        currentAddress.setCity(updatedAddress.getCity());
        currentAddress.setDistrict(updatedAddress.getDistrict());
        currentAddress.setStreet(updatedAddress.getStreet());
        currentAddress.setZip(updatedAddress.getZip());
        currentAddress.setPhoneNumber(updatedAddress.getPhoneNumber());
    }

    public List<Order> getAllUserOrders(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Order> orders = orderDao.getOrdersByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found!");
        }
        return orders;
    }

    public OrderWithProductsDTO getAllProductsByOrder(long orderId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Order> orders = orderDao.getOrdersByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found!");
        }
        if (!checkIfOrderExist(orders, orderId)) {
            throw new NotFoundException("This order does not belong to the user!");
        }
        OrderWithProductsDTO orderWithProductsDTO = new OrderWithProductsDTO();
        for (Order order : orders){
            if (order.getId() == orderId){
                orderWithProductsDTO.setOrder(order);
                break;
            }
        }
        List<ProductWithQuantityDTO> productsWithQuantity = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productDao.getProductsByOrder(orderId).entrySet()){
            ProductWithQuantityDTO productWithQuantityDTO = new ProductWithQuantityDTO();
            productWithQuantityDTO.setProduct(entry.getKey());
            productWithQuantityDTO.setQuantity(entry.getValue());
            productsWithQuantity.add(productWithQuantityDTO);
        }
        orderWithProductsDTO.setProducts(productsWithQuantity);
        return orderWithProductsDTO;
    }

    private boolean checkIfOrderExist(List<Order> orders, long orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return true;
            }
        }
        return false;
    }

    public List<ProductDTO> getFavouriteProducts(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Product> products = productDao.getFavouriteProducts(user.getId());
        if (products.isEmpty()) {
            throw new NotFoundException("No favourite products found!");
        }
        List<ProductDTO> productDto = new ArrayList<>();
        for (Product product : products) {
            if (product.isDeleted()) {
                continue;
            }
            productDto.add(new ProductDTO(product));
        }
        return productDto;
    }

    public ProductDTO addFavouriteProduct(long productId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        if (product.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        List<Product> products = productDao.getFavouriteProducts(user.getId());
        if (products.contains(product)) {
            throw new BadRequestException("Product is already added to favourites!");
        }
        productDao.addFavouriteProduct(user.getId(), productId);
        return new ProductDTO(product);
    }

    public ProductDTO deleteProduct(long productId, HttpSession session) throws SQLException {
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
