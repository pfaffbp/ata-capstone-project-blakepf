package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;
@DynamoDBTable(tableName =  "Notifications")
public class NotificationRecord {

        public String message;
        private String messageId;
        private String follower;
        private String following;

        @DynamoDBAttribute(attributeName = "message")
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @DynamoDBHashKey(attributeName = "messageId")
        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
        @DynamoDBAttribute(attributeName = "follower")
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "follower", attributeName = "follower")
        public String getFollower() {
            return follower;
        }

        public void setFollower(String follower) {
            this.follower = follower;
        }

        @DynamoDBAttribute(attributeName = "following")
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "following", attributeName = "following")
        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NotificationRecord that = (NotificationRecord) o;
            return Objects.equals(message, that.message) && Objects.equals(messageId, that.messageId) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, messageId, follower, following);
        }
}
