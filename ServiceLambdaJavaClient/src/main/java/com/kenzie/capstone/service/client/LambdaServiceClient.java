package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.NotificationData;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String SEND_MESSAGE_ENDPOINT = "message/add";


    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public ExampleData getExampleData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

    public ExampleData setExampleData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_EXAMPLE_ENDPOINT, data);
        ExampleData exampleData;
        try {
            exampleData = mapper.readValue(response, ExampleData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return exampleData;
    }

    public NotificationData setNotificatioNData(String userDisplayName, String friendDisplayName) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;

        try {
//            request = mapper.writeValueAsString()
        }
        String response = endpointUtility.postEndpoint(SEND_MESSAGE_ENDPOINT, userDisplayName, friendDisplayName);

        NotificationData notificationData;

        try {
            notificationData = mapper.readValue(response, NotificationData.class);
        } catch (Exception e){
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return notificationData;
    }
}