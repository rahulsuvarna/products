package com.cc.jlproduct.gateway;

import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.JLProductTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Gateway to invoke get products from JLP
 */
@Component
public class JLGateway implements IJLGateway {

    private final static Logger logger = LoggerFactory.getLogger(JLGateway.class);

    @Value("${jlp.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public List<JLProduct> getAllProductForCategory(@NotNull final String categoryId) {
        logger.debug("getAllProductForCategory(): with categoryId {}", categoryId);

        final String serviceURL = String.format(url, categoryId);
        final ResponseEntity<Map> response = restTemplate.exchange(serviceURL, HttpMethod.GET, null, Map.class);
        // extract the products. This could be null, return an empty list in that case
        final List<Map<String, Object>> products = Optional.ofNullable(response.getBody().get("products"))
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .orElse(Collections.emptyList());
        final List<JLProduct> lOfJLProducts = products.stream().map(JLProductTransformer::extractJLProduct).collect(Collectors.toList());

        logger.debug("JL gateway returning products for category : {} ==> {} ", categoryId, lOfJLProducts.size());
        return lOfJLProducts;
    }

//    public static void main(String[] args) {
//        new JLGateway().getAllProductForCategory("600001506");
//    }

}
