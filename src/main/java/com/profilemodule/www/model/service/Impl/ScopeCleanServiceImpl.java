package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.ScopeCleanEntity;
import com.profilemodule.www.model.repository.ScopeCleanRepository;
import com.profilemodule.www.model.service.ScopeCleanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScopeCleanServiceImpl implements ScopeCleanService {

    private final ScopeCleanRepository scopeCleanRepository;

    public ScopeCleanServiceImpl(ScopeCleanRepository scopeCleanRepository) {
        this.scopeCleanRepository = scopeCleanRepository;
    }

    @Override
    public List<ScopeCleanEntity> getAll() {
        return scopeCleanRepository.findAll();
    }
}
