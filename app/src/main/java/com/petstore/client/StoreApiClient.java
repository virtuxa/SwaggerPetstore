package com.petstore.client;

import com.petstore.model.Order;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class StoreApiClient extends BaseApiClient {
    private static final Logger log = LoggerFactory.getLogger(StoreApiClient.class);
    private static final String STORE_PATH = "/store";

    public Response getInventory() {
        log.info("Getting store inventory");
        return given()
            .spec(getRequestSpec())
            .get(STORE_PATH + "/inventory");
    }

    public Response getInventoryWithAuth(String apiKey) {
        log.info("Getting store inventory with API key: {}", apiKey);
        return given()
            .spec(getRequestSpec())
            .header("api_key", apiKey)
            .get(STORE_PATH + "/inventory");
    }

    public Response getInventoryWithOAuth(String token) {
        log.info("Getting store inventory with OAuth token: {}", token);
        return given()
            .spec(getRequestSpec())
            .auth()
            .oauth2(token)
            .get(STORE_PATH + "/inventory");
    }

    public Response placeOrder(Order order) {
        log.info("Placing order for pet: {}", order);
        return given()
            .spec(getRequestSpec())
            .body(order)
            .post(STORE_PATH + "/order");
    }

    public Response getOrderById(Long orderId) {
        log.info("Getting order by ID: {}", orderId);
        return given()
            .spec(getRequestSpec())
            .get(STORE_PATH + "/order/{orderId}", orderId);
    }

    public Response deleteOrder(Long orderId) {
        log.info("Deleting order by ID: {}", orderId);
        return given()
            .spec(getRequestSpec())
            .delete(STORE_PATH + "/order/{orderId}", orderId);
    }
} 