package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
//    GroupEntity findByPermissions(List<PermissionEntity> permissions);
}
