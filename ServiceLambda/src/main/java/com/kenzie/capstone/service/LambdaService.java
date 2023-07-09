package com.kenzie.capstone.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.NotificationData;
import com.kenzie.capstone.service.model.NotificationRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private NotificationDao notificationDao;

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
        NotificationData record = null;
        try {
            record = objectMapper.readValue(data, NotificationData.class);
        } catch(JsonProcessingException e){
            e.getCause();
        }
         NotificationRecord notificationRecord =  notificationDao.createNotification(displayName,
                 record.getRequest().getDisplayName(),
                 record.getRequest().getAction(),
                 record.isHasBeenViewed());
        return notificationRecord;
    }

    public PaginatedQueryList<NotificationRecord> getNotification(String displayName){
        PaginatedQueryList<NotificationRecord> notificationRecords = notificationDao.getNotification(displayName);
        return notificationRecords;
    }

}
