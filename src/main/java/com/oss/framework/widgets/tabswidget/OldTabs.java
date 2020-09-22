/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.tabswidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class OldTabs implements TabsInterface {
    private final WebDriver driver;
    private final WebDriverWait wait;
    @Deprecated
    private final WebElement tabs;
    private final String id;

    public static OldTabs create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(500);
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'tabsContainer')]");
        WebElement widget = driver.findElement(By.xpath("//div[contains(@class,'tabsContainer')]"));
        return new OldTabs(driver, wait, widget);
    }

    public static OldTabs createById(WebDriver driver, WebDriverWait wait, String id) {
//        DelayUtils.waitByXPath(wait, "//div[contains(@class,'tabsContainer') and contains(@data-attributename, "+id+")]");
//        WebElement widget = driver.findElement(By.xpath("//div[contains(@class,'tabsContainer') and contains(@data-attributename,"+id+")]"));
        return new OldTabs(driver, wait, id);
    }

    private OldTabs(WebDriver driver, WebDriverWait wait, String id) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = null;
        this.id = id;
    }

    private OldTabs(WebDriver driver, WebDriverWait wait, WebElement tabs) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = tabs;
        this.id = null;
    }

    private WebElement createTabs() {
        if(this.tabs != null) {
            return this.tabs;
        }
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'tabsContainer') and contains(@data-attributename, "+id+")]");
        WebElement widget = driver.findElement(By.xpath("//div[contains(@class,'tabsContainer') and contains(@data-attributename,"+id+")]"));
        return widget;
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(wait, createTabs(), ".//div[contains(@class,'tabsContainerTabs')]");
        WebElement allTabs = createTabs().findElement(By.xpath(".//div[contains(@class,'tabsContainerTabs')]"));
        DelayUtils.waitForNestedElements(wait, allTabs, ".//a[contains(text(),'" + tabLabel + "')]");
        WebElement tab = allTabs.findElement(By.xpath(".//a[contains(text(),'" + tabLabel + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(tab));
        tab.click();
    }

    @Override
    public void selectTabById(String id) {
        DelayUtils.waitForPageToLoad(driver,wait);
        if (isMoreVisible()) {
            WebElement moreTab = createTabs().findElement(By.xpath("//div[@class= 'dropdown-tab']"));
            wait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(wait, ".//a[@id='" + id + "']");
            WebElement tab = driver.findElement(By.xpath(".//a[@id='" + id + "']"));
            wait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }
        else {
            WebElement tab = createTabs().findElement(By.xpath(".//a[@id='" + id + "']"));
            wait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }

    }

    @Override
    public void callActionByLabel(String label) {
        ActionsInterface actionsContainer = OldActionsContainer.createFromParent(driver, wait, createTabs());
        actionsContainer.callActionByLabel(label);
    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        throw new RuntimeException("Method not implemented for the old tabs");
    }

    @Override
    public void callActionById(String id) {
        ActionsInterface actionsContainer = OldActionsContainer.createFromParent(driver, wait, createTabs());
        actionsContainer.callActionById(id);

    }

    @Override
    public boolean isNoData(String id) {
        return false;
    }

    @Override
    public void callActionById(String groupId, String id) {
        throw new RuntimeException("Method not implemented for the old tabs");
    }
    private boolean isMoreVisible(){
        DelayUtils.waitForNestedElements(wait,createTabs(),"//div[@class= 'tabsContainerTabs']");
        List<WebElement> isMore = createTabs().findElements(By.xpath("//div[@class= 'dropdown-tab']"));
        return !isMore.isEmpty();
    }
}
