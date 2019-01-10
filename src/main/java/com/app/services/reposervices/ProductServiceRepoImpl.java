package com.app.services.reposervices;

import com.app.domain.Product;
import com.app.repositories.ProductRepository;
import com.app.commands.ProductForm;
import com.app.converters.ProductFormToProduct;
import com.app.converters.ProductToProductForm;
import com.app.services.BundleService;
import com.app.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 6/9/2016.
 */
@Service
public class ProductServiceRepoImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BundleService bundleService;

    @Autowired
    private ProductFormToProduct productFormToProduct;
    @Autowired
    private ProductToProductForm productToProductForm;

    @Override
    @Cacheable(cacheNames = "products")
    public List<?> listAll() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @Override
    @Cacheable(cacheNames = "product", key = "#id")
    public Product getById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    @CachePut(cacheNames = "product", key = "#result.getId()")
    @CacheEvict(cacheNames = "products", allEntries = true)
    public Product saveOrUpdate(Product domainObject) {
        return productRepository.save(domainObject);
    }

    @Override
    @Caching(evict = {@CacheEvict(cacheNames = "products", allEntries = true), @CacheEvict(cacheNames = "product", key = "#id")})
    public void delete(Integer id) {
        Product product = productRepository.findById(id).get();
        product.getBundles().forEach(bundle -> {
            bundle.deleteProduct(product);
            bundleService.saveOrUpdate(bundle);
        });
        productRepository.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "product", key = "#result.getId()")
    @CacheEvict(cacheNames = "products", allEntries = true)
    public Product saveOrUpdateProductForm(ProductForm productForm) {
        return productRepository.save(productFormToProduct.convert(productForm));
    }

    @Override
    public ProductForm findProductFormById(Integer id) {
        return productToProductForm.convert(productRepository.findById(id).get());
    }
}
