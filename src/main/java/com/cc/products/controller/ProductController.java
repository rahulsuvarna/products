package com.cc.products.controller;

import com.cc.products.dto.Product;
import com.cc.products.service.ProductService;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Controller for Product
 */
@RestController
public class ProductController {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * method to retrieve products
     * @param categoryId the id of category from within which products are to be retrieved
     * @param labelType the label Type (Optional, default ShowWasNow)
     * @param reduced flag to indicate if price reduced only products should be returned (Optional, default true)
     * @return list of products, empty if none could be retrieved
     */
    @GetMapping("/categories/{categoryId}/products")
    public Single<List<Product>> getProducts(@PathVariable String categoryId,
                                     @RequestParam(required = false, defaultValue = "ShowWasNow") String labelType,
                                     @RequestParam(required = false, defaultValue = "true") boolean reduced) {
        LOGGER.debug("getProducts() for categoryId: {} and labelType: {}", categoryId, labelType);
        return productService.getProductsForCategory(categoryId, labelType, reduced);
    }

}
