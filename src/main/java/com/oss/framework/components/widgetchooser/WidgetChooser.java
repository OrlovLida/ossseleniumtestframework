package com.oss.framework.components.widgetchooser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class WidgetChooser {

    private static final String X_PATH_ID = "//div[@class='add-widget']";
    public static final String CSS_BUTTON_ADD = " .oss-button__inner--primary";
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

    public void clickAdd() {
        this.widgetChooser.findElement(By.cssSelector(CSS_BUTTON_ADD)).click();
    }

    public void clickCancel() {
        this.widgetChooser.findElement(By.xpath("./a[contains(@class,'btn-flat')]")).click();
    }

    public WidgetChooser toggleWidgetByTypeAndLabel(String widgetType, String widgetLabel) {
        getWidgetType(widgetType).click();
        Actions action = new Actions(driver);
        action.moveToElement(getWidgetByLabel(widgetLabel)).perform();
        getWidgetByLabel(widgetLabel).click();
        return this;
    }

    private WebElement getWidgetByLabel(String widgetLabel) {
        return widgetChooser.findElement(By.xpath(" .//p[text()='" + widgetLabel + "']"));
    }

    private WebElement getWidgetType(String widgetType) {
        return widgetChooser.findElement(By.xpath(" .//p[text()=' " + widgetType + "']"));
    }

}
