package com.app.repositories;

import com.app.domain.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tom on 6/9/2016.
 */
public interface ProductRepository extends CrudRepository<Product,Integer>{
}

