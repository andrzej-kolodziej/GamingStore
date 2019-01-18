package com.app.services;

import com.app.commands.ProductForm;
import com.app.converters.ProductFormToProduct;
import com.app.converters.ProductToProductForm;
import com.app.domain.Product;
import com.app.repositories.ProductRepository;
import com.app.services.reposervices.ProductServiceRepoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class ProductServiceRepoImplTest {
    
    @InjectMocks
    private ProductServiceRepoImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductFormToProduct productFormToProduct;

    @Mock
    private ProductToProductForm productToProductForm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllProducts() {
        List<Product> expectedProducts = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);
        expectedProducts.add(product1);
        expectedProducts.add(product2);

        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<?> actualProducts = productService.listAll();

        for (int i = 0; i < actualProducts.size(); i++)
            assertThat(expectedProducts.get(i).getId()).isEqualTo(((Product)actualProducts.get(i)).getId());

    }

    @Test
    public void shouldReturnProductOfGivenId() {
        Product expectedProduct = new Product();
        expectedProduct.setName("product");
        expectedProduct.setId(1);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getById(1);

        assertThat(actualProduct.getId()).isEqualTo(expectedProduct.getId());

        verify(productRepository, times(1)).findById(1);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldSaveProductToDb() {
        Product expectedProduct = new Product();
        expectedProduct.setId(1);
        expectedProduct.setName("product");
        expectedProduct.setImageUrl("url");
        expectedProduct.setDescription("description");

        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        Product actualProduct = productService.saveOrUpdate(expectedProduct);
        assertThat(actualProduct.getId()).isEqualTo(expectedProduct.getId());

        verify(productRepository, times(1)).save(expectedProduct);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldDeleteProduct() {
        productService.delete(1);
        verify(productRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldConvertProductFormIntoProductAndSaveItIntoDb() {
        ProductForm productForm = new ProductForm();
        productForm.setProductId(1);
        productForm.setProductName("product");

        Product expectedProduct = new Product();
        expectedProduct.setId(1);
        expectedProduct.setName("product");

        when(productFormToProduct.convert(productForm)).thenReturn(expectedProduct);

        Product actualProduct = productService.saveOrUpdateProductForm(productForm);
        assertThat(actualProduct.getId()).isEqualTo(expectedProduct.getId());

        verify(productRepository, times(1)).save(expectedProduct);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldReturnProductFormById() {
        ProductForm expectedProductForm = new ProductForm();
        expectedProductForm.setProductId(1);
        expectedProductForm.setProductName("product");

        Product product = new Product();
        Optional<Product> optionalProduct = Optional.of(product);

        when(productToProductForm.convert(any(Product.class))).thenReturn(expectedProductForm);
        when(productRepository.findById(anyInt())).thenReturn(optionalProduct);

        ProductForm actualProductForm = productService.findProductFormById(1);
        assertThat(actualProductForm.getProductId()).isEqualTo(expectedProductForm.getProductId());

        verify(productRepository, times(1)).findById(1);
        verify(productToProductForm, times(1)).convert(product);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productToProductForm);
    }
}
