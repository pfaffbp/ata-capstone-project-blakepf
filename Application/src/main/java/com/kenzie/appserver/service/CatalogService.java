package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.CatalogRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

}
