package com.oss.framework.components.common;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WidgetChooser {

    private static String X_PATH_ID = "//div[@class='widgets-chooser']";

    public static WidgetChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement widgetChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new WidgetChooser(driver, webDriverWait, widgetChooser);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement widgetChooser;

    private WidgetChooser (WebDriver driver, WebDriverWait webDriverWait,  WebElement widgetChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.widgetChooser = widgetChooser;
    }

    public WidgetChooser disableWidgetByLabel(String widgetLabel) {
        if(isWidgetSelectedByLabel(widgetLabel)) {
            toggleWidgetByLabel(widgetLabel);
        }
        return this;
    }

    public WidgetChooser enableWidgetByLabel(String widgetLabel) {
        if(!isWidgetSelectedByLabel(widgetLabel)) {
            toggleWidgetByLabel(widgetLabel);
        }
        return this;
    }

    private void toggleWidgetByLabel(String widgetLabel) {
        Actions action = new Actions(driver);
        action.moveToElement(getWidgetByLabel(widgetLabel)).perform();
        getWidgetByLabel(widgetLabel).click();
    }

    public boolean isWidgetSelectedByLabel(String widgetLabel) {
        return getWidgetByLabel(widgetLabel).findElement(By.xpath(".//input")).isSelected();
    }

    private WebElement getWidgetByLabel(String widgetLabel){
        return widgetChooser.findElement(By.xpath(".//label[text()='" + widgetLabel + "']/.."));
    }

    public void clickAdd() {
        this.widgetChooser.findElement(By.xpath(".//a[contains(@class,'btn-primary')]")).click();
    }

    public void clickCancel() {
        this.widgetChooser.findElement(By.xpath("./a[contains(@class,'btn-flat')]")).click();
    }


}
