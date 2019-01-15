package com.app.controllers;

import com.app.configuration.SpringSecurityTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class BundleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenNotAuthUser_whenGetList_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthUser_whenGetRootPath_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetList_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetRoot_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthUser_whenGetNew_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetNew_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthUser_whenGetShowGivenId_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetShowGivenId_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthUser_whenGetEditGivenId_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetEditGivenId_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}
