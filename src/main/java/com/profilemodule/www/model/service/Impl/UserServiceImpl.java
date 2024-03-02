package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.UserEntity;
import com.profilemodule.www.model.repository.UserRepository;
import com.profilemodule.www.model.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> get(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity update(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean delete(UserEntity entity) {
        userRepository.delete(entity);
        return true;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
