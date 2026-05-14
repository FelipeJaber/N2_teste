package org.felipejaber.n2teste.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.felipejaber.n2teste.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void deveCadastrarLivroESalvarNoBancoH2() {
        Map<String, Object> livro = new HashMap<>();
        livro.put("title", "Livro API H2");
        livro.put("author", "Autor API");
        livro.put("price", 59.90);
        livro.put("genre", "Teste API");

        Number idCriado = given()
                .contentType(ContentType.JSON)
                .body(livro)
        .when()
                .post("/books")
        .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotNull(idCriado);
        assertTrue(bookRepository.existsById(idCriado.longValue()));
    }

    @Test
    void deveExcluirLivroERemoverDoBancoH2() {
        Map<String, Object> livro = new HashMap<>();
        livro.put("title", "Livro Para Excluir");
        livro.put("author", "Autor Exclusao");
        livro.put("price", 29.90);
        livro.put("genre", "Teste Delete");

        Number idCriado = given()
                .contentType(ContentType.JSON)
                .body(livro)
        .when()
                .post("/books")
        .then()
                .statusCode(200)
                .extract()
                .path("id");

        Long id = idCriado.longValue();

        assertTrue(bookRepository.existsById(id));

        given()
        .when()
                .delete("/books/" + id)
        .then()
                .statusCode(200);

        assertFalse(bookRepository.existsById(id));
    }
}