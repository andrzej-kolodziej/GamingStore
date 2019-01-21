package com.app.controllers;

import com.app.commands.ProductForm;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.domain.Publisher;
import com.app.services.DeveloperService;
import com.app.services.ProductService;
import com.app.services.PublisherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private DeveloperService developerService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenGetProductsList_thenReturnAllProducts() throws Exception {
        List products = new ArrayList<>();
        Product product = new Product();
        product.setName("name");
        Developer developer = new Developer();
        developer.setName("developer name");
        product.setDeveloper(developer);
        products.add(product);
        when(productService.listAll()).thenReturn(products);
        String expectedView = "product/list";

        String actualView = productController.listAll(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("products", products);
        verify(productService, times(1)).listAll();
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenGetNewProduct_thenRetrieveAndListAllDevelopersAndPublishersFromDbAndReturnProductForm() throws Exception {
        Developer developer = new Developer();
        developer.setName("developer");
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        List developers = new ArrayList();
        developers.add(developer);
        List publishers = new ArrayList();
        publishers.add(publisher);

        when(developerService.listAll()).thenReturn(developers);
        when(publisherService.listAll()).thenReturn(publishers);

        String expectedView = "product/productform";

        String actualView = productController.newProduct(model);

        assertThat(actualView).isEqualTo(expectedView);

        verify(model, times(1)).addAttribute(eq("productForm"), any(ProductForm.class));
        verify(model, times(1)).addAttribute("developers", developers);
        verify(model, times(1)).addAttribute("publishers", publishers);
        verify(developerService, times(1)).listAll();
        verify(publisherService, times(1)).listAll();
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenEditProduct_thenRetrieveProductAndListAllDevelopersAndPublishersAndReturnProductFormWithGivenProduct() throws Exception {
        Developer developer = new Developer();
        developer.setName("developer");
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        List developers = new ArrayList();
        developers.add(developer);
        List publishers = new ArrayList();
        publishers.add(publisher);

        int productId = 1;
        ProductForm productForm = new ProductForm();
        productForm.setProductId(productId);
        productForm.setProductName("product");

        when(productService.findProductFormById(productId)).thenReturn(productForm);
        when(developerService.listAll()).thenReturn(developers);
        when(publisherService.listAll()).thenReturn(publishers);

        String expectedView = "product/productform";

        String actualView = productController.saveOrUpdateProduct(1, model);

        assertThat(actualView).isEqualTo(expectedView);

        verify(model, times(1)).addAttribute(eq("productForm"), any(ProductForm.class));
        verify(model, times(1)).addAttribute("developers", developers);
        verify(model, times(1)).addAttribute("publishers", publishers);
        verify(productService, times(1)).findProductFormById(productId);
        verify(developerService, times(1)).listAll();
        verify(publisherService, times(1)).listAll();
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void givenValidProductForm_whenSaveProduct_thenFetchFromDbByIdProductDeveloperAndPublisherAndSetIntoProductAndPersistThatProductIntoDbAndRedirectToShowGivenProduct()
        throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("product");

        ProductForm productForm = new ProductForm();
        productForm.setProductName("product");
        productForm.setProductDescription("description");
        productForm.setProductImageUrl("url");
        productForm.setProductYoutubeUrl("url");
        productForm.setProductPrice(BigDecimal.valueOf(10));

        Developer developer = new Developer();
        developer.setId(1);
        developer.setName("developer");
        developer.setDescription("description");
        developer.setImageUrl("url");

        Publisher publisher = new Publisher();
        publisher.setId(1);
        publisher.setName("publisher");
        publisher.setImageUrl("url");
        publisher.setDescription("description");

        productForm.setProductPublisher(publisher);
        productForm.setProductDeveloper(developer);

        when(productService.saveOrUpdateProductForm(productForm)).thenReturn(product);
        when(developerService.getById(1)).thenReturn(developer);
        when(publisherService.getById(1)).thenReturn(publisher);

        String expectedView = "redirect:/product/show/1";

        String actualView = productController.saveProduct(productForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);

        verify(developerService, times(1)).getById(1);
        verify(publisherService, times(1)).getById(1);
        verify(productService, times(1)).saveOrUpdateProductForm(productForm);
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void givenInvalidProductForm_whenSaveProduct_thenReturnFormWithGivenProductAndThatProductIsNotPersistedIntoDb() throws Exception {
        ProductForm productForm = new ProductForm();
        when(bindingResult.hasErrors()).thenReturn(true);

        String expectedView = "product/productform";

        String actualView = productController.saveProduct(productForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);

        verify(bindingResult, times(1)).hasErrors();
        verifyZeroInteractions(developerService);
        verifyZeroInteractions(publisherService);
        verifyZeroInteractions(productService);
    }

    @Test
    public void whenShowProduct_thenFetchProductFromDbAndReturnViewWithThatProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        when(productService.getById(1)).thenReturn(product);

        String expectedView = "product/show";

        String actualView = productController.showProduct(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("product", product);
        verify(productService, times(1)).getById(1);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void whenDeleteProduct_thenRemoveGivenProductFromDbAndRedirectToProductList() throws Exception {
        String expectedView = "redirect:/product/list";

        String actualView = productController.deleteProduct(1);

        assertThat(actualView).isEqualTo(expectedView);
        verify(productService, times(1)).delete(1);
        verifyNoMoreInteractions(productService);
    }
}
