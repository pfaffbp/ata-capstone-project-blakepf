package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.NotificationRequest;
import com.kenzie.appserver.service.NotificationService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import static groovy.json.JsonOutput.toJson;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;

@IntegrationTest
public class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    NotificationService notificationService;


    @Test
    public void testGetNotificationsBadRequest() throws Exception {
        // Prepare test data
        String displayName = "invalid.display.name";

        // Perform the request
        mvc.perform(get("/notification/getNotification/{displayName}", displayName))
                .andExpect(status().is(200));
    }
}