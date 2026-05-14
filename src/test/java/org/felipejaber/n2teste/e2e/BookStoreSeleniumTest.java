package org.felipejaber.n2teste.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.felipejaber.n2teste.model.Sale;
import org.felipejaber.n2teste.repository.SaleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookStoreSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Autowired
    private SaleRepository saleRepository;

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

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginUsername"))).sendKeys("admin");
        driver.findElement(By.id("loginPassword")).sendKeys("admin123");
        driver.findElement(By.cssSelector("#loginForm button[type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("currentUser")));

        assertTrue(driver.findElement(By.id("currentUser")).getText().contains("admin"));
    }

    private void acessarTelaVendas() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-screen='sales']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saleUserId")));
    }

    private void cadastrarVendaPelaTela(String quantidade) {
        driver.findElement(By.id("saleUserId")).clear();
        driver.findElement(By.id("saleUserId")).sendKeys("1");

        driver.findElement(By.id("saleBookId")).clear();
        driver.findElement(By.id("saleBookId")).sendKeys("1");

        driver.findElement(By.id("saleQuantity")).clear();
        driver.findElement(By.id("saleQuantity")).sendKeys(quantidade);

        driver.findElement(By.cssSelector("#saleForm button[type='submit']")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Venda registrada com sucesso"
        ));

        assertTrue(driver.findElement(By.tagName("body"))
                .getText()
                .contains("Venda registrada com sucesso"));
    }

    private Optional<Sale> buscarVendaNoBancoPorQuantidade(Integer quantidade) {
        return saleRepository.findAll()
                .stream()
                .filter(sale -> sale.getUserId().equals(1L))
                .filter(sale -> sale.getBookId().equals(1L))
                .filter(sale -> sale.getQuantity().equals(quantidade))
                .findFirst();
    }

@Test
void deveCadastrarEExcluirUsuarioComSucesso() {
    realizarLogin();

    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-screen='users']"))).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userUsername")));

    String usuario = "selenium_user_" + System.currentTimeMillis();

    driver.findElement(By.id("userUsername")).sendKeys(usuario);

    driver.findElement(By.id("userPassword")).sendKeys("123456");

    driver.findElement(By.cssSelector("#userForm button[type='submit']")).click();

    wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.tagName("body"),
            "Usuário criado com sucesso"
    ));

    assertTrue(driver.findElement(By.tagName("body"))
            .getText()
            .contains("Usuário criado com sucesso"));

    WebElement linhaUsuario = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//table[@id='usersTable']//tr[contains(., '" + usuario + "')]")
    ));

    linhaUsuario.findElement(By.cssSelector(".btn-delete")).click();

    wait.until(ExpectedConditions.alertIsPresent());
    driver.switchTo().alert().accept();

    wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.tagName("body"),
            "Usuário excluído com sucesso"
    ));

    assertTrue(driver.findElement(By.tagName("body"))
            .getText()
            .contains("Usuário excluído com sucesso"));
}
    @Test
    void deveExcluirVendaComSucesso() {
        realizarLogin();
        acessarTelaVendas();

        Integer quantidade = 52;

        cadastrarVendaPelaTela(String.valueOf(quantidade));

        Sale vendaCriada = buscarVendaNoBancoPorQuantidade(quantidade)
                .orElseThrow(() -> new AssertionError("Venda não foi salva no banco H2."));

        Long idVenda = vendaCriada.getId();

        WebElement linhaVenda = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@id='salesTable']//tr[td[text()='" + idVenda + "']]")
        ));

        linhaVenda.findElement(By.cssSelector(".btn-delete")).click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Venda excluída com sucesso"
        ));

        assertFalse(saleRepository.existsById(idVenda));
    }
}