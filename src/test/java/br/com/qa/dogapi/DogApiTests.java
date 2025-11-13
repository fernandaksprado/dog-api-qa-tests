package br.com.qa.dogapi;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes automatizados da Dog API")
public class DogApiTests extends BaseTest {

    @Nested
    @DisplayName("GET /breeds/list/all")
    class BreedsListTests {

        @Test
        @DisplayName("Deve retornar a lista completa de raças com status 200 e estrutura válida")
        void shouldReturnAllBreedsWithSuccess() {
            Map<String, Object> response =
                    given()
                            .accept(ContentType.JSON)
                    .when()
                            .get("/breeds/list/all")
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo("success"))
                            .body("message", notNullValue())
                    .extract()
                            .as(Map.class);

            Map<String, List<String>> message = (Map<String, List<String>>) response.get("message");

            // A mensagem deve conter várias raças
            assertThat(message)
                    .as("A lista de raças não deve ser vazia")
                    .isNotEmpty();

            // Algumas raças conhecidas devem estar presentes
            assertThat(message.keySet())
                    .as("A lista de raças deve conter algumas raças conhecidas")
                    .contains("hound", "bulldog", "terrier");
        }
    }

    @Nested
    @DisplayName("GET /breed/{breed}/images")
    class BreedImagesTests {

        @Test
        @DisplayName("Deve retornar todas as imagens de uma raça existente")
        void shouldReturnImagesForValidBreed() {
            String breed = "hound";

            List<String> images =
                    given()
                            .accept(ContentType.JSON)
                    .when()
                            .get("/breed/{breed}/images", breed)
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo("success"))
                            .body("message", notNullValue())
                    .extract()
                            .jsonPath()
                            .getList("message", String.class);

            assertThat(images)
                    .as("A lista de imagens não deve ser vazia")
                    .isNotEmpty();

            // Todas as URLs devem apontar para o domínio esperado
            assertThat(images)
                    .allSatisfy(url -> assertThat(url)
                            .as("URL de imagem deve ser válida: " + url)
                            .startsWith("https://images.dog.ceo/"));
        }

        @Test
        @DisplayName("Deve retornar erro 404 ao buscar imagens de uma raça inexistente")
        void shouldReturnErrorForInvalidBreed() {
            String invalidBreed = "breed-inexistente-xyz";

            given()
                    .accept(ContentType.JSON)
            .when()
                    .get("/breed/{breed}/images", invalidBreed)
            .then()
                    .log().all()
                    .statusCode(404)
                    .body("status", equalTo("error"))
                    .body("message", containsStringIgnoringCase("Breed not found"));
        }
    }

    @Nested
    @DisplayName("GET /breeds/image/random")
    class RandomImageTests {

        @Test
        @DisplayName("Deve retornar uma imagem aleatória com estrutura válida")
        void shouldReturnRandomImage() {
            String imageUrl =
                    given()
                            .accept(ContentType.JSON)
                    .when()
                            .get("/breeds/image/random")
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo("success"))
                            .body("message", notNullValue())
                    .extract()
                            .jsonPath()
                            .getString("message");

            assertThat(imageUrl)
                    .as("A URL retornada deve ser uma URL de imagem válida")
                    .startsWith("https://images.dog.ceo/")
                    .containsIgnoringCase(".jpg");
        }

        @Test
        @DisplayName("Deve retornar múltiplas imagens aleatórias quando solicitado")
        void shouldReturnMultipleRandomImages() {
            int quantity = 3;

            List<String> images =
                    given()
                            .accept(ContentType.JSON)
                    .when()
                            .get("/breeds/image/random/{quantity}", quantity)
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo("success"))
                            .body("message", hasSize(quantity))
                    .extract()
                            .jsonPath()
                            .getList("message", String.class);

            assertThat(images)
                    .hasSize(quantity)
                    .allSatisfy(url -> assertThat(url)
                            .as("URL de imagem deve ser válida: " + url)
                            .startsWith("https://images.dog.ceo/"));
        }
    }
}
