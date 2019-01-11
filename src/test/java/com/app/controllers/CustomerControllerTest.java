package com.app.controllers;

import com.app.commands.UserForm;
import com.app.configuration.SpringSecurityTestConfig;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndValidCustomerForm_whenPostNewCustomer_thenReturnOkStatusAndCreateNewCustomerToDbAndRedirectToLoginPage() throws Exception {
        when(roleService.getById(anyInt())).thenReturn(mock(Role.class));

        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "username")
                .param("userEmail", "email")
                .param("userPassword", "passwd"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/login"));

        ArgumentCaptor<UserForm> formObjectArgument = ArgumentCaptor.forClass(UserForm.class);
        UserForm userForm = formObjectArgument.getValue();
        int dummyId = 1;
        verify(userForm, times(1)).addRole(roleService.getById(dummyId));
        verifyNoMoreInteractions(userForm);
        verify(userService, times(1)).saveOrUpdateUserForm(formObjectArgument.capture());
        verifyNoMoreInteractions(userService);

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    //@WithMockUser(username = "admin", roles { "ROLE_ADMIN})
    public void givenAuthUser_whenGetOrderHistory_thenReturnOkStatusAndOrderHistoryView() throws Exception {
        User mockUser = mock(User.class);
        mockUser.setUserName("username");
        when(userService.findByUserName(anyString())).thenReturn(mockUser);

        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/orderhistory"))
                .andExpect(model().attribute("user", mockUser));

        verify(userService).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenNoAuthUser_whenGetOrderHistory_thenReturnOkStatusAndAccessDeniedView() throws Exception {
        mockMvc.perform(get("/customer/orderhistory"))
                .andExpect(status().isOk())
                .andExpect(view().name("access_denied"));
    }

    @Test
    public void whenGetCreateUser_thenReturnOkStatusAndCustomerFormView() throws Exception {
        mockMvc.perform(get("/customer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerForm"));
    }


}
