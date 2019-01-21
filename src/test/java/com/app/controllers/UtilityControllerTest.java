package com.app.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
public class UtilityControllerTest {
    @InjectMocks
    private UtilityController utilityController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenGetUnderConstruction_thenReturnWorkInProgressView() {
        String expectedView = "workinprogress";
        String actualView = utilityController.underConstruction();
        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void whenGetAbout_thenReturnWorkAboutView() {
        String expectedView = "about";
        String actualView = utilityController.doGet();
        assertThat(actualView).isEqualTo(expectedView);
    }
}
