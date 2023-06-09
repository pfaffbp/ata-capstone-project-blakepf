package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.CatalogRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CatalogRepository extends CrudRepository<CatalogRecord, String> {
}
