package com.kenzie.capstone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private NotificationDao notificationDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.notificationDao = mock(NotificationDao.class);
        this.lambdaService = new LambdaService(notificationDao);
    }

//    @Test
//    void setDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String data = "somedata";
//
//        // WHEN
//        ExampleData response = this.lambdaService.setExampleData(data);
//
//        // THEN
//        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());
//
//        assertNotNull(idCaptor.getValue(), "An ID is generated");
//        assertEquals(data, dataCaptor.getValue(), "The data is saved");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }
//
//    @Test
//    void getDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String id = "fakeid";
//        String data = "somedata";
//        ExampleRecord record = new ExampleRecord();
//        record.setId(id);
//        record.setData(data);
//
//
//        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));
//
//        // WHEN
//        ExampleData response = this.lambdaService.getExampleData(id);
//
//        // THEN
//        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());
//
//        assertEquals(id, idCaptor.getValue(), "The correct id is used");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(id, response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }

    // Write additional tests here

    @Test
    void testCreateRequest() {
        // Arrange
        String data = "{\"userRequest\":{\"displayName\":\"Bloopy\",\"action\":\"create\"},\"hasBeenViewed\":false}";
        String displayName = "Bloopy";

        SetNotificationData setNotificationData = new SetNotificationData();
        UserRequest userRequest = new UserRequest();
        userRequest.setDisplayName("Bloopy");
        userRequest.setAction("create");
        setNotificationData.setUserRequest(userRequest);
        setNotificationData.setHasBeenViewed(false);

        NotificationRecord expectedNotificationRecord = new NotificationRecord();
        expectedNotificationRecord.setRequestedUUID(displayName);
        expectedNotificationRecord.setUserRequest(userRequest.toString());
        expectedNotificationRecord.setHasBeenViewed(false);

        when(notificationDao.createNotification(eq(displayName), any(), any(), eq(false)))
                .thenReturn(expectedNotificationRecord);

        NotificationRecord result = lambdaService.createRequest(data, displayName);

        assertEquals(expectedNotificationRecord, result);
        verify(notificationDao, times(1)).createNotification(eq(displayName), any(), any(), eq(false));
    }

    @Test
    void getNotificationTest() {
        // GIVEN
        String displayName = "Alice";
        List<NotificationRecord> expectedRecords = List.of(
                new NotificationRecord(),
                new NotificationRecord()
        );
        when(notificationDao.getNotification(displayName)).thenReturn(expectedRecords);

        // WHEN
        List<NotificationRecord> result = lambdaService.getNotification(displayName);

        // THEN
        verify(notificationDao, times(1)).getNotification(displayName);
        assertEquals(expectedRecords, result);
    }

}