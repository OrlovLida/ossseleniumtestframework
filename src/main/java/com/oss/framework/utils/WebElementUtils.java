package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebElementUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebElementUtils.class);
    private static final String ELEMENT_NOT_PRESENT_INFO = "Element is not present.";
    private static final String RETRY_WARN = "Trying to click on element again.";

    private WebDriver driver;
    private WebDriverWait wait;

    private WebElementUtils(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static WebElementUtils create(WebDriver driver, WebDriverWait wait) {
        return new WebElementUtils(driver, wait);
    }

    public static boolean isElementPresent(WebElement webElement, By elementToWait) {
        return !webElement.findElements(elementToWait).isEmpty();
    }

    public void clickWebElement(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
        actions.click(webElement).build().perform();
    }

    public void clickWithRetry(WebElement elementToClick, By elementToWait) {
        clickWebElement(elementToClick);
        if (!isElementPresent(elementToWait)) {
            LOGGER.warn(RETRY_WARN);
            clickWebElement(elementToClick);
        }
        DelayUtils.waitForPresence(wait, elementToWait);
    }

    public boolean isElementPresent(By elementToWait) {
        try {
            DelayUtils.waitForPresence(new WebDriverWait(driver, 10), elementToWait);
            return true;
        } catch (TimeoutException e) {
            LOGGER.info(ELEMENT_NOT_PRESENT_INFO);
            return false;
        }
    }
}
