package com.app.services;

import com.app.domain.Product;
import com.app.commands.ProductForm;

/**
 * Created by tom on 6/9/2016.
 */
public interface ProductService extends CRUDservice<Product>{
    Product saveOrUpdateProductForm(ProductForm productForm);
    ProductForm findProductFormById(Integer id);
}
