package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserEntity> get(Long id);
    UserEntity update(UserEntity entity);
    List<UserEntity> getAll();
    boolean delete(UserEntity entity);
    UserEntity findByUsername(String username);


}
