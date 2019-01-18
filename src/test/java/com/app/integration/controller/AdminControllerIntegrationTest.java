package com.app.integration.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetAccessDenied_thenReturnOkStatusAndAccessDeniedView() throws Exception {
        mockMvc.perform(get("/access_denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("access_denied"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetWorkInProgress_thenReturnOkStatusAndWorkInProgressView() throws Exception {
        mockMvc.perform(get("/workinprogress"))
                .andExpect(status().isOk())
                .andExpect(view().name("workinprogress"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetLogin_thenReturnOkStatusAndLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetAdmin_thenReturnOkStatusAndViewAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("initaited", true))
                .andExpect(view().name("admin"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetEvictCache_thenReturnOkStatusAndRedirectToAdmin() throws Exception {
        mockMvc.perform(get("/evictcache"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("http://localhost/admin"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void whenGetLogout_thenReturnOkStatusAndRedirectToStore() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("http://localhost/store"));
    }

    @Test
    public void givenNotAuthenticated_whenGetAdmin_thenReturnFoundStatusAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}