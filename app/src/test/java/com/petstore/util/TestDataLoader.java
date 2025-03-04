package com.petstore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for loading test data from JSON files
 */
public class TestDataLoader {
    private static final Logger log = LoggerFactory.getLogger(TestDataLoader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode usersData;

    static {
        try (InputStream is = TestDataLoader.class.getResourceAsStream("/test-data/users.json")) {
            usersData = objectMapper.readTree(is);
        } catch (IOException e) {
            log.error("Error loading test data: {}", e.getMessage());
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public static User getTestUser() {
        return objectMapper.convertValue(usersData.get("testUser"), User.class);
    }

    public static User getAnotherTestUser() {
        return objectMapper.convertValue(usersData.get("anotherTestUser"), User.class);
    }

    public static User getUpdatedUser() {
        return objectMapper.convertValue(usersData.get("updatedUser"), User.class);
    }
} 