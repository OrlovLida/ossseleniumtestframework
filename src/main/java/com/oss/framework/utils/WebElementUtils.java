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

    private WebElementUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void clickWebElement(WebDriver driver, WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).build().perform();
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(webElement));
        actions.click(webElement).build().perform();
    }

    public static void clickWithRetry(WebDriver driver, WebElement elementToClick, By elementToWait) {
        clickWebElement(driver, elementToClick);
        if (!isElementPresent(driver, elementToWait)) {
            LOGGER.warn(RETRY_WARN);
            clickWebElement(driver, elementToClick);
        }
        DelayUtils.waitForPresence(new WebDriverWait(driver, 10), elementToWait);
    }

    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            DelayUtils.waitForPresence(new WebDriverWait(driver, 10), by);
            return true;
        } catch (TimeoutException e) {
            LOGGER.info(ELEMENT_NOT_PRESENT_INFO);
            return false;
        }
    }

    public static boolean isElementPresent(WebElement webElement, By by) {
        return !webElement.findElements(by).isEmpty();
    }
}
