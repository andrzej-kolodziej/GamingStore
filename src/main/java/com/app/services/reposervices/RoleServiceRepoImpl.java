package com.app.services.reposervices;

import com.app.domain.Role;
import com.app.repositories.RoleRepository;
import com.app.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 8/3/2016.
 */
@Service
public class RoleServiceRepoImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<?> listAll() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public Role getById(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role saveOrUpdate(Role domainObject) {
        return roleRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }

}
