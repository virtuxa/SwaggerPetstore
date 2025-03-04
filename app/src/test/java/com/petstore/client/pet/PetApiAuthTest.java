package com.petstore.client.pet;

import com.petstore.client.PetApiClient;
import com.petstore.util.TestPetLoader;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class PetApiAuthTest {
    private static final String VALID_API_KEY = "special-key";
    private static final String INVALID_API_KEY = "invalid-key";
    private static final String OAUTH2_TOKEN = "test:abc123";
    private static final String INVALID_OAUTH2_TOKEN = "invalid-token";
    private static final Long TEST_PET_ID = 1L;
    private static final String TEST_IMAGE_PATH = "test-data/test.jpg";
    
    private PetApiClient petApiClient;
    private File testImageFile;

    @BeforeEach
    void setUp() {
        petApiClient = new PetApiClient();
        URL imageUrl = getClass().getClassLoader().getResource(TEST_IMAGE_PATH);
        if (imageUrl == null) {
            throw new IllegalStateException("Test image file not found: " + TEST_IMAGE_PATH);
        }
        testImageFile = new File(imageUrl.getFile());
        if (!testImageFile.exists() || !testImageFile.canRead()) {
            throw new IllegalStateException("Cannot read test image file: " + testImageFile.getAbsolutePath());
        }
    }

    @Test
    void shouldDeletePetWithValidOAuth2Token() {
        Response response = petApiClient.deletePetWithOAuth(TEST_PET_ID, OAUTH2_TOKEN);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(200);
        assertThat(response.jsonPath().getString("type")).isEqualTo("unknown");
        assertThat(response.jsonPath().getString("message")).isNotEmpty();
    }

    @Test
    void shouldReturn401WhenDeletingPetWithInvalidOAuth2Token() {
        Response response = petApiClient.deletePetWithOAuth(TEST_PET_ID, INVALID_OAUTH2_TOKEN);
        assertThat(response.getStatusCode())
            .as("API should return 401 for invalid OAuth2 token")
            .isEqualTo(401);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(401);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid token")
            .contains("Invalid");
    }

    @Test
    void shouldUploadImageWithOAuth2() {
        Response response = petApiClient.uploadImageWithOAuth(TEST_PET_ID, "test image", testImageFile.getAbsolutePath(), OAUTH2_TOKEN);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(200);
        assertThat(response.jsonPath().getString("type")).isNotEmpty();
        assertThat(response.jsonPath().getString("message")).contains("uploaded");
    }

    @Test
    void shouldReturn401WhenUploadingImageWithInvalidOAuth2Token() {
        Response response = petApiClient.uploadImageWithOAuth(TEST_PET_ID, "test image", testImageFile.getAbsolutePath(), INVALID_OAUTH2_TOKEN);
        assertThat(response.getStatusCode())
            .as("API should return 401 for invalid OAuth2 token")
            .isEqualTo(401);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(401);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid token")
            .contains("Invalid");
    }

    @Test
    void shouldGetPetByIdWithValidApiKey() {
        Response response = petApiClient.getPetByIdWithAuth(TEST_PET_ID, VALID_API_KEY);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getLong("id")).isPositive();
        assertThat(response.jsonPath().getString("name")).isNotEmpty();
    }

    @Test
    void shouldReturn401WhenGettingPetWithInvalidApiKey() {
        Response response = petApiClient.getPetByIdWithAuth(TEST_PET_ID, INVALID_API_KEY);
        assertThat(response.getStatusCode())
            .as("API should return 401 for invalid API key")
            .isEqualTo(401);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(401);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid API key")
            .contains("Invalid");
    }

    @Test
    void shouldAddNewPetWithOAuth2() {
        Response response = petApiClient.addNewPetWithOAuth(TestPetLoader.getTestPet(), OAUTH2_TOKEN);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getLong("id")).isPositive();
        assertThat(response.jsonPath().getString("name")).isEqualTo("test pet");
    }

    @Test
    void shouldUpdatePetWithOAuth2() {
        Response response = petApiClient.updatePetWithOAuth(TestPetLoader.getUpdatedPet(), OAUTH2_TOKEN);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getLong("id")).isPositive();
        assertThat(response.jsonPath().getString("name")).isEqualTo("updated pet");
    }

    @Test
    void shouldReturn400WhenAddingInvalidPet() {
        Response response = petApiClient.addNewPetWithOAuth(TestPetLoader.getInvalidPet(), OAUTH2_TOKEN);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid pet data")
            .isEqualTo(400);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid data")
            .isNotEmpty();
    }

    @Test
    void shouldReturn400WhenUploadingInvalidImageFormat() {
        String invalidImagePath = "test-data/invalid.txt";
        Response response = petApiClient.uploadImageWithOAuth(TEST_PET_ID, "invalid image", invalidImagePath, OAUTH2_TOKEN);
        assertThat(response.getStatusCode())
            .as("API should return 400 for invalid image format")
            .isEqualTo(400);
        assertThat(response.jsonPath().getInt("code")).isEqualTo(400);
        assertThat(response.jsonPath().getString("type")).isEqualTo("error");
        assertThat(response.jsonPath().getString("message"))
            .as("Error message should indicate invalid format")
            .isNotEmpty();
    }
} 