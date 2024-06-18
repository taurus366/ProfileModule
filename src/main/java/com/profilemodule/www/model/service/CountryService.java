package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.CountryEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Optional<CountryEntity> get(Long id);
    List<CountryEntity> getAll();

    Page<CountryEntity> findAll(int page, int pageSize);

    List<CountryEntity> findAllByCodeName(String name, int page, int pageSize);

    boolean isTableEmpty();

}
