/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.tabswidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class OldTabs implements TabsInterface {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tabs;

    public static TabsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(500);
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'tabsContainer')]");
        WebElement widget = driver.findElement(By.xpath("//div[contains(@class,'tabsContainer')]"));
        return new OldTabs(driver, wait, widget);
    }

    private OldTabs(WebDriver driver, WebDriverWait wait, WebElement tabs) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = tabs;
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(wait, this.tabs, ".//div[contains(@class,'tabsContainerTabs')]");
        WebElement allTabs = this.tabs.findElement(By.xpath(".//div[contains(@class,'tabsContainerTabs')]"));
        DelayUtils.waitForNestedElements(wait, allTabs, ".//a[contains(text(),'" + tabLabel + "')]");
        WebElement tab = allTabs.findElement(By.xpath(".//a[contains(text(),'" + tabLabel + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(tab));
        tab.click();
    }

    @Override
    public void callActionByLabel(String label) {
        ActionsInterface actionsContainer = OldActionsContainer.createFromWidget(driver, wait, this.tabs);
        actionsContainer.callActionByLabel(label);
    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        throw new RuntimeException("Method not implemented for the old tabs");
    }

    @Override
    public void callActionById(String id) {
        throw new RuntimeException("Method not implemented for the old tabs");
    }

    @Override
    public void callActionById(String groupId, String id) {
        throw new RuntimeException("Method not implemented for the old tabs");
    }
}
