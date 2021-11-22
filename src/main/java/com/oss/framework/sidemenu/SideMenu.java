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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Kamil Szota
 */
public class SideMenu {

    private static final String ACTION_NAME_PATH_PATTERN = "//div[@class='menu__item-label' and text()='%s']";
    private static final String SIDE_MENU_CLASS = ".//div[@class='sideMenu'] | .//div[@class='sideMenu alpha-mode']";
    private final WebDriver driver;
    private final WebDriverWait wait;

    private SideMenu(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static SideMenu create(WebDriver driver, WebDriverWait wait) {
        return new SideMenu(driver, wait);
    }

    private WebElement getSideMenu() {
        DelayUtils.waitByXPath(wait, SIDE_MENU_CLASS);
        return driver.findElement(By.xpath(SIDE_MENU_CLASS));

    }

    public void callActionByLabel(String actionLabel, String... path) {
        String actionXpath;
        WebElement latestPath = getSideMenu();
        Actions action = new Actions(driver);
        action.moveToElement(latestPath).sendKeys(Keys.HOME).perform();
        for (String s : path) {
            DelayUtils.waitForPageToLoad(driver, wait);
            actionXpath = String.format(ACTION_NAME_PATH_PATTERN, s);
            latestPath = searchElement(latestPath, actionXpath);
            DelayUtils.sleep(1000);
            action.moveToElement(latestPath).click().perform();
        }
        callAction(actionLabel, latestPath);
    }

    private void callAction(String actionLabel, WebElement parent) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, actionLabel);
        WebElement foundedElement = searchElement(parent, actionXpath);
        DelayUtils.waitForPageToLoad(driver, wait);
        Actions actions = new Actions(driver);
        actions.moveToElement(foundedElement).click().perform();
    }

    private WebElement searchElement(WebElement webElement, String xpath) {
        for (int scrollDownCount = 0; scrollDownCount < 3; scrollDownCount++) {
            DelayUtils.waitForPageToLoad(driver, wait);
            if (!(driver.findElements(By.xpath(xpath)).isEmpty())) {
                DelayUtils.waitForPageToLoad(driver, wait);
                WebElement foundElement = driver.findElement(By.xpath(xpath));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundElement);
                return foundElement;
            }
            Actions action = new Actions(driver);
            action.moveToElement(webElement).sendKeys(Keys.PAGE_DOWN).perform();
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        return driver.findElement(By.xpath(xpath));
    }
}
