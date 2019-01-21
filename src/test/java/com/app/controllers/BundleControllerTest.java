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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class BundleControllerTest {

    @InjectMocks
    private BundleController bundleController;

    @Mock
    private BundleService bundleService;

    @Mock
    private ProductService productService;

    @Mock
    private ListSortingService listSortingService;

    @Mock
    private BundleFormToBundle bundleFormToBundle;

    @Mock
    private BundleToBundleForm bundleToBundleForm;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenListAll_thenRetrieveAllBundlesFromDbAndReturnListOfThatBundles() throws Exception {
        List mockBundles = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.setId(1);
        mockBundles.add(bundle);
        when(bundleService.listAll()).thenReturn(mockBundles);
        String expectedView = "bundle/list";

        String actualView = bundleController.listAll(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("bundles", mockBundles);
        verify(bundleService, times(1)).listAll();
        verifyNoMoreInteractions(bundleService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenNewBundle_thenReturnBundleForm() throws Exception {
        String expectedView = "bundle/bundleform";

        String actualView = bundleController.newBundle(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute(eq("bundleForm"), any(BundleForm.class));
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenShowBundle_thenFetchGivenBundleAndReturnViewWithIt() throws Exception {
        Bundle bundle = new Bundle();
        bundle.setId(1);
        bundle.setName("bundle");
        when(bundleService.getById(1)).thenReturn(bundle);
        String expectedView = "bundle/show";

        String actualView = bundleController.showBundle(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("bundle", bundle);
        verify(bundleService, times(1)).getById(1);
        verifyNoMoreInteractions(bundleService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenEditBundle_thenRetrieveThatBundleAndReturnBundleForm() throws Exception {
        Bundle bundle = new Bundle();
        bundle.setId(1);
        bundle.setName("bundle");
        bundle.setDescription("description");
        bundle.setImageUrl("url");
        bundle.setPrice(BigDecimal.valueOf(10));


        BundleForm bundleForm = new BundleForm();
        bundleForm.setBundleId(1);
        bundleForm.setBundleDescription("description");
        bundleForm.setBundleName("bundle");
        bundleForm.setBundleImageUrl("url");
        bundleForm.setBundlePrice(BigDecimal.valueOf(10));
        bundleForm.setBundlePruductIds(Arrays.asList(1,2));

        when(bundleService.getById(1)).thenReturn(bundle);
        when(bundleToBundleForm.convert(bundle)).thenReturn(bundleForm);

        String expectedView = "bundle/bundleform";

        String actualView = bundleController.editBundleForm(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("bundleForm", bundleForm);
        verify(bundleService, times(1)).getById(1);
        verify(bundleToBundleForm, times(1)).convert(bundle);
        verifyNoMoreInteractions(bundleService);
        verifyNoMoreInteractions(bundleToBundleForm);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void givenValidBundleForm_whenSaveBundle_thenBundleIsSavedIntoDbAndRedirectToShowGivenBundle() throws Exception {
        Bundle bundle = new Bundle();
        bundle.setId(1);
        bundle.setDescription("description");
        bundle.setImageUrl("url");
        bundle.setName("name");
        bundle.setPrice(BigDecimal.valueOf(10));

        BundleForm bundleForm = new BundleForm();
        bundleForm.setBundleId(1);
        bundleForm.setBundleDescription("description");
        bundleForm.setBundleName("bundle");
        bundleForm.setBundleImageUrl("url");
        bundleForm.setBundlePrice(BigDecimal.valueOf(10));
        bundleForm.setBundlePruductIds(Arrays.asList(1,2));

        when(bundleFormToBundle.convert(bundleForm)).thenReturn(bundle);
        when(bundleService.saveOrUpdate(any())).thenReturn(bundle);
        String expectedView = "redirect:/bundle/show/1";

        String actualView = bundleController.saveOrUpdateBundle(bundleForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        verify(bundleService, times(1)).saveOrUpdate(bundle);
        verify(bundleFormToBundle, times(1)).convert(bundleForm);
        verifyNoMoreInteractions(bundleService);
        verifyNoMoreInteractions(bundleFormToBundle);
    }

    @Test
    public void givenInvalidBundleForm_whenSaveBundle_thenReturnBundleFormAndTheBundleIsNotSavedIntoDb() throws Exception {
        BundleForm bundleForm = new BundleForm();
        bundleForm.setBundleId(1);
        bundleForm.setBundleDescription("");
        bundleForm.setBundleName("");
        bundleForm.setBundleImageUrl("");
        String expectedView = "bundle/bundleform";
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualView = bundleController.saveOrUpdateBundle(bundleForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        verify(bindingResult, times(1)).hasErrors();
        verifyZeroInteractions(bundleFormToBundle);
        verifyZeroInteractions(bundleService);
    }

    @Test
    public void whenDeleteBundle_thenRemoveBundleWithGivenIdFromDbAndAndRedirectToBundleList() throws Exception {
        String expectedView = "redirect:/bundle/list";

        String actualView = bundleController.deleteBundle(1);

        assertThat(actualView).isEqualTo(expectedView);
        verify(bundleService, times(1)).delete(1);
        verifyNoMoreInteractions(bundleService);
    }
}
