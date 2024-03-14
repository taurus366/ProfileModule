package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<GroupEntity> getAll();
    boolean delete(GroupEntity entity);
    Optional<GroupEntity> get(Long id);
    GroupEntity update(GroupEntity entity);
//    GroupEntity findByPermissions(String name);
}
