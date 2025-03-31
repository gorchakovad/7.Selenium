package ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    WebDriver driver;
    private static Actions actions;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/index.html";
    private static final String INFINITE_SCROLL_URL = "https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html";
    private static final String SHADOW_DOM_URL = "https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html";
    private static final String COOKIES_URL = "https://bonigarcia.dev/selenium-webdriver-java/cookies.html";
    private static final String IFRAMES_URL = "https://bonigarcia.dev/selenium-webdriver-java/iframes.html";
    private static final String DIALOG_BOXES_URL = "https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html";
    private static final String WEB_STORAGE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-storage.html";
    private String testText = "John Doe";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Infinite scroll")
    void infiniteScrollTests() {
        driver.get(INFINITE_SCROLL_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        List<WebElement> paragraphs = driver.findElements(By.xpath("//p[@class='lead']"));

        assertEquals(20, paragraphs.size(), "Ошибка на первом этапе");
        assertTrue(paragraphs.getFirst().getText().contains("Lorem ipsum dolor sit amet"), "Ошибка на первом этапе");

        new Actions(driver)
                .scrollToElement(paragraphs.get(19))
                .perform();
        new Actions(driver)
                .scrollByAmount(0, 100)
                .perform();

        List<WebElement> paragraphsNew = driver.findElements(By.xpath("//p[@class='lead']"));
        assertEquals(40, paragraphsNew.size(), "Ошибка на втором этапе");
        assertTrue(paragraphsNew.get(39).getText().contains("Magnis feugiat natoque proin"), "Ошибка на втором этапе");
    }

    @Test
    @DisplayName("Shadow Dom")
    void shadowDomTests() {
        driver.get(SHADOW_DOM_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        WebElement content = driver.findElement(By.id("content"));
        SearchContext shadowRoot = content.getShadowRoot();
        WebElement shadowText = shadowRoot.findElement(By.cssSelector("p"));
        assertEquals("Hello Shadow DOM", shadowText.getText());
    }

    @Test
    @DisplayName("Cookies")
    void cookiesTests() {
        driver.get(COOKIES_URL);
        WebElement button = driver.findElement(By.cssSelector("button"));
        WebElement cookiesList = driver.findElement(By.xpath("//p[@id='cookies-list']"));

        Cookie cookie = driver.manage().getCookieNamed("username");
        String cookieValue = cookie.getValue();
        String cookiePath = cookie.getPath();
        assertAll(
                () -> assertEquals("John Doe", cookieValue),
                () -> assertTrue(cookiePath.contains("/")));

        driver.manage().addCookie(new Cookie("Lol", "Lorem ipsum"));
        assertTrue(cookiesList.getText().isEmpty());
        new Actions(driver).click(button).perform();
        assertFalse(cookiesList.getText().isEmpty());
        assertTrue(cookiesList.getText().contains("ipsum"));
    }

    @Test
    @DisplayName("iFrames")
    void iFramesTests() {
        driver.get(IFRAMES_URL);
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("lead")));
        WebElement iframe = driver.findElement(By.id("my-iframe"));
        driver.switchTo().frame(iframe);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertTrue(driver.findElement(By.xpath("//p")).getText().contains("dolor sit amet consectetur"));
    }

    @Test
    @DisplayName("Alerts")
    void alertsTests()  {
        driver.get(DIALOG_BOXES_URL);
        WebElement launchAlert = driver.findElement(By.id("my-alert"));
        WebElement launchConfirm = driver.findElement(By.id("my-confirm"));
        WebElement launchPrompt = driver.findElement(By.id("my-prompt"));
        WebElement launchModal = driver.findElement(By.id("my-modal"));

        launchAlert.click();
        Alert alert1 = driver.switchTo().alert();
        assertEquals("Hello world!", alert1.getText());
        alert1.accept();

        launchConfirm.click();
        Alert alert2 = driver.switchTo().alert();
        assertTrue(alert2.getText().contains("correct"));
        alert2.dismiss();
        assertEquals("You chose: false", driver.findElement(By.xpath("//p[@id='confirm-text']")).getText());

        launchPrompt.click();
        String testText = "Lorem ipsum";
        Alert alert3 = driver.switchTo().alert();
        alert3.sendKeys(testText);
        alert3.accept();
        assertEquals("You typed: " + testText, driver.findElement(By.id("prompt-text")).getText());

        launchModal.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("button.btn-primary"))));
        assertEquals("This is the modal body", driver.findElement(By.xpath("//div[@class='modal-body']")).getText());
        driver.findElement(By.cssSelector("button.btn-primary")).click();
        assertTrue(driver.findElement(By.xpath("//p[@id='modal-text']")).getText().contains("Save changes"));
    }

    @Test
    @DisplayName("Web Storage")
    @Disabled
    void webStorageTests(){
    //пока ничего, метод устаревший...
    }
}