package com.app.repository;

import com.app.domain.Developer;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.domain.Publisher;
import com.app.repositories.DeveloperRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DeveloperRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DeveloperRepository developerRepository;

    @Test
    public void whenFindById_thenReturnDeveloper() {
        //given
        Developer developer = generateSampleDeveloper();

        testEntityManager.persist(developer);
        testEntityManager.flush();

        //when
        Developer found = developerRepository.findById(developer.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(developer.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllDevelopers() {
        //given
        Developer developer1 = generateSampleDeveloper();
        Developer developer2 = generateSampleDeveloper();
        int createdDevelopers = 2;
        testEntityManager.persist(developer1);
        testEntityManager.persist(developer2);
        testEntityManager.flush();

        //when
        Iterable<Developer> developers = developerRepository.findAll();

        //then
        for (Developer foundDeveloper : developers) {
            boolean hasTheSameId = foundDeveloper.getId() == developer1.getId() || foundDeveloper.getId() == developer2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(developerRepository.count()).isEqualTo(createdDevelopers);
    }

    @Test
    public void whenSave_thenDeveloperIsPersistedIntoDb() {
        //given
        Developer developer = generateSampleDeveloper();

        //when
        developerRepository.save(developer);

        //then
        assertThat(developer.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllDevelopersArePersistedIntoDb() {
        //given
        List<Developer> developers = Arrays.asList(generateSampleDeveloper(), generateSampleDeveloper());

        //when
        developerRepository.saveAll(developers);

        //then
        developers.forEach(developer -> assertThat(developer.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenDeveloperIsRemovedFromDb() {
        //given
        Developer developer = generateSampleDeveloper();
        testEntityManager.persist(developer);
        testEntityManager.flush();

        assertThat(developer.getId()).isNotZero();

        //when
        developerRepository.delete(developer);

        //then
        assertThat(developerRepository.findById(developer.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllDevelopersAreRemovedFromDb() {
        //given
        List<Developer> developers = Arrays.asList(generateSampleDeveloper(), generateSampleDeveloper());
        for (Developer developer : developers)
            testEntityManager.persist(developer);
        testEntityManager.flush();

        developers.forEach(developer -> assertThat(developer.getId()).isNotZero());

        //when
        developerRepository.deleteAll();

        //then
        assertThat(developerRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenDeveloperIsRemovedFromDb() {
        //given
        Developer developer = generateSampleDeveloper();
        testEntityManager.persist(developer);
        testEntityManager.flush();

        //when
        developerRepository.deleteById(developer.getId());

        //then
        assertThat(developerRepository.findById(developer.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSaveDeveloperWithAssociatedEntities_thenDeveloperWithAssociateEntitiesAreSavedIntoDb() {
        //given
        Developer developer = generateSampleDeveloper();
        Product product = new Product();
        product.setYoutubeUrl("url");
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10.0));
        List<Product> products = new ArrayList<>();
        products.add(product);
        developer.setProducts(products);

        //when
        developerRepository.save(developer);

        //then
        assertThat(developer.getId()).isNotZero();
        assertThat(developerRepository.existsById(developer.getId())).isTrue();
        assertThat(product.getId()).isNotZero();
    }

    @Test
    public void whenSaveAllDevelopersWithAssociatedEntities_thenAllDevelopersWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<Developer> developers = Arrays.asList(generateSampleDeveloper(), generateSampleDeveloper());

        Product product1 = new Product();
        product1.setYoutubeUrl("url1");
        product1.setName("product1");
        product1.setDescription("description1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        List<Product> products1 = new ArrayList<>();
        products1.add(product1);

        developers.get(0).setProducts(products1);

        Product product2 = new Product();
        product2.setYoutubeUrl("url2");
        product2.setName("product2");
        product2.setDescription("description2");
        product2.setPrice(BigDecimal.valueOf(10.0));
        List<Product> products2 = new ArrayList<>();
        products2.add(product2);

        developers.get(1).setProducts(products2);

        //when
        developerRepository.saveAll(developers);

        //then
        developers.forEach(developer -> {
            assertThat(developer.getId()).isNotZero();
            assertThat(developerRepository.existsById(developer.getId())).isTrue();
            assertThat(developer.getProducts().iterator().next().getId()).isNotZero();
        });
    }

    @Test
    public void whenSaveDeveloper_thenDeveloperIsPersisted() {
        //given
        Developer developer = generateSampleDeveloper();

        //when
        developerRepository.save(developer);

        //then
        assertThat(developer.getId()).isNotZero();
    }

    private Developer generateSampleDeveloper() {
        Developer developer = new Developer();
        developer.setImageUrl("url");
        developer.setDescription("description");
        developer.setName("developer");

        return developer;
    }
}
