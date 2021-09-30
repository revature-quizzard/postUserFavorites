package com.revature.post_user_favorites.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@DynamoDbBean
public class User {

    private String id;
    private String username;
    private List<SetDocument> favorite_sets;
    private List<SetDocument> created_sets;
    private String profile_picture;
    private int points;
    private int wins;
    private int losses;
    private String registration_date;
    private List<String> gameRecords;

    public User() {
        super();
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
