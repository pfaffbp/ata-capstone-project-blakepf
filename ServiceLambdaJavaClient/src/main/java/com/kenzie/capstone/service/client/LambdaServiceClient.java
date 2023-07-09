package com.kenzie.capstone.service.client;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.*;

import javax.management.Notification;
import java.util.List;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";

    private static final String NOTIFICATION_GET_ENDPOINT = "notification/getNotification/{displayName}";

    private static final String NOTIFICATION_SET_ENDPOINT = "notification/setNotification/{displayName}";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public List<NotificationData> getNotificationData(String displayName){
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(NOTIFICATION_GET_ENDPOINT.replace("{displayName}",
                displayName));

        List<NotificationData> notificationData = null;
        try{
            notificationData = mapper.readValue(response, new TypeReference<List<NotificationData>>(){});
        }catch(JsonProcessingException e){
            System.out.println( "Line 64 " + e.getCause());
        }
        return notificationData;
    }

    public NotificationData setNotificationData(SetNotificationData data, String displayName) {
        String jsonData = null;

        try {
            jsonData = mapper.writeValueAsString(data);
        }catch(Exception e){
            e.getMessage();
        }
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(NOTIFICATION_SET_ENDPOINT.replace("{displayName}", displayName), jsonData);

        NotificationData stringData;
        try{
            stringData = mapper.readValue(response, NotificationData.class);
        } catch (Exception e){
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return stringData;
    }
}