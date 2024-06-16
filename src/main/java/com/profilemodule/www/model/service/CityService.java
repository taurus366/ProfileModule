package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.CityEntity;

import java.util.List;
import java.util.Optional;

public interface CityService {

    Optional<CityEntity> get(Long id);
    CityEntity get2(Long id);
    List<CityEntity> getAll();
    CityEntity save(CityEntity entity);
    List<Long> searchByNameAndLangId(Long langId, String name);
}
