package com.example.emag.services;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.SpecificationDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.dto.ProductWithSpecsDTO;
import com.example.emag.model.dto.SpecificationWithProductIdDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Specification;
import com.example.emag.model.pojo.SubCategory;
import com.example.emag.model.pojo.User;
import com.example.emag.utils.SendEmailUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.emag.services.UserService.SESSION_KEY_LOGGED_USER;
@Service
public class AdminService extends AbstractService {

    @Autowired
    private ProductDAO productDao;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private SubCategoryDAO subCategoryDao;
    @Autowired
    private SpecificationDAO specificationDao;

    private static final String PRODUCT_NAME_PATTERN = "([A-Za-z0-9'\\.\\-\\s\\,]).{5,225}";
    private static final String PRODUCT_DESCRIPTION_PATTERN = "([A-Za-z0-9'\\.\\-\\s\\,]).{10,225}";

    private void checkForAdminRights(User user) throws AuthorizationException {
        if (user == null) throw new AuthorizationException();
        if (!user.isAdmin()) throw new AuthorizationException("You need to be admin to perform this!");
    }

    protected void checkForProductExistence(Product product) throws NotFoundException {
        if (product == null) throw new NotFoundException("Product not found");
    }

    public ProductWithSpecsDTO addProduct(ProductWithSpecsDTO productDto,
                                          HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForAdminRights(user);
        validateProductDto(productDto);
        SubCategory subCategory = subCategoryDao.getSubcategoryById(productDto.getProduct().getSubCategory().getId());
        if (subCategory == null) {
            throw new BadRequestException("No such subcategory found!");
        }
        productDao.addProduct(productDto.getProduct());
        productDto.getProduct().setSubCategory(subCategory);
        List<Specification> specifications = new ArrayList<>();
        for (SpecificationWithProductIdDTO specificationWithProductIdDTO : productDto.getSpecifications()){
            specificationWithProductIdDTO.setProductId(productDto.getProduct().getId());
            specifications.add(new Specification(specificationWithProductIdDTO));
        }
        specificationDao.addSpecification(specifications);
        return productDto;
    }

    private void validateProductDto(ProductWithSpecsDTO productDto) {
        if (!productDto.getProduct().getName().trim().matches(PRODUCT_NAME_PATTERN)) {
            throw new BadRequestException("Invalid name!");
        }
        if (!productDto.getProduct().getDescription().trim().matches(PRODUCT_DESCRIPTION_PATTERN)) {
            throw new BadRequestException("Invalid description!");
        }
        if (productDto.getProduct().getPrice() <= 0) {
            throw new BadRequestException("Invalid price!");
        }
        if (productDto.getProduct().getDiscount() < 0 && productDto.getProduct().getDiscount() >= 100) {
            throw new BadRequestException("Invalid discount!");
        }
        if (productDto.getProduct().getStock() <= 0) {
            throw new BadRequestException("Invalid stock!");
        }
        if (productDto.getProduct().getSubCategory().getId() <= 0) {
            throw new BadRequestException("Invalid subcategory id!");
        }
        //TODO validate specifications
    }

    public Product removeProduct(long productId,
                                 HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForAdminRights(user);
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("No product with this id!");
        }
        checkForProductExistence(product);
        productDao.removeProduct(productId);
        product.setDeleted(true);
        return product;
    }

    public Product editProduct(EditProductDTO editProductDTO,
                               HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForAdminRights(user);
        //TODO validate editProductDTO
        Product fetchedProduct = productDao.getProductById(editProductDTO.getId());
        checkForProductExistence(fetchedProduct);
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
            SendEmailUtil.sendMail(email, subject, body);
        }
    }
}
