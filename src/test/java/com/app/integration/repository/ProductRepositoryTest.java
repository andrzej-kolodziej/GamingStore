package com.app.integration.repository;

import com.app.domain.*;
import com.app.domain.Product;
import com.app.repositories.ProductRepository;
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
public class ProductRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindById_thenReturnProduct() {
        //given
        Product product = generateSampleProduct();

        testEntityManager.persist(product);
        testEntityManager.flush();

        //when
        Product found = productRepository.findById(product.getId()).get();

        //then
        assertThat(found.getId()).isEqualTo(product.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllProducts() {
        //given
        Product product1 = generateSampleProduct();
        Product product2 = generateSampleProduct();
        int createdProducts = 2;
        testEntityManager.persist(product1);
        testEntityManager.persist(product2);
        testEntityManager.flush();

        //when
        Iterable<Product> products = productRepository.findAll();

        //then
        for (Product foundProduct : products) {
            boolean hasTheSameId = foundProduct.getId() == product1.getId() || foundProduct.getId() == product2.getId();
            assertThat(hasTheSameId).isTrue();
        }

        assertThat(productRepository.count()).isEqualTo(createdProducts);
    }

    @Test
    public void whenSave_thenProductIsPersistedIntoDb() {
        //given
        Product product = generateSampleProduct();

        //when
        productRepository.save(product);

        //then
        assertThat(product.getId()).isNotZero();
    }

    @Test
    public void whenSaveAll_thenAllProductsArePersistedIntoDb() {
        //given
        List<Product> products = Arrays.asList(generateSampleProduct(), generateSampleProduct());

        //when
        productRepository.saveAll(products);

        //then
        products.forEach(product -> assertThat(product.getId()).isNotZero());
    }

    @Test
    public void whenDelete_thenProductIsRemovedFromDb() {
        //given
        Product product = generateSampleProduct();
        testEntityManager.persist(product);
        testEntityManager.flush();

        assertThat(product.getId()).isNotZero();

        //when
        productRepository.delete(product);

        //then
        assertThat(productRepository.findById(product.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenDeleteAll_thenAllProductsAreRemovedFromDb() {
        //given
        List<Product> products = Arrays.asList(generateSampleProduct(), generateSampleProduct());
        for (Product product : products)
            testEntityManager.persist(product);
        testEntityManager.flush();

        products.forEach(product -> assertThat(product.getId()).isNotZero());

        //when
        productRepository.deleteAll();

        //then
        assertThat(productRepository.findAll().iterator().hasNext()).isFalse();
    }

    @Test
    public void whenDeleteById_thenProductIsRemovedFromDb() {
        //given
        Product product = generateSampleProduct();
        testEntityManager.persist(product);
        testEntityManager.flush();

        //when
        productRepository.deleteById(product.getId());

        //then
        assertThat(productRepository.findById(product.getId()).isPresent()).isFalse();
    }

    @Test
    public void whenSaveProductWithAssociatedEntities_thenProductWithAssociateEntitiesAreSavedIntoDb() {
        //given
        Product product = generateSampleProduct();

        Developer developer = new Developer();
        developer.setName("developer");
        developer.setDescription("description");
        developer.setImageUrl("url");

        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        publisher.setImageUrl("url");
        publisher.setDescription("description");

        Bundle bundle = new Bundle();
        bundle.setDescription("description");
        bundle.setName("name");
        bundle.setImageUrl("url");
        bundle.setPrice(BigDecimal.valueOf(10));
        Set<Bundle> bundles = new HashSet<>();
        bundles.add(bundle);

        product.setDeveloper(developer);
        product.setPublisher(publisher);
        product.setBundles(bundles);

        //when
        productRepository.save(product);

        //then
        assertThat(product.getId()).isNotZero();
        assertThat(productRepository.existsById(product.getId())).isTrue();
        assertThat(developer.getId()).isNotZero();
        assertThat(publisher.getId()).isNotZero();
        bundles.forEach(theBundle -> assertThat(theBundle.getId()).isNotZero());
    }

    @Test
    public void whenSaveAllProductsWithAssociatedEntities_thenAllProductsWithAssociateEntitiesAreSavedIntoDb() {
        //given
        List<Product> products = Arrays.asList(generateSampleProduct(), generateSampleProduct());

        Developer developer1 = new Developer();
        developer1.setName("developer");
        developer1.setDescription("description");
        developer1.setImageUrl("url");

        Publisher publisher1 = new Publisher();
        publisher1.setName("publisher");
        publisher1.setImageUrl("url");
        publisher1.setDescription("description");

        Bundle bundle1 = new Bundle();
        bundle1.setDescription("description");
        bundle1.setName("name");
        bundle1.setImageUrl("url");
        bundle1.setPrice(BigDecimal.valueOf(10));
        Set<Bundle> bundles1 = new HashSet<>();
        bundles1.add(bundle1);

        products.get(0).setDeveloper(developer1);
        products.get(0).setPublisher(publisher1);
        products.get(0).setBundles(bundles1);

        Developer developer2 = new Developer();
        developer2.setName("developer");
        developer2.setDescription("description");
        developer2.setImageUrl("url");

        Publisher publisher2 = new Publisher();
        publisher2.setName("publisher");
        publisher2.setImageUrl("url");
        publisher2.setDescription("description");

        Bundle bundle2 = new Bundle();
        bundle2.setDescription("description");
        bundle2.setName("name");
        bundle2.setImageUrl("url");
        bundle2.setPrice(BigDecimal.valueOf(10));
        Set<Bundle> bundles2 = new HashSet<>();
        bundles2.add(bundle2);

        products.get(1).setDeveloper(developer2);
        products.get(1).setPublisher(publisher2);
        products.get(1).setBundles(bundles2);

        //when
        productRepository.saveAll(products);

        //then
        products.forEach(product -> {
            assertThat(product.getId()).isNotZero();
            assertThat(productRepository.existsById(product.getId())).isTrue();
            assertThat(product.getDeveloper().getId()).isNotZero();
            assertThat(product.getPublisher().getId()).isNotZero();
            assertThat(product.getBundles().iterator().next().getId()).isNotZero();
        });
    }

    @Test
    public void whenSaveProduct_thenProductIsPersisted() {
        //given
        Product product = generateSampleProduct();

        //when
        productRepository.save(product);

        //then
        assertThat(product.getId()).isNotZero();
    }

    private Product generateSampleProduct() {
        Product product = new Product();
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10));
        product.setYoutubeUrl("url");
        product.setImageUrl("url");
        return product;
    }
}
