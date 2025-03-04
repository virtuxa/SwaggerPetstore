package com.petstore.client;

import com.petstore.config.ApiConfig;
import com.petstore.model.Category;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class CategoryApiClient {
    private static final Logger log = LoggerFactory.getLogger(CategoryApiClient.class);
    private final RequestSpecification requestSpec;

    public CategoryApiClient() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.requestSpec = given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ApiConfig.CONTENT_TYPE);
    }
} 