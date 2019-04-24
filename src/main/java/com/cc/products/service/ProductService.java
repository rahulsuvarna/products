package com.cc.products.service;

import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.gateway.IJLGateway;
import com.cc.products.dto.ColorSwatches;
import com.cc.products.dto.Product;
import com.cc.products.service.utils.ColorSwatchTransformer;
import com.cc.products.service.utils.ProductUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Product Service class
 */
@Component
public class ProductService implements IProductService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final IJLGateway jlGateway;

    @Autowired
    public ProductService(IJLGateway jlGateway) {
        this.jlGateway = jlGateway;
    }

    public List<Product> getProductsForCategory(final @NotNull String categoryId, final String labelType, boolean reduced) {

        LOGGER.debug("getProductsForCategory() with categoryIs: {} and labelType: {}", categoryId, labelType);
        List<Product> lOfPriceReducedProducts = jlGateway.getAllProductForCategory(categoryId).stream()
                .filter(reduced ? ProductUtils::isProductPriceReduced : p -> true) // filter the product If price reduced
                .map(productMapper(labelType)) // map JLProduct to Product
                .collect(Collectors.toList());
        LOGGER.debug("returning getProductsForCategory() with product size: {}", lOfPriceReducedProducts.size());
        return lOfPriceReducedProducts;
    }

    private Function<JLProduct, Product> productMapper(String labelType) {
        return jlProduct -> {
            // map the color swatches
            List<ColorSwatches> colorSwatches = new ColorSwatchTransformer(jlProduct).transform();

            // format the price
            String nowPrice = ProductUtils.formatNowPrice(jlProduct);

            // map price label
            String priceLabel = ProductUtils.getPriceLabel(jlProduct, labelType);

            return Product.builder()
                    .productId(jlProduct.getProductId())
                    .title(jlProduct.getTitle())
                    .nowPrice(nowPrice)
                    .lOfColorSwatches(colorSwatches)
                    .priceLabel(priceLabel)
                    .build();
        };
    }
}
