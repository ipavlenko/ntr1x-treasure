package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Resource;

@Service
public class CategoryService implements ICategoryService {

    @Inject
    private EntityManager em;
    
    @Override
    public void createCategories(Resource resource, CreateCategory[] params) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateCategories(Resource resource, UpdateCategory[] params) {
        // TODO Auto-generated method stub
        
    }
}
