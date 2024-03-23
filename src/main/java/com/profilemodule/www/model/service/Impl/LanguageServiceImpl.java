package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.LanguageEntity;
import com.profilemodule.www.model.repository.LanguageRepository;
import com.profilemodule.www.model.service.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<LanguageEntity> getAll() {
        return languageRepository.findAll();
    }

    @Override
    public LanguageEntity getById(Long id) {
        return languageRepository.getReferenceById(id);
    }

    @Override
    public LanguageEntity save(LanguageEntity entity) {
        return languageRepository.save(entity);
    }

    @Override
    public List<LanguageEntity> findLanguagesByDefaultStatusTrue() {
        return languageRepository.findAllByIsDefaultTrue();
    }

    @Override
    public List<LanguageEntity> getAllByActive() {
        return languageRepository.findAllByActiveIsTrue();
    }
}
