package com.app.integration.repository;

import com.app.domain.Product;
import com.app.domain.Role;
import com.app.domain.User;
import com.app.repositories.RoleRepository;
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
public class RoleRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindById_thenReturnRole() {
        //given
        Role role = generateSampleRole();

        testEntityManager.persist(role);
        testEntityManager.flush();

        //when
        Role found = roleRepository.findById(role.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(role.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllRoles() {
        //given
        Role role1 = generateSampleRole();
        Role role2 = generateSampleRole();
        int createdRoles = 2;
        testEntityManager.persist(role1);
        testEntityManager.persist(role2);
        testEntityManager.flush();

        //when
        Iterable<Role> roles = roleRepository.findAll();

        //then
        for (Role foundRole : roles) {
            boolean hasTheSameId = foundRole.getId() == role1.getId() || foundRole.getId() == role2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(roleRepository.count()).isEqualTo(createdRoles);
    }

    @Test
    public void whenSave_thenRoleIsPersistedIntoDb() {
        //given
        Role role = generateSampleRole();

        //when
        roleRepository.save(role);

        //then
        assertThat(role.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllRolesArePersistedIntoDb() {
        //given
        List<Role> roles = Arrays.asList(generateSampleRole(), generateSampleRole());

        //when
        roleRepository.saveAll(roles);

        //then
        roles.forEach(role -> assertThat(role.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenRoleIsRemovedFromDb() {
        //given
        Role role = generateSampleRole();
        testEntityManager.persist(role);
        testEntityManager.flush();

        assertThat(role.getId()).isNotZero();

        //when
        roleRepository.delete(role);

        //then
        assertThat(roleRepository.findById(role.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllRolesAreRemovedFromDb() {
        //given
        List<Role> roles = Arrays.asList(generateSampleRole(), generateSampleRole());
        for (Role role : roles)
            testEntityManager.persist(role);
        testEntityManager.flush();

        roles.forEach(role -> assertThat(role.getId()).isNotZero());

        //when
        roleRepository.deleteAll();

        //then
        assertThat(roleRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenRoleIsRemovedFromDb() {
        //given
        Role role = generateSampleRole();
        testEntityManager.persist(role);
        testEntityManager.flush();

        //when
        roleRepository.deleteById(role.getId());

        //then
        assertThat(roleRepository.findById(role.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSaveRoleWithAssociatedEntities_thenRoleWithAssociateEntitiesAreSavedIntoDb() {
        //given
        Role role = generateSampleRole();
        User user = new User();
        user.setUserName("name");
        List<User> users = Arrays.asList(user);
        role.setUsers(users);
        //when
        roleRepository.save(role);

        //then
        assertThat(role.getId()).isNotZero();
        assertThat(roleRepository.existsById(role.getId())).isTrue();
        assertThat(users.get(0).getId()).isNotZero();
    }

    @Test
    public void whenSaveAllRolesWithAssociatedEntities_thenAllRolesWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<Role> roles = Arrays.asList(generateSampleRole(), generateSampleRole());

        User user = new User();
        user.setUserName("name");
        List<User> users1 = Arrays.asList(user);

        roles.get(0).setUsers(users1);

        User user2 = new User();
        user.setUserName("name");
        List<User> users2 = Arrays.asList(user2);

        roles.get(1).setUsers(users2);

        //when
        roleRepository.saveAll(roles);

        //then
        roles.forEach(role -> {
            assertThat(role.getId()).isNotZero();
            assertThat(roleRepository.existsById(role.getId())).isTrue();
            assertThat(role.getUsers().iterator().next().getId()).isNotZero();
        });
    }

    @Test
    public void whenSaveRole_thenRoleIsPersisted() {
        //given
        Role role = generateSampleRole();

        //when
        roleRepository.save(role);

        //then
        assertThat(role.getId()).isNotZero();
    }

    private Role generateSampleRole() {
        Role role = new Role();
        role.setRole("CUSTOM");

        return role;
    }
}
