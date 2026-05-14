package org.felipejaber.n2teste.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookStoreSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void realizarLogin() {
        driver.get("http://localhost:8080");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginUsername")))
                .sendKeys("admin");

        driver.findElement(By.id("loginPassword"))
                .sendKeys("admin123");

        driver.findElement(By.cssSelector("#loginForm button[type='submit']"))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("currentUser")));

        String usuarioLogado = driver.findElement(By.id("currentUser")).getText();

        assertTrue(usuarioLogado.contains("admin"));
    }

    @Test
    void deveRealizarLoginAcessarLivrosECadastrarLivroComSucesso() {
        realizarLogin();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-screen='books']")))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookTitle")));

        String tituloLivro = "Livro Selenium " + System.currentTimeMillis();

        driver.findElement(By.id("bookTitle")).sendKeys(tituloLivro);
        driver.findElement(By.id("bookAuthor")).sendKeys("Autor Teste Selenium");
        driver.findElement(By.id("bookGenre")).sendKeys("Testes");
        driver.findElement(By.id("bookPrice")).sendKeys("49.90");

        driver.findElement(By.cssSelector("#bookForm button[type='submit']"))
                .click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Livro criado com sucesso"
        ));

        assertTrue(driver.findElement(By.tagName("body"))
                .getText()
                .contains("Livro criado com sucesso"));
    }

    @Test
    void deveRealizarLoginAcessarUsuariosECadastrarUsuarioComSucesso() {
        realizarLogin();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-screen='users']")))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userUsername")));

        String usuario = "usuario_selenium_" + System.currentTimeMillis();

        driver.findElement(By.id("userUsername")).sendKeys(usuario);
        driver.findElement(By.id("userPassword")).sendKeys("123456");

        driver.findElement(By.cssSelector("#userForm button[type='submit']"))
                .click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Usuário criado com sucesso"
        ));

        assertTrue(driver.findElement(By.tagName("body"))
                .getText()
                .contains("Usuário criado com sucesso"));
    }
}