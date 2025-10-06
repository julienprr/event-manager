package com.julienprr.eventmanager.user_service;

import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.model.UserStatus;
import com.julienprr.eventmanager.user_service.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private static String signupRequest(String email) {
        return """
                {
                  "firstname": "John",
                  "lastname": "Doe",
                  "email": "%s",
                  "password": "secret123",
                  "role": "ATTENDEE"
                }
                """.formatted(email);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateUser() {
        // Use helper to create the user via the API
        String email = "john.doe@example.com";
        String requestBody = signupRequest(email);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        // Verify persistence in DB
        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        // Basic field assertions
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals("John", createdUser.getFirstname());
        Assertions.assertEquals("Doe", createdUser.getLastname());
        Assertions.assertEquals(email, createdUser.getEmail());
        Assertions.assertNotNull(createdUser.getCreatedAt());
    }

    @Test
    void shouldHashPassword() {
        String email = "john.doe@example.com";
        String requestBody = signupRequest(email);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        Assertions.assertNotEquals("secret123", createdUser.getPassword());
        Assertions.assertTrue(createdUser.getPassword().startsWith("$2a$"));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        String requestBody = signupRequest("john.doe@example.com");

        // first signup
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        // second signup with same email
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(409); // conflict
    }

    @Test
    void shouldUpdateUser() {
        String email = "john.doe@example.com";
        String requestBody = signupRequest(email);

        // first signup
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        String updateUserProfileRequest = """
                {
                  "bio": "Hi, I'm John Doe and I enjoy Jazz concerts !",
                  "city": "London",
                  "country": "United Kingdom",
                  "password": "secret123"
                }
                """;

        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateUserProfileRequest)
                .put("api/users/" + createdUser.getId() + "/profile")
                .then()
                .statusCode(200);

        User updatedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        // Basic field assertions
        Assertions.assertNotNull(updatedUser.getId());
        Assertions.assertEquals("John", updatedUser.getFirstname());
        Assertions.assertEquals("Doe", updatedUser.getLastname());
        Assertions.assertEquals(email, updatedUser.getEmail());
        Assertions.assertNotNull(updatedUser.getCreatedAt());
        Assertions.assertEquals("Hi, I'm John Doe and I enjoy Jazz concerts !", updatedUser.getBio());
        Assertions.assertEquals("London", updatedUser.getCity());
        Assertions.assertEquals("United Kingdom", updatedUser.getCountry());
        Assertions.assertNull(updatedUser.getAvatarUrl());
    }

    @Test
    void shouldUpdateUserNotificationSettings() {
        String email = "john.doe@example.com";
        String requestBody = signupRequest(email);

        // first signup
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        String updateNotificationSettingsRequest = """
                {
                  "emailNotificationsEnabled": "true",
                  "smsNotificationsEnabled": "false"
                }
                """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateNotificationSettingsRequest)
                .patch("api/users/" + createdUser.getId() + "/notification")
                .then()
                .statusCode(200);

        User updatedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        // Basic field assertions
        Assertions.assertNotNull(updatedUser.getId());
        Assertions.assertTrue(updatedUser.isEmailNotificationsEnabled());
        Assertions.assertFalse(updatedUser.isSmsNotificationsEnabled());
    }

    @Test
    void shouldChangeUserStatus() {
        String email = "john.doe@example.com";
        String requestBody = signupRequest(email);

        // first signup
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        String changeUserStatusRequest = """
                {
                  "status": "DELETED"
                }
                """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(changeUserStatusRequest)
                .patch("api/users/" + createdUser.getId() + "/status")
                .then()
                .statusCode(200);

        User updatedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AssertionError("User not found in DB"));

        // Basic field assertions
        Assertions.assertNotNull(updatedUser.getId());
        Assertions.assertEquals(UserStatus.DELETED, updatedUser.getStatus());
    }
}
