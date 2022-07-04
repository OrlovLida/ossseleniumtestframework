/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.navigation.sidemenu;

import java.util.List;

import org.openqa.selenium.By;
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
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Kamil Szota
 */
public class SideMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(SideMenu.class);
    private static final String MENU_LEVEL_XPATH = ".//div[@class='menuLevel']";
    private static final String ACTION_NAME_PATH_PATTERN = MENU_LEVEL_XPATH + "//div[@data-testid='%s']";
    private static final String SIDE_MENU_XPATH = ".//div[@class='sideMenu'] | .//div[@class='sideMenu alpha-mode']";
    private static final String SIDE_MENU_HOME_XPATH = ".//div[@class='sideMenu alpha-mode']//div[@data-testid='Home']";
    private static final String HOVER_MODE_XPATH = ".//nav[@id='ossSideMenu' and contains(@class, 'hover-mode')]";
    private static final String SIDE_MENU_BUTTON_CSS = "button.menuButton.alpha-mode";
    private static final String OPEN_SIDE_MENU_CSS = ".alpha-mode.open";
    private static final String CLASS = "class";
    private static final String IS_EXPANDED = "isExpanded";
    private static final String IS_ACTIVE = "isActive";
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
        turnOffHoverMode();
        moveToTopOfSideMenu();
        for (String path : paths) {
            if (!isPathExpanded(path)) {
                callAction(path);
                waitForClickedPath(path);
            }
        }
        callAction(testid);
        waitForClickedAction(testid);
    }

    private WebElement getSideMenu() {
        DelayUtils.waitByXPath(wait, SIDE_MENU_XPATH);
        return driver.findElement(By.xpath(SIDE_MENU_XPATH));
    }

    private void callAction(String testid) {
        LOGGER.info("Search {}", testid);
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, testid);
        searchWithRetry(actionXpath);
        clickOnElement(moveToElement(actionXpath));
    }

    private boolean searchElement(String xpath) {
        boolean elementPresent = isElementPresent(By.xpath(xpath));
        for (int scrollDownCount = 0; scrollDownCount < 5; scrollDownCount++) {
            if (elementPresent) {
                return true;
            }
            moveDownOnTheSideMenu();
            elementPresent = isElementPresent(By.xpath(xpath));
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
        WebElementUtils.clickWebElement(driver, foundedElement);
    }

    private WebElement moveToElement(String xpath) {
        List<WebElement> foundElements = driver.findElements(By.xpath(xpath));
        WebElement foundElement = foundElements.get(foundElements.size() - 1);
        WebElementUtils.moveToElement(driver, foundElement);
        return foundElement;
    }

    private void moveDownOnTheSideMenu() {
        LOGGER.info("Moving down on the side menu.");
        List<WebElement> presentMenuElements = getPresentMenuElements();
        WebElementUtils.moveToElement(driver, presentMenuElements.get(presentMenuElements.size() - 1));
        DelayUtils.waitForLoadBars(wait, getSideMenu());
    }

    private void moveToTopOfSideMenu() {
        Actions action = new Actions(driver);
        if (!isHomePresent()) {
            LOGGER.info("Moving up on the side menu.");
            action.moveToElement(getSideMenu()).build().perform();
            action.moveToElement(getSideMenu()).sendKeys(Keys.HOME).build().perform();
            DelayUtils.waitForLoadBars(wait, getSideMenu());
        }
        action.moveToElement(driver.findElement(By.xpath(SIDE_MENU_HOME_XPATH))).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SIDE_MENU_HOME_XPATH)));
    }

    private List<WebElement> getPresentMenuElements() {
        return driver.findElement(By.xpath(SIDE_MENU_XPATH)).findElements(By.xpath(MENU_LEVEL_XPATH));

    }

    private boolean isHomePresent() {
        return isElementPresent(By.xpath(SIDE_MENU_HOME_XPATH));
    }

    private boolean isElementPresent(By by) {
        return !driver.findElements(by).isEmpty();
    }

    private void waitForClickedAction(String testid) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, testid);
        List<WebElement> elementList = driver.findElements(By.xpath(actionXpath));
        WebElement action = elementList.get(elementList.size() - 1);
        WebDriverWait shortWait = new WebDriverWait(driver, 15);
        try {
            shortWait.until(ExpectedConditions.attributeContains(action, CLASS, IS_ACTIVE));
        } catch (TimeoutException e) {
            LOGGER.warn("Action not active after first click. Retrying.");
            clickOnElement(action);
            shortWait.until(ExpectedConditions.attributeContains(action, CLASS, IS_ACTIVE));
        }
        LOGGER.info("{} clicked.", testid);
    }

    private void waitForClickedPath(String path) {
        String pathXpath = String.format(ACTION_NAME_PATH_PATTERN, path);
        WebDriverWait shortWait = new WebDriverWait(driver, 15);
        try {
            shortWait.until(ExpectedConditions.attributeContains(By.xpath(pathXpath), CLASS, IS_EXPANDED));
        } catch (TimeoutException e) {
            LOGGER.warn("Path not expanded after first click. Retrying.");
            clickOnElement(driver.findElement(By.xpath(pathXpath)));
            shortWait.until(ExpectedConditions.attributeContains(By.xpath(pathXpath), CLASS, IS_EXPANDED));
        }
        LOGGER.info("{} expanded.", path);
    }

    private boolean isPathExpanded(String path) {
        String pathXpath = String.format(ACTION_NAME_PATH_PATTERN, path);
        searchWithRetry(pathXpath);
        WebElement pathElement = driver.findElement(By.xpath(pathXpath));
        return pathElement.findElement(By.className("own-chevron")).getAttribute("aria-label").equals("Collapse");
    }

    private boolean isHoverModeOn() {
        return !driver.findElements(By.xpath(HOVER_MODE_XPATH)).isEmpty();
    }

    private void turnOffHoverMode() {
        if (isHoverModeOn()) {
            WebElement menuButton = driver.findElement(By.cssSelector(SIDE_MENU_BUTTON_CSS));
            WebElementUtils.clickWithRetry(driver, menuButton, By.cssSelector(OPEN_SIDE_MENU_CSS));
        }
    }
}
