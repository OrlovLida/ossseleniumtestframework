package com.oss.framework.utils;

import java.time.Duration;
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
    public static final int HUMAN_REACTION_MS = 250;
    public static final String FA_SPIN_XPATH = ".//i[contains(@class,'fa-spin')]";
    public static final String SPINNER_XPATH = ".//div[@class='Spinner']";
    public static final String LOAD_BARS_XPATH = ".//div[@class='load-bar']";
    public static final String BARS_LOADERS_XPATH = ".//div[@class='barsLoader']";
    public static final String SKELETON_PRELOADERS_XPATH = ".//div[@class='skeleton-preloader']";
    public static final String ACTION_IN_PROGRESS_XPATH = ".//a[@class='action inProgress']";
    public static final String OSS_APP_XPATH = "//div[@id='ossApp']";
    public static final String INTERRUPTED_EXCEPTION = "Interrputed exception occured.";
    private static final Logger log = LoggerFactory.getLogger(DelayUtils.class);
    private static final String SKELETON_PRELOADER_2_CLASS = "skeletonPreloader";
    
    private DelayUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void sleep() {
        sleep(1000);
    }
    
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn(INTERRUPTED_EXCEPTION);
            Thread.currentThread().interrupt();
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
    
    public static void waitForNestedElements(WebDriverWait wait, WebElement parent, By by) {
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, by));
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
        try {
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException e) {
            log.error("Some element(s) not disappear in expected time");
        }
    }
    
    public static void waitForElementDisappear(WebDriverWait wait, By by) {
        waitForNumberOfElementsToBe(wait, by, 0);
    }

    public static void waitForVisibility(WebDriverWait wait, List<WebElement> webElements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public static void waitForClickability(WebDriverWait wait, WebElement webelement) {
        wait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public static void waitForPageToLoad(WebDriver driver, WebDriverWait wait) {
        if (!isPageLoaded(driver, wait, 120000)) {
            log.warn("Page did not load for a two minutes!");
        }
    }

    public static boolean isPageLoaded(WebDriver driver, WebDriverWait wait, int expectedTime) {
        DelayUtils.sleep(1000);
        waitByXPath(wait, OSS_APP_XPATH);
        List<WebElement> newList = listOfLoaders(driver);
        long startTime = System.currentTimeMillis();
        while ((!newList.isEmpty()) && ((System.currentTimeMillis() - startTime) < expectedTime)) {
            try {
                wait.until(ExpectedConditions.invisibilityOfAllElements(newList));
            } catch (TimeoutException | ScriptTimeoutException e) {
                log.warn("Some element(s) could not be loaded in the expected time");
            }
            DelayUtils.sleep(500);
            newList = listOfLoaders(driver);
        }
        return (System.currentTimeMillis() - startTime) <= expectedTime;
    }
    
    public static void waitForButtonDisappear(WebDriver driver, String buttonXpath) {
        DelayUtils.sleep(1000);
        List<WebElement> buttons = driver.findElements(By.xpath(buttonXpath));
        waitForElementsDisappear(new WebDriverWait(driver,  Duration.ofSeconds(90)), buttons);
    }
    
    public static void waitForSpinners(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> spinners = webElement.findElements(By.xpath(FA_SPIN_XPATH));
        waitForElementsDisappear(webDriverWait, spinners);
        
    }
    
    public static void waitForLoadBars(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> loadBars = webElement.findElements(By.xpath(LOAD_BARS_XPATH));
        List<WebElement> barsLoader = webElement.findElements(By.xpath(BARS_LOADERS_XPATH));
        List<WebElement> newList = new ArrayList<>(loadBars);
        newList.addAll(barsLoader);
        waitForElementsDisappear(webDriverWait, newList);
    }
    
    public static void waitForElementToLoad(WebDriverWait webDriverWait, WebElement webElement) {
        DelayUtils.sleep(1000);
        List<WebElement> faSpins = webElement.findElements(By.xpath(FA_SPIN_XPATH));
        List<WebElement> spinners = webElement.findElements(By.xpath(SPINNER_XPATH));
        List<WebElement> loadBars = webElement.findElements(By.xpath(LOAD_BARS_XPATH));
        List<WebElement> barsLoader = webElement.findElements(By.xpath(BARS_LOADERS_XPATH));
        List<WebElement> skeletonPreloader = webElement.findElements(By.xpath(SKELETON_PRELOADERS_XPATH));
        List<WebElement> skeletonPreloader2 = webElement.findElements(By.className(SKELETON_PRELOADER_2_CLASS));
        List<WebElement> actionInProgress = webElement.findElements(By.xpath(ACTION_IN_PROGRESS_XPATH));
        List<WebElement> newList = new ArrayList<>(faSpins);
        newList.addAll(spinners);
        newList.addAll(loadBars);
        newList.addAll(barsLoader);
        newList.addAll(skeletonPreloader);
        newList.addAll(actionInProgress);
        newList.addAll(skeletonPreloader2);
        waitForElementsDisappear(webDriverWait, newList);
    }
    
    private static void waitForNumberOfElementsToBe(WebDriverWait wait, By by, int number) {
        wait.until(ExpectedConditions.numberOfElementsToBe(by, number));
    }
    
    private static List<WebElement> listOfLoaders(WebDriver driver) {
        List<WebElement> faSpins = driver.findElements(By.xpath(FA_SPIN_XPATH));
        List<WebElement> spinners = driver.findElements(By.xpath(SPINNER_XPATH));
        List<WebElement> loadBars = driver.findElements(By.xpath(LOAD_BARS_XPATH));
        List<WebElement> barsLoader = driver.findElements(By.xpath(BARS_LOADERS_XPATH));
        List<WebElement> skeletonPreloader = driver.findElements(By.xpath(SKELETON_PRELOADERS_XPATH));
        List<WebElement> skeletonPreloader2 = driver.findElements(By.className(SKELETON_PRELOADER_2_CLASS));
        List<WebElement> actionInProgress = driver.findElements(By.xpath(ACTION_IN_PROGRESS_XPATH));
        List<WebElement> newList = new ArrayList<>(faSpins);
        newList.addAll(spinners);
        newList.addAll(loadBars);
        newList.addAll(barsLoader);
        newList.addAll(skeletonPreloader);
        newList.addAll(actionInProgress);
        newList.addAll(skeletonPreloader2);
        return newList;
    }
    
    private static void waitForElementsDisappear(WebDriverWait webDriverWait, List<WebElement> webElements) {
        if (!webElements.isEmpty()) {
            DelayUtils.waitForElementDisappear(webDriverWait, webElements.get(0));
        }
    }
}
