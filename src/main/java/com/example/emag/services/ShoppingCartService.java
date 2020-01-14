package com.example.emag.services;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.DBManager;
import com.example.emag.model.dao.OrderDAO;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dto.ProductsWithPriceDTO;
import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.utils.TransformationUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.example.emag.controller.UserController.SESSION_KEY_LOGGED_USER;

@Service
public class ShoppingCartService extends AbstractService{

    public static final int ORDER_STATUS_NEW = 1;
    @Autowired
    private ProductDAO productDao;

    @Autowired
    private OrderDAO orderDao;

    //get products in cart
    @SneakyThrows
    public Map<Product, Integer> getProductsFromCart(User user, Map<Product, Integer> products) {

        checkForLoggedUser(user);
        return products;
    }

    //add product to cart
    @SneakyThrows
    public Map<Product, Integer> addProductToCart(long productId, User user, Map<Product, Integer> products) {

        checkForLoggedUser(user);
        Product fetchedProduct = productDao.getProductById(productId);
        checkForProductExistence(fetchedProduct);
        if (fetchedProduct.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity()) > 0) {
            if (!products.containsKey(fetchedProduct)) {
                products.put(fetchedProduct, 1);
                productDao.addReservedQuantity(fetchedProduct.getId(), 1);
            } else {
                throw new BadRequestException("This product already in cart!");
            }
        } else {
            throw new NotFoundException("The product is ot available!");
        }
        return products;
    }

    //remove product from cart
    @SneakyThrows
    public Map<Product, Integer> removeProductFromCart(long productId, User user, Map<Product, Integer> products) {

        checkForLoggedUser(user);
        Product fetchedProduct = productDao.getProductById(productId);
        checkForProductExistence(fetchedProduct);
        if (products.containsKey(fetchedProduct)) {
            try {
                productDao.removeReservedQuantity(fetchedProduct.getId(),
                        products.get(fetchedProduct));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            products.remove(fetchedProduct);
        } else {
            throw new NotFoundException("This product is not in cart!");
        }
        return products;
    }

    //edit quantities in cart
    @SneakyThrows
    @PutMapping("/users/cart/products/{productId}/pieces/{quantity}")
    public Map<Product, Integer> editProductsInCart(long productId, int quantity, User user, Map<Product, Integer> products) {

        checkForLoggedUser(user);
        Product fetchedProduct = productDao.getProductById(productId);
        checkForProductExistence(fetchedProduct);
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Quantity should be in interval between 1 and 10");
        }
        if (products.containsKey(fetchedProduct)) {
            if ((fetchedProduct.getStock() - fetchedProduct.getReservedQuantity() +
                products.get(fetchedProduct)) < quantity) {
                throw new NotFoundException("The quantity is not available on stock");
            } else {
                if (quantity < products.get(fetchedProduct)) {
                    productDao.removeReservedQuantity(fetchedProduct.getId(),
                            products.get(fetchedProduct) - quantity);
                } else {
                    productDao.addReservedQuantity(fetchedProduct.getId(),
                            quantity - products.get(fetchedProduct));
                }
                products.put(fetchedProduct, quantity);

            }
        } else {
            throw new NotFoundException("This product is not in cart!");
        }
        return products;
    }

    //checkout
    @SneakyThrows
    public Order checkout(long paymentTypeId, User user, Map<Product, Integer> products) {

        checkForLoggedUser(user);
        Order.PaymentType paymentType = orderDao.getPaymentTypeById(paymentTypeId);
        if (paymentType == null) {
            throw new NotFoundException("Payment type not found!");
        }
        double totalPrice = TransformationUtil.transformMap(products).getTotalPrice();
        Order order = new Order(totalPrice, LocalDate.now(), user, paymentType,
                new Order.Status(ORDER_STATUS_NEW));
        order = orderDao.addOrder(order, products);

        return order;
    }
}
