package com.app.repositories;

import com.app.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tom on 6/8/2016.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String username);
}
