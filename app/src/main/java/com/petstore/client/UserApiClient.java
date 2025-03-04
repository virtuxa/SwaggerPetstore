package com.petstore.client;

import com.petstore.config.ApiConfig;
import com.petstore.model.User;
import com.petstore.model.ApiResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserApiClient {
    private static final Logger log = LoggerFactory.getLogger(UserApiClient.class);
    private final RequestSpecification requestSpec;

    public UserApiClient() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.requestSpec = given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ApiConfig.CONTENT_TYPE);
    }

    public Response createUsersWithList(List<User> users) {
        log.info("Creating users with list: {}", users);
        try {
            Response response = requestSpec
                    .body(users)
                    .post("/user/createWithList");
            log.info("Create users with list response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error creating users with list: {}", e.getMessage());
            throw e;
        }
    }

    public Response getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        try {
            Response response = requestSpec
                    .pathParam("username", username)
                    .get("/user/{username}");
            log.info("Get user response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage());
            throw e;
        }
    }

    public Response updateUser(String username, User user) {
        log.info("Updating user: {} with data: {}", username, user);
        try {
            Response response = requestSpec
                    .pathParam("username", username)
                    .body(user)
                    .put("/user/{username}");
            log.info("Update user response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }

    public Response deleteUser(String username) {
        log.info("Deleting user: {}", username);
        try {
            Response response = requestSpec
                    .pathParam("username", username)
                    .when()
                    .delete("/user/{username}")
                    .then()
                    .extract()
                    .response();
            log.info("Delete user response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
    }

    public Response login(String username, String password) {
        log.info("Logging in user: {}", username);
        try {
            Response response = requestSpec
                    .queryParam("username", username)
                    .queryParam("password", password)
                    .get("/user/login");
            log.info("Login response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            throw e;
        }
    }

    public Response logout() {
        log.info("Logging out user");
        try {
            Response response = requestSpec
                    .get("/user/logout");
            log.info("Logout response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error logging out: {}", e.getMessage());
            throw e;
        }
    }

    public Response createUsersWithArray(User[] users) {
        log.info("Creating users with array: {}", (Object) users);
        try {
            Response response = requestSpec
                    .body(users)
                    .post("/user/createWithArray");
            log.info("Create users with array response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error creating users with array: {}", e.getMessage());
            throw e;
        }
    }

    public Response createUser(User user) {
        log.info("Creating user: {}", user);
        try {
            Response response = requestSpec
                    .body(user)
                    .post("/user");
            log.info("Create user response: {}", response.asPrettyString());
            return response;
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }
} 