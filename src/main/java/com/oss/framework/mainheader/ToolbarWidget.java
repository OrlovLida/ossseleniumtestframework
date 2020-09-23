package com.oss.framework.mainheader;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToolbarWidget {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbarWidget;

    private static final String TOOLBAR_WIDGET_XPATH = "//div[@class='oss-header-toolbar']";
    private static final String LOGIN_PANEL_XPATH = ".//div[@class='toolbarWidget loginPanel']";
    private static final String NOTIFICATION_XPATH = ".//div[@class='toolbarWidget globalNotification']";
    private static final String QUERY_CONTEXT_CONTAINER_XPATH = ".//div[@class='toolbarWidget queryContextContainer']";


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

    public void openLoginPanel(){
        if (!isOpen(getloginPanel()))
            getloginPanel().click();
    }

    public void openNotificationPanel(){
        if (!isOpen(getNotificationPanel()))
            getNotificationPanel().click();
    }

    public void openQueryContextContainer(){
        if (!isOpen(getQueryContextContainer()))
            getQueryContextContainer().click();
    }

    public void closeLoginPanel(){
        if (isOpen(getloginPanel()))
            getloginPanel().click();
    }

    public void closeNotificationPanel(){
        if (isOpen(getNotificationPanel()))
            getNotificationPanel().click();
    }

    public void closeQueryContextContainer(){
        if (isOpen(getQueryContextContainer()))
            getQueryContextContainer().click();
    }


    private WebElement getloginPanel(){
        DelayUtils.waitByXPath(wait, LOGIN_PANEL_XPATH);
        return this.toolbarWidget.findElement(By.xpath(LOGIN_PANEL_XPATH));
    }

    private WebElement getQueryContextContainer(){
        DelayUtils.waitByXPath(wait, QUERY_CONTEXT_CONTAINER_XPATH);
        return this.toolbarWidget.findElement(By.xpath(QUERY_CONTEXT_CONTAINER_XPATH));
    }

    private WebElement getNotificationPanel(){
        DelayUtils.waitByXPath(wait, NOTIFICATION_XPATH);
        return this.toolbarWidget.findElement(By.xpath(NOTIFICATION_XPATH));
    }

    private boolean isOpen(WebElement element){
        return element.findElements(By.xpath(".//a[contains (@class, 'clicked')]")).size()>0;
    }
    }
