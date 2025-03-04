package com.petstore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.config.JacksonConfig;
import com.petstore.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestOrderLoader {
    private static final Logger log = LoggerFactory.getLogger(TestOrderLoader.class);
    private static final ObjectMapper objectMapper = JacksonConfig.getObjectMapper();
    private static JsonNode ordersData;

    static {
        try (InputStream is = TestOrderLoader.class.getResourceAsStream("/test-data/orders.json")) {
            ordersData = objectMapper.readTree(is);
        } catch (IOException e) {
            log.error("Error loading orders test data: {}", e.getMessage());
            throw new RuntimeException("Failed to load orders test data", e);
        }
    }

    public static Order getTestOrder() {
        return objectMapper.convertValue(ordersData.get("testOrder"), Order.class);
    }

    public static Order getInvalidOrder() {
        return objectMapper.convertValue(ordersData.get("invalidOrder"), Order.class);
    }

    public static Order getUpdatedOrder() {
        return objectMapper.convertValue(ordersData.get("updatedOrder"), Order.class);
    }
} 