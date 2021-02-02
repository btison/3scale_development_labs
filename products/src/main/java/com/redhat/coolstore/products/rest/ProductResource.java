package com.redhat.coolstore.products.rest;

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.coolstore.products.repository.Product;
import com.redhat.coolstore.products.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api")
public class ProductResource {

    private static final Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Inject
    Repository repository;

    @Path("/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@Context HttpHeaders headers) {

        try {
            List<Product> products = repository.allProducts();
            return Response.ok(products).build();
        } catch (Exception e) {
            log.error("Exception while retrieving products", e);
            return Response.serverError().build();
        }
    }

    @Path("/product/{productId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productId") Long productId, @Context HttpHeaders headers) {

        Product product = repository.productById(productId);
        try {
            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(product).build();
            }
        } catch (Exception e) {
            log.error("Exception while retrieving product with id " + productId, e);
            return Response.serverError().build();
        }
    }

    @Path("/product")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product){
        try {
            repository.create(product);
            return Response.created(new URI("api/product/" + product.getId())).build();
        } catch (Exception e) {
            log.error("Exception while creating product " + product.getName(), e);
            return Response.serverError().build();
        }
    }

    @Path("/product/{productId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("productId") Long productId){
        try {
            repository.deleteProduct(productId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Exception while deleting product with id " + productId, e);
            return Response.serverError().build();
        }
    }

}
