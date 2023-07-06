package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class UserDao {
    private DynamoDBMapper mapper;

    public UserDao(DynamoDBMapper mapper){
        this.mapper = mapper;
    }
}
