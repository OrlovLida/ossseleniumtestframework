package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebElementUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebElementUtils.class);
    private static final String RETRY_WARN = "Trying to click on element again.";
    
    private WebElementUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void clickWebElement(WebDriver driver, WebElement webElement) {
        moveToElement(driver, webElement);
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(driver);
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
        return !driver.findElements(by).isEmpty();
    }
    
    public static boolean isElementPresent(WebElement webElement, By by) {
        return !webElement.findElements(by).isEmpty();
    }

    public static void moveToElement(WebDriver driver, WebElement webElement) {
        if (!isDisplayedInViewport(webElement)) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({ behavior: 'instant', block: 'center', inline: 'center' });", webElement);
        }
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).build().perform();
    }
    
    public static boolean isDisplayedInViewport(WebElement element) {
        WebDriver driver = ((RemoteWebElement) element).getWrappedDriver();
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var elem = arguments[0],                        " +
                        "  box = elem.getBoundingClientRect(),      " +
                        "  cx = box.left + box.width -1,            " +
                        "  cy = box.top + box.height -1,            " +
                        "  e = document.elementFromPoint(cx, cy);   " +
                        "  dx = box.left,                           " +
                        "  dy = box.top,                            " +
                        "  f = document.elementFromPoint(dx, dy);   " +
                        "for (; e; e = e.parentElement) {           " +
                        "  if (e === elem && f === elem)            " +
                        "    return true;                           " +
                        "}                                          " +
                        "return false;                              ",
                element);
    }
}
