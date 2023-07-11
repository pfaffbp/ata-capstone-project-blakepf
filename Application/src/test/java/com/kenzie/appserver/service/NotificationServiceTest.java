package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.NotificationRequest;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.NotificationData;
import com.kenzie.capstone.service.model.SetNotificationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private LambdaServiceClient lambdaServiceClient;

    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        notificationService = new NotificationService(lambdaServiceClient);
    }

    @Test
    public void testCreateNotification() {
        String displayName = "Bubu";
        NotificationRequest request = new NotificationRequest();

        when(lambdaServiceClient.setNotificationData(any(SetNotificationData.class), eq(displayName)))
                .thenReturn(new NotificationData());
        NotificationData result = notificationService.createNotification(displayName, request);

        verify(lambdaServiceClient, times(1)).setNotificationData(any(SetNotificationData.class), eq(displayName));
        assertEquals(NotificationData.class, result.getClass());
    }

    @Test
    public void testGetNotifications() {
        String displayName = "Serena";

        List<NotificationData> mockedResponse = Collections.singletonList(new NotificationData());
        when(lambdaServiceClient.getNotificationData(any())).thenReturn(mockedResponse);

        List<NotificationData> result = notificationService.getNotifications(displayName);

        assertNotNull(result);
        assertEquals(mockedResponse, result);

        verify(lambdaServiceClient, times(1)).getNotificationData(eq(displayName));
    }
}