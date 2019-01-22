package com.app.controllers;

import com.app.domain.Developer;
import com.app.domain.Publisher;
import com.app.domain.Publisher;
import com.app.services.PublisherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PublisherControllerTest {
    @InjectMocks
    private PublisherController publisherController;

    @Mock
    private PublisherService publisherService;
    
    @Mock
    private Model model;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenGetPublishersList_thenReturnAllPublishers() throws Exception {
        List publishers = new ArrayList<>();
        Publisher publisher = new Publisher();
        publisher.setName("name");
        publishers.add(publisher);

        when(publisherService.listAll()).thenReturn(publishers);
        String expectedView = "publisher/list";

        String actualView = publisherController.listAll(model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("publishers", publishers);
        verify(publisherService, times(1)).listAll();
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenShowPublisher_thenFetchPublisherFromDbAndReturnViewWithThatPublisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setId(1);
        when(publisherService.getById(1)).thenReturn(publisher);

        String expectedView = "publisher/show";

        String actualView = publisherController.show(1, model);

        assertThat(actualView).isEqualTo(expectedView);
        verify(model, times(1)).addAttribute("publisher", publisher);
        verify(publisherService, times(1)).getById(1);
        verifyNoMoreInteractions(publisherService);
    }

    @Test
    public void whenGetNewPublisher_thenRetrieveAndListAllDevelopersAndPublishersFromDbAndReturnPublisherForm() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        List publishers = new ArrayList();
        publishers.add(publisher);

        String expectedView = "publisher/publisherform";

        String actualView = publisherController.newPublisher(model);

        assertThat(actualView).isEqualTo(expectedView);

        verify(model, times(1)).addAttribute(eq("publisher"), any(Publisher.class));
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenSavePublisher_thenPersistThatPublisherIntoDbAndRedirectToShowThePublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        publisher.setId(1);
        when(publisherService.saveOrUpdate(publisher)).thenReturn(publisher);
        String expectedView = "redirect:/publisher/show/1";

        String actualView = publisherController.saveOrUpdate(publisher);

        assertThat(actualView).isEqualTo(expectedView);
        verify(publisherService, times(1)).saveOrUpdate(publisher);
        verifyNoMoreInteractions(publisherService);
    }

    @Test
    public void whenEditPublisher_thenRetrievePublisherAndListAllDevelopersAndPublishersAndReturnPublisherFormWithGivenPublisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        publisher.setId(1);
        when(publisherService.getById(1)).thenReturn(publisher);

        String expectedView = "publisher/publisherform";

        String actualView = publisherController.edit(1, model);

        assertThat(actualView).isEqualTo(expectedView);

        verify(model, times(1)).addAttribute(eq("publisher"), any(Publisher.class));
        verify(publisherService, times(1)).getById(1);
        verifyNoMoreInteractions(publisherService);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void whenDeletePublisher_thenRemoveGivenPublisherFromDbAndRedirectToPublisherList() throws Exception {
        String expectedView = "redirect:/publisher/list";

        String actualView = publisherController.delete(1);

        assertThat(actualView).isEqualTo(expectedView);
        verify(publisherService, times(1)).delete(1);
        verifyNoMoreInteractions(publisherService);
    }
}
