package com.oss.framework.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public abstract class Widget {

    private static final String SEARCH_INPUT = ".//input[@class='form-control SearchText']";
    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;
    protected final String id;
    protected WebElement ossWindow;

    public enum WidgetType {
        TABLE_WIDGET, OLD_TABLE_WIDGET, PROPERTY_PANEL
    }

    public static void waitForWidget(WebDriverWait wait, String widgetClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(widgetClass)));
    }

    public static void waitForWidgetById(WebDriverWait wait, String widgetId) {
        DelayUtils.waitBy(wait, By.xpath(createWidgetPath(widgetId)));
    }

    private static String createWidgetPath(String widgetId) {
        return "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']";
    }

    @Deprecated
    public Widget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[contains(@class, '" + widgetClass + "')]"));
        this.webDriverWait = webDriverWait;
        this.id = null;
    }

    @Deprecated
    public Widget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = webElement;
        this.webDriverWait = webDriverWait;
        this.id = null;
    }

    public Widget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        this.webDriverWait = webDriverWait;
        this.id = widgetId;
    }

    public Widget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement widget) {
        this.driver = driver;
        this.webElement = widget;
        this.webDriverWait = webDriverWait;
        this.id = widgetId;
    }

    protected WebElement refreshWidgetByID() {
        if (this.id == null) {
            throw new RuntimeException("Not supported if id is not defined, use constructor with id");
        }
        return driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + this.id + "']"));
    }

    //TODO: move to advanced search component
    @Deprecated
    public WebElement getSearchInput() {
        DelayUtils.waitForBy(webDriverWait, By.xpath(SEARCH_INPUT));
        return this.webElement.findElement(By.xpath(SEARCH_INPUT));
    }

    @Deprecated
    public void callOssWindowActionById(String groupId, String actionId) {
        this.ossWindow = webElement.findElement(By.xpath("//ancestor::div[contains(@class,'OssWindow')]"));
        ActionsInterface actions = ActionsContainer.createFromParent(ossWindow, driver, webDriverWait);
        actions.callActionById(groupId, actionId);
    }
}
