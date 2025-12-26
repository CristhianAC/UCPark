package es.unican.ps.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Selenium Manager handles driver binaries automatically
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateParking() {
        // 1. Open the application (adjust port/context if needed)
        driver.get("http://localhost:8080/");

        // 2. Search for user by email
        // Logic from TestDataLoader: juan.perez@example.com exists
        WebElement emailInput = driver.findElement(By.id("searchForm:email"));
        emailInput.sendKeys("juan.perez@example.com");

        WebElement searchButton = driver.findElement(By.xpath("//input[@value='Search']"));
        searchButton.click();

        // 3. Verify we are on the selection page
        assertTrue(driver.getCurrentUrl().contains("newParking"));

        // 4. Select a vehicle (click the first 'Select' button found)
        WebElement selectButton = driver.findElement(By.xpath("//input[@value='Select']"));

        // Capture element to wait for staleness
        WebElement oldMinutesInput = driver.findElement(By.id("parkingForm:minutes"));

        selectButton.click();

        // Wait for page reload (old element becomes stale)
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.stalenessOf(oldMinutesInput));

        // 5. Enter minutes for parking
        WebElement minutesInput = driver.findElement(By.id("parkingForm:minutes"));
        minutesInput.sendKeys("60");

        // 6. Create Parking
        WebElement createButton = driver.findElement(By.xpath("//input[@value='Create Parking']"));
        createButton.click();

        // 7. Verify success page
        assertTrue(driver.getCurrentUrl().contains("success"));
        assertTrue(driver.getPageSource().contains("Operation Successful"));
    }
}
