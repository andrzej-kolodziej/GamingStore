package com.app.controllers;


import com.app.commands.UserForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.configurations.SpringSecurityConfig;
import com.app.domain.Address;
import com.app.domain.Role;
import com.app.domain.User;
import com.app.services.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        /*classes = {SpringSecurityTestConfig.class, SpringSecurityConfig.class}*/)
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc(secure = true)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "admin", value = "admin", roles = {"ADMIN"})
    public void whenGetOrderHistory_thenReturnOkStatusAndOrderHistoryView() throws Exception {
        User mockUser = mock(User.class);
        mockUser.setUserName("username");
        when(userService.findByUserName(anyString())).thenReturn(mockUser);

        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/orderhistory"))
                .andExpect(model().attribute("user", mockUser));

        verify(userService).findByUserName(eq("admin"));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenNoPrincipal_whenGetOrderHistory_thenReturnFoundStatusAndAccessDeniedView() throws Exception {
        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isFound())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void whenGetCreateUser_thenReturnOkStatusAndCustomerFormView() throws Exception {
        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerform"));
    }

    @Test
    public void givenValidCustomerForm_whenPostNewCustomer_thenReturnFoundStatusAndCreateNewCustomerToDbAndRedirectToLoginPage() throws Exception {
        when(roleService.getById(anyInt())).thenReturn(mock(Role.class));

        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "username")
                .param("userEmail", "email")
                .param("userPassword", "passwd").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));

        ArgumentCaptor<UserForm> formObjectArgument = ArgumentCaptor.forClass(UserForm.class);
        verify(userService, times(1)).saveOrUpdateUserForm(formObjectArgument.capture());
        verifyNoMoreInteractions(userService);
    }


    @Test
    public void givenInvalidUserForm_whenPostNewCustomer_thenReturnCustomerFormView() throws Exception {
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
    public void givenPrincipal_whenGetCustomerSetting_thenReturnUserForm() throws Exception {
        User mockUser = mock(User.class);
        mockUser.setUserName("admin");
        mockUser.setId(1);

        UserForm userFormMock = mock(UserForm.class);
        userFormMock.setUserName("admin");
        userFormMock.setUserPassword("passwd");
        Address userBillingAddress = new Address();
        userBillingAddress.setAddressLine1("address line 1");
        userBillingAddress.setAddressLine2("address line 2");
        userFormMock.setUserBillingAddress(userBillingAddress);

        when(userService.findByUserName("admin")).thenReturn(mockUser);
        when(userService.findUserFormById(anyInt())).thenReturn(userFormMock);

        mockMvc.perform(get("/customer/setting"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userForm", isNotNull()))
                .andExpect(view().name("customer/customerform"));

        ArgumentCaptor<UserForm> userFormArgumentCaptor = ArgumentCaptor.forClass(UserForm.class);
        verify(userService, times(1)).findUserFormById(eq(1));
        verify(userService, times(1)).findByUserName(eq("admin"));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenNoPrincipal_whenGetCustomerSetting_thenReturnAccessDeniedViewAndOkStatus() throws Exception {
        mockMvc.perform(get("/customer/setting"))
                .andExpect(view().name("access_denied"))
                .andExpect(status().isOk());
    }
}
