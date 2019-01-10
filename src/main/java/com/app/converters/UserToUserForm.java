package com.app.converters;

import com.app.domain.User;
import com.app.commands.UserForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by tom on 8/9/2016.
 */
@Component
public class UserToUserForm implements Converter<User, UserForm> {

    @Override
    public UserForm convert(User user) {
        UserForm userForm = new UserForm();
        userForm.setUserId(user.getId());
        userForm.setUserVersion(user.getVersion());
        userForm.setUserName(user.getUserName());
        userForm.setUserPassword("Default Password");
        userForm.setUserEmail(user.getEmail());
        userForm.setRoles(user.getRoles());
        userForm.setUserBillingAddress(user.getBillingAddress());
        return userForm;
    }
}
