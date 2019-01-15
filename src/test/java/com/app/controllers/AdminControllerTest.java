package com.app.controllers;

import com.app.ResTexampleApplication;
import com.app.configuration.SpringSecurityTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        /*classes = SpringSecurityTestConfig.class*/)
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAccessDenied_thenReturnOkStatusAndAccessDeniedView() throws Exception {
        mockMvc.perform(get("/access_denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void whenGetWorkInProgress_thenReturnOkStatusAndWorkInProgressView() throws Exception {
        mockMvc.perform(get("/workinprogress"))
                .andExpect(status().isOk())
                .andExpect(view().name("workinprogress"));
    }

    @Test
    public void whenGetLogin_thenReturnOkStatusAndLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void whenGetAdmin_thenReturnOkStatusAndViewAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("initaited", true))
                .andExpect(view().name("admin"));
    }

    @Test
    public void whenGetEvictCache_thenReturnOkStatusAndRedirectToAdmin() throws Exception {
        mockMvc.perform(get("/evictcache"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("http://localhost/admin"));
    }

    @Test
    public void whenGetLogout_thenReturnOkStatusAndRedirectToStore() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("http://localhost/store"));
    }
}