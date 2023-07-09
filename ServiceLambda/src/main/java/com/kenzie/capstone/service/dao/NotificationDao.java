package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.NotificationRecord;
import com.kenzie.capstone.service.model.UserRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationDao {
    private DynamoDBMapper mapper;

    private final String NOTIFICATION_TABLE = "NotificationTable";

    public NotificationDao(DynamoDBMapper mapper){
        this.mapper = mapper;
    }

//    public NotificationRecord storeNotification(String uuid, Map<String, String> notification){
//        NotificationRecord record = mapper.load(NotificationRecord.class, uuid);
//
//        if(record == null){
//            NotificationRecord notificationRecord = new NotificationRecord();
//            notificationRecord.setRequestedUUID(uuid);
//            notificationRecord.setNotification(notification);
//            return notificationRecord;
//        } else {
//            Map<String, String> map = record.getNotification();
//            map.putAll(notification);
//            NotificationRecord updateNotification = new NotificationRecord();
//            updateNotification.setRequestedUUID(uuid);
//            updateNotification.setNotification(map);
//
//            return updateNotification;
//        }
//    }

    public NotificationRecord createNotification(String requestedUUID, String displayName, String action, boolean viewed){
//        UserRequest request = new UserRequest();
//        request.setAction(action);
//        request.setDisplayName(displayName);
//
//        NotificationRecord notificationRecord = new NotificationRecord();
//        notificationRecord.setRequestedUUID(requestedUUID);
//        notificationRecord.setHasBeenViewed(false);
//        notificationRecord.setRequest(request);

        UserRequest request = new UserRequest();
        request.setAction(action);
        request.setDisplayName(displayName);

        NotificationRecord notificationRecord = new NotificationRecord();
        notificationRecord.setRequestedUUID(requestedUUID);
        notificationRecord.setUserRequest(request.toString());
        notificationRecord.setHasBeenViewed(viewed);

        try {
//            mapper.save(notificationRecord, new DynamoDBSaveExpression()
//                    .withExpected(ImmutableMap.of(
//                            "requestUUID",
//                            new ExpectedAttributeValue().withExists(false)
//                    )));

            mapper.save(notificationRecord);
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }
       return notificationRecord;
    }

//        System.out.println("IN THE LAMBDA number 1");
//        mapper.save(notificationRecord);
//        System.out.println("IN THE LAMBDA number 23");
//
//
//        NotificationRecord notificationRecord1 = mapper.load(notificationRecord);
//
//        System.out.println("IN THE LAMBDA " + notificationRecord1);
//        return notificationRecord;
//    }

    public List<NotificationRecord> getNotification(String displayName){
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(":requestedUUID", new AttributeValue().withS(displayName));
//        attributeValueMap.put("hasBeenViewed:", new AttributeValue().withBOOL(false));

        DynamoDBQueryExpression<NotificationRecord> queryExpression = new DynamoDBQueryExpression<NotificationRecord>()
                .withConsistentRead(false)
//                .withKeyConditionExpression("displayName = :displayName and hasBeenViewed = :hasBeenViewed")
                .withKeyConditionExpression("requestedUUID = :requestedUUID")
                .withExpressionAttributeValues(attributeValueMap);


        PaginatedQueryList<NotificationRecord> queryList = mapper.query(NotificationRecord.class, queryExpression);
        System.out.println(queryList.toString());
        return queryList.stream()
                .filter(notificationRecord -> notificationRecord.isHasBeenViewed() == false)
                .collect(Collectors.toList());


//        return queryList;
    }



}
