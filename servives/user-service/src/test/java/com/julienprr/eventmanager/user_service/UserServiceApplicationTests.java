package com.julienprr.eventmanager.user_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {

	@LocalServerPort
	private int port;

	@ServiceConnection
	static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		postgreSQLContainer.start();
	}

	@Test
	void shouldCreateUser() {
String requestBody = """
		{
		  "firstname": "Julien",
		  "lastname": "Perrier",
		  "email": "julien@example.com",
		  "password": "secret123",
		  "role": "ATTENDEE"
		}
		""";

RestAssured.given()
		.contentType(ContentType.JSON)
		.when()
		.body(requestBody)
		.post("api/users.signup")
		.then()
		.statusCode(201)
		.body("id", Matchers.notNullValue())
		.body("firstname", Matchers.equalTo("Julien"))
		.body("lastname", Matchers.equalTo("Perrier"));




	}

}
