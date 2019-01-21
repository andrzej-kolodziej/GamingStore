package com.app.controllers;

import com.app.ResTexampleApplication;
import com.app.configuration.SpringSecurityTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private Model model;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenAccessDenied_thenAccessDeniedView() throws Exception {
        String expectedView = "access_denied";

        String actualView = adminController.notAuth();

        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void whenWorkInProgress_thenReturnWorkInProgressView() throws Exception {
        String expectedView = "workinprogress";

        String actualView = adminController.workInProgressPages();

        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void whenLogin_thenReturnLoginView() throws Exception {
        String expectedView = "login";

        String actualView = adminController.loginForm();

        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void whenAdmin_thenReturnViewAdmin() throws Exception {
        String expectedView = "admin";

        String actualView = adminController.admin(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("initaited", true);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenEvictCache_thenReturnRedirectToAdmin() throws Exception {
        String expectedView = "redirect:/admin";

        String actualView = adminController.evictcache();

        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void whenLogout_thenReturnRedirectToStore() throws Exception {
        String expectedView = "redirect:/store";

        String actualView = adminController.logout(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        assertThat(actualView).isEqualTo(expectedView);
    }
}