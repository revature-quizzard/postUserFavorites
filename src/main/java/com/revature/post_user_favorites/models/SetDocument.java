package com.revature.post_user_favorites.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@DynamoDbBean
public class SetDocument {

    private String id;
    private String setName;
    private List<Tag> tags;
    private boolean isPublic;
    private String author;
    private int views;
    private int plays;
    private int studies;
    private int favorites;

    public SetDocument() {
        super();
    }
}
