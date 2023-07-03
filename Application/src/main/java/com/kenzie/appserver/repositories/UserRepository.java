package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface UserRepository extends CrudRepository<UserRecord, String> {

    Optional<UserRecord> findByDisplayName(String displayName);
}
