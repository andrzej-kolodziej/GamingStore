package com.app.repository;

import com.app.domain.Bundle;
import com.app.domain.Developer;
import com.app.domain.Product;
import com.app.domain.Publisher;
import com.app.repositories.BundleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BundleRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BundleRepository bundleRepository;

    @Test
    public void whenFindById_thenReturnBundle() {
        //given
        Bundle bundle = new Bundle();

        Set<Product> products = new HashSet<>();

        Developer developer = new Developer();
        developer.setDescription("description");
        developer.setImageUrl("https://dummyimage.com/300");
        developer.setName("name");

        Publisher publisher = new Publisher();
        publisher.setDescription("description");
        publisher.setImageUrl("https://dummyimage.com/300");
        publisher.setName("name");

        Product product = new Product();
        product.setDeveloper(developer);
        product.setPublisher(publisher);
        product.setYoutubeUrl("url");

        products.add(product);

        bundle.setProducts(products);
        testEntityManager.persist(bundle);
        testEntityManager.flush();

        //when
        Bundle found = bundleRepository.findById(bundle.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(bundle.getId());
    }
}