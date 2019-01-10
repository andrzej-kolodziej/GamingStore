package com.app.repositories;

import com.app.domain.Developer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tom on 6/13/2016.
 */
public interface DeveloperRepository extends CrudRepository<Developer,Integer> {
}
