package com.cc.products.service;

import com.cc.products.dto.Product;

import java.util.List;

public interface IProductService {
    List<Product> getProductsForCategory(String categoryId, String labelType, boolean reduced);
}
