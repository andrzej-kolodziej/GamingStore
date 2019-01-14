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

import java.lang.reflect.Array;
import java.math.BigDecimal;
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
        int createdBundles = 2;
        testEntityManager.persist(bundle1);
        testEntityManager.persist(bundle2);
        testEntityManager.flush();

        //when
        Iterable<Bundle> bundles = bundleRepository.findAll();

        //then
        for (Bundle foundBundle : bundles) {
            boolean hasTheSameId = foundBundle.getId() == bundle1.getId() || foundBundle.getId() == bundle2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(bundleRepository.count()).isEqualTo(createdBundles);
    }

    @Test
    public void whenSave_thenBundleIsPersistedIntoDb() {
        //given
        Bundle bundle = generateSampleBundle();

        //when
        bundleRepository.save(bundle);

        //then
        assertThat(bundle.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllBundlesArePersistedIntoDb() {
        //given
        List<Bundle> bundles = Arrays.asList(generateSampleBundle(), generateSampleBundle());

        //when
        bundleRepository.saveAll(bundles);

        //then
        bundles.forEach(bundle -> assertThat(bundle.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenBundleIsRemovedFromDb() {
        //given
        Bundle bundle = generateSampleBundle();
        testEntityManager.persist(bundle);
        testEntityManager.flush();

        assertThat(bundle.getId()).isNotZero();

        //when
        bundleRepository.delete(bundle);

        //then
        assertThat(bundleRepository.findById(bundle.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllBundlesAreRemovedFromDb() {
        //given
        List<Bundle> bundles = Arrays.asList(generateSampleBundle(), generateSampleBundle());
        for (Bundle bundle : bundles)
            testEntityManager.persist(bundle);
        testEntityManager.flush();

        bundles.forEach(bundle -> assertThat(bundle.getId()).isNotZero());

        //when
        bundleRepository.deleteAll();

        //then
        assertThat(bundleRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenBundleIsRemovedFromDb() {
        //given
        Bundle bundle = generateSampleBundle();
        testEntityManager.persist(bundle);
        testEntityManager.flush();

        //when
        bundleRepository.deleteById(bundle.getId());

        //then
        assertThat(bundleRepository.findById(bundle.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSaveBundleWithAssociatedEntities_thenBundleWithAssociateEntitiesAreSavedIntoDb() {
        //given
        Bundle bundle = generateSampleBundle();
        Product product = new Product();
        product.setYoutubeUrl("url");
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products = new HashSet<>();
        products.add(product);
        bundle.setProducts(products);

        //when
        bundleRepository.save(bundle);

        //then
        assertThat(bundle.getId()).isNotZero();
        assertThat(bundleRepository.existsById(bundle.getId())).isTrue();
        assertThat(product.getId()).isNotZero();
    }

    @Test
    public void whenSaveAllBundlesWithAssociatedEntities_thenAllBundlesWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<Bundle> bundles = Arrays.asList(generateSampleBundle(), generateSampleBundle());

        Product product1 = new Product();
        product1.setYoutubeUrl("url1");
        product1.setName("product1");
        product1.setDescription("description1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products1 = new HashSet<>();
        products1.add(product1);

        bundles.get(0).setProducts(products1);

        Product product2 = new Product();
        product2.setYoutubeUrl("url2");
        product2.setName("product2");
        product2.setDescription("description2");
        product2.setPrice(BigDecimal.valueOf(10.0));
        Set<Product> products2 = new HashSet<>();
        products2.add(product2);

        bundles.get(1).setProducts(products2);

        //when
        bundleRepository.saveAll(bundles);

        //then
        bundles.forEach(bundle -> {
            assertThat(bundle.getId()).isNotZero();
            assertThat(bundleRepository.existsById(bundle.getId())).isTrue();
            assertThat(bundle.getProducts().iterator().next().getId()).isNotZero();
        });
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

        return bundle;
    }
}