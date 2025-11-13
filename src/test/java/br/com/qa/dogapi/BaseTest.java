package br.com.qa.dogapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

/**
 * Classe base para os testes da Dog API.
 * Centraliza a configuração comum a todos os testes.
 */
public abstract class BaseTest {

    @BeforeAll
    static void setup() {
        // URL base da Dog API
        RestAssured.baseURI = "https://dog.ceo/api";
    }
}
