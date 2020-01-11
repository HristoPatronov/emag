package com.example.emag.controller;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.SpecificationDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.dto.ProductDTO;
import com.example.emag.model.dto.ProductWithSpecsDTO;
import com.example.emag.model.dto.SpecificationWithProductIdDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Specification;
import com.example.emag.model.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController extends AbstractController{

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private SubCategoryDAO subCategoryDao;
    @Autowired
    private SpecificationDAO specificationDao;
    //add product
    @SneakyThrows
    @PostMapping("/admin/products")
    public ProductWithSpecsDTO addProduct(@RequestBody ProductWithSpecsDTO productDto,
                              HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        checkForAdminRights(user);
        //TODO validate product
        productDao.addProduct(productDto.getProduct());
        productDto.getProduct().setSubCategory(subCategoryDao.getSubcategoryById(productDto.getProduct().getSubCategory().getId()));
        List<Specification> specifications = new ArrayList<>();
        for (SpecificationWithProductIdDTO specificationWithProductIdDTO : productDto.getSpecifications()){
            specificationWithProductIdDTO.setProductId(productDto.getProduct().getId());
            specifications.add(new Specification(specificationWithProductIdDTO));
        }
        specificationDao.addSpecification(specifications);
        return productDto;
    }

    //remove product by ID
    @SneakyThrows
    @DeleteMapping("/admin/products/{productId}")
    public Product removeProduct(@PathVariable(name="productId") long productId,
                                 HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        checkForAdminRights(user);
        Product product = productDao.getProductById(productId);
        //checkForProductExistence(product);
        productDao.removeProduct(productId);
        product.setDeleted(true);
        return product;
    }

    //edit product
    @SneakyThrows
    @PutMapping("admin/products")
    public Product editProduct(@RequestBody EditProductDTO editProductDTO,
                               HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        //checkForLoggedUser(user);
        checkForAdminRights(user);
        //TODO validate editProductDTO
        Product fetchedProduct = productDao.getProductById(editProductDTO.getId());
        //checkForProductExistence(fetchedProduct);
        if (fetchedProduct.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        if (editProductDTO.getDiscount() > fetchedProduct.getDiscount()) {
            startMailThread(editProductDTO);
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

    //make a thread for e-mail
    private void startMailThread(EditProductDTO product) {
        Thread email = new Thread(() -> sendEmailToSubscribedUsers(product.getDiscount(), product));
        email.start();
    }

    //send e-mail to all subscribed users when discount is setted
    @SneakyThrows
    private void sendEmailToSubscribedUsers(Integer discount,
                                            EditProductDTO product) {
        List<String> emails = userDao.getAllSubscribedUsers();
        double newPrice = product.getPrice()* (1 - (double)product.getDiscount()/100);
        String subject = "Special offer";
        String body = String.format("Dear Mr./Ms., \n\nWe have a special discount of %d%s for our Product - %s" +
                "\n\nYou can buy it now only for %.2f lv. instead of regular price of %.2f lv." +
                "\n\nThe product is waiting for you" +
                "\n\nYour Emag Team", discount, "%", product.getName(), newPrice, product.getPrice());
        for (String email : emails) {
            SendEmailController.sendMail(email, subject, body);
        }
    }
}
