package com.revature.post_user_favorites;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.SneakyThrows;

public class UserRepository {

    private final DynamoDBMapper dbReader;

    public UserRepository() {
        dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    }

    @SneakyThrows
    public User findUserById(String id) {
        User user = dbReader.load(User.class, id);
        return user;
    }

    @SneakyThrows
    public User saveUser(User user) {
        dbReader.save(user);
        return user;
    }
}
