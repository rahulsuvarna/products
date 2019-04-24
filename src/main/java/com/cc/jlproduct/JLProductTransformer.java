package com.cc.jlproduct;

import com.cc.jlproduct.dto.JLColorSwatches;
import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.dto.Price;
import javafx.util.converter.CurrencyStringConverter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class JLProductTransformer {
    public static JLProduct extractJLProduct(Map<String, Object> product) {
        String productId = castToStringOrReturnEmpty(product, "productId");
        String title = castToStringOrReturnEmpty(product, "title");

        Map<String, Object> jlPrice = Optional.ofNullable(product.get("price"))
                .filter(Map.class::isInstance)
                .map(Map.class::cast).orElse(Collections.emptyMap());
        Currency currency = Optional.ofNullable(jlPrice.get("currency0")).filter(String.class::isInstance).map(String.class::cast).map(Currency::getInstance).orElse(Currency.getInstance("GBP"));
        BigDecimal was = castToBigDecimalOrReturn(jlPrice, "was", null);
        BigDecimal then1 = castToBigDecimalOrReturn(jlPrice, "then1", null);
        BigDecimal then2 = castToBigDecimalOrReturn(jlPrice, "then2", null);
        BigDecimal now = castToBigDecimalOrReturn(jlPrice, "now", BigDecimal.ZERO.setScale(2));
        Price price = Price.builder().was(was).then1(then1).then2(then2).now(now).currency(currency).build();

        List<Map<String, Object>> colorSwatches = Optional.ofNullable(product.get("colorSwatches"))
                .filter(List.class::isInstance)
                .map(List.class::cast).orElse(Collections.emptyList());
        List<JLColorSwatches> lOfJLColorSwatches = colorSwatches.stream().map(swatches -> {
            String basicColor = Optional.ofNullable(swatches.get("basicColor")).filter(String.class::isInstance).map(String.class::cast).orElse(null);
            String skuId = Optional.ofNullable(swatches.get("skuId")).filter(String.class::isInstance).map(String.class::cast).orElse(null);
            return JLColorSwatches.builder().color(basicColor).skuId(skuId).build();
        }).collect(Collectors.toList());

        return JLProduct.builder().productId(productId).title(title).price(price).lOfJLColorSwatches(lOfJLColorSwatches).build();
    }

    private static BigDecimal castToBigDecimalOrReturn(Map<String, Object> jlPrice, String name, BigDecimal returnValue) {
        return castToString(jlPrice, name).map(value -> new BigDecimal(value).setScale(2, RoundingMode.CEILING)).orElse(returnValue);
    }

    private static Optional<String> castToString(Map<String, Object> jlPrice, String name) {
        return Optional.ofNullable(jlPrice.get(name)).filter(String.class::isInstance).filter(el -> !el.toString().trim().isEmpty()).map(String.class::cast);
    }

    private static String castToStringOrReturnEmpty(Map<String, Object> product, String productId) {
        return castToString(product, productId).orElse("");
    }
}
