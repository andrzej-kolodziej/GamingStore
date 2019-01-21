package com.app.controllers;

import com.app.commands.DeveloperForm;
import com.app.converters.DeveloperFormToDeveloper;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.services.DeveloperService;
import com.app.services.ProductService;
import com.app.services.PublisherService;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class DeveloperControllerTest {

    @InjectMocks
    private DeveloperController developerController;

    @Mock
    private DeveloperService developerService;

    @Mock
    private ProductService productService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private DeveloperFormToDeveloper developerFormToDeveloper;

    @Mock
    private DeveloperForm developerForm;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenGetDevelopersList_thenFetchAllDevelopersAndReturnDeveloperList() throws Exception {
        List developers = new ArrayList<>();
        Developer developer = new Developer();
        developer.setName("developer");
        developer.setImageUrl("url");
        developer.setDescription("description");
        developers.add(developer);
        when(developerService.listAll()).thenReturn(developers);
        String expectedView = "developer/list";

        String actualView = developerController.list(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("developers", developers);
        verify(developerService, times(1)).listAll();
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenShowDeveloperWithGivenId_thenGetThatDeveloperFromDbAndReturnViewWithGivenDeveloper() throws Exception {
        Developer developer = new Developer();
        developer.setId(1);
        developer.setName("developer");
        developer.setImageUrl("url");
        developer.setDescription("description");
        when(developerService.getById(1)).thenReturn(developer);
        String expectedView = "developer/show";

        String actualView = developerController.show(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(developerService, times(1)).getById(anyInt());
        verify(model, times(1)).addAttribute("developer", developer);
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenGetNewDeveloper_thenReturnDeveloperFormView() throws Exception {
        String expectedView = "developer/developerform";

        String actualView = developerController.newDeveloper(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute(eq("developerForm"), any(DeveloperForm.class));
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenAddDeveloperProduct_thenAddNewProductToDeveloperAndReturnDeveloperForm() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperImageUrl("url");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperProducts(new ArrayList<>());
        String expectedView = "developer/developerform";

        String actualView = developerController.addProduct(developerForm);

        assertThat(actualView).isEqualTo(expectedView);
        List<Product> products =  developerForm.getDeveloperProducts();
        assertThat(products.get(products.size()-1)).isNotNull();
        assertThat(products.get(products.size()-1)).isOfAnyClassIn(Product.class);
    }

    @Test
    public void whenRemoveDeveloperProduct_thenRemoveThatProductFromDeveloperAndReturnDeveloperForm() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperImageUrl("url");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperProducts(new ArrayList<>());
        developerForm.getDeveloperProducts().add(new Product());
        developerForm.getDeveloperProducts().add(new Product());
        String expectedView = "developer/developerform";
        when(httpServletRequest.getParameter("removeProduct")).thenReturn("1");

        String actualView = developerController.removeProduct(developerForm, httpServletRequest);

        assertThat(actualView).isEqualTo(expectedView);
        assertThat(developerForm.getDeveloperProducts().size()).isEqualTo(1);
    }

    @Test
    public void givenValidDeveloperForm_whenSaveDeveloper_thenSaveDeveloperIntoDbAndRedirectToShowDeveloperPage() throws Exception {
        Developer developer = new Developer();
        developer.setName("name");
        developer.setDescription("description");
        developer.setId(1);
        developer.setImageUrl("url");

        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperImageUrl("url");

        when(developerService.saveOrUpdateDeveloperForm(developerForm)).thenReturn(developer);
        String expectedView = "redirect:/developer/show/1";

        String actualView = developerController.save(developerForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        verify(developerService, times(1)).saveOrUpdateDeveloperForm(any(DeveloperForm.class));
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void givenInvalidDeveloperForm_whenSaveDeveloper_thenDoNotSaveDeveloperIntoDbAndReturnDeveloperForm() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperName("");
        developerForm.setDeveloperDescription("");
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperImageUrl("");
        when(bindingResult.hasErrors()).thenReturn(true);
        String expectedView = "developer/developerform";

        String actualView = developerController.save(developerForm, bindingResult);

        assertThat(actualView).isEqualTo(expectedView);
        verifyZeroInteractions(developerService);
    }

    @Test
    public void whenEditDeveloper_thenFetchThatDeveloperFromDbAndReturnDeveloperFormView() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperImageUrl("url");
        when(developerService.getDeveloperFormById(1)).thenReturn(developerForm);
        String expectedView = "developer/developerform";

        String actualView = developerController.edit(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model ,times(1)).addAttribute("developerForm", developerForm);
        verify(developerService,  times(1)).getDeveloperFormById(anyInt());
        verifyNoMoreInteractions(developerService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenDeleteDeveloper_thenRemoveDeveloperFromDbAndRedirectToDeveloperList() throws Exception {
        String expectedView = "redirect:/developer/list";

        String actualView = developerController.delete(1);

        assertThat(actualView).isEqualTo(expectedView);

        verify(developerService, times(1)).delete(1);
        verifyNoMoreInteractions(developerService);
    }
}
