package com.cc.products.service;

import com.cc.jlproduct.dto.JLColorSwatches;
import com.cc.jlproduct.dto.JLProduct;
import com.cc.jlproduct.dto.Price;
import com.cc.jlproduct.gateway.JLGateway;
import com.cc.products.dto.ColorSwatches;
import com.cc.products.dto.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private JLGateway jlGateway;

    @Test
    public void getProductsForCategory_NonFound() {
        given(jlGateway.getAllProductForCategory(any())).willReturn(Collections.emptyList());
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(0));
    }

    @Test
    public void getProductsForCategory_OneFound() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234").title("Some Title")
                .price(Price.builder().was(BigDecimal.TEN).now(BigDecimal.ONE).currency(Currency.getInstance("GBP")).build()).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getProductId(), is("1234"));
        assertThat(actual.get(0).getTitle(), is("Some Title"));
    }

    @Test
    public void getProductsForCategory_NoneFound_WithAReducedPrice() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234").price(Price.builder()
                .was(null).now(BigDecimal.ONE).currency(Currency.getInstance("GBP")).build()).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(0));
    }

    @Test
    public void getProductsForCategory_NoneFound_WithSamePrice() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234").price(Price.builder()
                .was(BigDecimal.ONE).now(BigDecimal.ONE).currency(Currency.getInstance("GBP")).build()).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(0));
    }

    @Test
    public void getProductsForCategory_Found_WithProperColourSwatch() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.ONE).was(BigDecimal.TEN).currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Blue").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches().get(0), is(ColorSwatches.builder().color("Blue").rgbColor("0000FF").skuId("32948348").build()));
    }

    @Test
    public void getProductsForCategory_Found_WithoutColourSwatch() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.ONE).was(BigDecimal.TEN).currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches().get(0), is(ColorSwatches.builder().color("Olive").rgbColor(null).skuId("32948348").build()));
    }

    @Test
    public void getProductsForCategory_Found_NowPriceBelowTen() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.ONE).was(BigDecimal.TEN).currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£1.00"));
    }

    @Test
    public void getProductsForCategory_Found_NowPriceAboveTen() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.valueOf(20.21)).was(BigDecimal.valueOf(21.21)).currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£20"));
    }

    @Test
    public void getProductsForCategory_Found_DefaultPriceLabel() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.valueOf(20.21)).was(BigDecimal.valueOf(21.21)).currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasNow ", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£20"));
        assertThat(actual.get(0).getPriceLabel(), is("Was £21, now £20"));
    }

    @Test
    public void getProductsForCategory_ShowNowWasThen2PriceLabel() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.valueOf(20.21))
                        .was(BigDecimal.valueOf(21.21))
                        .then1(BigDecimal.valueOf(22.35))
                        .then2(null)
                        .currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasThenNow", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£20"));
        assertThat(actual.get(0).getPriceLabel(), is("Was £21, Then £22, now £20"));
    }

    @Test
    public void getProductsForCategory_ShowNowWasThen1PriceLabel() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.valueOf(20.21))
                        .was(BigDecimal.valueOf(21.21))
                        .then1(BigDecimal.valueOf(22.35))
                        .then2(null)
                        .currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowWasThenNow", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£20"));
        assertThat(actual.get(0).getPriceLabel(), is("Was £21, Then £22, now £20"));
    }

    @Test
    public void getProductsForCategory_ShowPercDscountPriceLabel() {
        List<JLProduct> list = new ArrayList<>(Arrays.asList(JLProduct.builder().productId("1234")
                .price(Price.builder().now(BigDecimal.valueOf(20.21))
                        .was(BigDecimal.valueOf(21.21))
                        .then1(BigDecimal.valueOf(22.35))
                        .then2(null)
                        .currency(Currency.getInstance("GBP")).build())
                .lOfJLColorSwatches(Arrays.asList(JLColorSwatches.builder().color("Olive").skuId("32948348").build())).build()));
        given(jlGateway.getAllProductForCategory(any())).willReturn(list);
        List<Product> actual = productService.getProductsForCategory("12345678", "ShowPercDscount", true);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getLOfColorSwatches(), hasSize(1));
        assertThat(actual.get(0).getNowPrice(), is("£20"));
        assertThat(actual.get(0).getPriceLabel(), is("5% off, now £20"));
    }

}