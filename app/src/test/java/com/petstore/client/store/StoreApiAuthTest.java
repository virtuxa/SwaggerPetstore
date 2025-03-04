package com.petstore.client.store;

import com.petstore.client.StoreApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreApiAuthTest {
    private static final String VALID_API_KEY = "api-key";
    private static final String INVALID_API_KEY = "invalid-key";
    
    private StoreApiClient storeApiClient;

    @BeforeEach
    void setUp() {
        storeApiClient = new StoreApiClient();
    }

    @Test
    void shouldGetInventoryWithValidApiKey() {
        Response response = storeApiClient.getInventoryWithAuth(VALID_API_KEY);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).isNotEmpty();
        assertThat(response.getHeader("access-control-allow-headers"))
            .contains("Content-Type", "api_key", "Authorization");
    }

    @Test
    void shouldReturn401WhenGettingInventoryWithInvalidApiKey() {
        Response response = storeApiClient.getInventoryWithAuth(INVALID_API_KEY);

        assertThat(response.getStatusCode())
            .as("API should return 401 for invalid API key")
            .isEqualTo(401);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid API key")
            .contains("Invalid");
    }

    @Test
    void shouldGetInventoryWithoutAuth() {
        Response response = storeApiClient.getInventory();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).isNotEmpty();
        assertThat(response.getHeader("access-control-allow-headers"))
            .contains("Content-Type", "api_key", "Authorization");
    }
} 