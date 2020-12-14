/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.sidemenu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Kamil Szota
 */
public class SideMenu {

    private static final String ACTION_NAME_PATH_PATTERN = "//div[@class='menu__item-label' and text()='%s']";
    private static final String SIDE_MENU_CLASS = "sideMenu";
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
        return driver.findElement(By.className(SIDE_MENU_CLASS));
    }

    public void callActionByLabel(String actionLabel, String... path) {
        String actionXpath;
        WebElement latestPath = getSideMenu();
        for (String s : path) {
            actionXpath = String.format(ACTION_NAME_PATH_PATTERN, s);
            DelayUtils.waitByXPath(wait, actionXpath);
            latestPath = latestPath.findElement(By.xpath(actionXpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", latestPath);
            Actions action = new Actions(driver);
            action.moveToElement(latestPath).click().perform();
        }
        callAction(actionLabel, latestPath);
    }

    private void callAction(String actionLabel, WebElement parent) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, actionLabel);
        DelayUtils.waitByXPath(wait, actionXpath);
        Actions actions = new Actions(driver);
        WebElement foundedElement = wait.until(
                ExpectedConditions.elementToBeClickable(parent.findElement(By.xpath(actionXpath))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundedElement);
        actions.moveToElement(foundedElement).click().perform();
    }
}
