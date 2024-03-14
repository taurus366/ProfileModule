package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.ScopeCleanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeCleanRepository extends JpaRepository<ScopeCleanEntity, Long> {
}
