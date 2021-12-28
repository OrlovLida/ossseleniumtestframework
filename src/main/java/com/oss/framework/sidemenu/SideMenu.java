/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.sidemenu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Kamil Szota
 */
public class SideMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(SideMenu.class);
    private static final String ACTION_NAME_PATH_PATTERN = "//div[@class='menuLevel']//div[@data-testid='%s']";
    private static final String SIDE_MENU_CLASS = ".//div[@class='sideMenu'] | .//div[@class='sideMenu alpha-mode']";
    private static final String SIDE_MENU_HOME = ".//div[@class='sideMenu alpha-mode']//div[@data-testid='Home']";
    private static final String CLASS = "class";
    private final WebDriver driver;
    private final WebDriverWait wait;

    private SideMenu(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static SideMenu create(WebDriver driver, WebDriverWait wait) {
        return new SideMenu(driver, wait);
    }

    public void callActionByLabel(String testid, String... paths) {
        DelayUtils.waitForLoadBars(wait, getSideMenu());
        moveToTopOfSideMenu();
        for (String path : paths) {
            callAction(path);
            waitForClickedPath(path);
        }
        callAction(testid);
        waitForClickedAction(testid);
    }

    private WebElement getSideMenu() {
        DelayUtils.waitByXPath(wait, SIDE_MENU_CLASS);
        return driver.findElement(By.xpath(SIDE_MENU_CLASS));
    }

    private void callAction(String testid) {
        LOGGER.info("Search {}", testid);
        DelayUtils.waitForLoadBars(wait, getSideMenu());
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, testid);
        searchWithRetry(actionXpath);
        LOGGER.info("Click on {}", testid);
        clickOnElement(moveToElement(actionXpath));
    }

    private boolean searchElement(String xpath) {
        for (int scrollDownCount = 0; scrollDownCount < 3; scrollDownCount++) {
            DelayUtils.waitForLoadBars(wait, getSideMenu());
            if (isElementPresent(By.xpath(xpath))) {
                return true;
            }
            moveDownOnTheSideMenu();
        }
        return isElementPresent(By.xpath(xpath));
    }

    private void searchWithRetry(String xpath) {
        if (searchElement(xpath)) {
            return;
        }
        LOGGER.warn("Object not found in the side menu. Retrying.");
        moveToTopOfSideMenu();
        searchElement(xpath);
    }

    private void clickOnElement(WebElement foundedElement) {
        DelayUtils.waitForLoadBars(wait, getSideMenu());
        Actions actions = new Actions(driver);
        actions.moveToElement(foundedElement).click().build().perform();
    }

    private WebElement moveToElement(String xpath) {
        DelayUtils.waitForLoadBars(wait, getSideMenu());
        WebElement foundElement = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundElement);
        return foundElement;
    }

    private void moveDownOnTheSideMenu() {
        LOGGER.info("Moving down on the side menu.");
        moveOnTheSideMenu(Keys.PAGE_DOWN);
    }

    private void moveToTopOfSideMenu() {
        Actions action = new Actions(driver);
        if (!isHomePresent()) {
            LOGGER.info("Moving up on the side menu.");
            moveOnTheSideMenu(Keys.HOME);
        }
        LOGGER.info("Moving to the top of side menu.");
        action.moveToElement(driver.findElement(By.xpath(SIDE_MENU_HOME))).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SIDE_MENU_HOME)));
    }

    private void moveOnTheSideMenu(Keys key) {
        DelayUtils.waitForLoadBars(wait, getSideMenu());
        Actions action = new Actions(driver);
        action.moveToElement(getSideMenu()).build().perform();
        action.moveToElement(getSideMenu()).sendKeys(key).build().perform();
        DelayUtils.waitForLoadBars(wait, getSideMenu());
    }

    private boolean isHomePresent() {
        return isElementPresent(By.xpath(SIDE_MENU_HOME));
    }

    private boolean isElementPresent(By by) {
        return !driver.findElements(by).isEmpty();
    }

    private void waitForClickedAction(String testid) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, testid);
        LOGGER.debug("Waiting for {} to be clicked.", testid);
        try {
            wait.until(ExpectedConditions.attributeContains(By.xpath(actionXpath), CLASS, "isActive"));
        } catch (TimeoutException e) {
            LOGGER.warn("Action not active after first click. Retrying.");
            clickOnElement(driver.findElement(By.xpath(actionXpath)));
            wait.until(ExpectedConditions.attributeContains(By.xpath(actionXpath), CLASS, "isActive"));
        }
        LOGGER.info("{} clicked.", testid);
    }

    private void waitForClickedPath(String path) {
        String pathXpath = String.format(ACTION_NAME_PATH_PATTERN, path);
        LOGGER.debug("Waiting for {} to be expanded", path);
        try {
            wait.until(ExpectedConditions.attributeContains(By.xpath(pathXpath), CLASS, "isExpanded"));
        } catch (TimeoutException e) {
            LOGGER.warn("Path not expanded after first click. Retrying.");
            clickOnElement(driver.findElement(By.xpath(pathXpath)));
            wait.until(ExpectedConditions.attributeContains(By.xpath(pathXpath), CLASS, "isExpanded"));
        }
        LOGGER.info("{} expanded.", path);
    }
}
