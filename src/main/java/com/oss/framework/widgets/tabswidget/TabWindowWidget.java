package com.oss.framework.widgets.tabswidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class TabWindowWidget implements TabsInterface {

    private static final String DROPDOWN_TAB_XPATH = ".//div[@class= 'dropdown']";
    private static final String OSS_WINDOW_TAB_XPATH = "//div[@class='OssWindow tabWindow']";
    private static final String ALL_TABS_XPATH = "//div[@class='tabs']";
    private static final String ACTIONS_CONTAINER_XPATH = "//*[@class='actionsContainer']";
    private static final String CONTEXT_ACTIONS_XPATH = "//div[contains(@class, 'windowToolbar')] | " + ACTIONS_CONTAINER_XPATH;
    private static final String TAB_BY_LABEL_PATTERN = ".//button[@class='oss-tab']//div[contains(text(),'%s')]";
    private static final String TAB_BY_ID_PATTERN = "//button[@aria-controls='%s']";
    private static final String NO_DATA_PATTERN = "//div[@" + CSSUtils.TEST_ID + "='%s']//h3[contains(@class,'noDataWithColumns')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tabs;

    private TabWindowWidget(WebDriver driver, WebDriverWait wait, WebElement tabs) {
        this.driver = driver;
        this.wait = wait;
        this.tabs = tabs;
    }

    public static TabsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.sleep(500);
        DelayUtils.waitByXPath(wait, OSS_WINDOW_TAB_XPATH);
        WebElement widget = driver.findElement(By.xpath(OSS_WINDOW_TAB_XPATH));
        return new TabWindowWidget(driver, wait, widget);
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(wait, this.tabs, OSS_WINDOW_TAB_XPATH);
        String xpath = String.format(TAB_BY_LABEL_PATTERN, tabLabel);
        getTabToSelect(xpath).click();
    }

    @Override
    public void selectTabById(String ariaControls) {
        DelayUtils.waitForNestedElements(wait, this.tabs, OSS_WINDOW_TAB_XPATH);
        String xpath = String.format(TAB_BY_ID_PATTERN, ariaControls);
        getTabToSelect(xpath).click();
    }

    @Override
    public void callActionByLabel(String label) {
        getActionsInterface().callActionByLabel(label);
    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        getActionsInterface().callActionByLabel(groupLabel, label);
    }

    @Override
    public void callActionById(String groupLabel, String id) {
        getActionsInterface().callActionById(groupLabel, id);
    }

    @Override
    public void callActionById(String id) {
        getActionsInterface().callActionById(id);
    }

    @Override
    public boolean hasNoData(String id) {
        List<WebElement> noData =
                driver.findElements(By.xpath(String.format(NO_DATA_PATTERN, id)));
        return !noData.isEmpty();
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForNestedElements(wait, this.tabs, CONTEXT_ACTIONS_XPATH);
        boolean isNewActionContainer = isElementPresent(driver, By.xpath(ACTIONS_CONTAINER_XPATH));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(this.tabs, driver, wait);
        } else {
            return OldActionsContainer.createFromParent(driver, wait, this.tabs);
        }
    }

    private boolean isElementPresent(WebDriver driver, By by) {
        return !driver.findElements(by).isEmpty();
    }

    private boolean isMorePresent() {
        DelayUtils.waitForNestedElements(wait, this.tabs, ALL_TABS_XPATH);
        WebElement allTabs = this.tabs.findElement(By.xpath(ALL_TABS_XPATH));
        List<WebElement> isMore = allTabs.findElements(By.xpath(DROPDOWN_TAB_XPATH));
        return !isMore.isEmpty();
    }

    private WebElement getTabToSelect(String xPathForTab) {
        if (isMorePresent()) {
            WebElement moreTab = this.tabs.findElement(By.xpath(DROPDOWN_TAB_XPATH));
            wait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(wait, xPathForTab);
            return driver.findElement(By.xpath(xPathForTab));
        } else {
            return this.tabs.findElement(By.xpath(xPathForTab));
        }
    }
}
