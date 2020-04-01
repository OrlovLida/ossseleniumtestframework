package com.oss;

import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LoginPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;


import static com.oss.configuration.Configuration.CONFIGURATION;

public class BaseTestCase {

    static final String BASIC_URL = CONFIGURATION.getValue("baseUrl");
    static final String MOCK_PATH = CONFIGURATION.getValue("mockPath");

    public WebDriver driver;
    public WebDriverWait webDriverWait;
    HomePage homePage;

    @BeforeClass
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverPath"));
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
        options.addArguments("start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, 50);
        LoginPage loginPage = new LoginPage(driver, BASIC_URL).open();
        this.homePage = loginPage.login();
    }


    @AfterClass
    public void closeBrowser() {
        if(driver != null) {
            driver.quit();
        }
    }

}
