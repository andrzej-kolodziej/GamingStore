package com.tom.test.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tom on 6/8/2016.
 */
@Entity
public class Bundle extends CommonGoodDetails{

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "BUNDLE_PRODUCT",
            joinColumns = @JoinColumn(name = "BUNDLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private Set<Product> products = new HashSet<>();

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        if (product == null)
            throw new NullPointerException("Can't add null product.");
        getProducts().add(product);
    }
    public void deleteProduct(Product product){
        if (product == null)
            throw new NullPointerException("Can't add null product.");
        getProducts().remove(product);
    }
}
