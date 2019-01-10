package com.app.converters;

import com.app.domain.User;
import com.app.commands.UserForm;
import com.app.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by tom on 8/9/2016.
 */
@Component
public class UserFormToUser implements Converter<UserForm , User> {
    @Autowired
    EncryptionService encryptionService;

    @Override
    public User convert(UserForm userForm) {
        User user = new User();

        user.setId(userForm.getUserId());
        user.setVersion(userForm.getUserVersion());
        user.setUserName(userForm.getUserName());
        user.setPassword(encryptionService.encryptString(userForm.getUserPassword()));
        user.setEmail(userForm.getUserEmail());
        user.setBillingAddress(userForm.getUserBillingAddress());
        user.setRoles(userForm.getRoles());
        return user;
    }
}
