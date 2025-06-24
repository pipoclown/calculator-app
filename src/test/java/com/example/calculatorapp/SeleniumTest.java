package com.example.calculatorapp;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.chrome.ChromeOptions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {
    private WebDriver driver;

    @BeforeAll
    public void setup() throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080").openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.connect();
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("App is not responding at http://localhost:8080");
            }
        } catch (IOException e) {
            throw new RuntimeException("Spring Boot app is not running on http://localhost:8080", e);
        }

        WebDriverManager.chromedriver().setup();
		
        ChromeOptions options = new ChromeOptions();
	options.addArguments("--headless=new"); // use "--headless" if "new" causes issues
        options.addArguments("--no-sandbox");
	options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

       // driver = new ChromeDriver();
    }

    private void performTest(String num1, String num2, String operation, String expected) {
        driver.get("http://localhost:8080");
        driver.findElement(By.name("number1")).sendKeys(num1);
        driver.findElement(By.name("number2")).sendKeys(num2);

        try {
            Thread.sleep(3000); // Pause to visually confirm input
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.cssSelector("button[value='" + operation + "']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("calc-result")));

        System.out.println("Result for " + operation + ": " + result.getText());
        Assertions.assertTrue(result.getText().contains(expected));

        // Pause after result to allow visual inspection
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testAddition() {
        performTest("10", "5", "add", "15");
    }

   // @Test
   // @Order(2)
   // public void testSubtraction() {
   //     performTest("10", "4", "subtract", "6");
   // }

   // @Test
   // @Order(3)
   // public void testMultiplication() {
   //     performTest("3", "7", "multiply", "21");
   // }

   // @Test
   // @Order(4)
   // public void testDivision() {
   //     performTest("20", "4", "divide", "5");
   // }

    @AfterAll
    public void teardown() {
        driver.quit();
    }
}


