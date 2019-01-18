package com.app.integration.controller;

import com.app.commands.BundleForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.configurations.SpringSecurityConfig;
import com.app.converters.BundleFormToBundle;
import com.app.converters.BundleToBundleForm;
import com.app.domain.Bundle;
import com.app.services.BundleService;
import com.app.services.ProductService;
import com.app.services.sortList.ListSortingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc(secure = true)
public class BundleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetList_thenRetrieveAllBundlesFromDbAndReturnListOfThatBundles() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/list"))
                .andExpect(model().attributeExists("bundles"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetRootPath_thenRetrieveAllBundlesFromDbAndReturnListOfThatBundles() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/list"))
                .andExpect(model().attributeExists("bundles"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetNewBundle_thenReturnBundleForm() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attributeExists("bundleForm"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenShowBundle_thenFetchGivenBundleAndReturnViewWithIt() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/show"))
                .andExpect(model().attributeExists("bundle"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenEditGivenId_thenRetrieveThatBundleAndReturnBundleForm() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attributeExists("bundleForm"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndValidBundleForm_whenSaveBundle_thenBundleIsSavedIntoDbAndRedirectToShowGivenBundle() throws Exception {
        mockMvc.perform(post("/bundle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bundleName", "name")
                .param("bundleDescription", "description")
                .param("bundleImageUrl", "url")
                .param("bundlePrice", "10").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/bundle/show/*"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndInvalidBundleForm_whenSaveBundle_thenReturnBundleFormAndTheBundleIsNotSavedIntoDb() throws Exception {
        mockMvc.perform(post("/bundle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bundleName", "")
                .param("bundleDescription", "")
                .param("bundleImageUrl", "")
                .param("bundlePrice", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenDeleteBundle_thenRemoveBundleWithGivenIdFromDbAndAndRedirectToBundleList() throws Exception {
        mockMvc.perform(get("/bundle/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bundle/list"));
    }

    @Test
    public void givenNotAuthenticated_whenGetList_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenGetRootPath_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenGetNewList_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenShowBundle_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenEditBundle_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenSaveBundle_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/bundle/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bundleName", "name")
                .param("bundleDescription", "description")
                .param("bundleImageUrl", "url")
                .param("bundlePrice", "10").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndNoCsrfToken_whenSaveBundle_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/bundle/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bundleName", "name")
                .param("bundleDescription", "description")
                .param("bundleImageUrl", "url")
                .param("bundlePrice", "10"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenDeleteBundle_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/bundle/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
