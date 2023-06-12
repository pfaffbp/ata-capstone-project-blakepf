package com.kenzie.appserver.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

    @JsonProperty("Page")
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
