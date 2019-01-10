package com.app.services;

import com.app.domain.User;
import com.app.commands.UserForm;

/**
 * Created by tom on 6/8/2016.
 */
public interface UserService extends CRUDservice<User> {
    User saveOrUpdateUserForm(UserForm userForm);
    UserForm findUserFormById(Integer id);
    User findByUserName(String userName);
}
