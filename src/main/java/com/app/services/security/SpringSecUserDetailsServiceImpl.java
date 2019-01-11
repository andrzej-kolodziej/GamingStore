package com.app.services.security;

import com.app.converters.UserToUserDetails;
import com.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by tom on 8/10/2016.
 */
@Service
@Primary
@Qualifier("springSecUserDetailsServiceImpl")
public class SpringSecUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    UserToUserDetails userToUserDetails;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userToUserDetails.convert(userService.findByUserName(s));
    }
}
