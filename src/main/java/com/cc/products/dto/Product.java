package com.cc.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String productId;
    private String title;
    private List<ColorSwatches> lOfColorSwatches;
    private String nowPrice;
    private String priceLabel;
    @JsonIgnore
    private BigDecimal reduction;
}
