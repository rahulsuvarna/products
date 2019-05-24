package com.cc.products.service;

import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.gateway.IJLGateway;
import com.cc.products.dto.ColorSwatches;
import com.cc.products.dto.Product;
import com.cc.products.service.utils.ColorSwatchTransformer;
import com.cc.products.service.utils.ProductUtils;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * Product Service class
 */
@Component
public class ProductService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final IJLGateway jlGateway;

    @Autowired
    public ProductService(IJLGateway jlGateway) {
        this.jlGateway = jlGateway;
    }

    public Single<List<Product>> getProductsForCategory(final @NotNull String categoryId, final String labelType, boolean reduced) {

        LOGGER.debug("getProductsForCategory() with categoryIs: {} and labelType: {}", categoryId, labelType);
        List<Product> lOfPriceReducedProducts = jlGateway.getAllProductForCategory(categoryId).stream()
                .filter(reduced ? ProductUtils::isProductPriceReduced : p -> true) // filter the product If price reduced
                .map(productMapper(labelType)) // map JLProduct to Product
                .sorted(comparing(Product::getReduction).reversed()) // sort the list based on reduction
                .collect(Collectors.toList());
        LOGGER.debug("returning getProductsForCategory() with product size: {}", lOfPriceReducedProducts.size());
        return Single.just(lOfPriceReducedProducts).subscribeOn(Schedulers.io());
    }

    private Function<JLProduct, Product> productMapper(String labelType) {
        return jlProduct -> {
            // map the color swatches
            List<ColorSwatches> colorSwatches = new ColorSwatchTransformer(jlProduct).transform();

            // format the price
            String nowPrice = ProductUtils.formatNowPrice(jlProduct);

            // map price label
            String priceLabel = ProductUtils.getPriceLabel(jlProduct, labelType);

            BigDecimal reduction = jlProduct.getPrice().getWas().subtract(jlProduct.getPrice().getNow());

            return Product.builder()
                    .productId(jlProduct.getProductId())
                    .title(jlProduct.getTitle())
                    .nowPrice(nowPrice)
                    .reduction(reduction)
                    .lOfColorSwatches(colorSwatches)
                    .priceLabel(priceLabel)
                    .build();
        };
    }
}
