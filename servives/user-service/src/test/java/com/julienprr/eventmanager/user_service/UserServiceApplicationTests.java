package com.julienprr.eventmanager.user_service;

import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
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

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateUser() {
        String requestBody = """
                {
                  "firstname": "John",
                  "lastname": "Doe",
                  "email": "john.doe@example.com",
                  "password": "secret123",
                  "role": "ATTENDEE"
                }
                """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("firstname", Matchers.equalTo("John"))
                .body("lastname", Matchers.equalTo("Doe"))
                .body("email", Matchers.equalTo("john.doe@example.com"));
    }

    @Test
    void shouldHashPassword() {
        // create user first
        // then fetch it
        User user = userRepository.findByEmail("john.doe@example.com").orElseThrow();
        Assertions.assertNotEquals("secret123", user.getPassword());
        Assertions.assertTrue(user.getPassword().startsWith("$2a$"));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        String requestBody = """
                {
                  "firstname": "John",
                  "lastname": "Doe",
                  "email": "john.doe1@example.com",
                  "password": "secret123",
                  "role": "ATTENDEE"
                }
                """;

        // first signup
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(201);

        // second signup with same email → should fail
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/users/signup")
                .then()
                .statusCode(409); // conflict
    }
}
