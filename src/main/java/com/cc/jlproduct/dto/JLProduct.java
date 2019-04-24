package com.cc.jlproduct.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class JLProduct {
    private String productId;
    private String title;
    private Price price;
    private List<JLColorSwatches> lOfJLColorSwatches;
}
