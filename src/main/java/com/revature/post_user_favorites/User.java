package com.revature.post_user_favorites;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBTable(tableName = "Users")
public class User {

    @DynamoDBHashKey
    @DynamoDBAttribute
    private String id;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private List<Object> favoriteSets;

    @DynamoDBAttribute
    private List<Object> createdSets;

    @DynamoDBAttribute
    private String profilePicture;

    @DynamoDBAttribute
    private int points;

    @DynamoDBAttribute
    private int wins;

    @DynamoDBAttribute
    private int losses;

    @DynamoDBAttribute
    private String registrationDate;

    @DynamoDBAttribute
    private List<String> gameRecords;

}
