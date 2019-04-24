package com.cc.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ColorSwatches {

    private String color;
    private String rgbColor;
    private String skuId;
}
