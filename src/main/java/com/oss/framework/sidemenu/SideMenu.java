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
            latestPath = searchElement(latestPath, actionXpath);
            DelayUtils.sleep(500);
            Actions action = new Actions(driver);
            action.moveToElement(latestPath).click().perform();
        }
        callAction(actionLabel, latestPath);
    }

    private void callAction(String actionLabel, WebElement parent) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, actionLabel);
        WebElement foundedElement = searchElement(parent, actionXpath);
        DelayUtils.sleep(500);
        Actions actions = new Actions(driver);
        actions.moveToElement(foundedElement).click().perform();
    }

    private WebElement searchElement(WebElement webElement, String xpath) {
        for (int scrollDownCount = 0; scrollDownCount < 3; scrollDownCount++) {
            if (!(webElement.findElements(By.xpath(xpath)).isEmpty())) {
                WebElement foundElement = webElement.findElement(By.xpath(xpath));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundElement);
                return foundElement;
            }
            Actions action = new Actions(driver);
            action.moveToElement(webElement).sendKeys(Keys.PAGE_DOWN).perform();
            DelayUtils.sleep(500);
        }
        return webElement.findElement(By.xpath(xpath));
    }
}
