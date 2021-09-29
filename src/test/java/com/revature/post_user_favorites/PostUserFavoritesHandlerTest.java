package com.revature.post_user_favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.post_user_favorites.stubs.TestLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

public class PostUserFavoritesHandlerTest {

    static TestLogger testLogger;
    static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    PostUserFavoritesHandler sut;
    Context mockContext;
    UserRepository mockUserRepository;

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
        sut = new PostUserFavoritesHandler(mockUserRepository);
        mockContext = mock(Context.class);
        when(mockContext.getLogger()).thenReturn(testLogger);
    }

    @AfterEach
    public void afterEachTest() {
        sut = null;
        reset(mockContext, mockUserRepository);
    }
}