package com.cc.products.controller;

import com.cc.products.dto.Product;

import java.util.List;

public interface IProductController {
    List<Product> getProducts(String categoryId, String labelType, boolean reduced);
}
