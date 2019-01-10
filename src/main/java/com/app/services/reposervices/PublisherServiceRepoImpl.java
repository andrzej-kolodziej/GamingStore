package com.app.services.reposervices;

import com.app.domain.Publisher;
import com.app.repositories.PublisherRepository;
import com.app.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 6/21/2016.
 */
@Service
public class PublisherServiceRepoImpl implements PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public List<?> listAll() {
        ArrayList<Publisher> publishers = new ArrayList<>();
        publisherRepository.findAll().forEach(publishers::add);
        return publishers;
    }

    @Override
    public Publisher getById(Integer id) {
        return publisherRepository.findById(id).get();
    }

    @Override
    public Publisher saveOrUpdate(Publisher domainObject) {
        return publisherRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        publisherRepository.findById(id).get().getProducts().forEach(product -> product.setPublisher(publisherRepository.findById(1).get()));
        publisherRepository.deleteById(id);
    }
}
