package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadingImages {
    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/loading-images.html";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Loading images")
    void loadingImagesTests() {
        WebElement status = driver.findElement(By.cssSelector("p.lead"));
        WebDriverWait wait3sec = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebDriverWait wait10sec = new WebDriverWait(driver, Duration.ofSeconds(10));

        assertEquals("Please wait until the images are loaded...", status.getText());
        WebElement compass = wait3sec.until(ExpectedConditions.presenceOfElementLocated(By.id("compass")));
        assertEquals("img/compass.png", compass.getDomAttribute("src"));

        wait10sec.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        List<WebElement> imgList = driver.findElements(By.xpath("//div[@id = 'image-container']/img"));
        assertEquals(4, imgList.size());
        assertEquals("Done!", status.getText());
    }

    @Test
    @DisplayName("Убедимся, что лоадер пропал")
    void loaderTests() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/index.html");
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(BASE_URL);
        WebElement loader = driver.findElement(By.cssSelector("span.spinner-border"));
        assertTrue(loader.isDisplayed());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        List<WebElement> loaderList = driver.findElements(By.cssSelector("span.spinner-border"));
        assertTrue(loaderList.isEmpty());
    }
}
