package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.NotificationRecord;
import com.kenzie.capstone.service.model.UserRequest;

public class NotificationDao {
    private DynamoDBMapper mapper;

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
        notificationRecord.setUserRequest(request);
        notificationRecord.setHasBeenViewed(viewed);


        System.out.println(mapper);
        System.out.println(mapper.getClass());
        System.out.println(mapper.getTableModel(NotificationRecord.class).toString());

        System.out.println("mapper chain prints end here");

        System.out.println("IN THE LAMBDA number 1");
        System.out.println(mapper.toString());
        try {
            mapper.save(notificationRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "requestUUID",
                            new ExpectedAttributeValue().withExists(false)
                    )));
            System.out.println("IN THE LAMBDA number 23");
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }
        System.out.println("IN THE LAMBDA number 23");
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

    public NotificationRecord notificationViewed(String requestedUUID, String displayName, String action){
        UserRequest request = new UserRequest();
        request.setAction(action);
        request.setDisplayName(displayName);

        NotificationRecord notificationRecord = new NotificationRecord();
        notificationRecord.setRequestedUUID(requestedUUID);
        notificationRecord.setHasBeenViewed(false);
//        notificationRecord.setRequest(request);

        mapper.delete(notificationRecord);
        return notificationRecord;
    }



}
