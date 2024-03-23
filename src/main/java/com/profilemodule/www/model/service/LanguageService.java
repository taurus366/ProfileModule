package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.LanguageEntity;

import java.util.List;

public interface LanguageService {

    List<LanguageEntity> getAll();
    LanguageEntity getById(Long id);

    LanguageEntity save(LanguageEntity entity);
    List<LanguageEntity> findLanguagesByDefaultStatusTrue();
    List<LanguageEntity> getAllByActive();

}
