package com.app.controllers;

import com.app.commands.BundleForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.configurations.SpringSecurityConfig;
import com.app.converters.BundleFormToBundle;
import com.app.converters.BundleToBundleForm;
import com.app.domain.Bundle;
import com.app.services.BundleService;
import com.app.services.ProductService;
import com.app.services.sortList.ListSortingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,

        classes = {/*SpringSecurityTestConfig.class,*/ /*SpringSecurityConfig.class*/})
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
    public void whenGetList_thenReturnOkStatusAndReturnBundleList() throws Exception {
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
    public void givenBundleServiceReturnsNull_whenGetList_thenReturnOkStatusAndThrowsException() throws Exception {
        when(bundleService.listAll()).thenReturn(null);
        mockMvc.perform(get("/bundle/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bundles", null));
    }

    @Test(expected = NullPointerException.class)
    public void givenBundleServiceReturnsNull_whenGetRootPath_thenReturnOkStatusAndThrowsException() throws Exception {
        when(bundleService.listAll()).thenReturn(null);
        mockMvc.perform(get("/bundle/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bundles", null));
    }

    @Test
    public void whenGetNew_thenReturnOkStatusAndReturnBundleForm() throws Exception {
        mockMvc.perform(get("/bundle/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attributeExists("bundleForm"));
    }

    @Test
    public void _whenGetShowGivenId_thenReturnOkStatusAndReturnGivenBundle() throws Exception {
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
    public void givenBundleServiceReturnsNull_whenGetShowGivenId_thenReturnOkStatusAndThrowException() throws Exception {
        when(bundleService.getById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/bundle/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/show"))
                .andExpect(model().attribute("bundle", null));
    }

    @Test
    public void whenGetEditGivenId_thenReturnOkStatusAndReturnBundleForm() throws Exception {
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
    public void givenBundleServiceReturnsNull_whenGetEditGivenId_thenReturnOkStatusAndThrowException() throws Exception {
        when(bundleService.getById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/bundle/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("bundle/bundleform"))
                .andExpect(model().attribute("bundleForm", null));

        verify(bundleService, times(1)).getById(anyInt());
        verifyNoMoreInteractions(bundleService);
    }

    @Test
    public void givenValidBundleForm_whenPostToSaveOrUpdateBundle_thenBundleIsSavedIntoDbANdRedirectedToBundleShow() throws Exception {
        Bundle bundleMock = mock(Bundle.class);
        bundleMock.setId(1);
        bundleMock.setDescription("description");
        bundleMock.setImageUrl("url");
        bundleMock.setName("name");
        bundleMock.setPrice(BigDecimal.valueOf(10));

        when(bundleService.saveOrUpdate(any())).thenReturn(bundleMock);

        mockMvc.perform(post("/bundle")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bundleName", "name")
                .param("bundleDescription", "description")
                .param("bundleImageUrl", "url")
                .param("bundlePrice", "10").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bundle/show/0"));

        verify(bundleService, times(1)).saveOrUpdate(any());
        verifyNoMoreInteractions(bundleService);
    }
}
