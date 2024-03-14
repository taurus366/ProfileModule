package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.GroupEntity;
import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.model.repository.GroupRepository;
import com.profilemodule.www.model.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupEntity> getAll() {
        return groupRepository.findAll() ;
    }

    @Override
    public boolean delete(GroupEntity entity) {
        groupRepository.delete(entity);
        return true;
    }

    @Override
    public Optional<GroupEntity> get(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public GroupEntity update(GroupEntity entity) {
        return groupRepository.save(entity);
    }

//    @Override
//    public GroupEntity findByPermissions(List<PermissionEnum> permissionEnumList) {
////        return groupRepository.findByPermissions(permissionEnumList);
//    }
}
