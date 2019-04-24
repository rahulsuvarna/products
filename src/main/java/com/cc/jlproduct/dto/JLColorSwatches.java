package com.cc.jlproduct.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class JLColorSwatches {
    private String color;
    private String skuId;
}
