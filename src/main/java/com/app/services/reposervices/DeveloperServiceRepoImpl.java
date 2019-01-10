package com.app.services.reposervices;

import com.app.converters.DeveloperFormToDeveloper;
import com.app.converters.DeveloperToDeveloperForm;
import com.app.domain.Developer;
import com.app.repositories.DeveloperRepository;
import com.app.commands.DeveloperForm;
import com.app.services.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 6/13/2016.
 */
@Service
public class DeveloperServiceRepoImpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperFormToDeveloper developerFormToDeveloper;

    @Autowired
    private DeveloperToDeveloperForm developerToDeveloperForm;

    @Override
    public List<?> listAll() {
        ArrayList<Developer> developers = new ArrayList<>();
        developerRepository.findAll().forEach(developers::add);
        return developers;
    }

    @Override
    public Developer getById(Integer id) {
        return developerRepository.findById(id).get();
    }

    @Override
    public Developer saveOrUpdate(Developer domainObject) {
        return developerRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        developerRepository.findById(id).get().getProducts().forEach(product -> product.setDeveloper(developerRepository.findById(1).get()));
        developerRepository.deleteById(id);
    }

    @Override
    public Developer saveOrUpdateDeveloperForm(DeveloperForm developerForm) {
        Developer developer = developerFormToDeveloper.convert(developerForm);
        return developerRepository.save(developer);
    }

    @Override
    public DeveloperForm getDeveloperFormById(Integer id) {
        return developerToDeveloperForm.convert(developerRepository.findById(id).get());
    }
}
