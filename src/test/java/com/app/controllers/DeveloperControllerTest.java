package com.app.controllers;

import com.app.commands.DeveloperForm;
import com.app.converters.DeveloperFormToDeveloper;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.services.DeveloperService;
import com.app.services.ProductService;
import com.app.services.PublisherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
public class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperService developerService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private DeveloperFormToDeveloper developerFormToDeveloper;

    @MockBean
    private DeveloperForm developerForm;

    @Test
    public void whenGetDevelopersList_thenReturnOkStatusAndDeveloperList() throws Exception {
        List developers = new ArrayList<>();
        Developer developer = new Developer();
        developer.setName("developer");
        developer.setImageUrl("url");
        developer.setDescription("description");
        developers.add(developer);

        when(developerService.listAll()).thenReturn(developers);

        mockMvc.perform(get("/developer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/list"))
                .andExpect(model().attribute("developers", developers));

        verify(developerService, times(1)).listAll();
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void whenGetDevelopersRootPath_thenReturnOkStatusAndDeveloperList() throws Exception {
        List developers = new ArrayList<>();
        Developer developer = new Developer();
        developer.setName("developer");
        developer.setImageUrl("url");
        developer.setDescription("description");
        developers.add(developer);

        when(developerService.listAll()).thenReturn(developers);

        mockMvc.perform(get("/developer/"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/list"))
                .andExpect(model().attribute("developers", developers));

        verify(developerService, times(1)).listAll();
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void whenGetShowDeveloperWithGivenId_thenReturnOkStatusAndViewWithGivenDeveloper() throws Exception {
        Developer developer = new Developer();
        developer.setId(1);
        developer.setName("developer");
        developer.setImageUrl("url");
        developer.setDescription("description");

        when(developerService.getById(anyInt())).thenReturn(developer);

        mockMvc.perform(get("/developer/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/show"))
                .andExpect(model().attribute("developer", developer));

        verify(developerService, times(1)).getById(anyInt());
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void whenGetNewDeveloper_thenReturnOkStatusAndDeveloperFormView() throws Exception {
        mockMvc.perform(get("/developer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"))
                .andExpect(model().attributeExists("developerForm"));
    }

    @Test
    public void whenAddDeveloperProduct_thenReturnOkStatusAndAddDeveloperProductForm() throws Exception {
        mockMvc.perform(get("/developer?addProduct")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));
    }

    @Test
    public void whenRemoveDeveloperProduct_thenReturnOkStatusAndDeveloperForm() throws Exception {
        mockMvc.perform(get("/developer?removeProduct")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description")
                .requestAttr("removeProduct", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));
    }

    @Test
    public void givenValidDeveloperForm_whenSaveDeveloper_thenReturnFoundStatusAndSaveDeveloperIntoDbAndRedirectToShowDeveloperPage() throws Exception {
        Developer developer = new Developer();
        developer.setName("name");
        developer.setDescription("description");
        developer.setId(1);
        developer.setImageUrl("url");
        when(developerService.saveOrUpdateDeveloperForm(any(DeveloperForm.class))).thenReturn(developer);

        mockMvc.perform(post("/developer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/developer/show/1"));

        verify(developerService, times(1)).saveOrUpdateDeveloperForm(any(DeveloperForm.class));
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void givenInvalidDeveloperForm_whenSaveDeveloper_thenReturnOkStatusAndSaveDeveloperIntoDbAndRedirectToShowDeveloperPage() throws Exception {
        mockMvc.perform(post("/developer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "")
                .param("developerImageUrl", "")
                .param("developerId", "1")
                .param("developerDescription", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));
    }

    @Test
    public void whenEditDeveloper_thenReturnOkStatusAndDeveloperFormView() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperImageUrl("url");
        when(developerService.getDeveloperFormById(anyInt())).thenReturn(developerForm);

        mockMvc.perform(get("/developer/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));

        verify(developerService,  times(1)).getDeveloperFormById(anyInt());
        verifyNoMoreInteractions(developerService);
    }

    @Test
    public void whenDeleteDeveloper_thenRemoveDeveloperFromDbAndReturnFoundStatusAndRedirectToDeveloperList() throws Exception {
        mockMvc.perform(get("/developer/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/developer/list"));

        verify(developerService, times(1)).delete(1);
        verifyNoMoreInteractions(developerService);
    }
}
