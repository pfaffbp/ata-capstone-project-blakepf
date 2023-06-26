package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ReviewRecord;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<ReviewRecord, String> {

}
