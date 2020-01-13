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
import com.example.emag.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.emag.utils.UserUtil.SESSION_KEY_LOGGED_USER;

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

    public User registerUser(RegisterUserDTO userDto) throws SQLException {
        UserUtil.validateRegisterDTO(userDto);
        if (userDao.getUserByEmail(userDto.getEMail()) != null) {
            throw new AuthorizationException("User with same e-mail already exist!");
        }
        User user = new User(userDto);
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        userDao.registerUser(user);
        return user;
    }


    public User loginUser(LoginUserDTO userDto) throws SQLException {
        UserUtil.validateLogin(userDto);
        User user = userDao.getUserByEmail(userDto.getEMail());
        if(user == null || userDto.getPassword().isEmpty()) {
            throw new AuthorizationException("Invalid credentials");
        }
        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password!");
        }
        return  user;
    }

    public void logoutUser(Map<Product, Integer> products) throws SQLException {
        if(products != null && !products.isEmpty()) {
            productDao.removeReservedQuantities(products);
        }
    }

    public UserWithoutPasswordDTO changeSubscription(User user) throws SQLException {
        checkForLoggedUser(user);
        user.setSubscribed(!user.isSubscribed());
        userDao.changeSubscriptionStatus(user.getId(), user.isSubscribed());
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO getUserInfo(User user) throws SQLException {

        checkForLoggedUser(user);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO updateUserInfo(UserWithoutPasswordDTO userDto, User user) throws SQLException {
        checkForLoggedUser(user);
        UserUtil.validateEditProfile(userDto);
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setEMail(userDto.getEMail());
        userDao.updateUserInfo(user);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO changePassword(UserPasswordDTO userPasswordDto, User user) throws SQLException {
        checkForLoggedUser(user);
        UserUtil.validateChangePassword(userPasswordDto);
        if (!BCrypt.checkpw(userPasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthorizationException("The old password does not match to the current password");
        }
        if (!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())) {
            throw new BadRequestException("The new password does not match to confirm password");
        }
        String hashedPassword = BCrypt.hashpw(userPasswordDto.getNewPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userDao.changePassword(user.getId(), hashedPassword);
        return new UserWithoutPasswordDTO(user);
    }

    public List<AddressDTO> getAllAddresses(User user) throws SQLException {

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

    public AddressDTO addAddress(Address address, User user) throws SQLException {

        checkForLoggedUser(user);
        UserUtil.validateAddress(address);
        address.setUser(user);
        addressDao.addAddress(user.getId(), address);
        return new AddressDTO(address);
    }



    public AddressDTO removeAddress(long addressId, User user) throws SQLException {

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

    public AddressDTO editAddress(Address address, User user) throws SQLException {

        checkForLoggedUser(user);
        Address currentAddress = addressDao.getAddress(address.getId());
        checkForAddressExistence(currentAddress);
        UserUtil.validateAddress(address);
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

    public List<Order> getAllUserOrders(User user) throws SQLException {

        checkForLoggedUser(user);
        List<Order> orders = orderDao.getOrdersByUserId(user.getId());
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found!");
        }
        return orders;
    }

    public OrderWithProductsDTO getAllProductsByOrder(long orderId, User user) throws SQLException {

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

    public List<ProductDTO> getFavouriteProducts(User user) throws SQLException {

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

    public ProductDTO addFavouriteProduct(long productId, User user) throws SQLException {

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

    public ProductDTO deleteProduct(long productId, User user) throws SQLException {

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
