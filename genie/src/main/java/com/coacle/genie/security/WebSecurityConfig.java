package com.coacle.genie.security;

import com.coacle.genie.api.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserController userController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return s -> {
            if ("admin".equals(s)) {
                return User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("password")
                        .roles("ADMIN")
                        .build();
            } else {
                Optional<com.coacle.genie.model.User> user = userController.getUsers()
                        .stream().filter(u -> s.equals(u.getId())).findAny();
                if (!user.isPresent()) {
                    throw new UsernameNotFoundException("No user with id: " + s + " found");
                }
                return User.withDefaultPasswordEncoder()
                        .username(user.get().getId())
                        .password(user.get().getEmail())
                        .roles("USER")
                        .build();
            }
        };
    }
}