package com.revature.post_user_favorites;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

// endpoint is /users/favorites?user_id={id}
public class PostUserFavoritesHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final UserRepository userRepository;

    public PostUserFavoritesHandler() {
        userRepository = new UserRepository();
    }

    public PostUserFavoritesHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        Map<String, String> pathParams = requestEvent.getQueryStringParameters();
        SetDocument setDocument = mapper.fromJson(requestEvent.getBody(), SetDocument.class);

        if (pathParams == null || setDocument == null) {
            responseEvent.setStatusCode(400);
            return responseEvent;
        }

        User user = userRepository.findUserById(pathParams.get("user_id"));

        return responseEvent;
    }
}
