package com.revature.post_user_favorites;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class Tag {
    private String tagName;
    private String tagColor;
}
