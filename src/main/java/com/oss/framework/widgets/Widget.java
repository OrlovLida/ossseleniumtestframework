package com.oss.framework.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class Widget {

    private final String searchInput = ".//input[@class='form-control SearchText']";
    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;

    public Widget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(widgetClass));
        this.webDriverWait = webDriverWait;
    }

    public static void waitForWidget(WebDriverWait wait, String widgetClass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(widgetClass)));
    }

    private static String createWidgetPath(String widgetId) {
        return "//div[@data-attributename='"+widgetId+"']";
    }

    //TODO: move to advanced search component
    public WebElement getSearchInput(){
        waitForBy(By.xpath(searchInput));
        return this.webElement.findElement(By.xpath(searchInput));
    }

    //TODO: create wrapper for actions
    private WebElement getAction(String actionId) {
        return this.webElement.findElement(By.xpath(".//div[@id='" + actionId + "']/div")); }

    private List<WebElement> getActions() {
        return this.webElement.findElements(By.xpath(".//div[@class='actionsContainer']/div")); }

    //TODO: rewrite method
    public Boolean isActionDisplayed(String expectedAction) {
        Boolean result = false;
        waitForBy(By.xpath(".//div[@id='CREATE']/div"));
        for (WebElement e : getActions()) {
            if (e.getAttribute("id").equals(expectedAction)) {
                result = true;
            }
        }
        return result;
    }

    public void waitForVisibility(WebElement webelement) {
        webDriverWait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(webelement));
    }

    protected void waitForVisibility(List<WebElement> webElements) {
        webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void waitForClickability(WebElement webelement) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public void waitForBy(By by) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
