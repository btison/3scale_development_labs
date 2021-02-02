package com.redhat.coolstore.products.rest;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.coolstore.products.repository.Product;
import com.redhat.coolstore.products.repository.Repository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

@QuarkusTest
public class ProductResourceTest {

    @InjectMock
    Repository repository;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test
    void testGetProductById() throws JsonProcessingException {

        Product product = new Product.Builder(1L).name("my product 1").price(new BigDecimal("100.99")).build();

        when(repository.productById(any(Long.class))).thenReturn(product);

        String body = given().when().get("/api/product/1")
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).extract().asString();

        String json =  new ObjectMapper().writeValueAsString(product);
        assertThat(body, jsonEquals(json));
        verify(repository).productById(1L);
    }

    @Test
    void testGetProductByIdNotFound() throws JsonProcessingException {

        when(repository.productById(any(Long.class))).thenReturn(null);

        given().when().get("/api/product/1")
                .then().assertThat().statusCode(404);

        verify(repository).productById(1L);
    }

    @Test
    void testGetProductByIdWhenException() throws JsonProcessingException {

        when(repository.productById(any(Long.class))).thenThrow(new RuntimeException());

        given().when().get("/api/product/1")
                .then().assertThat().statusCode(500);

        verify(repository).productById(1L);
    }

    @Test
    void testGetAllProducts() throws JsonProcessingException {

        Product product1 = new Product.Builder(1L).name("my product 1").price(new BigDecimal("100.99")).build();
        Product product2 = new Product.Builder(1L).name("my product 2").price(new BigDecimal("99.99")).build();

        List<Product> products = Arrays.asList(product1, product2);

        when(repository.allProducts()).thenReturn(products);

        String body = given().when().get("/api/products")
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).extract().asString();

        String json =  new ObjectMapper().writeValueAsString(products);
        assertThat(body, jsonEquals(json));
        verify(repository).allProducts();
    }

    @Test
    void testCreateProduct() {

        String body = "{\"name\":\"my product 1\",\"price\":100.99}";

        given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/api/product")
                .then().assertThat().statusCode(201).body(equalTo(""));

        verify(repository).create(productCaptor.capture());
        Product product = productCaptor.getValue();
        assertThat(product, notNullValue());
        assertThat(product.getId(), equalTo(0L));
        assertThat(product.getName(), equalTo("my product 1"));
        assertThat(product.getPrice(), equalTo(new BigDecimal("100.99")));
    }

    @Test
    void testDeleteProduct() {

        given().when().with().delete("/api/product/1")
                .then().assertThat().statusCode(204).body(equalTo(""));

        verify(repository).deleteProduct(1L);
    }

}
