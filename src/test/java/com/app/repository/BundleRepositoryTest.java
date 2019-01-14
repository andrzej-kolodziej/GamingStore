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

import java.util.*;

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
        Bundle bundle = generateSampleBundle();

        testEntityManager.persist(bundle);
        testEntityManager.flush();

        //when
        Bundle found = bundleRepository.findById(bundle.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(bundle.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllBundles() {
        //given
        Bundle bundle1 = generateSampleBundle();
        Bundle bundle2 = generateSampleBundle();

        testEntityManager.persist(bundle1);
        testEntityManager.persist(bundle2);
        testEntityManager.flush();

        //when
        Iterable<Bundle> bundles = bundleRepository.findAll();
        Bundle foundBundle1 = bundles.iterator().next();
        Bundle foundBundle2 = bundles.iterator().next();

        //then
        assertThat(foundBundle1.getId()).isEqualTo(bundle1.getId());
        assertThat(foundBundle2.getId()).isEqualTo(bundle2.getId());
    }

    @Test
    public void whenSaveBundle_thenBundleIsPersisted() {
        //given
        Bundle bundle = generateSampleBundle();

        //when
        bundleRepository.save(bundle);

        //then
        assertThat(bundle.getId()).isNotZero();
    }

    private Bundle generateSampleBundle() {
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
        return bundle;
    }

}