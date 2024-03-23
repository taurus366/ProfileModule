package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    @Query("SELECT lang from LanguageEntity lang WHERE lang.isDefault = true")
    List<LanguageEntity> findAllByIsDefaultTrue();

    List<LanguageEntity> findAllByActiveIsTrue();
}
