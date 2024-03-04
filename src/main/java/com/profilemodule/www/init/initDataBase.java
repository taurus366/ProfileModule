package com.profilemodule.www.init;

import com.profilemodule.www.model.entity.GroupEntity;
import com.profilemodule.www.model.entity.PermissionEntity;
import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.model.repository.GroupRepository;
import com.profilemodule.www.model.repository.PermissionRepository;
import com.profilemodule.www.model.repository.UserRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
@Component
public class initDataBase implements CommandLineRunner {


    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public initDataBase(UserRepository userRepository, GroupRepository groupRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        initAdmin();

    }


    protected void initAdmin() {

//        Session session =

        if(userRepository.findAll().isEmpty()) {
            final UserEntity admin = new UserEntity();
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setUsername("admin");
            userRepository.save(admin);
        }

        if(groupRepository.findAll().isEmpty()) {

            PermissionEntity permission = new PermissionEntity();
            permission.setPermission(PermissionEnum.VIEW);

            GroupEntity entity = new GroupEntity();
            entity.getPermissions().add(permission);
            entity.setName("ADMIN");
            groupRepository.save(entity);
        }


        final UserEntity admin = userRepository.findByUsername("admin");


        if(!groupRepository.findAll().isEmpty() && admin != null) {
            Hibernate.initialize(admin.getGroups());

            final List<GroupEntity> groups = groupRepository.findAll();
            admin.getGroups().addAll(new HashSet<>(groups));
            userRepository.save(admin);
        }






    }
}
