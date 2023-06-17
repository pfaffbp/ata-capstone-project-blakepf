package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.FriendRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
public interface FriendRepository extends CrudRepository<FriendRecord, String> {

}
