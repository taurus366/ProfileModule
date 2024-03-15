package com.profilemodule.www.model.service;

import com.profilemodule.www.model.entity.ScopeEntity;

import java.util.List;

public interface ScopeService {

    List<ScopeEntity> getAll();
    boolean remove(ScopeEntity entity);
    ScopeEntity save(ScopeEntity entity);


}
