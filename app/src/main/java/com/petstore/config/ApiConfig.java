package com.petstore.config;

/**
 * Конфигурация для API Petstore
 */
public class ApiConfig {
    public static final String BASE_URL = "https://petstore.swagger.io/v2";
    public static final String CONTENT_TYPE = "application/json";
    
    // Маршруты API
    public static final String PET_ENDPOINT = "/pet";
    public static final String STORE_ENDPOINT = "/store";
    public static final String USER_ENDPOINT = "/user";
} 