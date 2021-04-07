package com.oss.framework.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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

    public static void waitForPresenceAndVisibility(WebDriverWait wait, By locator) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementDisappear(WebDriverWait wait, WebElement webElement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.invisibilityOf(webElement));
    }

    public static void waitForVisibility(WebDriverWait wait, List<WebElement> webElements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public static void waitForClickability(WebDriverWait wait, WebElement webelement) {
        wait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public static void waitForBy(WebDriverWait wait, By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitForPageToLoad(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(1000);
        List<WebElement> spinners = driver.findElements(By.xpath("//i[contains(@class,'fa-spin')]"));
        List<WebElement> loadBars = driver.findElements(By.xpath("//div[@class='load-bar']"));
        List<WebElement> appPreloader = driver.findElements(By.xpath("//div[contains(@class, 'appPreloader') and not(contains(@class, 'noDataContainer'))]"));
        List<WebElement> preloaderWrapper = driver.findElements(By.xpath("//div[@class='preloaderWrapper']"));
        List<WebElement> newList = new ArrayList<>(spinners);
        newList.addAll(loadBars);
        newList.addAll(appPreloader);
        newList.addAll(preloaderWrapper);
        long startTime = System.currentTimeMillis();
        while ((newList.size() > 0) && ((System.currentTimeMillis() - startTime) < 120000)) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(newList));
            spinners = driver.findElements(By.xpath("//i[contains(@class,'fa-spin')]"));
            loadBars = driver.findElements(By.xpath("//div[@class='load-bar']"));
            appPreloader = driver.findElements(By.xpath("//div[contains(@class, 'appPreloader') and not(contains(@class, 'noDataContainer'))]"));
            preloaderWrapper = driver.findElements(By.xpath("//div[@class='preloaderWrapper']"));
            newList = new ArrayList<>(spinners);
            newList.addAll(loadBars);
            newList.addAll(appPreloader);
            newList.addAll(preloaderWrapper);
        }
        if ((System.currentTimeMillis() - startTime) > 120000) {
            System.out.println("Page did not load for a two minutes!");
        }
    }

}
