package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.CountryEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    List<CountryEntity> findAllByCode(String name, PageRequest pageRequest);
}
