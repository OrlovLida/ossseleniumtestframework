package com.oss.framework.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class ToolbarWidget {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbarWidget;
    
    private static final String TOOLBAR_WIDGET_XPATH = "//div[@class='oss-header-cont']";
    private static final String LOGIN_PANEL_BUTTON_XPATH = ".//div[contains(@class,'toolbarWidget login')]";
    private static final String LOGIN_PANEL_XPATH = ".//div[@class='login-panel']";
    private static final String NOTIFICATION_BUTTON_XPATH = ".//div[@class='toolbarWidget globalNotification']";
    private static final String NOTIFICATION_PANEL_XPATH =
            ".//div[@class='toolbarWidget globalNotification']//a[contains(@class,'clicked')]";
    private static final String QUERY_CONTEXT_BUTTON_XPATH = ".//div[@class='toolbarWidget queryContextContainer']";
    private static final String QUERY_CONTEXT_PANEL_XPATH =
            ".//div[@class='toolbarWidget queryContextContainer']//a[contains(@class,'clicked')]";
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
        WebElement toolbar = driver.findElement(By.xpath(TOOLBAR_WIDGET_XPATH));
        return new ToolbarWidget(driver, wait, toolbar);
    }
    
    public void openLoginPanel() {
        if (!isOpen(LOGIN_PANEL_XPATH)) {
            callAction(LOGIN_PANEL_BUTTON_XPATH);
        }
    }
    
    public void openNotificationPanel() {
        if (!isOpen(NOTIFICATION_PANEL_XPATH)) {
            callAction(NOTIFICATION_BUTTON_XPATH);
        }
    }
    
    public void openQueryContextContainer() {
        if (!isOpen(QUERY_CONTEXT_PANEL_XPATH)) {
            callAction(QUERY_CONTEXT_BUTTON_XPATH);
        }
    }
    
    public void openSharePanel() {
        if (!isOpen(SHARE_PANEL_XPATH)) {
           callAction(SHARE_PANEL_ICON_XPATH);
        }
    }
    
    public void closeLoginPanel() {
        if (isOpen(LOGIN_PANEL_XPATH))
            callAction(LOGIN_PANEL_BUTTON_XPATH);
    }
    
    public void closeNotificationPanel() {
        if (isOpen(NOTIFICATION_PANEL_XPATH))
            callAction(NOTIFICATION_BUTTON_XPATH);
    }
    
    public void closeQueryContextContainer() {
        if (isOpen(QUERY_CONTEXT_PANEL_XPATH)) {
            callAction(QUERY_CONTEXT_BUTTON_XPATH);
        }
    }
    
    public void closeSharePanel() {
        if (isOpen(SHARE_PANEL_XPATH)) {
            callAction(SHARE_PANEL_ICON_XPATH);
        }
    }
    
    // pending the solution of OSSWEB-9263
    public void typeAndEnterInGlobalSearch(String value) {
        getGlobalSearch().findElement(By.xpath(".//input")).sendKeys(value);
        getGlobalSearch().findElement(By.xpath(".//input")).sendKeys(Keys.ENTER);
    }

    private boolean isOpen(String panelXpath) {
        return driver.findElements(By.xpath(panelXpath)).size() > 0;
    }
    
    private WebElement getGlobalSearch() {
        DelayUtils.waitByXPath(wait, GLOBAL_SEARCH_INPUT_XPATH);
        return this.toolbarWidget.findElement(By.xpath(GLOBAL_SEARCH_INPUT_XPATH));
    }

    private void callAction(String buttonXpath){
        DelayUtils.waitByXPath(wait, buttonXpath);
        this.toolbarWidget.findElement(By.xpath(buttonXpath)).click();
    }
}
