package com.redhat.coolstore.products.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class RepositoryTest {

    @Inject
    Repository repository;

    @Inject
    EntityManager entityManager;

    @Inject
    UserTransaction transaction;

    @BeforeEach
    @Transactional
    void deleteAllFromTable() {
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }

    @Test
    void testCreateEntity() {
        Product product = new Product.Builder().name("my product").price(new BigDecimal("100.99")).build();

        repository.create(product);
        assertThat(product.getId(), not(equalTo(0)));
    }

    @Test
    void testAllProducts() {
        Product product1 = new Product.Builder().name("my product 1").price(new BigDecimal("100.99")).build();
        Product product2 = new Product.Builder().name("my product 2").price(new BigDecimal("99.99")).build();

        createProducts(Arrays.asList(product1, product2));

        List<Product> products = repository.allProducts();
        assertThat(products.size(), equalTo(2));
    }

    @Test
    void testProductById() {
        Product product1 = new Product.Builder().name("my product 1").price(new BigDecimal("100.99")).build();
        Product product2 = new Product.Builder().name("my product 2").price(new BigDecimal("99.99")).build();
        createProducts(Arrays.asList(product1, product2));

        Product found = repository.productById(product1.getId());
        assertThat(found, notNullValue());
        assertThat(found.getName(), equalTo("my product 1"));
        assertThat(found.getPrice(), equalTo(new BigDecimal("100.99")));
    }

    @Test
    void testProductByIdNotFound() {
        Product product1 = new Product.Builder().name("my product 1").price(new BigDecimal("100.99")).build();
        Product product2 = new Product.Builder().name("my product 2").price(new BigDecimal("99.99")).build();
        createProducts(Arrays.asList(product1, product2));

        Product found = repository.productById(product1.getId()+10);
        assertThat(found, nullValue());
    }

    @Test
    void testDeleteProduct() {
        Product product1 = new Product.Builder().name("my product 1").price(new BigDecimal("100.99")).build();
        Product product2 = new Product.Builder().name("my product 2").price(new BigDecimal("99.99")).build();
        createProducts(Arrays.asList(product1, product2));

        repository.deleteProduct(product1.getId());

        Product found = repository.productById(product1.getId());
        assertThat(found, nullValue());
        found = repository.productById(product2.getId());
        assertThat(found, notNullValue());


    }

    @Transactional
    void createProducts(List<Product> products) {
        products.forEach(p -> entityManager.persist(p));
    }

}
