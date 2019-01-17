package com.app.controllers;

import com.app.ResTexampleApplication;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.domain.Bundle;
import com.app.domain.Product;
import com.app.domain.User;
import com.app.services.BundleService;
import com.app.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {/*SpringSecurityTestConfig.class,*/ /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc(secure = true)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BundleService bundleService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(value = "admin", username = "admin", roles = {"ADMIN"})
    public void givenBundleAndPrincipal_whenGetIndexRoot_thenReturndBundleProductViewAndSetupUserEmail() throws Exception {
        int bundleId = 1;
        Bundle bundle = new Bundle();
        bundle.setId(bundleId);
        bundle.setPrice(BigDecimal.valueOf(10));
        bundle.setImageUrl("url");
        bundle.setName("name");
        bundle.setDescription("description");

        Set<Product> products = new HashSet<>();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(20));
        product.setDescription("product description");
        product.setName("product name");
        products.add(product);
        bundle.setProducts(products);

        when(bundleService.getById(bundleId)).thenReturn(bundle);

        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName(anyString())).thenReturn(user);

        mockMvc.perform(get("/")
                .sessionAttr("userEmail", "useremail"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("bundle", bundle))
                .andExpect(model().attribute("products", bundle.getProducts()))
                .andExpect(model().attribute("userEmail", "useremail"))
                .andExpect(model().attribute("page", "index"));

        verify(bundleService, times(1)).getById(1);
        verify(userService, times(1)).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", roles = {"ADMIN"})
    public void givenValidBundleIdAndTotalBundleNumberIsGreaterThanZeroAndPricipalIsDefined_whenGetIndexOfBundleId_thenReturnIndexViewWithGivenBundleAndSetupUserEmail() throws Exception {
        int bundleId = 1;
        Bundle bundle = new Bundle();
        bundle.setId(bundleId);
        bundle.setPrice(BigDecimal.valueOf(10));
        bundle.setImageUrl("url");
        bundle.setName("name");
        bundle.setDescription("description");

        Set<Product> products = new HashSet<>();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(20));
        product.setDescription("product description");
        product.setName("product name");
        products.add(product);
        bundle.setProducts(products);

        when(bundleService.count()).thenReturn(10L);
        when(bundleService.getById(bundleId)).thenReturn(bundle);

        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName(anyString())).thenReturn(user);

        mockMvc.perform(get("/index/{id}", bundleId)
                .sessionAttr("userEmail", "useremail"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("bundle", bundle))
                .andExpect(model().attribute("products", bundle.getProducts()))
                .andExpect(model().attribute("userEmail", "useremail"))
                .andExpect(model().attribute("page", "index"));

        verify(bundleService, times(1)).getById(bundleId);
        verify(userService, times(1)).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenInvalidBundleId_whenGetIndexOfBundleId_thenReturnOkStatusAndAccessDeniedView() throws Exception {
        when(bundleService.count()).thenReturn(10L);
        mockMvc.perform(get("/index/{id}", 20))
                .andExpect(status().isOk())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void givenBundleCountIsZero_whenGetIndexOfBundleId_thenReturnIndexPageAndOkStatus() throws Exception {
        when(bundleService.count()).thenReturn(0L);
        mockMvc.perform(get("/index/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", roles = {"ADMIN"})
    public void givenValidBundleIdAndTotalBundleNumberIsGreaterThanZeroAndPricipalIsDefinedAndSessionUserEmailIsNull_whenGetIndexOfBundleId_thenReturnIndexViewWithGivenBundleAndSetupUserEmailAndOkStatus() throws Exception {
        int bundleId = 1;
        Bundle bundle = new Bundle();
        bundle.setId(bundleId);
        bundle.setPrice(BigDecimal.valueOf(10));
        bundle.setImageUrl("url");
        bundle.setName("name");
        bundle.setDescription("description");

        Set<Product> products = new HashSet<>();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(20));
        product.setDescription("product description");
        product.setName("product name");
        products.add(product);
        bundle.setProducts(products);

        when(bundleService.count()).thenReturn(10L);
        when(bundleService.getById(bundleId)).thenReturn(bundle);

        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName(anyString())).thenReturn(user);

        mockMvc.perform(get("/index/{id}", bundleId))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("bundle", bundle))
                .andExpect(model().attribute("products", bundle.getProducts()))
                .andExpect(model().attribute("userEmail", "useremail"))
                .andExpect(model().attribute("page", "index"));

        verify(bundleService, times(1)).getById(bundleId);
        verify(userService, times(1)).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }
}
