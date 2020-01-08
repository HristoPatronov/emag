package com.example.emag.controller;

import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AdminController extends AbstractController{

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private SubCategoryDAO subCategoryDao;

    //add product
    @PostMapping("/admin/products")
    public Product addProduct(@RequestBody Product product, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        checkForAdminRights(user);
        //TODO validate product
        productDao.addProduct(product);
        product.setSubCategory(subCategoryDao.getSubcategoryById(product.getSubCategory().getId()));
        return product;
    }

    //remove product by ID TODO how to delete it from DB
    @DeleteMapping("/admin/products/{productId}")
    public Product removeProduct(@PathVariable(name="productId") long productId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        checkForAdminRights(user);
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        productDao.removeProduct(productId);
        return product;
    }

    //edit product
    @PutMapping("admin/products")
    public Product editProduct(@RequestBody EditProductDTO editProductDTO, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        checkForAdminRights(user);
        //TODO validate editProductDTO
        Product fetchedProduct = productDao.getProductById(editProductDTO.getId());
        checkForProductExistence(fetchedProduct);
        if (editProductDTO.getDiscount() > fetchedProduct.getDiscount()) {
            sendEmailToSubscribedUsers(editProductDTO.getDiscount(), editProductDTO);
        }
        productDao.editProduct(editProductInfo(fetchedProduct, editProductDTO));
        return fetchedProduct;
    }

    private Product editProductInfo(Product currentProduct, EditProductDTO updatedProduct) {
        currentProduct.setName(updatedProduct.getName());
        currentProduct.setDescription(updatedProduct.getDescription());
        currentProduct.setPrice(updatedProduct.getPrice());
        currentProduct.setDiscount(updatedProduct.getDiscount());
        currentProduct.setStock(updatedProduct.getStock());
        return currentProduct;
    }

    //send e-mail to all subscribed users when discount is setted
    private void sendEmailToSubscribedUsers(Integer discount, EditProductDTO product) throws SQLException {
        List<String> emails = userDao.getAllSubscribedUsers();
        double newPrice = product.getPrice()* (1 - (double)product.getDiscount()/100);
        String subject = "Discount";
        String body = String.format("Dear Mr./Ms., \n\nWe have a special discount of %d%s for our Product - %s" +
                "\n\nYou can buy it now only for %.2f lv. instead of regular price of %.2f lv." +
                "\n\nThe product is waiting for you" +
                "\n\nYour Emag Team", discount, "%", product.getName(), newPrice, product.getPrice());
        for (String email : emails) {
            SendEmailController.sendMail(email, subject, body);
        }
    }
}
