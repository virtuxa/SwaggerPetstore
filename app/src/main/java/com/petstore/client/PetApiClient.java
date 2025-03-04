package com.petstore.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.petstore.config.ApiConfig;
import com.petstore.model.Pet;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;

public class PetApiClient extends BaseApiClient {
    private static final Logger log = LoggerFactory.getLogger(PetApiClient.class);
    private static final String PET_PATH = "/pet";
    private final RequestSpecification requestSpec;

    public PetApiClient() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.requestSpec = given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ApiConfig.CONTENT_TYPE);
    }

    public Response getPetById(Long petId) {
        log.info("Getting pet by ID: {}", petId);
        return given()
            .spec(getRequestSpec())
            .get(PET_PATH + "/{petId}", petId);
    }

    public Response createPet(Pet pet) {
        log.info("Creating new pet: {}", pet);
        try {
            Response response = requestSpec
                    .body(pet)
                    .when()
                    .post(ApiConfig.PET_ENDPOINT)
                    .then()
                    .extract()
                    .response();
            log.info("Create pet response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Error creating pet: {}", e.getMessage());
            throw e;
        }
    }

    public Response updatePet(Pet pet) {
        log.info("Updating pet: {}", pet);
        try {
            Response response = requestSpec
                    .body(pet)
                    .when()
                    .put(ApiConfig.PET_ENDPOINT)
                    .then()
                    .extract()
                    .response();
            log.info("Update pet response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Error updating pet: {}", e.getMessage());
            throw e;
        }
    }

    public Response deletePet(Long petId) {
        log.info("Deleting pet by ID: {}", petId);
        try {
            Response response = requestSpec
                    .pathParam("petId", petId)
                    .when()
                    .delete(ApiConfig.PET_ENDPOINT + "/{petId}")
                    .then()
                    .extract()
                    .response();
            log.info("Delete pet response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Error deleting pet: {}", e.getMessage());
            throw e;
        }
    }

    public Response findPetsByStatus(String status) {
        log.info("Finding pets by status: {}", status);
        return given()
            .spec(getRequestSpec())
            .queryParam("status", status)
            .get(PET_PATH + "/findByStatus");
    }

    public Response uploadImageWithOAuth(Long petId, String additionalMetadata, String imagePath, String oauthToken) {
        log.info("Uploading image for pet ID {} using OAuth2 token", petId);
        return getRequestSpec()
            .header("Authorization", "Bearer " + oauthToken)
            .multiPart("additionalMetadata", additionalMetadata)
            .multiPart("file", new File(imagePath))
            .post(PET_PATH + "/{petId}/uploadImage", petId);
    }

    public Response addNewPetWithOAuth(String pet, String token) {
        log.info("Adding new pet with OAuth token");
        return given()
            .spec(getRequestSpec())
            .auth()
            .oauth2(token)
            .body(pet)
            .post(PET_PATH);
    }

    public Response updatePetWithOAuth(String pet, String token) {
        log.info("Updating pet with OAuth token");
        return given()
            .spec(getRequestSpec())
            .auth()
            .oauth2(token)
            .body(pet)
            .put(PET_PATH);
    }

    public Response deletePetWithAuth(Long petId, String apiKey) {
        log.info("Deleting pet with ID {} using API key", petId);
        return given()
            .spec(getRequestSpec())
            .header("api_key", apiKey)
            .delete(PET_PATH + "/{petId}", petId);
    }

    public Response updatePetWithFormData(Long petId, String name, String status) {
        log.info("Updating pet with ID {} using form data", petId);
        return given()
            .spec(getRequestSpec())
            .formParam("name", name)
            .formParam("status", status)
            .post(PET_PATH + "/{petId}", petId);
    }

    public Response deletePetWithOAuth(Long petId, String oauthToken) {
        log.info("Deleting pet with ID {} using OAuth2 token", petId);
        return getRequestSpec()
            .header("Authorization", "Bearer " + oauthToken)
            .delete(PET_PATH + "/{petId}", petId);
    }

    public Response getPetByIdWithAuth(Long petId, String apiKey) {
        log.info("Getting pet with ID {} using API key", petId);
        return getRequestSpec()
            .header("api_key", apiKey)
            .get(PET_PATH + "/{petId}", petId);
    }
} 