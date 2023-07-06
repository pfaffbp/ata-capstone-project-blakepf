package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.UserData;

import java.util.List;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";
    private static final String GET_USER_ENDPOINT = "user/{displayName}/searchByDisplayName";
    private static final String SET_USER_ENDPOINT = "user";
    private static final String GET_ALL_USERS_ENDPOINT = "user/getAllUsers";
    private static final String PUT_UPDATE_USER_ENDPOINT = "user/updateUser";


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

    public UserData getUserDataByDisplayName(String displayName) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_USER_ENDPOINT.replace("{displayName}", displayName));
        UserData userData;
        try {
            userData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return userData;
    }

    public UserData setUserData(UserData userData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String data;
        try {
            data = mapper.writeValueAsString(userData);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map serialize object to JSON: " + e);
        }
        String response = endpointUtility.postEndpoint(SET_USER_ENDPOINT, data);
        UserData updatedUserData;
        try {
            updatedUserData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return updatedUserData;
    }

    public List<UserData> getAllUsersData() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_USERS_ENDPOINT);
        List<UserData> usersData;
        try {
            usersData = mapper.readValue(response, new TypeReference<List<UserData>>() {});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return usersData;
    }

    public UserData updateUser(UserData userData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String data;
        try {
            data = mapper.writeValueAsString(userData);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map serialize object to JSON: " + e);
        }
        String response = endpointUtility.postEndpoint(PUT_UPDATE_USER_ENDPOINT, data);
        UserData updatedUserData;
        try {
            updatedUserData = mapper.readValue(response, UserData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return updatedUserData;
    }
}

