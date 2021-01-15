package com.oss.framework.widgets;

import java.util.List;

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

    private final String searchInput = ".//input[@class='form-control SearchText']";
    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;
    protected final String id;
    protected WebElement ossWindow;

    public enum WidgetType{
        TABLE_WIDGET, OLD_TABLE_WIDGET, PROPERTY_PANEL

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

    public static void waitForWidget(WebDriverWait wait, String widgetClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(widgetClass)));
    }

    public Widget(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[@"+ CSSUtils.TEST_ID +"='" + dataAttributeName + "']"));
        this.webDriverWait = webDriverWait;
        this.id = dataAttributeName;
    }

    protected WebElement refreshWidgetByID() {
        if(this.id == null) {
            throw new RuntimeException("Not supported if id is not defined, use constructor with id");
        }
        return driver.findElement(By.xpath("//div[@"+ CSSUtils.TEST_ID +"='" + this.id + "']"));
    }

    private static String createWidgetPath(String widgetId) {
        return "//div[@"+ CSSUtils.TEST_ID +"'" + widgetId + "']";
    }

    //TODO: move to advanced search component
    public WebElement getSearchInput() {
        DelayUtils.waitForBy(webDriverWait, By.xpath(searchInput));
        return this.webElement.findElement(By.xpath(searchInput));
    }

    //TODO: create wrapper for actions
    private WebElement getAction(String actionId) {
        return this.webElement.findElement(By.xpath(".//div[@id='" + actionId + "']/div"));
    }

    private List<WebElement> getActions() {
        return this.webElement.findElements(By.xpath(".//div[@class='actionsContainer']/div"));
    }

    //TODO: rewrite method
    public Boolean isActionDisplayed(String expectedAction) {
        Boolean result = false;
        DelayUtils.waitForBy(webDriverWait, By.xpath(".//div[@id='CREATE']/div"));
        for (WebElement e : getActions()) {
            if (e.getAttribute("id").equals(expectedAction)) {
                result = true;
            }
        }
        return result;
    }

    public void callOssWindowActionById(String groupId, String actionId) {
        this.ossWindow = webElement.findElement(By.xpath("//ancestor::div[contains(@class,'OssWindow')]"));
        ActionsInterface actions = ActionsContainer.createFromParent(ossWindow, driver, webDriverWait);
        actions.callActionById(groupId, actionId);
    }
}
