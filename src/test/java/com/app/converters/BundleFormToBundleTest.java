package com.app.converters;

import com.app.commands.BundleForm;
import com.app.domain.Bundle;
import com.app.domain.Product;
import com.app.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BundleFormToBundleTest {
    @InjectMocks
    private BundleFormToBundle bundleFormToBundle;

    @Mock
    private ProductService productService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldConvertbundleFormToBundle() {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("product1");
        Product product2 = new Product();
        product1.setId(2);
        product1.setName("product2");
        Product product3 = new Product();
        product1.setId(3);
        product1.setName("product3");

        when(productService.getById(1)).thenReturn(product1);
        when(productService.getById(2)).thenReturn(product2);
        when(productService.getById(3)).thenReturn(product3);

        BundleForm bundleForm = new BundleForm();
        bundleForm.setBundlePrice(BigDecimal.valueOf(10));
        bundleForm.setBundleName("name");
        bundleForm.setBundleImageUrl("url");
        bundleForm.setBundleId(1);
        bundleForm.setBundleDescription("description");
        bundleForm.setBundlePruductIds(Arrays.asList(1,2,3));

        Bundle expectedBundle = new Bundle();
        expectedBundle.setId(1);
        expectedBundle.setName("name");
        expectedBundle.setImageUrl("url");
        expectedBundle.setDescription("description");
        expectedBundle.setPrice(BigDecimal.valueOf(10));
        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        Bundle actualBundle = bundleFormToBundle.convert(bundleForm);

        assertThat(actualBundle.getId()).isEqualTo(expectedBundle.getId());
        assertThat(actualBundle.getName()).isEqualTo(expectedBundle.getName());
        assertThat(actualBundle.getDescription()).isEqualTo(expectedBundle.getDescription());
        assertThat(actualBundle.getPrice()).isEqualByComparingTo(expectedBundle.getPrice());
        assertThat(actualBundle.getProducts().size()).isEqualTo(expectedBundle.getProducts().size());
    }

}
