package com.app.integration.controller;

import com.app.configuration.SpringSecurityTestConfig;
import com.app.domain.Bundle;
import com.app.domain.Product;
import com.app.domain.User;
import com.app.services.BundleService;
import com.app.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class/*, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc(secure = true)
public class IndexControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "admin", username = "admin", roles = {"ADMIN"})
    public void givenBundleAndPrincipal_whenGetIndexRoot_thenReturndBundleProductViewAndSetupUserEmail() throws Exception {
        mockMvc.perform(get("/")
                .sessionAttr("userEmail", "useremail"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("bundle"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("userEmail", "useremail"))
                .andExpect(model().attribute("page", "index"));
    }

    @Test
    public void givenInvalidBundleId_whenGetIndexOfBundleId_thenReturnOkStatusAndAccessDeniedView() throws Exception {
        mockMvc.perform(get("/index/{id}", 200))
                .andExpect(status().isOk())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void givenBundleCountIsZero_whenGetIndexOfBundleId_thenReturnIndexPageAndOkStatus() throws Exception {
        mockMvc.perform(get("/index/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", roles = {"ADMIN"})
    public void givenValidBundleIdAndTotalBundleNumberIsGreaterThanZeroAndPricipalIsDefinedAndSessionUserEmailIsNull_whenGetIndexOfBundleId_thenReturnIndexViewWithGivenBundleAndSetupUserEmailAndOkStatus() throws Exception {
        mockMvc.perform(get("/index/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("bundle"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("userEmail", "useremail"))
                .andExpect(model().attribute("page", "index"));
    }
}
