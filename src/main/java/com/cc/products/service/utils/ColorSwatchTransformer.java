package com.cc.products.service.utils;

import com.cc.jlproduct.dto.JLProduct;
import com.cc.products.dto.BasicColor;
import com.cc.products.dto.ColorSwatches;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tranform utility to tranform the JLP ColorSwatches
 */
public class ColorSwatchTransformer {
    private JLProduct jlProduct;

    public ColorSwatchTransformer(JLProduct jlProduct) {
        this.jlProduct = jlProduct;
    }

    public List<ColorSwatches> transform() {
        return Optional.ofNullable(jlProduct.getLOfJLColorSwatches()).orElse(Collections.emptyList()).stream()
                                .map(jlColorSwatches -> {
                                    String jlpColor = jlColorSwatches.getColor();
                                    Optional<BasicColor> basicColor = Optional.ofNullable(BasicColor.forName(jlpColor.toUpperCase()));
                                    // get hexcode for basic color or return null if not found
                                    String hexCode = basicColor.map(BasicColor::getHexCode).orElse(null);
                                    return ColorSwatches.builder().color(jlpColor).rgbColor(hexCode).skuId(jlColorSwatches.getSkuId()).build();
                                }).collect(Collectors.toList());
    }
}
