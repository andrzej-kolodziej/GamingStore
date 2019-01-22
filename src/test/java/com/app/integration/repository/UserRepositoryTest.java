package com.app.integration.repository;

import com.app.domain.*;
import com.app.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindById_thenReturnUser() {
        //given
        User user = generateSampleUser();

        testEntityManager.persist(user);
        testEntityManager.flush();

        //when
        User found = userRepository.findById(user.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllUsers() {
        //given
        User user1 = generateSampleUser();
        User user2 = generateSampleUser();
        int createdUsers = 2;
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.flush();

        //when
        Iterable<User> users = userRepository.findAll();

        //then
        for (User foundUser : users) {
            boolean hasTheSameId = foundUser.getId() == user1.getId() || foundUser.getId() == user2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(userRepository.count()).isEqualTo(createdUsers);
    }

    @Test
    public void whenSave_thenUserIsPersistedIntoDb() {
        //given
        User user = generateSampleUser();

        //when
        userRepository.save(user);

        //then
        assertThat(user.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllUsersArePersistedIntoDb() {
        //given
        List<User> users = Arrays.asList(generateSampleUser(), generateSampleUser());

        //when
        userRepository.saveAll(users);

        //then
        users.forEach(user -> assertThat(user.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenUserIsRemovedFromDb() {
        //given
        User user = generateSampleUser();
        testEntityManager.persist(user);
        testEntityManager.flush();

        assertThat(user.getId()).isNotZero();

        //when
        userRepository.delete(user);

        //then
        assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllUsersAreRemovedFromDb() {
        //given
        List<User> users = Arrays.asList(generateSampleUser(), generateSampleUser());
        for (User user : users)
            testEntityManager.persist(user);
        testEntityManager.flush();

        users.forEach(user -> assertThat(user.getId()).isNotZero());

        //when
        userRepository.deleteAll();

        //then
        assertThat(userRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenUserIsRemovedFromDb() {
        //given
        User user = generateSampleUser();
        testEntityManager.persist(user);
        testEntityManager.flush();

        //when
        userRepository.deleteById(user.getId());

        //then
        assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSaveUserWithAssociatedEntities_thenUserWithAssociateEntitiesAreSavedIntoDb() {
        //given
        User user = generateSampleUser();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderType("ordertype");
        orderHistory.setTotalPrice(BigDecimal.valueOf(10));
        user.addOrderHistory(orderHistory);
        //when
        userRepository.save(user);

        //then
        assertThat(user.getId()).isNotZero();
        assertThat(userRepository.existsById(user.getId())).isTrue();
        assertThat(user.getOrderHistories().get(0).getId()).isNotZero();
    }

    @Test
    public void whenSaveAllUsersWithAssociatedEntities_thenAllUsersWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<User> users = Arrays.asList(generateSampleUser(), generateSampleUser());

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderType("ordertype");
        orderHistory.setTotalPrice(BigDecimal.valueOf(10));

        users.get(0).addOrderHistory(orderHistory);

        OrderHistory orderHistory2 = new OrderHistory();
        orderHistory2.setOrderType("ordertype");
        orderHistory2.setTotalPrice(BigDecimal.valueOf(10));

        users.get(1).addOrderHistory(orderHistory2);
        userRepository.saveAll(users);

        //then
        users.forEach(user -> {
            assertThat(user.getId()).isNotZero();
            assertThat(userRepository.existsById(user.getId())).isTrue();
            assertThat(user.getOrderHistories().iterator().next().getId()).isNotZero();
        });
    }

    @Test
    public void whenSaveUser_thenUserIsPersisted() {
        //given
        User user = generateSampleUser();

        //when
        userRepository.save(user);

        //then
        assertThat(user.getId()).isNotZero();
    }

    private User generateSampleUser() {
        User user = new User();

        user.setUserName("username");
        user.setEmail("email");

        return user;
    }
}
