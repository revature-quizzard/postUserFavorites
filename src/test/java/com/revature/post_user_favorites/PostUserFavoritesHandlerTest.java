package com.revature.post_user_favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.post_user_favorites.models.SetDocument;
import com.revature.post_user_favorites.models.User;
import com.revature.post_user_favorites.repositories.SetRepository;
import com.revature.post_user_favorites.repositories.UserRepository;
import com.revature.post_user_favorites.stubs.TestLogger;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostUserFavoritesHandlerTest {

    static TestLogger testLogger;
    static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    PostUserFavoritesHandler sut;
    Context mockContext;
    UserRepository mockUserRepository;
    SetRepository mockSetRepo;

    @BeforeAll
    public static void beforeAllTests() {
        testLogger = new TestLogger();
    }

    @AfterAll
    public static void afterAllTests() {
        testLogger.close();
    }

    @BeforeEach
    public void beforeEachTest() {
        mockUserRepository = mock(UserRepository.class);
        mockSetRepo = mock(SetRepository.class);
        sut = new PostUserFavoritesHandler(mockUserRepository, mockSetRepo);
        mockContext = mock(Context.class);
        when(mockContext.getLogger()).thenReturn(testLogger);
    }

    @AfterEach
    public void afterEachTest() {
        sut = null;
        reset(mockContext, mockUserRepository);
    }

    @Test
    public void given_validParamsAndValidRequestBody_returnsSuccess() {
        SetDocument requestBody = SetDocument.builder()
                .id("valid")
                .setName("valid")
                .tags(new ArrayList<>())
                .isPublic(true)
                .author("valid")
                .views(0)
                .plays(0)
                .studies(0)
                .favorites(0)
                .build();

        User validUser = User.builder()
                .id("valid")
                .username("valid")
                .favoriteSets(new ArrayList<>())
                .createdSets(new ArrayList<>())
                .profilePicture("valid")
                .points(0)
                .wins(0)
                .losses(0)
                .registrationDate("valid")
                .gameRecords(new ArrayList<>())
                .build();

        User expectedUser = User.builder()
                .id("valid")
                .username("valid")
                .favoriteSets(new ArrayList<>())
                .createdSets(new ArrayList<>())
                .profilePicture("valid")
                .points(0)
                .wins(0)
                .losses(0)
                .registrationDate("valid")
                .gameRecords(new ArrayList<>())
                .build();

        expectedUser.getFavoriteSets().add(requestBody);

        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/users/favorites");
        mockRequestEvent.withHttpMethod("POST");
        mockRequestEvent.withHeaders(null);
        mockRequestEvent.withBody(mapper.toJson(requestBody));
        mockRequestEvent.withQueryStringParameters(Collections.singletonMap("user_id", "valid"));

        when(mockUserRepository.findUserById(anyString())).thenReturn(validUser);
        when(mockUserRepository.saveUser(any())).thenReturn(expectedUser);

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        expectedResponse.setBody(mapper.toJson(expectedUser));

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        expectedResponse.setHeaders(headers);

        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        verify(mockUserRepository, times(1)).findUserById(anyString());
        verify(mockUserRepository, times(1)).saveUser(any());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void given_noRequestBody_returnBadRequestStatusCode() {
        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/users/favorites");
        mockRequestEvent.withHttpMethod("POST");
        mockRequestEvent.withHeaders(null);
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(Collections.singletonMap("user_id", "valid"));

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        expectedResponse.setStatusCode(HttpStatusCode.BAD_REQUEST);

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        expectedResponse.setHeaders(headers);

        expectedResponse.setBody("Missing params in request");

        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        verify(mockUserRepository, times(0)).saveUser(any());
        verify(mockUserRepository, times(0)).findUserById(any());
        assertEquals(expectedResponse, actualResponse);
    }
}