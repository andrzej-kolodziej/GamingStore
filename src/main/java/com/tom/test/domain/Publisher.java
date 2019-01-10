package com.tom.test.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

/**
 * Created by tom on 6/9/2016.
 */
@Entity
public class Publisher extends AbstartDomainClass{

    @NotEmpty
    private String name;
    private String description;
    private String imageUrl;

    @OneToMany(mappedBy = "publisher")
    private Set<Product> products = new HashSet<>();

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addProduct(Product product){
        if (product == null)
            throw new NullPointerException("Can't add null product.");
        if (product.getPublisher() != null)
            throw new IllegalStateException("Product is already assigned to a Poblisher.");
        getProducts().add(product);
        product.setPublisher(this);
    }
}
