package org.felipejaber.n2teste.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void deveListarLivrosComSucesso() {
        given()
        .when()
            .get("/books")
        .then()
            .statusCode(200);
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        String body = """
                {
                    "title": "O Pequeno Príncipe",
                    "author": "Antoine de Saint-Exupéry",
                    "price": 39.90,
                    "genre": "Fábula"
                }
                """;

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/books")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("title", equalTo("O Pequeno Príncipe"))
            .body("author", equalTo("Antoine de Saint-Exupéry"))
            .body("genre", equalTo("Fábula"));
    }
}