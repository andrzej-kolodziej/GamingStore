package com.app.controllers;

import com.app.commands.UserForm;
import com.app.domain.User;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        /*classes = {SpringSecurityTestConfig.class, SpringSecurityConfig.class}*/)
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndNoCsrfToken_whenPostNewCustomer_thenReturnForbiddenStatus() throws Exception {
        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "username")
                .param("userEmail", "email")
                .param("userPassword", "passwd"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenValidFormWithCsrfTokenAndNotAuthUser_whenPostNewCustomer_thenReturnForbiddenStatus() throws Exception {
        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "username")
                .param("userEmail", "email")
                .param("userPassword", "passwd").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetCustomerSetting_thenReturnUserForm() throws Exception {
        mockMvc.perform(get("/customer/setting"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerform"));
    }

    @Test
    public void givenNotAuthUser_whenGetCustomerSetting_thenReturnAccessDeniedViewAndOkStatus() throws Exception {
        mockMvc.perform(get("/customer/setting"))
                .andExpect(view().name("access_denied"))
                .andExpect(status().isOk());
    }
}
