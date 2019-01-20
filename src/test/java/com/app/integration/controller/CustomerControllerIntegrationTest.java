package com.app.integration.controller;


import com.app.commands.UserForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.domain.Address;
import com.app.domain.Role;
import com.app.domain.User;
import com.app.services.RoleService;
import com.app.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class}/*, SpringSecurityConfig.class}*/)
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc(secure = true)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", value = "admin", roles = {"ADMIN"})
    public void givenPrincipal_whenGetOrderHistory_thenFetchUserFromDbAndReturnHistoryOfThatUser() throws Exception {
        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/orderhistory"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    public void givenNoPrincipal_whenGetOrderHistory_thenAccessDeniedViewAndNoUserIsFetchedFromDb() throws Exception {
        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isFound())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void whenGetNewUser_thenReturnCustomerForm() throws Exception {
        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerform"));
    }

    @Test
    public void givenValidCustomerForm_whenSaveNewCustomer_thenSaveNewCustomerToDbAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "username")
                .param("userEmail", "email")
                .param("userPassword", "passwd").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));

        ArgumentCaptor<UserForm> formObjectArgument = ArgumentCaptor.forClass(UserForm.class);
    }

    @Test
    public void givenInvalidUserForm_whenSaveNewCustomer_thenReturnCustomerFormViewAndNoCustomerIsSavedIntoDb() throws Exception {
        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "")
                .param("userEmail", "")
                .param("userPassword", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerform"));

    }

    @Test
    @WithMockUser(username = "admin", value = "admin", roles = {"ADMIN"})
    public void givenPrincipal_whenGetCustomerSetting_thenFetchThatCustomerAndReturnUserForm() throws Exception {
        mockMvc.perform(get("/customer/setting"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userForm", isNotNull()))
                .andExpect(view().name("customer/customerform"));
    }

    @Test
    public void givenNoPrincipal_whenGetCustomerSetting_thenReturnAccessDeniedViewAndOkStatus() throws Exception {
        mockMvc.perform(get("/customer/setting"))
                .andExpect(view().name("access_denied"))
                .andExpect(status().isOk());
    }
}
