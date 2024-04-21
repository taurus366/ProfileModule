package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.CityEntity;
import com.profilemodule.www.model.repository.CityRepository;
import com.profilemodule.www.model.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Optional<CityEntity> get(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<CityEntity> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public CityEntity save(CityEntity entity) {
        return cityRepository.save(entity);
    }
}
