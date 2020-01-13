package com.example.emag.services;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.ReviewDAO;
import com.example.emag.model.dao.SpecificationDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dto.ProductFilteringDTO;
import com.example.emag.model.dto.ProductWithAllDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Review;
import com.example.emag.model.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ProductService extends AbstractService {

    @Autowired
    private ProductDAO productDao;
    @Autowired
    private SpecificationDAO specificationDAO;
    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private SubCategoryDAO subCategoryDao;

    protected void checkForProductExistence(Product product) throws NotFoundException {
        if (product == null) throw new NotFoundException("Product not found");
    }

    public ProductWithAllDTO getProduct(long productId,
                                        HttpSession session) throws SQLException {
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        if (product.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        List<Specification> specifications = specificationDAO.getSpecificationsForProduct(productId);
        List<Review> reviews = reviewDAO.getAllReviewsForProduct(productId);
        ProductWithAllDTO productWithAllDTO = new ProductWithAllDTO(product, specifications, reviews);
        checkForProductExistence(product);
        return productWithAllDTO;
    }

    public List<Product> productsFromSearch(ProductFilteringDTO productFilteringDTO, HttpSession session) throws SQLException {
        if (productFilteringDTO.getSubCategoryId() != null && productFilteringDTO.getSubCategoryId() < 1) {
            throw new BadRequestException("Invalid input for subcategory!");
        }
        if (productFilteringDTO.getMinPrice() != null && productFilteringDTO.getMinPrice() < 1) {
            throw new BadRequestException("Invalid input for min price!");
        }
        if (productFilteringDTO.getMaxPrice() != null && productFilteringDTO.getMaxPrice() < 1) {
            throw new BadRequestException("Invalid input for max price!");
        }
        if (productFilteringDTO.getSearchText() != null && productFilteringDTO.getSearchText().trim().isEmpty()) {
            throw new BadRequestException("Invalid input search");
        }
        if (productFilteringDTO.getOrderBy() != null &&
                (!productFilteringDTO.getOrderBy().equals("ASC") && !productFilteringDTO.getOrderBy().equals("DESC"))) {
            throw new BadRequestException("Invalid order by field");
        }
        if (productFilteringDTO.getSubCategoryId() == null && productFilteringDTO.getSearchText() == null){
            throw new BadRequestException("You cannot search for products without sub category and search text");
        }
        if (productFilteringDTO.getColumn() != null && !productFilteringDTO.getColumn().equals("price")) {
            throw new BadRequestException("Wrong input for field - column!");
        }
        if (subCategoryDao.getSubcategoryById(productFilteringDTO.getSubCategoryId()) == null) {
            throw new NotFoundException("No such subcategory!");
        }
        List<Product> currentProducts;
        if (productFilteringDTO.getSubCategoryId() != null){
            currentProducts = productDao.getProductsBySubCategory(productFilteringDTO.getSubCategoryId(),
                    productFilteringDTO.getColumn(),
                    productFilteringDTO.getMinPrice(),
                    productFilteringDTO.getMaxPrice(),
                    productFilteringDTO.getOrderBy());
        } else if (productFilteringDTO.getSearchText() != null && !productFilteringDTO.getSearchText().trim().isEmpty()){
            currentProducts = productDao.getProductsFromSearch(productFilteringDTO.getSearchText().trim().toLowerCase(),
                    productFilteringDTO.getColumn(),
                    productFilteringDTO.getMinPrice(),
                    productFilteringDTO.getMaxPrice(),
                    productFilteringDTO.getOrderBy());
        } else {
            throw new BadRequestException("Can't search without good input information");
        }
        return currentProducts;
    }
}
