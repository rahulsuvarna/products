package com.cc.products;

import com.cc.products.dto.Product;
import com.cc.products.service.ProductService;
import io.reactivex.Observable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductsAPITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public RestTemplate restTemplate;

    @MockBean
    private ProductService productService;

//    @Test
//    public void testGetProducts_CategoryNotProvided() throws Exception {
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Observable.just(Collections.emptyList()));
//
//        this.mockMvc.perform(get("/categories//products"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Ignore
//    public void testGetProducts_ProductNotFound() throws Exception {
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Observable.just(Collections.emptyList()));
//
//        this.mockMvc.perform(get("/categories/12345678/products"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
//
//    @Test
//    public void testGetProducts_ProductsFound() throws Exception {
//
//        List<Product> list = new ArrayList<>(Arrays.asList(Product.builder().productId("1234").build(), Product.builder().productId("5678").build()));
//        given(productService.getProductsForCategory(any(), any(), anyBoolean())).willReturn(Observable.just(list));
//
//        this.mockMvc.perform(get("/categories/12345678/products"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[{'productId':'1234'},{'productId':'5678'}]"));
//    }

}
