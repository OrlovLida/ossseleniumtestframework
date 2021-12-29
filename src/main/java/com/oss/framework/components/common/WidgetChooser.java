package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class WidgetChooser {

    private static final String X_PATH_ID = "//div[@class='widgets-chooser']";
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement widgetChooser;
    private WidgetChooser(WebDriver driver, WebDriverWait webDriverWait, WebElement widgetChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.widgetChooser = widgetChooser;
    }

    public static WidgetChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement widgetChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new WidgetChooser(driver, webDriverWait, widgetChooser);
    }

    public WidgetChooser disableWidgetsByLabel(String... widgetLabels) {
        for (String widgetLabel : widgetLabels) {
            if (isWidgetSelected(widgetLabel)) {
                toggleWidgetByLabel(widgetLabel);
            }
        }
        return this;
    }

    public WidgetChooser enableWidgetsByLabel(String... widgetLabels) {
        for (String widgetLabel : widgetLabels) {
            if (!isWidgetSelected(widgetLabel)) {
                toggleWidgetByLabel(widgetLabel);
            }
        }
        return this;
    }

    public void setWidgetsState(String[] enableWidgetLabels, String[] disableWidgetLabels) {
        enableWidgetsByLabel(enableWidgetLabels);
        disableWidgetsByLabel(disableWidgetLabels);
        clickAdd();
    }

    public boolean isWidgetSelected(String widgetLabel) {
        return getWidgetByLabel(widgetLabel).findElement(By.xpath(".//input")).isSelected();
    }

    public void clickAdd() {
        this.widgetChooser.findElement(By.xpath(".//a[contains(@class,'btn-primary')]")).click();
    }

    public void clickCancel() {
        this.widgetChooser.findElement(By.xpath("./a[contains(@class,'btn-flat')]")).click();
    }

    private void toggleWidgetByLabel(String widgetLabel) {
        Actions action = new Actions(driver);
        action.moveToElement(getWidgetByLabel(widgetLabel)).perform();
        getWidgetByLabel(widgetLabel).click();
    }

    private WebElement getWidgetByLabel(String widgetLabel) {
        return widgetChooser.findElement(By.xpath(".//label[text()='" + widgetLabel + "']/.."));
    }

}
