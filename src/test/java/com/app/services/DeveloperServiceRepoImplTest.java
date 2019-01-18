package com.app.services;

import com.app.commands.DeveloperForm;
import com.app.converters.DeveloperFormToDeveloper;
import com.app.converters.DeveloperToDeveloperForm;
import com.app.domain.Developer;
import com.app.domain.Developer;
import com.app.repositories.DeveloperRepository;
import com.app.services.reposervices.DeveloperServiceRepoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class DeveloperServiceRepoImplTest {

    @InjectMocks
    private DeveloperServiceRepoImpl developerService;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private DeveloperFormToDeveloper developerFormToDeveloper;

    @Mock
    private DeveloperToDeveloperForm developerToDeveloperForm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllDevelopers() {
        List<Developer> expectedDevelopers = new ArrayList<>();
        Developer developer1 = new Developer();
        developer1.setId(1);
        Developer developer2 = new Developer();
        developer2.setId(2);
        expectedDevelopers.add(developer1);
        expectedDevelopers.add(developer2);

        when(developerRepository.findAll()).thenReturn(expectedDevelopers);

        List<?> actualDevelopers = developerService.listAll();

        for (int i = 0; i < actualDevelopers.size(); i++)
            assertThat(expectedDevelopers.get(i).getId()).isEqualTo(((Developer)actualDevelopers.get(i)).getId());

    }

    @Test
    public void shouldReturnDeveloperOfGivenId() {
        Developer expectedDeveloper = new Developer();
        expectedDeveloper.setName("developer");
        expectedDeveloper.setId(1);

        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(expectedDeveloper));

        Developer actualDeveloper = developerService.getById(1);

        assertThat(actualDeveloper.getId()).isEqualTo(expectedDeveloper.getId());

        verify(developerRepository, times(1)).findById(1);
        verifyNoMoreInteractions(developerRepository);
    }

    @Test
    public void shouldSaveDeveloperToDb() {
        Developer expectedDeveloper = new Developer();
        expectedDeveloper.setId(1);
        expectedDeveloper.setName("developer");
        expectedDeveloper.setImageUrl("url");
        expectedDeveloper.setDescription("description");

        when(developerRepository.save(expectedDeveloper)).thenReturn(expectedDeveloper);

        Developer actualDeveloper = developerService.saveOrUpdate(expectedDeveloper);
        assertThat(actualDeveloper.getId()).isEqualTo(expectedDeveloper.getId());

        verify(developerRepository, times(1)).save(expectedDeveloper);
        verifyNoMoreInteractions(developerRepository);
    }

    @Test
    public void shouldDeleteDeveloper() {
        developerService.delete(1);
        verify(developerRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(developerRepository);
    }

    @Test
    public void shouldConvertDeveloperFormIntoDeveloperAndSaveItIntoDb() {
        DeveloperForm developerForm = new DeveloperForm();
        developerForm.setDeveloperId(1);
        developerForm.setDeveloperName("developer");

        Developer expectedDeveloper = new Developer();
        expectedDeveloper.setId(1);
        expectedDeveloper.setName("developer");

        when(developerFormToDeveloper.convert(developerForm)).thenReturn(expectedDeveloper);

        Developer actualDeveloper = developerService.saveOrUpdateDeveloperForm(developerForm);
        assertThat(actualDeveloper.getId()).isEqualTo(expectedDeveloper.getId());

        verify(developerRepository, times(1)).save(expectedDeveloper);
        verifyNoMoreInteractions(developerRepository);
    }

    @Test
    public void shouldReturnDeveloperFormById() {
        DeveloperForm expectedDeveloperForm = new DeveloperForm();
        expectedDeveloperForm.setDeveloperId(1);
        expectedDeveloperForm.setDeveloperName("developer");

        Developer developer = new Developer();
        Optional<Developer> optionalDeveloper = Optional.of(developer);

        when(developerToDeveloperForm.convert(any(Developer.class))).thenReturn(expectedDeveloperForm);
        when(developerRepository.findById(anyInt())).thenReturn(optionalDeveloper);

        DeveloperForm actualDeveloperForm = developerService.getDeveloperFormById(1);
        assertThat(actualDeveloperForm.getDeveloperId()).isEqualTo(expectedDeveloperForm.getDeveloperId());

        verify(developerRepository, times(1)).findById(1);
        verify(developerToDeveloperForm, times(1)).convert(developer);
        verifyNoMoreInteractions(developerRepository);
        verifyNoMoreInteractions(developerToDeveloperForm);
    }
}
