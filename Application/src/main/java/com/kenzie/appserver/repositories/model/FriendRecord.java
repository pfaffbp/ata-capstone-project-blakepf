package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import nonapi.io.github.classgraph.json.Id;

import java.util.Date;

@DynamoDBTable(tableName = "Friend")
public class FriendRecord {
    @Id
    @DynamoDBHashKey(attributeName = "id")
    private Integer id;
    @DynamoDBAttribute(attributeName = "createdDate")
    private Date createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createdDate;
    }

    public void setCreateDate(Date createDate) {
        this.createdDate = createDate;
    }
}
