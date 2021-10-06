package com.oss.framework.mainheader;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToolbarWidget {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbarWidget;

    private static final String TOOLBAR_WIDGET_XPATH = "//header[@class='oss-header']";
    private static final String LOGIN_PANEL_BUTTON_XPATH = ".//div[contains(@class,'toolbarWidget login')]";
    private static final String LOGIN_PANEL_XPATH = ".//div[@class='login-panel']";
    private static final String NOTIFICATION_XPATH = ".//div[@class='toolbarWidget globalNotification']";
    private static final String NOTIFICATION_PANEL_XPATH = ".//div[@class='notificationWrapper']";
    private static final String QUERY_CONTEXT_CONTAINER_XPATH = ".//div[@class='toolbarWidget queryContextContainer']";
    private static final String QUERY_CONTEXT_PANEL_XPATH = ".//div[@class='toolbarWidget queryContextContainer']//div[@class='CustomSelectList-rowsContainer']";
    private static final String GLOBAL_SEARCH_INPUT_XPATH = ".//div[@class='ExtendedSearchComponent']";
    private static final String SHARE_PANEL_ICON_XPATH = ".//*[@title = 'Share']";
    private static final String SHARE_PANEL_XPATH = ".//div[@class='shareTool']";


    private ToolbarWidget(WebDriver driver, WebDriverWait wait, WebElement toolbarWidget) {
        this.driver = driver;
        this.wait = wait;
        this.toolbarWidget = toolbarWidget;
    }

    public static ToolbarWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, TOOLBAR_WIDGET_XPATH);
        WebElement buttonPanel = driver.findElement(By.xpath(TOOLBAR_WIDGET_XPATH));
        return new ToolbarWidget(driver, wait, buttonPanel);
    }

    public void openLoginPanel() {
        if (!isOpen(LOGIN_PANEL_XPATH)) {
            getLoginPanel().click();
        }
    }

    public void openNotificationPanel() {
        if (!isOpen(NOTIFICATION_PANEL_XPATH)) {
            getNotificationPanel().click();
        }
    }

    public void openQueryContextContainer() {
        if (!isOpen(QUERY_CONTEXT_PANEL_XPATH)) {
            getQueryContextContainer().click();
        }
    }

    public void openSharePanel() {
        if (!isOpen(SHARE_PANEL_XPATH)) {
            getSharePanel().click();
        }
    }

    public void closeLoginPanel() {
        if (isOpen(LOGIN_PANEL_XPATH))
            getLoginPanel().click();
    }

    public void closeNotificationPanel() {
        if (isOpen(NOTIFICATION_PANEL_XPATH))
            getNotificationPanel().click();
    }

    public void closeQueryContextContainer() {
        if (isOpen(QUERY_CONTEXT_PANEL_XPATH)) {
            getQueryContextContainer().click();
        }
    }

    public void closeSharePanel() {
        if (isOpen(SHARE_PANEL_XPATH)) {
            getSharePanel().click();
        }
    }

    //pending the solution of OSSWEB-9263
    public void typeAndEnterInGlobalSearch(String value) {
        getGlobalSearch().findElement(By.xpath(".//input")).sendKeys(value);
        getGlobalSearch().findElement(By.xpath(".//input")).sendKeys(Keys.ENTER);
    }

    private WebElement getLoginPanel() {
        DelayUtils.waitByXPath(wait, LOGIN_PANEL_BUTTON_XPATH);
        return this.toolbarWidget.findElement(By.xpath(LOGIN_PANEL_BUTTON_XPATH));
    }

    private WebElement getQueryContextContainer() {
        DelayUtils.waitByXPath(wait, QUERY_CONTEXT_CONTAINER_XPATH);
        return this.toolbarWidget.findElement(By.xpath(QUERY_CONTEXT_CONTAINER_XPATH));
    }

    private WebElement getNotificationPanel() {
        DelayUtils.waitByXPath(wait, NOTIFICATION_XPATH);
        return this.toolbarWidget.findElement(By.xpath(NOTIFICATION_XPATH));
    }

    private boolean isOpen(String panelXpath) {
        return driver.findElements(By.xpath(panelXpath)).size() > 0;
    }

    private WebElement getGlobalSearch() {
        DelayUtils.waitByXPath(wait, GLOBAL_SEARCH_INPUT_XPATH);
        return this.toolbarWidget.findElement(By.xpath(GLOBAL_SEARCH_INPUT_XPATH));
    }

    private WebElement getSharePanel() {
        DelayUtils.waitByXPath(wait, SHARE_PANEL_ICON_XPATH);
        return this.toolbarWidget.findElement(By.xpath(SHARE_PANEL_ICON_XPATH));
    }
}
