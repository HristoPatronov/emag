package com.example.emag.services;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.ReviewDAO;
import com.example.emag.model.dao.SpecificationDAO;
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

    protected void checkForProductExistence(Product product) throws SQLException {
        if (product == null) throw new NotFoundException("Product not found");
    }

    public ProductWithAllDTO getProduct(long productId,
                                        HttpSession session) throws SQLException {
        Product product = productDao.getProductById(productId);
        List<Specification> specifications = specificationDAO.getSpecificationsForProduct(productId);
        List<Review> reviews = reviewDAO.getAllReviewsForProduct(productId);
        ProductWithAllDTO productWithAllDTO = new ProductWithAllDTO(product, specifications, reviews);
        checkForProductExistence(product);
        if (product.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        return productWithAllDTO;
    }

    public List<Product> productsFromSearch(ProductFilteringDTO productFilteringDTO, HttpSession session) throws SQLException {
        if (productFilteringDTO.getMinPrice() < 0
            || productFilteringDTO.getMaxPrice() < 0
            || productFilteringDTO.getSubCategoryId() < 1){
            throw new BadRequestException("You cannot search for products with wrong input information");
        }
        if (productFilteringDTO.getSubCategoryId() == null && productFilteringDTO.getSearchText() == null){
            throw new BadRequestException("You cannot search for products without sub category and search text");
        }
        List<Product> currentProducts = new ArrayList<>();
        if (productFilteringDTO.getSubCategoryId() != null){
            currentProducts = productDao.getProductsBySubCategory(productFilteringDTO.getSubCategoryId(),
                    productFilteringDTO.getColumn(),
                    productFilteringDTO.getMinPrice(),
                    productFilteringDTO.getMaxPrice(),
                    productFilteringDTO.getOrderBy());
        } else if (productFilteringDTO.getSearchText() != null && !productFilteringDTO.getSearchText().trim().isEmpty()){
            currentProducts = productDao.getProductsFromSearch( productFilteringDTO.getSearchText(),
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
