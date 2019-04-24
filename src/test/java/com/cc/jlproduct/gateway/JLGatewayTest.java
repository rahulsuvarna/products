package com.cc.jlproduct.gateway;

import com.cc.jlproduct.dto.JLProduct;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JLGatewayTest {

    private static final String url = "url/somecode";
    @InjectMocks
    private JLGateway jlGateway;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(jlGateway, "url", "url/%s", String.class);
    }

    @Test
    public void testGetAllProductsForCategory_NoProducts() {
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(Collections.EMPTY_MAP, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(0)));

    }

    @Test
    public void testGetAllProductsForCategory_EmptyProducts() {
        HashMap<String, Object> emptyProducts = new HashMap<>();
        emptyProducts.put("products", Collections.emptyList());
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(emptyProducts, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(0)));

    }

    @Test
    public void testGetAllProductsForCategory_OneProduct_PriceDetailsEmpty() {
        Map<String, Object> response = new HashMap<>();

        HashMap<String, Object> product = new HashMap<>();
        product.put("productId", "12345678");
        product.put("title", "title");

        HashMap<String, Object> price = new HashMap<>();
        price.put("was", "");
        price.put("then1", "");
        price.put("then2", "");
        price.put("now", "");
        price.put("currency", "GBP");
        product.put("price", price);

        List<Map<String, Object>> colorSwatches = new ArrayList<>();
        product.put("colorSwatches", colorSwatches);

        ArrayList<Map> responseProducts = new ArrayList<>();
        responseProducts.add(product);
        response.put("products", responseProducts);
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(1)));
        assertThat(actual.get(0).getPrice().getWas(), is(nullValue()));
        assertThat(actual.get(0).getPrice().getThen1(), is(nullValue()));
        assertThat(actual.get(0).getPrice().getThen2(), is(nullValue()));
        assertThat(actual.get(0).getPrice().getNow(), is(BigDecimal.ZERO.setScale(2)));
    }

    @Test
    public void testGetAllProductsForCategory_OneProduct_ColorSwatchesEmpty() {
        Map<String, Object> response = new HashMap<>();

        HashMap<String, Object> product = new HashMap<>();
        product.put("productId", "12345678");
        product.put("title", "title");

        HashMap<String, Object> price = new HashMap<>();
        price.put("was", "");
        price.put("then1", "");
        price.put("then2", "");
        price.put("now", new HashMap<String, String>());
        price.put("currency", "GBP");
        product.put("price", price);

        List<Map<String, Object>> colorSwatches = new ArrayList<>();
        product.put("colorSwatches", colorSwatches);

        ArrayList<Map> responseProducts = new ArrayList<>();
        responseProducts.add(product);
        response.put("products", responseProducts);
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(1)));
        assertThat(actual.get(0).getLOfJLColorSwatches(), is(notNullValue()));
        assertThat(actual.get(0).getLOfJLColorSwatches(), hasSize(0));
    }

    @Test
    public void testGetAllProductsForCategory_OneProduct() {
        Map<String, Object> response = new HashMap<>();

        HashMap<String, Object> product = new HashMap<>();
        product.put("productId", "12345678");
        product.put("title", "title");

        HashMap<String, Object> price = new HashMap<>();
        price.put("was", "21.21");
        price.put("then1", "22.21");
        price.put("then2", "23.21");
        price.put("now", "24.21");
        price.put("currency", "GBP");
        product.put("price", price);

        HashMap<String, Object> colorSwatch = new HashMap<>();
        colorSwatch.put("basicColor", "Blue");
        colorSwatch.put("skuId", "567890");
        List<Map<String, Object>> colorSwatches = new ArrayList<>();
        colorSwatches.add(colorSwatch);
        product.put("colorSwatches", colorSwatches);

        ArrayList<Map> responseProducts = new ArrayList<>();
        responseProducts.add(product);
        response.put("products", responseProducts);
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(1)));

    }

    @Test
    public void testGetAllProductsForCategory_OneProduct_PriceNowNotString() {
        Map<String, Object> response = new HashMap<>();

        HashMap<String, Object> product = new HashMap<>();
        product.put("productId", "12345678");
        product.put("title", "title");

        HashMap<String, Object> price = new HashMap<>();
        price.put("was", "");
        price.put("then1", "");
        price.put("then2", "");
        price.put("now", new HashMap<String, String>());
        price.put("currency", "GBP");
        product.put("price", price);

        HashMap<String, Object> colorSwatch = new HashMap<>();
        colorSwatch.put("basicColor", "Blue");
        colorSwatch.put("skuId", "567890");
        List<Map<String, Object>> colorSwatches = new ArrayList<>();
        colorSwatches.add(colorSwatch);
        product.put("colorSwatches", colorSwatches);

        ArrayList<Map> responseProducts = new ArrayList<>();
        responseProducts.add(product);
        response.put("products", responseProducts);
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(1)));
        assertThat(actual.get(0).getPrice().getNow(), is(BigDecimal.ZERO.setScale(2)));
    }


    @Test
    public void testGetAllProductsForCategory_OneProduct_AllNonValidValues() {
        Map<String, Object> response = new HashMap<>();

        HashMap<String, Object> product = new HashMap<>();
        product.put("productId", 12345678);
        product.put("title", 21);

        HashMap<String, Object> price = new HashMap<>();
        price.put("was", 21.21);
        price.put("then1", 22.21);
        price.put("then2", 23.21);
        price.put("now", 24.21);
        price.put("currency", null);
        product.put("price", price);

        HashMap<String, Object> colorSwatch = new HashMap<>();
        colorSwatch.put("basicColor", 1);
        colorSwatch.put("skuId", 567890);
        List<Map<String, Object>> colorSwatches = new ArrayList<>();
        colorSwatches.add(colorSwatch);
        product.put("colorSwatches", colorSwatches);

        ArrayList<Map> responseProducts = new ArrayList<>();
        responseProducts.add(product);
        response.put("products", responseProducts);
        when(restTemplate.exchange(url, HttpMethod.GET, null, Map.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        List<JLProduct> actual = jlGateway.getAllProductForCategory("somecode");
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(hasSize(1)));

    }

}