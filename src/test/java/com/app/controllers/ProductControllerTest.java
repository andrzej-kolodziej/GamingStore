package com.app.controllers;

import com.app.commands.ProductForm;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.domain.Publisher;
import com.app.services.DeveloperService;
import com.app.services.ProductService;
import com.app.services.PublisherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {/*SpringSecurityTestConfig.class,*/ /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc(secure = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private DeveloperService developerService;

    @MockBean
    private PublisherService publisherService;

    @Test
    public void whenGetProductsList_thenReturnAllProductsAndOkStatus() throws Exception {
        List products = new ArrayList<>();
        Product product = new Product();
        product.setName("name");
        Developer developer = new Developer();
        developer.setName("developer name");
        product.setDeveloper(developer);
        products.add(product);
        when(productService.listAll()).thenReturn(products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attribute("products", products));

        verify(productService, times(1)).listAll();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void whenGetProductRoot_thenReturnAllProductsAndOkStatus() throws Exception {
        List products = new ArrayList<>();
        Product product = new Product();
        product.setName("name");
        Developer developer = new Developer();
        developer.setName("developer name");
        product.setDeveloper(developer);
        products.add(product);
        when(productService.listAll()).thenReturn(products);

        mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attribute("products", products));

        verify(productService, times(1)).listAll();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void whenGetNewProduct_thenRetrieveAndListAllDevelopersAndPublishersAndReturnProductForm() throws Exception {
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

        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"))
                .andExpect(model().attributeExists("productForm"))
                .andExpect(model().attribute("developers", developers))
                .andExpect(model().attribute("publishers", publishers));

        verify(developerService, times(1)).listAll();
        verify(publisherService, times(1)).listAll();
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
    }

    @Test
    public void whenEditProduct_thenRetrieveProductAndListAllDevelopersAndPublishersAndReturnProductForm() throws Exception {
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

        mockMvc.perform(get("/product/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"))
                .andExpect(model().attribute("productForm", productForm))
                .andExpect(model().attribute("developers", developers))
                .andExpect(model().attribute("publishers", publishers));

        verify(productService, times(1)).findProductFormById(productId);
        verify(developerService, times(1)).listAll();
        verify(publisherService, times(1)).listAll();
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
    }

    @Test
    public void givenValidProductForm_whenSaveProduct_thenFetchFromDbByIdProductDeveloperAndPublisherAndSetIntoProductAndPersistThatProductIntoDbAndRedirectToShowGivenProduct()
        throws Exception {

        int productId = 1, developerId = 1, publisherId = 1;
        Product product = new Product();
        product.setId(productId);
        when(productService.saveOrUpdateProductForm(any(ProductForm.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("productName", "name")
                .param("productImageUrl", "url")
                .param("productYoutubeUrl", "url")
                .param("productPrice", "10")
                .param("productDeveloper.id", "" + developerId)
                .param("productPublisher.id", "" + publisherId).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/product/show/" + productId));

        verify(developerService, times(1)).getById(developerId);
        verify(publisherService, times(1)).getById(publisherId);
        verify(productService, times(1)).saveOrUpdateProductForm(any(ProductForm.class));
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void givenInvalidProductForm_whenSaveProduct_thenReturnFormWithGivenProductAndThatProductIsNotPersistedIntoDb() throws Exception {
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("productName", "")
                .param("productImageUrl", "")
                .param("productYoutubeUrl", "")
                .param("productPrice", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"));

        verifyZeroInteractions(developerService);
        verifyZeroInteractions(publisherService);
        verifyZeroInteractions(productService);
    }

    @Test
    public void whenShowProduct_thenFetchProductFromDbAndReturnViewWithThatProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        when(productService.getById(1)).thenReturn(product);

        mockMvc.perform(get("/product/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("product/show"))
                .andExpect(model().attributeExists("product"));

        verify(productService, times(1)).getById(1);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void whenDeleteProduct_thenRemoveGivenProductFromDbAndRedirectToProductList() throws Exception {
        mockMvc.perform(get("/product/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, times(1)).delete(1);
        verifyNoMoreInteractions(productService);
    }
}
