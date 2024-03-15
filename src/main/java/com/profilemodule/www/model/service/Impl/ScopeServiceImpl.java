package com.profilemodule.www.model.service.Impl;

import com.profilemodule.www.model.entity.ScopeEntity;
import com.profilemodule.www.model.repository.ScopeRepository;
import com.profilemodule.www.model.service.ScopeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;

    public ScopeServiceImpl(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public List<ScopeEntity> getAll() {
        return scopeRepository.findAll();
    }

    @Override
    public boolean remove(ScopeEntity entity) {
        scopeRepository.delete(entity);
        return true;
    }

    @Override
    public ScopeEntity save(ScopeEntity entity) {
        return scopeRepository.save(entity);
    }
}
