package com.petstore.client.user;

import com.petstore.client.UserApiClient;
import com.petstore.model.User;
import com.petstore.util.TestDataLoader;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserApiClientTest {
    private UserApiClient userApiClient;

    @BeforeEach
    void setUp() {
        userApiClient = new UserApiClient();
    }

    @Test
    void shouldCreateUser() {
        User user = TestDataLoader.getTestUser();
        Response response = userApiClient.createUser(user);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldCreateUsersWithArray() {
        User[] users = {
            TestDataLoader.getTestUser(),
            TestDataLoader.getAnotherTestUser()
        };
        Response response = userApiClient.createUsersWithArray(users);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldCreateUsersWithList() {
        List<User> users = Arrays.asList(
            TestDataLoader.getTestUser(),
            TestDataLoader.getAnotherTestUser()
        );
        Response response = userApiClient.createUsersWithList(users);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldGetUserByUsername() {
        User testUser = TestDataLoader.getTestUser();
        createTestUserInSystem(testUser);
        Response response = userApiClient.getUserByUsername(testUser.getUsername());
        assertThat(response.getStatusCode()).isEqualTo(200);
        User user = response.as(User.class);
        assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void shouldReturn404WhenUserNotFound() {
        String nonExistentUsername = "nonexistentuser123";
        Response response = userApiClient.getUserByUsername(nonExistentUsername);
        assertThat(response.getStatusCode())
            .as("API should return 404 for non-existent user")
            .isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate user not found")
            .contains("not found");
    }

    @Test
    void shouldReturn400WhenInvalidUsernameSupplied() {
        String invalidUsername = "@#$%^";
        Response response = userApiClient.getUserByUsername(invalidUsername);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid username")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid username")
            .contains("Invalid");
    }

    @Test
    void shouldUpdateUser() {
        User testUser = TestDataLoader.getTestUser();
        createTestUserInSystem(testUser);
        User updatedUser = TestDataLoader.getUpdatedUser();
        Response response = userApiClient.updateUser(testUser.getUsername(), updatedUser);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentUser() {
        String nonExistentUsername = "nonexistentuser123";
        User updatedUser = TestDataLoader.getUpdatedUser();
        Response response = userApiClient.updateUser(nonExistentUsername, updatedUser);
        assertThat(response.getStatusCode())
            .as("API should return 404 for non-existent user")
            .isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate user not found")
            .contains("not found");
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidUsername() {
        String invalidUsername = "@#$%^";
        User updatedUser = TestDataLoader.getUpdatedUser();
        Response response = userApiClient.updateUser(invalidUsername, updatedUser);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid username")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid username")
            .contains("Invalid");
    }

    @Test
    void shouldDeleteUser() {
        User testUser = TestDataLoader.getTestUser();
        createTestUserInSystem(testUser);
        Response response = userApiClient.deleteUser(testUser.getUsername());
        assertThat(response.getStatusCode()).isEqualTo(200);

        Response getResponse = userApiClient.getUserByUsername(testUser.getUsername());
        assertThat(getResponse.getStatusCode()).isEqualTo(404);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentUser() {
        String nonExistentUsername = "nonexistentuser123";
        Response response = userApiClient.deleteUser(nonExistentUsername);
        assertThat(response.getStatusCode())
            .as("API should return 404 for non-existent user")
            .isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate user not found")
            .contains("not found");
    }

    @Test
    void shouldReturn400WhenDeletingWithInvalidUsername() {
        String invalidUsername = "@#$%^";
        Response response = userApiClient.deleteUser(invalidUsername);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid username")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid username")
            .contains("Invalid");
    }

    @Test
    void shouldLoginUserAndReturnSessionHeaders() {
        User testUser = TestDataLoader.getTestUser();
        createTestUserInSystem(testUser);
        Response response = userApiClient.login(testUser.getUsername(), testUser.getPassword());
        assertThat(response.getStatusCode()).isEqualTo(200);
        
        String expiresAfter = response.getHeader("X-Expires-After");
        String rateLimit = response.getHeader("X-Rate-Limit");
        
        assertThat(expiresAfter)
            .as("X-Expires-After header should be present")
            .isNotEmpty();
        assertThat(rateLimit)
            .as("X-Rate-Limit header should be present")
            .isNotEmpty();
    }

    @Test
    void shouldReturn400WhenLoginWithInvalidCredentials() {
        String invalidUsername = "@#$%^";
        String invalidPassword = "";
        Response response = userApiClient.login(invalidUsername, invalidPassword);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid credentials")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid credentials")
            .contains("Invalid");
    }

    @Test
    void shouldLogoutUser() {
        Response response = userApiClient.logout();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    private void createTestUserInSystem(User user) {
        userApiClient.createUser(user);
    }
} 