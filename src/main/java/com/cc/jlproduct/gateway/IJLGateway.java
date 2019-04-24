package com.cc.jlproduct.gateway;

import com.cc.jlproduct.dto.JLProduct;

import java.util.List;

public interface IJLGateway {
    List<JLProduct> getAllProductForCategory(final String categoryId);

}
