package com.oss.framework.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DelayUtils {
    public static int HUMAN_REACTION_MS = 250;

    public static void sleep() {
        sleep(1000);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //Do nothing
        }
    }

    public static void waitBy(WebDriverWait wait, By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitByXPath(WebDriverWait wait, String xPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
    }

    public static void waitForNestedElements(WebDriverWait wait, WebElement parent, String xPath) {
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, By.xpath(xPath)));
    }

    public static void waitForNestedElements(WebDriverWait wait, String parentXpath, String xPath) {
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(By.xpath(parentXpath), By.xpath(xPath)));
    }

    public static void waitByElement(WebDriverWait wait, WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForVisibility(WebDriverWait wait, WebElement webelement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(webelement));
    }

    public static void waitForElementDisapear(WebDriverWait wait, WebElement webElement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.invisibilityOf(webElement));
    }

    protected static void waitForVisibility(WebDriverWait wait, List<WebElement> webElements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public static void waitForClickability(WebDriverWait wait, WebElement webelement) {
        wait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public static void waitForBy(WebDriverWait wait, By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
