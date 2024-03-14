package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepository extends JpaRepository<ScopeEntity, Long> {
}
