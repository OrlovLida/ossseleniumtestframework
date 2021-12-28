package com.oss.framework.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayUtils {
    private static final Logger log = LoggerFactory.getLogger(DelayUtils.class);
    public static final int HUMAN_REACTION_MS = 250;
    public static final String SPINNERS = ".//i[contains(@class,'fa-spin')]";
    public static final String LOAD_BARS = ".//div[@class='load-bar']";
    public static final String BARS_LOADERS = ".//div[@class='barsLoader']";
    public static final String SKELETON_PRELOADERS = ".//div[@class='skeleton-preloader']";
    public static final String ACTION_IN_PROGRESS = ".//a[@class='action inProgress']";

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

    public static void waitForPresence(WebDriverWait wait, By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
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
        waitByXPath(wait, "//div[@id='ossApp']");
        List<WebElement> newList = listOfLoaders(driver);
        long startTime = System.currentTimeMillis();
        while ((!newList.isEmpty()) && ((System.currentTimeMillis() - startTime) < 120000)) {
            try {
                wait.until(ExpectedConditions.invisibilityOfAllElements(newList));
            } catch (TimeoutException | ScriptTimeoutException e) {
                log.warn("Some element(s) could not be loaded in the expected time");
            }
            DelayUtils.sleep(500);
            newList = listOfLoaders(driver);
        }
        if ((System.currentTimeMillis() - startTime) > 120000) {
            log.warn("Page did not load for a two minutes!");
        }
    }

    private static List<WebElement> listOfLoaders(WebDriver driver) {
        List<WebElement> spinners = driver.findElements(By.xpath(SPINNERS));
        List<WebElement> loadBars = driver.findElements(By.xpath(LOAD_BARS));
        List<WebElement> barsLoader = driver.findElements(By.xpath(BARS_LOADERS));
        List<WebElement> skeletonPreloader = driver.findElements(By.xpath(SKELETON_PRELOADERS));
        List<WebElement> actionInProgress = driver.findElements(By.xpath(ACTION_IN_PROGRESS));
        List<WebElement> newList = new ArrayList<>(spinners);
        newList.addAll(loadBars);
        newList.addAll(barsLoader);
        newList.addAll(skeletonPreloader);
        newList.addAll(actionInProgress);
        return newList;
    }

    public static void waitForButtonDisappear(WebDriver driver, String buttonXpath) {
        DelayUtils.sleep(1000);
        List<WebElement> buttons = driver.findElements(By.xpath(buttonXpath));
        waitForElementsDisappear(new WebDriverWait(driver, 90), buttons);
    }

    public static void waitForSpinners(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> spinners = webElement.findElements(By.xpath(SPINNERS));
        waitForElementsDisappear(webDriverWait, spinners);

    }

    public static void waitForLoadBars(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> loadBars = webElement.findElements(By.xpath(LOAD_BARS));
        List<WebElement> barsLoader = webElement.findElements(By.xpath(BARS_LOADERS));
        List<WebElement> newList = new ArrayList<>(loadBars);
        newList.addAll(barsLoader);
        waitForElementsDisappear(webDriverWait, newList);
    }

    public static void waitForElementToLoad(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> spinners = webElement.findElements(By.xpath(SPINNERS));
        List<WebElement> loadBars = webElement.findElements(By.xpath(LOAD_BARS));
        List<WebElement> barsLoader = webElement.findElements(By.xpath(BARS_LOADERS));
        List<WebElement> skeletonPreloader = webElement.findElements(By.xpath(SKELETON_PRELOADERS));
        List<WebElement> actionInProgress = webElement.findElements(By.xpath(ACTION_IN_PROGRESS));
        List<WebElement> newList = new ArrayList<>(spinners);
        newList.addAll(loadBars);
        newList.addAll(barsLoader);
        newList.addAll(skeletonPreloader);
        newList.addAll(actionInProgress);
        waitForElementsDisappear(webDriverWait, newList);
    }

    private static void waitForElementsDisappear(WebDriverWait webDriverWait, List<WebElement> webElements) {
        if (!webElements.isEmpty()) {
            DelayUtils.waitForElementDisappear(webDriverWait, webElements.get(0));
        }
    }
}
