package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.NotificationRequest;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.NotificationData;
import com.kenzie.capstone.service.model.SetNotificationData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private LambdaServiceClient lambdaServiceClient;

    public NotificationService(LambdaServiceClient lambdaServiceClient){
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public NotificationData createNotification(String displayName, NotificationRequest request){
        SetNotificationData data = new SetNotificationData();
        data.setHasBeenViewed(false);
        data.setUserRequest(request.getUserRequest());
        data.setRequestedUUID(displayName);

        return lambdaServiceClient.setNotificationData(data, displayName);
    }

    public List<NotificationData> getNotifications(String displayName){

        return lambdaServiceClient.getNotificationData(displayName);
    }

}
