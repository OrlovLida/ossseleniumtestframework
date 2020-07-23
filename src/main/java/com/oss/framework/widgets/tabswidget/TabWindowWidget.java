package com.oss.framework.widgets.tabswidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;

public class TabWindowWidget implements TabsInterface {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tabs;

    public static TabsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(500);
        DelayUtils.waitByXPath(wait, "//div[@class='OssWindow tabWindow']");
        WebElement widget = driver.findElement(By.xpath("//div[@class='OssWindow tabWindow']"));
        return new TabWindowWidget(driver, wait, widget);
    }

    private TabWindowWidget(WebDriver driver, WebDriverWait wait, WebElement tabs) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = tabs;
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(wait, this.tabs, "//div[@class='OssWindow tabWindow']");
        WebElement allTabs = this.tabs.findElement(By.xpath("//ul[@role='tablist']"));
        DelayUtils.waitForNestedElements(wait, allTabs, "//div[contains(text(),'" + tabLabel + "')]");
        WebElement tab = allTabs.findElement(By.xpath("//div[contains(text(),'" + tabLabel + "')]"));
        tab.click();
    }

    @Override
    public void callActionByLabel(String label) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.tabs, driver, wait);
        actionsContainer.callActionByLabel(label);
    }

    @Override
    public void callActionById(String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.tabs, driver, wait);
        actionsContainer.callActionById(id);
    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.tabs, driver, wait);
        actionsContainer.callActionByLabel(groupLabel, label);
    }

}
