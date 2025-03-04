package com.petstore.client;

import com.petstore.config.JacksonConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApiClient {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private final RequestSpecification requestSpec;

    protected BaseApiClient() {
        RestAssuredConfig config = RestAssuredConfig.config()
            .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                .jackson2ObjectMapperFactory((cls, charset) -> JacksonConfig.getObjectMapper()));

        requestSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType(ContentType.JSON)
            .setConfig(config)
            .build();
    }

    protected RequestSpecification getRequestSpec() {
        return requestSpec;
    }
} 