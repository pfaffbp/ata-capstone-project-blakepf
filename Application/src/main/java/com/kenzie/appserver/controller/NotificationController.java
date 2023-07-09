package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.NotificationRequest;
import com.kenzie.appserver.service.NotificationService;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.NotificationData;
import com.kenzie.capstone.service.model.SetNotificationData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping("/setNotification/{displayName}")
    public ResponseEntity<NotificationData> createNotification(@PathVariable String displayName,
                                                @RequestBody NotificationRequest request){
        NotificationData data = notificationService.createNotification(displayName, request);
        if(data == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(data);
    }

    @GetMapping("/getNotification/{displayName}")
    public ResponseEntity<List<NotificationData>> getNotifications(@PathVariable String displayName){
        List<NotificationData> notificationDataList = notificationService.getNotifications(displayName);

        if(notificationDataList == null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(notificationDataList);
    }
}
