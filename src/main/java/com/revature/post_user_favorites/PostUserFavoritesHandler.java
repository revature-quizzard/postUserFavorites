package com.revature.post_user_favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.post_user_favorites.models.SetDocument;
import com.revature.post_user_favorites.models.User;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;

public class PostUserFavoritesHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final UserRepository userRepository;

    public PostUserFavoritesHandler() {
        userRepository = UserRepository.getInstance();
    }

    public PostUserFavoritesHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This lambda function will take in a user_id as a query parameter and a document.
     * The document is a new "Set" that a user favorites. The function will find the user,
     * add the document, and then save it to the database.
     *
     * @param requestEvent
     * @return
     * @author Robert Ni
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        Map<String, String> queryStringParams = requestEvent.getQueryStringParameters();
        SetDocument setDocument = mapper.fromJson(requestEvent.getBody(), SetDocument.class);

        // returns a bad request status code if params or document to be added is null
        if (queryStringParams == null || setDocument == null) {
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("Missing params in request");
            return responseEvent;
        }

        User user = userRepository.findUserById(queryStringParams.get("user_id")); // search for user in database

        if (user == null) {
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("No user found");
            return responseEvent;
        }

        user.getFavorite_sets().add(setDocument); // add the document to favorites
        responseEvent.setBody(mapper.toJson(userRepository.saveUser(user))); // save the user
        return responseEvent;
    }
}
