package com.oss.framework.components.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;

public class ToolbarWidget {

    private static final String TOOLBAR_WIDGET_XPATH = "//header[contains(@class, 'header')]";
    private static final String LOGIN_PANEL_BUTTON_XPATH = ".//div[contains(@class, 'login')]";
    private static final String LOGIN_PANEL_XPATH = ".//div[@class='login-panel']";
    private static final String NOTIFICATION_BUTTON_XPATH = ".//div[@class='notifications-button']";
    private static final String NOTIFICATION_PANEL_XPATH = ".//div[@class='notifications__panel__wrapper']";
    private static final String QUERY_CONTEXT_BUTTON_XPATH = ".//div[@class='query-context']//div[@role='button']";
    private static final String QUERY_CONTEXT_PANEL_XPATH =
            ".//div[@class='icon-dropdown-action-list query-context__dropdown']";
    private static final String GLOBAL_SEARCH_INPUT_XPATH = ".//div[@class='oss-input__input-content']";
    private static final String SHARE_PANEL_ICON_XPATH = ".//*[@data-testid='ButtonShareView']";
    private static final String SHARE_PANEL_XPATH = ".//div[@data-testid='Share_view_popup']";
    private static final String VIEW_TITLE_XPATH = ".//div[contains(@class, 'header-title')]";
    private static final String CLOSE_SHARE_PANEL_ICON_ID = "Share_view_popup-close_button";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbar;

    private ToolbarWidget(WebDriver driver, WebDriverWait wait, WebElement toolbar) {
        this.driver = driver;
        this.wait = wait;
        this.toolbar = toolbar;
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
            Button.createById(driver, CLOSE_SHARE_PANEL_ICON_ID).click();
        }
    }

    // pending the solution of OSSWEB-9263
    public void typeAndEnterInGlobalSearch(String value) {
        WebElement input = getGlobalSearch().findElement(By.xpath(".//input"));
        input.sendKeys(value);
        DelayUtils.waitForSpinners(wait, getGlobalSearch());
        input.sendKeys(Keys.ARROW_DOWN);
        input.sendKeys(Keys.ENTER);
    }

    public String getUserName() {
        WebElement loginButton = toolbar.findElement(By.xpath(LOGIN_PANEL_BUTTON_XPATH));
        return loginButton.getText();
    }

    public String getViewTitle() {
        return toolbar.findElement(By.xpath(VIEW_TITLE_XPATH)).getText();
    }

    public String getQueryContext() {
        return toolbar.findElement(By.xpath(QUERY_CONTEXT_BUTTON_XPATH)).getText();
    }

    private boolean isOpen(String panelXpath) {
        return !driver.findElements(By.xpath(panelXpath)).isEmpty();
    }

    private WebElement getGlobalSearch() {
        DelayUtils.waitByXPath(wait, GLOBAL_SEARCH_INPUT_XPATH);
        return this.toolbar.findElement(By.xpath(GLOBAL_SEARCH_INPUT_XPATH));
    }

    private void callAction(String buttonXpath) {
        DelayUtils.waitByXPath(wait, buttonXpath);
        this.toolbar.findElement(By.xpath(buttonXpath)).click();
    }
}
