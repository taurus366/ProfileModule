package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.GroupEntity;
import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(),
                    getAuthorities(userEntity));
        }
    }

    private static List<SimpleGrantedAuthority> getAuthorities(UserEntity userEntity) {

        final Set<GroupEntity> userGroups = userEntity.getGroups();

        List<SimpleGrantedAuthority> permissions = userGroups
                .stream()
                .flatMap(group -> group.getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(String.format("%s%s_%s", "ROLE_", group.getName().toUpperCase(), permission.getPermission().name().toUpperCase()))))
                .toList();
        return permissions;
    }

//    public void reloadAuthorities(String username) {
//        UserDetails userDetails = loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

}

//        List<GrantedAuthority> g = new ArrayList<>();
//        g.add(new SimpleGrantedAuthority("ROLE_ABOUT"));

//        return g;

//    @RolesAllowed({"ABOUT"})
//    check users groups permisison for read,
//    FOR WRITE,UPLOAD,DELETE
//     AuthenticationContext authenticationContext = new AuthenticationContext();
//        authenticationContext.getAuthenticatedUser(UserDetails.class).ifPresent(userEntity -> {
//            System.out.println(userEntity.getUsername());
//        });

//    while loading getAuthorities - > if there is VIEW,UPDATE,DELETE,READ change them to ->>>
//    1.- get name of the permissionGroup(example: ABOUT) then read permissions (example: VIEW, UPDATE, READ) and
//    new SimpleGrantedAuthority("ABOUT_VIEW") for menu item @ROlesAllowed("ABOUT_VIEW")
//    new SimpleGrantedAuthority("ABOUT_READ") .....
//     private List<GrantedAuthority> getAuthorities(UserEntity userEntity) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (GroupEntity group : userEntity.getGroups()) {
//            for (PermissionEntity permission : group.getPermissions()) {
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            }
//        }
//        return authorities;
//    }
