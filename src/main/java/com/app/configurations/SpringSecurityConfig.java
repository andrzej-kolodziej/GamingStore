package com.app.configurations;


import com.app.services.security.SpringSecUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;


/**
 * Created by tom on 8/9/2016.
 */
@EnableWebSecurity
@Configuration
//@ConditionalOnProperty(value = "app.security.basic.enabled", havingValue = "false")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    private AuthenticationProvider authenticationProvider;

    @Autowired
    private SpringSecUserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Qualifier("daoAuthenticationProvider")
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!securityEnabled)
            return;
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/store").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/workinprogress").permitAll()
                .antMatchers("/privacy-policy").permitAll()
                .antMatchers("/user-agreement").permitAll()
                .antMatchers("/index/**").permitAll()
                .antMatchers("/cart").permitAll()
                .antMatchers("/generatedummydata").permitAll()
                .antMatchers("/customer/post").authenticated()
                .antMatchers("/customer/orderhistory").authenticated()
                .antMatchers("/customer/**").permitAll()
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/framents/**").permitAll()
                .antMatchers("/bundle/**").authenticated()
                .antMatchers("/developer/**").authenticated()
                .antMatchers("/product/**").authenticated()
                .antMatchers("/publisher/**").authenticated()
                .antMatchers("/user/**").authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll().logoutUrl("/logout")
                .deleteCookies("my-remember-me")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/store")
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");
                /*.and()
                .rememberMe()
                .tokenValiditySeconds(2678400);*/
        //http.csrf().disable();
        //http.headers().frameOptions().disable();
    }
}
