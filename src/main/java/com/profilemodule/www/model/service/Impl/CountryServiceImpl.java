package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.CountryEntity;
import com.profilemodule.www.model.repository.CountryRepository;
import com.profilemodule.www.model.service.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<CountryEntity> findAll(int page, int pageSize) {
        return countryRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public List<CountryEntity> findAllByCodeName(String name, int page, int pageSize) {
        return countryRepository.findAllByCode(name.toLowerCase(), PageRequest.of(page, pageSize));
    }
}
