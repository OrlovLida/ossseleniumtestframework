package com.oss.framework.widgets;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;

public abstract class Widget {

    private final String searchInput = ".//input[@class='form-control SearchText']";
    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;
    protected WebElement ossWindow;

    public Widget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[contains(@class, '" + widgetClass + "')]"));
        this.webDriverWait = webDriverWait;
        this.ossWindow = webElement.findElement(By.xpath("//div[contains(@class, '" + widgetClass + "')]/ancestor::div[contains(@class,'OssWindow')]"));
    }

    public Widget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = webElement;
        this.webDriverWait = webDriverWait;
    }

    public static void waitForWidget(WebDriverWait wait, String widgetClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(widgetClass)));
    }

    public Widget(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//div[@data-attributename='" + dataAttributeName + "']"));
        this.ossWindow = webElement.findElement(By.xpath("//div[@data-attributename='" + dataAttributeName + "']/ancestor::div[contains(@class,'OssWindow')]"));
        this.webDriverWait = webDriverWait;
    }

    private static String createWidgetPath(String widgetId) {
        return "//div[@data-attributename='" + widgetId + "']";
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

    public void callActionById(String groupId, String actionId) {
        ActionsInterface actions = ActionsContainer.createFromParent(ossWindow, driver, webDriverWait);
        actions.callActionById(groupId, actionId);
    }
}
