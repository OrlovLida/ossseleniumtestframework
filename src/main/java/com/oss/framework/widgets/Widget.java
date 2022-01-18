package com.oss.framework.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public abstract class Widget {

    private static final String WEB_ELEMENT_PATTERN = "[" + CSSUtils.TEST_ID + "='%s']";

    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;
    protected final String id;

    @Deprecated //OSSWEB-16424
    public Widget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[contains(@class, '" + widgetClass + "')]"));
        this.webDriverWait = webDriverWait;
        this.id = null;
    }

    protected Widget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        this.driver = driver;
        this.webElement = driver.findElement(By.cssSelector(String.format(WEB_ELEMENT_PATTERN, widgetId)));
        this.webDriverWait = webDriverWait;
        this.id = widgetId;
    }

    protected Widget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement widget) {
        this.driver = driver;
        this.webElement = widget;
        this.webDriverWait = webDriverWait;
        this.id = widgetId;
    }

    public static void waitForWidget(WebDriverWait wait, String widgetClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(widgetClass)));
    }

    public static void waitForWidgetById(WebDriverWait wait, String widgetId) {
        DelayUtils.waitBy(wait, By.cssSelector(String.format(WEB_ELEMENT_PATTERN, widgetId)));
    }

    protected WebElement refreshWidgetById() {
        if (this.id == null) {
            throw new UnsupportedOperationException("Not supported if id is not defined, use constructor with id");
        }
        return driver.findElement(By.cssSelector(String.format(WEB_ELEMENT_PATTERN, this.id)));
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public enum WidgetType {
        TABLE_WIDGET, OLD_TABLE_WIDGET, PROPERTY_PANEL
    }
}
