/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.sidemenu;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Kamil Szota
 */
public class SideMenu {

    private static final String ACTION_NAME_PATH_PATTERN = "//div[@class='menu__item-label' and text()='%s']";
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SideMenu(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void callActionByLabel(String actionLabel, String... path) {
        String actionXpath;
        for (String s : path) {
            actionXpath = String.format(ACTION_NAME_PATH_PATTERN, s);
            DelayUtils.waitByXPath(wait, actionXpath);
            driver.findElement(By.xpath(actionXpath)).click();
        }
        callAction(actionLabel);
    }

    private void callAction(String actionLabel) {
        String actionXpath = String.format(ACTION_NAME_PATH_PATTERN, actionLabel);
        DelayUtils.waitByXPath(wait, actionXpath);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath(actionXpath)))
                .sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
                .pause(500).build().perform();
        actions.moveToElement(driver.findElement(By.xpath(actionXpath)))
                .click().build().perform();
    }
}
