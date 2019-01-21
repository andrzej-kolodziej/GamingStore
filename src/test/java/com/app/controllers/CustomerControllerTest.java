package com.app.controllers;


import com.app.commands.UserForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.configurations.SpringSecurityConfig;
import com.app.domain.Address;
import com.app.domain.Role;
import com.app.domain.User;
import com.app.services.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenPrincipal_whenGetOrderHistory_thenFetchUserFromDbAndReturnHistoryOfThatUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUserName("username");
        when(principal.getName()).thenReturn("user");
        when(userService.findByUserName("user")).thenReturn(user);
        String expectedView = "customer/orderhistory";

        String actualView = customerController.orderHistory(model, principal);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("user", user);
        verify(userService, times(1)).findByUserName("user");
        verifyNoMoreInteractions(userService);
        verifyZeroInteractions(model);
    }

    @Test
    public void givenNoPrincipal_whenGetOrderHistory_thenAccessDeniedViewAndNoUserIsFetchedFromDb() throws Exception {
        String expectedView = "access_denied";

        String actualView = customerController.orderHistory(model, null);

        assertThat(actualView).isEqualTo(expectedView);

        verifyZeroInteractions(userService);
    }

    @Test
    public void whenCreateUser_thenReturnCustomerForm() throws Exception {
        String expectedView = "customer/customerform";

        String actualView = customerController.createUser(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute(eq("userForm"), any(UserForm.class));
        verifyNoMoreInteractions(model);
    }

    @Test
    public void givenValidCustomerForm_whenSaveNewCustomer_thenSaveNewCustomerToDbAndRedirectToLoginPage() throws Exception {
        Role role = new Role();
        role.setRole("ROLE_ADMIN");
        role.setId(1);

        UserForm userForm = new UserForm();
        userForm.setUserName("user");
        userForm.setRoles(new ArrayList<>());
        userForm.setUserId(1);
        userForm.setUserEmail("email");

        when(roleService.getById(1)).thenReturn(role);
        String expectedView = "redirect:/login";

        String actualView = customerController.saveOrUpdateCustomer(userForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        assertThat(userForm.getRoles().get(0).getId()).isEqualTo(role.getId());
        verify(roleService, times(1)).getById(anyInt());
        verify(userService, times(1)).saveOrUpdateUserForm(userForm);

        verifyNoMoreInteractions(userService);
        verifyZeroInteractions(roleService);
    }

    @Test
    public void givenInvalidUserForm_whenSaveNewCustomer_thenReturnCustomerFormViewAndNoCustomerIsSavedIntoDb() throws Exception {
        UserForm userForm = new UserForm();
        userForm.setUserName("");
        userForm.setRoles(new ArrayList<>());
        userForm.setUserId(1);
        userForm.setUserEmail("");
        when(bindingResult.hasErrors()).thenReturn(true);
        String expectedView = "customer/customerform";

        String actualView = customerController.saveOrUpdateCustomer(userForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        verifyNoMoreInteractions(roleService);
        verifyZeroInteractions(userService);
    }

    @Test
    public void givenPrincipal_whenGetCustomerSetting_thenFetchThatCustomerAndReturnUserForm() throws Exception {
        User user = new User();
        user.setUserName("admin");
        user.setId(1);

        UserForm userForm = new UserForm();
        userForm.setUserName("admin");
        userForm.setUserPassword("passwd");

        when(principal.getName()).thenReturn("admin");
        when(userService.findByUserName("admin")).thenReturn(user);
        when(userService.findUserFormById(1)).thenReturn(userForm);
        String expectedView = "customer/customerform";

        String actualView = customerController.customerSetting(model, principal);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("userForm", userForm);
        verify(userService, times(1)).findUserFormById(eq(1));
        verify(userService, times(1)).findByUserName(eq("admin"));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenNoPrincipal_whenGetCustomerSetting_thenReturnAccessDeniedViewAndOkStatus() throws Exception {
        String expectedView = "access_denied";

        String actualView = customerController.customerSetting(model, null);

        assertThat(actualView).isEqualTo(expectedView);
    }
}
