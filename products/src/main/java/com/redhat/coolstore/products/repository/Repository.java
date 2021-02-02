package com.redhat.coolstore.products.repository;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class Repository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public Product create(Product product) {
        entityManager.persist(product);
        return product;
    }

    @Transactional
    public List<Product> allProducts() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    @Transactional
    public Product productById(long productId) {
        return entityManager.find(Product.class, productId);
    }

    @Transactional
    public void deleteProduct(long productId) {
        entityManager.createQuery("DELETE from Product p where p.id=:id")
                .setParameter("id", productId)
                .executeUpdate();
    }
}
