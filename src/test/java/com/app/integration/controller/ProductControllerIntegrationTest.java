package com.app.integration.controller;

import com.app.commands.ProductForm;
import com.app.configuration.SpringSecurityTestConfig;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class/*, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc(secure = true)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetProductsList_thenReturnAllProducts() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetProductRoot_thenReturnAllProducts() throws Exception {
        mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetNewProduct_thenRetrieveAndListAllDevelopersAndPublishersFromDbAndReturnProductForm() throws Exception {
        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"))
                .andExpect(model().attributeExists("productForm"))
                .andExpect(model().attributeExists("developers"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenEditProduct_thenRetrieveProductAndListAllDevelopersAndPublishersAndReturnProductFormWithGivenProduct() throws Exception {
        mockMvc.perform(get("/product/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"))
                .andExpect(model().attributeExists("productForm"))
                .andExpect(model().attributeExists("developers"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndValidProductForm_whenSaveProduct_thenFetchFromDbByIdProductDeveloperAndPublisherAndSetIntoProductAndPersistThatProductIntoDbAndRedirectToShowGivenProduct()
        throws Exception {
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("productName", "name")
                .param("productImageUrl", "url")
                .param("productYoutubeUrl", "url")
                .param("productPrice", "10")
                .param("productDeveloper.id", "" + 1)
                .param("productPublisher.id", "" + 1).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/product/show/1"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndInvalidProductForm_whenSaveProduct_thenReturnFormWithGivenProductAndThatProductIsNotPersistedIntoDb() throws Exception {
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("productName", "")
                .param("productImageUrl", "")
                .param("productYoutubeUrl", "")
                .param("productPrice", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productform"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenShowProduct_thenFetchProductFromDbAndReturnViewWithThatProduct() throws Exception {
        mockMvc.perform(get("/product/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("product/show"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenDeleteProduct_thenRemoveGivenProductFromDbAndRedirectToProductList() throws Exception {
        mockMvc.perform(get("/product/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void givenNotAuthenticated_whenGetProductRoot_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenGetProductsList_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenGetNewProduct_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenEditProduct_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/edit/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenSaveProduct_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("productName", "name")
                .param("productImageUrl", "url")
                .param("productYoutubeUrl", "url")
                .param("productPrice", "10")
                .param("productDeveloper.id", "" + 1)
                .param("productPublisher.id", "" + 1).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenShowProduct_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/show/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenDeleteProduct_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/product/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
