package com.app.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.lang.reflect.Array;
import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User user = new User("user", "user", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        User admin = new User("admin", "admin", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"),
                                                                                 new SimpleGrantedAuthority("ROLE_ADMIN")));
        return new InMemoryUserDetailsManager(Arrays.asList(user, admin));
    }
}
