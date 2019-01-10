package com.app.repositories;

import com.app.domain.Publisher;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tom on 6/21/2016.
 */
public interface PublisherRepository extends CrudRepository<Publisher,Integer> {
}
