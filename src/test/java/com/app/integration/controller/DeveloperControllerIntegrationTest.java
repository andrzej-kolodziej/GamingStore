package com.app.integration.controller;

import com.app.commands.DeveloperForm;
import com.app.configuration.SpringSecurityTestConfig;
import com.app.converters.DeveloperFormToDeveloper;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.services.DeveloperService;
import com.app.services.ProductService;
import com.app.services.PublisherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
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

        classes = {SpringSecurityTestConfig.class/*, /*SpringSecurityConfig.class*/})
@TestPropertySource(
        locations = "classpath:test.application.properties")
@AutoConfigureMockMvc(secure = false)
public class DeveloperControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetDevelopersList_thenFetchAllDevelopersAndReturnDeveloperList() throws Exception {
        mockMvc.perform(get("/developer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/list"))
                .andExpect(model().attributeExists("developers"));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetDevelopersRootPath_thenFetchAllDevelopersAndReturnDeveloperList() throws Exception {
        mockMvc.perform(get("/developer/"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/list"))
                .andExpect(model().attributeExists("developers"));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenShowDeveloperWithGivenId_thenGetThatDeveloperFromDbAndReturnViewWithGivenDeveloper() throws Exception {
        mockMvc.perform(get("/developer/show/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/show"))
                .andExpect(model().attributeExists("developer"));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenGetNewDeveloper_thenReturnDeveloperFormView() throws Exception {
        mockMvc.perform(get("/developer/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"))
                .andExpect(model().attributeExists("developerForm"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenAddDeveloperProduct_thenAddNewProductToDeveloperAndReturnDeveloperForm() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperImageUrl("url");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperProducts(new ArrayList<>());

        mockMvc.perform(get("/developer?addProduct")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                /*.param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description"))*/
                .flashAttr("developerForm", developerForm))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));

        List<Product> products =  developerForm.getDeveloperProducts();
        assertThat(products.get(products.size()-1)).isNotNull();
        assertThat(products.get(products.size()-1)).isOfAnyClassIn(Product.class);
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenRemoveDeveloperProduct_thenRemoveThatProductFromDeveloperAndReturnDeveloperForm() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperImageUrl("url");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperProducts(new ArrayList<>());
        developerForm.getDeveloperProducts().add(new Product());
        developerForm.getDeveloperProducts().add(new Product());

        mockMvc.perform(get("/developer?removeProduct")
                //.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                /*.param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description")
                .requestAttr("removeProduct", "1"))*/
                .requestAttr("removeProduct", 1)
                .flashAttr("developerForm", developerForm).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"));

        assertThat(developerForm.getDeveloperProducts().get(1)).isNull();
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserValidDeveloperForm_whenSaveDeveloper_thenSaveDeveloperIntoDbAndRedirectToShowDeveloperPage() throws Exception {
        Developer developer = new Developer();
        developer.setName("name");
        developer.setDescription("description");
        developer.setId(1);
        developer.setImageUrl("url");

        mockMvc.perform(post("/developer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/developer/show/1"));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUserInvalidDeveloperForm_whenSaveDeveloper_thenDoNotSaveDeveloperIntoDbAndReturnDeveloperForm() throws Exception {
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
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenEditDeveloper_thenFetchThatDeveloperFromDbAndReturnDeveloperFormView() throws Exception {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperName("name");
        developerForm.setDeveloperDescription("description");
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperImageUrl("url");

        mockMvc.perform(get("/developer/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("developer/developerform"))
                .andExpect(model().attribute("developerForm", developerForm));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void givenAuthUser_whenDeleteDeveloper_thenRemoveDeveloperFromDbAndRedirectToDeveloperList() throws Exception {
        mockMvc.perform(get("/developer/delete/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/developer/list"));

    }

    @Test
    public void givenNotAuthenticated_whenGetRootPath_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenShowDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer/show/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenGetNewDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenAddDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer?addProduct")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "name")
                .param("developerImageUrl", "url")
                .param("developerId", "1")
                .param("developerDescription", "description").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenRemoveDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer?removeProduct")
                .requestAttr("removeProduct", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenSaveDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/developer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("developerName", "")
                .param("developerImageUrl", "")
                .param("developerId", "1")
                .param("developerDescription", "").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenEditDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer/edit/{id}", 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void givenNotAuthenticated_whenDeleteDeveloper_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/developer/delete/{id}"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
