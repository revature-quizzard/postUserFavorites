package com.revature.post_user_favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.post_user_favorites.stubs.TestLogger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostUserFavoritesHandlerTest {

    static TestLogger testLogger;
    private Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    PostUserFavoritesHandler sut;
    Context mockContext;
    UserRepository mockUserRepo;

    @BeforeAll
    static void beforeAll() { testLogger = new TestLogger(); }

    @BeforeEach
    void setUp() {
        mockContext = mock(Context.class);
        mockUserRepo = mock(UserRepository.class);

        sut = new PostUserFavoritesHandler(mockUserRepo);

        when(mockContext.getLogger()).thenReturn(testLogger);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        mockContext = null;
        mockUserRepo = null;
    }

    @AfterAll
    void afterAll() {
        testLogger.close();
    }

    @Test
    void handleRequest() {
    }
}