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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
public class IndexControllerTest {

    @InjectMocks
    private IndexController indexController;

    @Mock
    private BundleService bundleService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @Mock
    private HttpSession session;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
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
        when(principal.getName()).thenReturn("user");

        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName(anyString())).thenReturn(user);
        String expectedView = "index";

        String actualView = indexController.home(model, principal, session);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("bundle", bundle);
        verify(model, times(1)).addAttribute("products", bundle.getProducts());
        verify(indexController, times(1)).setupUserEmail(model, principal, session);
        verify(bundleService, times(1)).getById(1);
        verify(userService, times(1)).findByUserName("admin");
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void givenTotalBundleCountIsGreaterThanZeroAndBundleIdIsValid_whenShowBundle_thenReturnIndexViewWithGivenBundleAndSetupUserEmail() throws Exception {
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
        when(principal.getName()).thenReturn("user");

        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName(anyString())).thenReturn(user);
        String expectedView = "index";

        String actualView = indexController.showBundle(1, model, principal, session);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("bundle", bundle);
        verify(model, times(1)).addAttribute("products", bundle.getProducts());
        verify(indexController, times(1)).setupUserEmail(model, principal, session);
        verify(bundleService, times(1)).getById(bundleId);
        verify(userService, times(1)).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenInvalidBundleId_whenGetIndexOfBundleId_thenAccessDeniedView() throws Exception {
        when(bundleService.count()).thenReturn(10L);
        String expectedView = "access_denied";

        String actualView = indexController.showBundle(15, model, principal, session);

        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void givenBundleCountIsZero_whenShowBundle_thenIndexPageAndDoNotSetupUserEmail() throws Exception {
        when(bundleService.count()).thenReturn(0L);
        String expectedView = "access_denied";

        String actualView = indexController.showBundle(15, model, principal, session);

        assertThat(actualView).isEqualTo(expectedView);
        verifyZeroInteractions(indexController);
    }

    @Test
    public void givenPrincipalAndSessionUserEmailIsDefinedAndSessionUserEmailIsEqualToUserEmail_whenSetupUserEmail_thenFetchUserAndInsertUserEmailIntoModel() {
        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName("user")).thenReturn(user);
        when(session.getAttribute("userEmail")).thenReturn("useremail");
        when(principal.getName()).thenReturn("user");

        indexController.setupUserEmail(model, principal, session);

        verify(model, times(1)).addAttribute("userEmail", "useremail");
        verify(userService, times(1)).findByUserName("user");
        verifyNoMoreInteractions(model);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenPrincipalAndSessionUserEmailIsDefinedAndSessionUserEmailIsNotEqualToUserEmail_whenSetupUserEmail_thenFetchUserAndInsertUserEmailIntoModel() {
        User user = new User();
        user.setEmail("useremail1");
        when(userService.findByUserName("user")).thenReturn(user);
        when(session.getAttribute("userEmail")).thenReturn("useremail2");
        when(principal.getName()).thenReturn("user");

        indexController.setupUserEmail(model, principal, session);

        verify(model, times(1)).addAttribute("userEmail", "useremail");
        verify(userService, times(1)).findByUserName("user");
        verifyZeroInteractions(model);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenPrincipalAndSessionUserEmailIsNull_whenSetupUserEmail_thenFetchUserAndInsertUserEmailIntoModel() {
        User user = new User();
        user.setEmail("useremail");
        when(userService.findByUserName("user")).thenReturn(user);
        when(principal.getName()).thenReturn("user");

        indexController.setupUserEmail(model, principal, session);

        verify(model, times(1)).addAttribute("userEmail", "useremail");
        verify(userService, times(1)).findByUserName("user");
        verifyNoMoreInteractions(model);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenNoPricipal_whenSetupUSerEmail_thenDoNothing() {
        indexController.setupUserEmail(model, null, session);

        verifyZeroInteractions(model);
    }
}
