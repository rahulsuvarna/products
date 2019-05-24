package com.cc.products.service;

import com.cc.products.dto.Product;
import io.reactivex.Observable;

import java.util.List;

public interface IProductService {
    Observable<List<Product>> getProductsForCategory(String categoryId, String labelType, boolean reduced);
}
