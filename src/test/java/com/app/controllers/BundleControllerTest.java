package com.app.controllers;

import com.app.commands.BundleForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.converters.BundleFormToBundle;
import com.app.converters.BundleToBundleForm;
import com.app.domain.Bundle;
import com.app.services.BundleService;
import com.app.services.ProductService;
import com.app.services.sortList.ListSortingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityTestConfig.class, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:application.properties")
@AutoConfigureMockMvc(secure = false)
public class BundleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BundleService bundleService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ListSortingService listSortingService;

    @MockBean
    private BundleFormToBundle bundleFormToBundle;

    @MockBean
    private BundleToBundleForm bundleToBundleForm;

    @Test
    public void givenNotAuthUser_whenGetList_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthUser_whenGetRootPath_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetList_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetRoot_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetList_thenReturnOkStatusAndReturnBundleList() throws Exception {
        List mockBundles = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.setId(1);
        mockBundles.add(bundle);
        when(bundleService.listAll()).thenReturn(mockBundles);
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/list"))
                .andExpect(model().attribute("bundles", mockBundles));

        verify(bundleService, times(1)).listAll();
        verifyNoMoreInteractions(bundleService);
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetRootPath_thenReturnOkStatusAndReturnBundleList() throws Exception {
        List mockBundles = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.setId(1);
        mockBundles.add(bundle);
        when(bundleService.listAll()).thenReturn(mockBundles);
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/list"))
                .andExpect(model().attribute("bundles", mockBundles));

        verify(bundleService, times(1)).listAll();
        verifyNoMoreInteractions(bundleService);
    }

    @Test(expected = NullPointerException.class)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndBundleServiceReturnsNull_whenGetList_thenReturnOkStatusAndThrowsException() throws Exception {
        when(bundleService.listAll()).thenReturn(null);
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bundles", null));
    }

    @Test(expected = NullPointerException.class)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserAndBundleServiceReturnsNull_whenGetRootPath_thenReturnOkStatusAndThrowsException() throws Exception {
        when(bundleService.listAll()).thenReturn(null);
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bundles", null));
    }

    @Test
    public void givenNotAuthUser_whenGetNew_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetNew_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetNew_thenReturnOkStatusAndReturnBundleForm() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attributeExists("bundleForm"));
    }

    @Test
    public void givenNotAuthUser_whenGetShowGivenId_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetShowGivenId_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/show/{id}"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetShowGivenId_thenReturnOkStatusAndReturnGivenBundle() throws Exception {
        Bundle mockBundle = mock(Bundle.class);
        when(bundleService.getById(anyInt())).thenReturn(mockBundle);

        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/show"))
                .andExpect(model().attribute("bundle", mockBundle));

        verify(bundleService, times(1)).getById(anyInt());
        verifyNoMoreInteractions(bundleService);
    }

    @Test(expected = NestedServletException.class)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserBundleServiceReturnsNull_whenGetShowGivenId_thenReturnOkStatusAndThrowException() throws Exception {
        when(bundleService.getById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/show"))
                .andExpect(model().attribute("bundle", null));
    }

    @Test
    public void givenNotAuthUser_whenGetEditGivenId_thenReturnFoundStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserWithoutAdminRole_whenGetEditGivenId_thenReturnForbiddenStatusAndRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bundle/edit/{id}"))
                .andExpect(status().isForbidden())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetEditGivenId_thenReturnOkStatusAndReturnBundleForm() throws Exception {
        Bundle mockBundle = mock(Bundle.class);
        BundleToBundleForm bundleToBundleFormMock = mock(BundleToBundleForm.class);

        BundleForm bundleFormMock = mock(BundleForm.class);
        bundleFormMock.setBundleId(1);
        bundleFormMock.setBundleDescription("description");
        bundleFormMock.setBundleName("name");
        bundleFormMock.setBundleImageUrl("url");
        bundleFormMock.setBundlePrice(BigDecimal.valueOf(10));
        bundleFormMock.setBundleVersion(10);
        bundleFormMock.setBundlePruductIds(Arrays.asList(1,2));

        when(bundleService.getById(anyInt())).thenReturn(mockBundle);
        when(bundleToBundleFormMock.convert(mockBundle)).thenReturn(bundleFormMock);

        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attribute("bundleForm", bundleFormMock));

        verify(bundleService, times(1)).getById(anyInt());
        BundleForm actualBundleForm = verify(bundleToBundleFormMock, times(1)).convert(mockBundle);
        assertThat(actualBundleForm).isEqualTo(bundleFormMock);
        verifyNoMoreInteractions(bundleService);
        verifyNoMoreInteractions(bundleToBundleFormMock);
    }

    @Test(expected = NullPointerException.class)
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserBundleServiceReturnsNull_whenGetEditGivenId_thenReturnOkStatusAndThrowException() throws Exception {
        when(bundleService.getById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attribute("bundleForm", null));

        verify(bundleService, times(1)).getById(anyInt());
        verifyNoMoreInteractions(bundleService);
    }

}
