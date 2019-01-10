package com.app.repositories;

import com.app.domain.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tom on 8/3/2016.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
