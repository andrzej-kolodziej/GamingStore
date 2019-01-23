package com.app.services;

import com.app.domain.Role;
import com.app.repositories.RoleRepository;
import com.app.services.reposervices.RoleServiceRepoImpl;
import com.app.services.reposervices.RoleServiceRepoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class RoleServiceRepoImplTest {
    @InjectMocks
    private RoleServiceRepoImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllRoles() {
        List<Role> expectedRoles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1);
        Role role2 = new Role();
        role2.setId(2);
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<?> actualRoles = roleService.listAll();

        for (int i = 0; i < actualRoles.size(); i++)
            assertThat(expectedRoles.get(i).getId()).isEqualTo(((Role)actualRoles.get(i)).getId());

    }

    @Test
    public void shouldReturnRoleOfGivenId() {
        Role expectedRole = new Role();
        expectedRole.setRole("role");
        expectedRole.setId(1);

        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(expectedRole));

        Role actualRole = roleService.getById(1);

        assertThat(actualRole.getId()).isEqualTo(expectedRole.getId());

        verify(roleRepository, times(1)).findById(1);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void shouldSaveRoleToDb() {
        Role expectedRole = new Role();
        expectedRole.setId(1);
        expectedRole.setRole("role");

        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);

        Role actualRole = roleService.saveOrUpdate(expectedRole);
        assertThat(actualRole.getId()).isEqualTo(expectedRole.getId());

        verify(roleRepository, times(1)).save(expectedRole);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void shouldDeleteRole() {
        roleService.delete(1);
        verify(roleRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(roleRepository);
    }
}
