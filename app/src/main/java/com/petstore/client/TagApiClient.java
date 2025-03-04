package com.petstore.client;

import com.petstore.config.ApiConfig;
import com.petstore.model.Tag;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class TagApiClient {
    private static final Logger log = LoggerFactory.getLogger(TagApiClient.class);
    private final RequestSpecification requestSpec;

    public TagApiClient() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.requestSpec = given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ApiConfig.CONTENT_TYPE);
    }
} 