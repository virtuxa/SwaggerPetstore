package com.petstore.client.store;

import com.petstore.client.StoreApiClient;
import com.petstore.model.Order;
import com.petstore.util.TestOrderLoader;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreApiClientTest {
    private StoreApiClient storeApiClient;

    @BeforeEach
    void setUp() {
        storeApiClient = new StoreApiClient();
    }

    @Test
    void shouldGetInventory() {
        Response response = storeApiClient.getInventory();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).isNotEmpty();
    }

    @Test
    void shouldPlaceOrder() {
        Order order = TestOrderLoader.getTestOrder();
        Response response = storeApiClient.placeOrder(order);
        assertThat(response.getStatusCode()).isEqualTo(200);
        Order createdOrder = response.as(Order.class);
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getPetId()).isEqualTo(order.getPetId());
        assertThat(createdOrder.getQuantity()).isEqualTo(order.getQuantity());
        assertThat(createdOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(createdOrder.getComplete()).isEqualTo(order.getComplete());
    }

    @Test
    void shouldGetOrderById() {
        Order order = createAndPlaceTestOrder();
        Response response = storeApiClient.getOrderById(order.getId());
        assertThat(response.getStatusCode()).isEqualTo(200);
        Order retrievedOrder = response.as(Order.class);
        assertThat(retrievedOrder.getId()).isEqualTo(order.getId());
        assertThat(retrievedOrder.getPetId()).isEqualTo(order.getPetId());
        assertThat(retrievedOrder.getQuantity()).isEqualTo(order.getQuantity());
        assertThat(retrievedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(retrievedOrder.getComplete()).isEqualTo(order.getComplete());
    }

    @Test
    void shouldDeleteOrder() {
        Order order = createAndPlaceTestOrder();
        Long orderId = order.getId();
        
        Response deleteResponse = storeApiClient.deleteOrder(orderId);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
        assertThat(deleteResponse.jsonPath().getInt("code")).isEqualTo(200);
        assertThat(deleteResponse.jsonPath().getString("type")).isEqualTo("unknown");
        assertThat(deleteResponse.jsonPath().getString("message")).isNotEmpty();
        
        Response getResponse = storeApiClient.getOrderById(orderId);
        assertThat(getResponse.getStatusCode()).isEqualTo(404);
        assertThat(getResponse.jsonPath().getString("type")).isEqualTo("error");
        assertThat(getResponse.jsonPath().getString("message")).isNotEmpty();
    }

    @Test
    void shouldReturn404WhenGettingNonExistentOrder() {
        Long nonExistentOrderId = 999999L;
        Response response = storeApiClient.getOrderById(nonExistentOrderId);
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message")).isEqualTo("Order not found");
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentOrder() {
        Long nonExistentOrderId = 999999L;
        Response response = storeApiClient.deleteOrder(nonExistentOrderId);
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message")).isEqualTo("Order not found");
    }

    @Test
    void shouldReturn400WhenGettingOrderWithNegativeId() {
        Long invalidOrderId = -1L;
        Response response = storeApiClient.getOrderById(invalidOrderId);
        assertThat(response.getStatusCode())
            .as("API should return 400 for negative order ID")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid ID")
            .contains("Invalid");
    }

    @Test
    void shouldReturn400WhenGettingOrderWithZeroId() {
        Long invalidOrderId = 0L;
        Response response = storeApiClient.getOrderById(invalidOrderId);
        assertThat(response.getStatusCode())
            .as("API should return 400 for zero order ID")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid ID")
            .contains("Invalid");
    }

    @Test
    void shouldReturn400WhenDeletingOrderWithNegativeId() {
        Long invalidOrderId = -1L;
        Response response = storeApiClient.deleteOrder(invalidOrderId);
        assertThat(response.getStatusCode())
            .as("API should return 400 for negative order ID")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid ID")
            .contains("Invalid");
    }

    @Test
    void shouldReturn400WhenDeletingOrderWithZeroId() {
        Long invalidOrderId = 0L;
        Response response = storeApiClient.deleteOrder(invalidOrderId);
        assertThat(response.getStatusCode())
            .as("API should return 400 for zero order ID")
            .isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid ID")
            .contains("Invalid");
    }

    @Test
    void shouldHandleInvalidOrderData() {
        Order invalidOrder = TestOrderLoader.getInvalidOrder();
        Response response = storeApiClient.placeOrder(invalidOrder);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid order data")
            .isEqualTo(400);
    }

    @Test
    void shouldHandleInvalidShipDate() {
        Order order = TestOrderLoader.getTestOrder();
        order.setShipDate(null);
        Response response = storeApiClient.placeOrder(order);
        assertThat(response.getStatusCode())
            .as("API should handle null ship date")
            .isEqualTo(200);
        Order createdOrder = response.as(Order.class);
        assertThat(createdOrder.getShipDate()).isNull();
    }

    @Test
    void shouldHandleMaxLongOrderId() {
        Long maxOrderId = Long.MAX_VALUE;
        Response response = storeApiClient.getOrderById(maxOrderId);
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message")).isEqualTo("Order not found");
    }

    @Test
    void shouldVerifyOrderIsActuallyDeleted() {
        Order order = createAndPlaceTestOrder();
        Long orderId = order.getId();

        Response deleteResponse = storeApiClient.deleteOrder(orderId);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);

        Response getResponse = storeApiClient.getOrderById(orderId);
        assertThat(getResponse.getStatusCode()).isEqualTo(404);
        assertThat(getResponse.jsonPath().getString("type")).isEqualTo("error");
        assertThat(getResponse.jsonPath().getString("message")).isEqualTo("Order not found");
    }

    private Order createAndPlaceTestOrder() {
        Order order = TestOrderLoader.getTestOrder();
        Response response = storeApiClient.placeOrder(order);
        return response.as(Order.class);
    }
} 