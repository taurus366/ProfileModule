package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.repository.CountryRepository;
import com.profilemodule.www.model.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Optional<CountryEntity> get(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public List<CountryEntity> getAll() {
        return countryRepository.findAll();
    }
}
