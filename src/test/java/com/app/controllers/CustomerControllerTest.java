package com.app.controllers;

import com.app.configuration.SpringSecurityTestConfig;
import com.app.services.*;
import com.app.services.security.EncryptionService;
import com.app.services.security.SpringSecUserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
    //@WithMockUser(username = "admin", roles { "ROLE_ADMIN})
    public void givenAuthUser_whenGetOrderHistory_thenReturnOkStatusAndOrderHistoryView() throws Exception {
        mockMvc.perform(get("/customer/orderhistory")).andExpect(status().isOk()).andExpect(view().name("customer/orderhistory"));
        verify(userService).findByUserName("admin");
        verifyNoMoreInteractions(userService);
    }
}
