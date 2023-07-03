package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.DAO.GraphQLResponse;

import java.util.List;

public class FrontPageAnimeRequest {
//    private List<CatalogResponse> catalogResponseList;
//
//    @JsonProperty("catalogResponseList")
//    public List<CatalogResponse> getCatalogResponseList() {
//        return catalogResponseList;
//    }
//
//    public void setCatalogResponseList(List<CatalogResponse> catalogResponseList) {
//        this.catalogResponseList = catalogResponseList;
//    }

    private GraphQLResponse graphQLResponse;

    @JsonProperty("graphQLResponse")
    public GraphQLResponse getGraphQLResponse() {
        return graphQLResponse;
    }

    public void setGraphQLResponse(GraphQLResponse graphQLResponse) {
        this.graphQLResponse = graphQLResponse;
    }
}
