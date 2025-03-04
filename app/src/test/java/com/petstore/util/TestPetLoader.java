package com.petstore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestPetLoader {
    private static final Logger log = LoggerFactory.getLogger(TestPetLoader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode petsData;

    static {
        try (InputStream is = TestPetLoader.class.getResourceAsStream("/test-data/pets.json")) {
            petsData = objectMapper.readTree(is);
        } catch (IOException e) {
            log.error("Error loading pets test data: {}", e.getMessage());
            throw new RuntimeException("Failed to load pets test data", e);
        }
    }

    public static String getTestPet() {
        return petsData.get("testPet").toString();
    }

    public static String getInvalidPet() {
        return petsData.get("invalidPet").toString();
    }

    public static String getUpdatedPet() {
        return petsData.get("updatedPet").toString();
    }
} 