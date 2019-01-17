package com.app.services;

import com.app.domain.Bundle;
import com.app.repositories.BundleRepository;
import com.app.services.reposervices.BundleServiceReopImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BundleServiceReopImplTest {

    @InjectMocks
    private BundleServiceReopImpl bundleService;

    @Mock
    private BundleRepository bundleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllBundlesFromRepository() {
        List<Bundle> expectedBundles = new ArrayList<>();
        Bundle bundle1 = new Bundle();
        bundle1.setName("bundle1");
        bundle1.setId(1);
        Bundle bundle2 = new Bundle();
        bundle2.setName("bundle2");
        bundle2.setId(2);

        when(bundleRepository.findAll()).thenReturn(expectedBundles);

        List<?> actualBundles = bundleService.listAll();

        for (int i = 0; i < expectedBundles.size(); i++) {
            assertThat(((Bundle)actualBundles.get(i)).getId()).isEqualTo(expectedBundles.get(i).getId());
        }

        verify(bundleRepository, times(1)).findAll();
        verifyNoMoreInteractions(bundleRepository);
    }

    @Test
    public void shouldReturnBundleOfGivenId() {
        Bundle expectedBundle = new Bundle();
        expectedBundle.setName("bundle");
        expectedBundle.setId(1);

        when(bundleRepository.findById(anyInt())).thenReturn(Optional.of(expectedBundle));

        Bundle actualBundle = bundleService.getById(1);

        assertThat(actualBundle.getId()).isEqualTo(expectedBundle.getId());

        verify(bundleRepository, times(1)).findById(1);
        verifyNoMoreInteractions(bundleRepository);
    }

    @Test
    public void shouldSaveBundleToDb() {
        Bundle expectedBundle = new Bundle();
        expectedBundle.setId(1);
        expectedBundle.setName("bundle");
        expectedBundle.setPrice(BigDecimal.valueOf(10));
        expectedBundle.setImageUrl("url");
        expectedBundle.setDescription("description");

        when(bundleRepository.save(expectedBundle)).thenReturn(expectedBundle);

        Bundle actualBundle = bundleService.saveOrUpdate(expectedBundle);
        assertThat(actualBundle.getId()).isEqualTo(expectedBundle.getId());

        verify(bundleRepository, times(1)).save(expectedBundle);
        verifyNoMoreInteractions(bundleRepository);
    }

    @Test
    public void shouldDeleteBundle() {
        bundleService.delete(1);
        verify(bundleRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(bundleRepository);
    }

    @Test
    public void shouldReturnCorrectBundleCount() {
        long expectedBundleCount = 2;
        when(bundleRepository.count()).thenReturn(expectedBundleCount);

        long actualBundleCount = bundleRepository.count();

        assertThat(actualBundleCount).isEqualTo(expectedBundleCount);

        verify(bundleRepository, times(1)).count();
        verifyNoMoreInteractions(bundleRepository);
    }
}
