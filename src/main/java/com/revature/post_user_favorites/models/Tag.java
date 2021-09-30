package com.revature.post_user_favorites.models;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Tag {

    private String name;
    private String color;

    public Tag() {
        super();
    }
}
