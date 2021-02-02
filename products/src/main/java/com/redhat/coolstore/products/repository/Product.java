package com.redhat.coolstore.products.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="products")
@Access(AccessType.FIELD)
@SequenceGenerator(name="ProductsSeq", sequenceName="products_seq", allocationSize = 10)
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="ProductsSeq")
	@Column(name = "productid")
	private long id;

	@Column(name = "productname")
	private String name;

	@Column(name = "productprice")
	private BigDecimal price;

	public Product() {
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}


	public static class Builder {

		private final Product product;

		public Builder() {
			product = new Product();
		}

		public Builder(long id) {
			product = new Product();
			product.id = id;
		}

		public Builder name(String name) {
			product.name = name;
			return this;
		}

		public Builder price(BigDecimal price) {
			product.price = price;
			return this;
		}

		public Product build() {
			return product;
		}

	}
}