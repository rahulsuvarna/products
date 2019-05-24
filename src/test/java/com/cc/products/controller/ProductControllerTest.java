package com.cc.products.controller;

import com.cc.products.dto.Product;
import com.cc.products.service.ProductService;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController controller;

    @Mock
    private ProductService productService;

//    @Test
//    public void getProducts_NullCategory() {
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Single.just(Collections.emptyList()));
//
//        List<Product> actual = controller.getProducts(null, "ShowWasNow ", true);
//        assertThat(actual, hasSize(0));
//    }
//
//    @Test
//    public void getProducts_NoProducts() {
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Single.just(Collections.emptyList()));
//
//        List<Product> actual = controller.getProducts("12345678", "ShowWasNow ", true);
//        assertThat(actual, hasSize(0));
//    }
//
//    @Test
//    public void getProducts_Products() {
//        List<Product> list = new ArrayList<>(Arrays.asList(Product.builder().productId("1234").build(), Product.builder().productId("5678").build()));
//
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Single.just(list));
//
//        List<Product> actual = controller.getProducts("12345678", "ShowWasNow ", true);
//        assertThat(actual, hasSize(2));
//    }
}