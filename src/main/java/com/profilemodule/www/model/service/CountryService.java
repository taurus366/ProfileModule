package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.CountryEntity;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Optional<CountryEntity> get(Long id);
    List<CountryEntity> getAll();
}
