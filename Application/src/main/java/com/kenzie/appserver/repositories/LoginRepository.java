package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.repositories.model.LoginRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface LoginRepository extends CrudRepository<LoginRecord, String> {
     Optional<LoginRecord> findByEmail(String email);

}
