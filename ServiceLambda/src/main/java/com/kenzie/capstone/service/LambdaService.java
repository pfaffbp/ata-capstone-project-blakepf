package com.kenzie.capstone.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.dao.ExampleDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LambdaService {

    private NotificationDao notificationDao;

    static final Logger log = LogManager.getLogger();

    @Inject
    public LambdaService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

//    public ExampleData getExampleData(String id) {
//        List<ExampleRecord> records = exampleDao.getExampleData(id);
//        if (records.size() > 0) {
//            return new ExampleData(records.get(0).getId(), records.get(0).getData());
//        }
//        return null;
//    }
//
//    public ExampleData setExampleData(String data) {
//        String id = UUID.randomUUID().toString();
//        ExampleRecord record = exampleDao.setExampleData(id, data);
//        return new ExampleData(id, data);
//    }


    public NotificationRecord createRequest(String data, String displayName){
        ObjectMapper objectMapper = new ObjectMapper();
        SetNotificationData record = null;
        try {
            record = objectMapper.readValue(data, SetNotificationData.class);
        } catch(JsonProcessingException e){
            e.getCause();
        }
         NotificationRecord notificationRecord =  notificationDao.createNotification(displayName,
                 record.getUserRequest().getDisplayName(),
                 record.getUserRequest().getAction(),
                 record.isHasBeenViewed());

//        log.info(record.getUserRequest());
//        String userDisplay = record.getUserRequest().substring(0, record.getUserRequest().charAt(':'));
//        String action = record.getUserRequest().substring(record.getUserRequest().charAt(':'), record.getUserRequest().length() - 1);
//
//        log.info(userDisplay);
//        log.info(action);
//
//        System.out.println("line here 58 lambdaSrevice " + record);
//        NotificationRecord notificationRecord = notificationDao.createNotification(displayName, userDisplay, action, record.isHasBeenViewed());
//        System.out.println("line here 60 lamdaservice" + notificationRecord);
        return notificationRecord;
    }

    public List<NotificationRecord> getNotification(String displayName){
        List<NotificationRecord> notificationRecords = notificationDao.getNotification(displayName);
        System.out.println(notificationRecords.toString());
        return notificationRecords;
    }

}

