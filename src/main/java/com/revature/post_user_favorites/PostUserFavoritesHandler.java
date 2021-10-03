package com.revature.post_user_favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.post_user_favorites.models.Set;
import com.revature.post_user_favorites.models.SetDocument;
import com.revature.post_user_favorites.models.User;
import com.revature.post_user_favorites.repositories.SetRepository;
import com.revature.post_user_favorites.repositories.UserRepository;
import jdk.jfr.internal.Logger;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

public class PostUserFavoritesHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final UserRepository userRepository;
    private final SetRepository setRepo;

    public PostUserFavoritesHandler() {
        userRepository = new UserRepository();
        setRepo = new SetRepository();
    }

    public PostUserFavoritesHandler(UserRepository userRepository, SetRepository setRepo) {
        this.userRepository = userRepository;
        this.setRepo = setRepo;
    }

    /**
     * This lambda function will take in a user_id as a query parameter and a document.
     * The document is a new "SetDocument" that a user favorites. The function will find the user,
     * add the document, and then update it in the database.
     *
     * @param requestEvent
     * @return
     * @authors Robert Ni, Cody McDonald
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        // For CORS
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        responseEvent.setHeaders(headers);

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

        Set set = setRepo.getSetById(setDocument.getId());

        if (set == null){
            logger.log("No set found with provided ID.");
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("Set not found");
            return responseEvent;
        }

        User user = userRepository.findUserById(queryStringParams.get("user_id")); // search for user in database

        if (user == null) {
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("No user found");
            return responseEvent;
        }

        set.setFavorites(set.getFavorites() + 1);
        setDocument = new SetDocument(set);
        setRepo.updateSet(set);
        user.getFavoriteSets().add(setDocument); // add the document to favorites
        responseEvent.setBody(mapper.toJson(userRepository.saveUser(user))); // save the user
        return responseEvent;
    }
}
