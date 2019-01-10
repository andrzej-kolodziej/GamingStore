package com.app.services;

import com.app.domain.Developer;
import com.app.commands.DeveloperForm;

/**
 * Created by tom on 6/13/2016.
 */
public interface DeveloperService extends CRUDservice<Developer> {
    Developer saveOrUpdateDeveloperForm(DeveloperForm developerForm);
    DeveloperForm getDeveloperFormById(Integer id);
}
