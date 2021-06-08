package com.oss.framework.widgets.tabswidget;

import java.util.List;

import com.oss.framework.components.contextactions.OldActionsContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class TabWindowWidget implements TabsInterface {
    
    private static final String DROPDOWN_TAB = ".//div[@class= 'dropdown']";
    private static final String OSS_WINDOW_TAB = "//div[@class='OssWindow tabWindow']";
    private static final String ALL_TABS = "//div[@class='tabs']";
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tabs;
    
    public static TabsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(500);
        DelayUtils.waitByXPath(wait, OSS_WINDOW_TAB);
        WebElement widget = driver.findElement(By.xpath(OSS_WINDOW_TAB));
        return new TabWindowWidget(driver, wait, widget);
    }
    
    private TabWindowWidget(WebDriver driver, WebDriverWait wait, WebElement tabs) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = tabs;
    }
    
    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(wait, this.tabs, OSS_WINDOW_TAB);
        String xpath = ".//button[@class='oss-tab']//div[contains(text(),'" + tabLabel + "')]";
        getTabToSelect(xpath).click();
        
    }
    
    @Override
    public void selectTabById(String ariaControls) {
        DelayUtils.waitForNestedElements(wait, this.tabs, OSS_WINDOW_TAB);
        String xpath = "//button[@aria-controls='" + ariaControls + "']";
        getTabToSelect(xpath).click();
        
    }
    
    @Override
    public void callActionByLabel(String label) {
        getActionsInterface().callActionByLabel(label);
    }

    @Override
    public void callActionById(String id) {
        getActionsInterface().callActionById(id);
    }
    
    @Override
    public void callActionByLabel(String groupLabel, String label) {
        getActionsInterface().callActionByLabel(groupLabel, label);
    }
    
    @Override
    public void callActionById(String groupLabel, String id) {
        getActionsInterface().callActionById(groupLabel, id);
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForNestedElements(wait, this.tabs,
                "//div[contains(@class, 'windowToolbar')] | //*[@class='actionsContainer']");
        boolean isNewActionContainer = isElementPresent(driver, By.className("actionsContainer"));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(this.tabs, driver, wait);
        } else {
            return OldActionsContainer.createFromParent(driver, wait, this.tabs);
        }
    }

    private boolean isElementPresent(WebDriver driver, By by) {
        return !driver.findElements(by).isEmpty();
    }
    
    @Override
    public boolean isNoData(String id) {
        List<WebElement> noData =
                driver.findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + id + "']//h3[contains(@class,'noDataWithColumns')]"));
        return !noData.isEmpty();
    }
    
    @Override
    public void clickButtonByLabel(String label) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(this.tabs, driver, wait);
        buttonContainer.callActionByLabel(label);
    }
    
    @Override
    public void callAction(String groupId, String actionId) {
        throw new RuntimeException("Method not implemented");
    }
    
    private boolean isMoreVisible() {
        DelayUtils.waitForNestedElements(wait, this.tabs, ALL_TABS);
        WebElement allTabs = this.tabs.findElement(By.xpath(ALL_TABS));
        List<WebElement> isMore = allTabs.findElements(By.xpath(DROPDOWN_TAB));
        return !isMore.isEmpty();
        
    }
    
    private WebElement getTabToSelect(String xPathForTab) {
        if (isMoreVisible()) {
            WebElement moreTab = this.tabs.findElement(By.xpath(DROPDOWN_TAB));
            wait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(wait, xPathForTab);
            return driver.findElement(By.xpath(xPathForTab));
        } else {
            return this.tabs.findElement(By.xpath(xPathForTab));
        }
    }
}
