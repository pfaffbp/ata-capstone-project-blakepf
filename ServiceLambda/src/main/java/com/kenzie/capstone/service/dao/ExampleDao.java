package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;

import java.util.List;

public class ExampleDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ExampleDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public ExampleData storeExampleData(ExampleData exampleData) {
        try {
            mapper.save(exampleData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return exampleData;
    }

    public List<ExampleRecord> getExampleData(String id) {
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(id);

        DynamoDBQueryExpression<ExampleRecord> queryExpression = new DynamoDBQueryExpression<ExampleRecord>()
                .withHashKeyValues(exampleRecord)
                .withConsistentRead(false);

        return mapper.query(ExampleRecord.class, queryExpression);
    }

    public ExampleRecord setExampleData(String id, String data) {
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(id);
        exampleRecord.setData(data);

        try {
            mapper.save(exampleRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return exampleRecord;
    }

    public List<UserRecord> getUserData (String displayName) {
        UserRecord record = new UserRecord();
        record.setDisplayName(displayName);

        DynamoDBQueryExpression<UserRecord> queryExpression = new DynamoDBQueryExpression<UserRecord>()
                .withHashKeyValues(record)
                .withConsistentRead(false);

        return mapper.query(UserRecord.class, queryExpression);
    }

    public List <UserRecord> getAllUsers() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(UserRecord.class, scanExpression);
    }
}
