package com.app.services;

import com.app.domain.Publisher;
import com.app.repositories.PublisherRepository;
import com.app.services.reposervices.PublisherServiceRepoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class PublisherServiceRepoImplTest {

    @InjectMocks
    private PublisherServiceRepoImpl publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllPublishersFromRepository() {
        List<Publisher> expectedPublishers = new ArrayList<>();
        Publisher publisher1 = new Publisher();
        publisher1.setName("publisher1");
        publisher1.setId(1);
        Publisher publisher2 = new Publisher();
        publisher2.setName("publisher2");
        publisher2.setId(2);

        when(publisherRepository.findAll()).thenReturn(expectedPublishers);

        List<?> actualPublishers = publisherService.listAll();

        for (int i = 0; i < expectedPublishers.size(); i++) {
            assertThat(((Publisher)actualPublishers.get(i)).getId()).isEqualTo(expectedPublishers.get(i).getId());
        }

        verify(publisherRepository, times(1)).findAll();
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    public void shouldReturnPublisherOfGivenId() {
        Publisher expectedPublisher = new Publisher();
        expectedPublisher.setName("publisher");
        expectedPublisher.setId(1);

        when(publisherRepository.findById(anyInt())).thenReturn(Optional.of(expectedPublisher));

        Publisher actualPublisher = publisherService.getById(1);

        assertThat(actualPublisher.getId()).isEqualTo(expectedPublisher.getId());

        verify(publisherRepository, times(1)).findById(1);
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    public void shouldSavePublisherToDb() {
        Publisher expectedPublisher = new Publisher();
        expectedPublisher.setId(1);
        expectedPublisher.setName("publisher");
        expectedPublisher.setImageUrl("url");
        expectedPublisher.setDescription("description");

        when(publisherRepository.save(expectedPublisher)).thenReturn(expectedPublisher);

        Publisher actualPublisher = publisherService.saveOrUpdate(expectedPublisher);
        assertThat(actualPublisher.getId()).isEqualTo(expectedPublisher.getId());

        verify(publisherRepository, times(1)).save(expectedPublisher);
        verifyNoMoreInteractions(publisherRepository);
    }

    @Test
    public void shouldDeletePublisher() {
        publisherService.delete(1);
        verify(publisherRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(publisherRepository);
    }
}
