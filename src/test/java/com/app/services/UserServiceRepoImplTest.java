package com.app.services;

import com.app.converters.UserFormToUser;
import com.app.converters.UserToUserForm;
import com.app.domain.User;
import com.app.repositories.UserRepository;
import com.app.services.reposervices.UserServiceRepoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceRepoImplTest {
    
    @InjectMocks
    private UserServiceRepoImpl userService;

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserFormToUser userFormToUser;
    
    @Mock
    private UserToUserForm userToUserForm;

    @Test
    public void shouldReturnAllUsersFromRepository() {
        List<User> expectedUsers = new ArrayList<>();
        User user1 = new User();
        user1.setUserName("user1");
        user1.setId(1);
        User user2 = new User();
        user2.setUserName("user2");
        user2.setId(2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<?> actualUsers = userService.listAll();

        for (int i = 0; i < expectedUsers.size(); i++) {
            assertThat(((User)actualUsers.get(i)).getId()).isEqualTo(expectedUsers.get(i).getId());
        }

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldReturnUserOfGivenId() {
        User expectedUser = new User();
        expectedUser.setUserName("user");
        expectedUser.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getById(1);

        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());

        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldSaveUserToDb() {
        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setUserName("user");
        expectedUser.setEmail("email");

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userService.saveOrUpdate(expectedUser);
        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());

        verify(userRepository, times(1)).save(expectedUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldDeleteUser() {
        userService.delete(1);
        verify(userRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(userRepository);
    }
}
