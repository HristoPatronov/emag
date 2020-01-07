package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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

    //add product
    @PostMapping("/addProduct")
    public Product addProduct(Product product, Model model, HttpSession session) throws SQLException {
        int id = (Integer) session.getAttribute("userId");
            if (!userDao.isAdminByUserId(id)){
                throw new AuthorizationException("You need to be admin to perform this");
            }
            productDao.addProduct(product);
            model.addAttribute("add", "product added successfully");
        return product;
    }

    //remove product by ID
    @DeleteMapping("/removeProduct")
    public void removeProduct(@RequestParam int idProduct, Model model, HttpSession session) throws SQLException {
        int id = (Integer) session.getAttribute("userId");
            if (!userDao.isAdminByUserId(id)){
                throw new AuthorizationException("You need to be admin to perform this");
            }
            Product product = productDao.getProductById(idProduct);
            if (product == null) {
                throw new NotFoundException("Product doesn't exist");
            }
        productDao.removeProduct(idProduct);
    }

    //set discount
    @PutMapping("/setDiscount")
    public Product setDiscountAtProduct(@RequestParam int Productid, @RequestParam int discount, Model model, HttpSession session) throws SQLException {
        int id = (int) session.getAttribute("userId");
            if (!userDao.isAdminByUserId(id)){
                throw new AuthorizationException("You need to be admin to perform this");
            }
            Product product = productDao.getProductById(Productid);
            if (product == null) {
                throw new NotFoundException("The product doesn't exist");
            }
        productDao.setDiscount(Productid, discount);
        model.addAttribute("discount", "Discount was set successfully");
        //send email to subscribers
        List<String> email = userDao.getAllSubscribedUsers();
        for (String string : email){
            SendEmailController.sendMail(string, "discount",
                    discount + "% discount was setted to product " + product.getName() + " " +
                            product.getDescription() + " available pcs: " + product.getStock());
        }
        return product;
    }

    //update quantity
    @PutMapping("/updateQuantity")
    public Product updateQuantity(@RequestParam int id, @RequestParam int newQuantity, Model model, HttpSession session) throws SQLException {
        int userId = (Integer) session.getAttribute("userId");
            if (!userDao.isAdminByUserId(userId)){
                throw new AuthorizationException("You need to be admin to perform this");
            }
            Product product = productDao.getProductById(id);
            if (product == null) {
                throw new NotFoundException("Product doesn't exist");
            }
        productDao.updateQuantity(id, newQuantity);
        return product;
    }
}
