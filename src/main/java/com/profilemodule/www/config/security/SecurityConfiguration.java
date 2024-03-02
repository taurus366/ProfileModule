package com.profilemodule.www.config.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
        );

    }
}
