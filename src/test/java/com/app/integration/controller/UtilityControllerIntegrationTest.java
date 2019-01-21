package com.app.integration.controller;

import com.app.configuration.SpringSecurityTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class/*, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc(secure = true)
public class UtilityControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAbout_thenReturnAboutView() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attribute("page", "about"));
    }

    @Test
    public void whenGetWorkInProgress_thenReturnWorkInProgressView() throws Exception {
        mockMvc.perform(get("/workinprogress"))
                .andExpect(status().isOk())
                .andExpect(view().name("workinprogress"))
                .andExpect(model().attribute("page", "about"));
    }

    @Test
    public void whenGetPrivacyPolicy_thenReturnWorkInProgressView() throws Exception {
        mockMvc.perform(get("/privacy-policy"))
                .andExpect(status().isOk())
                .andExpect(view().name("workinprogress"))
                .andExpect(model().attribute("page", "about"));
    }

    @Test
    public void whenGetUserAgreement_thenReturnWorkInProgressView() throws Exception {
        mockMvc.perform(get("/user-agreement"))
                .andExpect(status().isOk())
                .andExpect(view().name("workinprogress"))
                .andExpect(model().attribute("page", "about"));
    }
}
