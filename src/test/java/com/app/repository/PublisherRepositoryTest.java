package com.app.repository;

import com.app.domain.Publisher;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.domain.Publisher;
import com.app.repositories.PublisherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PublisherRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void whenFindById_thenReturnPublisher() {
        //given
        Publisher publisher = generateSamplePublisher();

        testEntityManager.persist(publisher);
        testEntityManager.flush();

        //when
        Publisher found = publisherRepository.findById(publisher.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(publisher.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllPublishers() {
        //given
        Publisher publisher1 = generateSamplePublisher();
        Publisher publisher2 = generateSamplePublisher();
        int createdPublishers = 2;
        testEntityManager.persist(publisher1);
        testEntityManager.persist(publisher2);
        testEntityManager.flush();

        //when
        Iterable<Publisher> publishers = publisherRepository.findAll();

        //then
        for (Publisher foundPublisher : publishers) {
            boolean hasTheSameId = foundPublisher.getId() == publisher1.getId() || foundPublisher.getId() == publisher2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(publisherRepository.count()).isEqualTo(createdPublishers);
    }

    @Test
    public void whenSave_thenPublisherIsPersistedIntoDb() {
        //given
        Publisher publisher = generateSamplePublisher();

        //when
        publisherRepository.save(publisher);

        //then
        assertThat(publisher.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllPublishersArePersistedIntoDb() {
        //given
        List<Publisher> publishers = Arrays.asList(generateSamplePublisher(), generateSamplePublisher());

        //when
        publisherRepository.saveAll(publishers);

        //then
        publishers.forEach(publisher -> assertThat(publisher.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenPublisherIsRemovedFromDb() {
        //given
        Publisher publisher = generateSamplePublisher();
        testEntityManager.persist(publisher);
        testEntityManager.flush();

        assertThat(publisher.getId()).isNotZero();

        //when
        publisherRepository.delete(publisher);

        //then
        assertThat(publisherRepository.findById(publisher.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllPublishersAreRemovedFromDb() {
        //given
        List<Publisher> publishers = Arrays.asList(generateSamplePublisher(), generateSamplePublisher());
        for (Publisher publisher : publishers)
            testEntityManager.persist(publisher);
        testEntityManager.flush();

        publishers.forEach(publisher -> assertThat(publisher.getId()).isNotZero());

        //when
        publisherRepository.deleteAll();

        //then
        assertThat(publisherRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenPublisherIsRemovedFromDb() {
        //given
        Publisher publisher = generateSamplePublisher();
        testEntityManager.persist(publisher);
        testEntityManager.flush();

        //when
        publisherRepository.deleteById(publisher.getId());

        //then
        assertThat(publisherRepository.findById(publisher.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSavePublisherWithAssociatedEntities_thenPublisherWithAssociateEntitiesAreSavedIntoDb() {
        //given
        Publisher publisher = generateSamplePublisher();
        Product product = new Product();
        product.setYoutubeUrl("url");
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products = new HashSet<>();
        products.add(product);
        publisher.setProducts(products);

        //when
        publisherRepository.save(publisher);

        //then
        assertThat(publisher.getId()).isNotZero();
        assertThat(publisherRepository.existsById(publisher.getId())).isTrue();
        assertThat(product.getId()).isNotZero();
    }

    @Test
    public void whenSaveAllPublishersWithAssociatedEntities_thenAllPublishersWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<Publisher> publishers = Arrays.asList(generateSamplePublisher(), generateSamplePublisher());

        Product product1 = new Product();
        product1.setYoutubeUrl("url1");
        product1.setName("product1");
        product1.setDescription("description1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products1 = new HashSet<>();
        products1.add(product1);

        publishers.get(0).setProducts(products1);

        Product product2 = new Product();
        product2.setYoutubeUrl("url2");
        product2.setName("product2");
        product2.setDescription("description2");
        product2.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products2 = new HashSet<>();
        products2.add(product2);

        publishers.get(1).setProducts(products2);

        //when
        publisherRepository.saveAll(publishers);

        //then
        publishers.forEach(publisher -> {
            assertThat(publisher.getId()).isNotZero();
            assertThat(publisherRepository.existsById(publisher.getId())).isTrue();
            assertThat(publisher.getProducts().iterator().next().getId()).isNotZero();
        });
    }

    @Test
    public void whenSavePublisher_thenPublisherIsPersisted() {
        //given
        Publisher publisher = generateSamplePublisher();

        //when
        publisherRepository.save(publisher);

        //then
        assertThat(publisher.getId()).isNotZero();
    }

    private Publisher generateSamplePublisher() {
        Publisher publisher = new Publisher();
        publisher.setDescription("description");
        publisher.setImageUrl("url");
        publisher.setName("publisher");

        return publisher;
    }

}