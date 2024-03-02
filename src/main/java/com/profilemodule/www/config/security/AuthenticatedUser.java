package com.profilemodule.www.config.security;

import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(UserRepository userRepository, AuthenticationContext authenticationContext) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserEntity> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }
}
