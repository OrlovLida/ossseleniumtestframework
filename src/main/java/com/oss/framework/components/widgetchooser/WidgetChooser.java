package com.oss.framework.components.widgetchooser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class WidgetChooser {

    private static final String ID_ADD_WIDGET_XPATH = "//div[@class='add-widget']";
    private static final String BUTTON_ADD_CSS = " .oss-button__inner--primary";
    private static final String CANCEL_BUTTON_XPATH = "./a[contains(@class,'btn-flat')]";
    private static final String TEXT_TYPE = ".//p[text()='%s']";
    private static final String TEXT_LABEL = ".//p[text()='%s']";
    private final WebDriver driver;
    private final WebElement widgetChooserElement;
    private final WebDriverWait wait;

    private WidgetChooser(WebDriver driver, WebDriverWait wait, WebElement widgetChooser) {
        this.driver = driver;
        this.wait = wait;
        this.widgetChooserElement = widgetChooser;
    }

    public static WidgetChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitByXPath(webDriverWait, ID_ADD_WIDGET_XPATH);
        WebElement widgetChooser = driver.findElement(By.xpath(ID_ADD_WIDGET_XPATH));
        return new WidgetChooser(driver, webDriverWait, widgetChooser);
    }

    public void clickAdd() {
        this.widgetChooserElement.findElement(By.cssSelector(BUTTON_ADD_CSS)).click();
    }

    public void clickCancel() {
        this.widgetChooserElement.findElement(By.xpath(CANCEL_BUTTON_XPATH)).click();
    }

    public void addWidget(String widgetType, String widgetLabel) {
        getWidgetType(widgetType).click();
        WebElementUtils.clickWebElement(driver, getWidgetByLabel(widgetLabel));
        clickAdd();
    }

    private WebElement getWidgetByLabel(String widgetLabel) {
        DelayUtils.waitForNestedElements(wait, widgetChooserElement, By.xpath(String.format(TEXT_LABEL, widgetLabel)));
        return widgetChooserElement.findElement(By.xpath(String.format(TEXT_LABEL, widgetLabel)));
    }

    private WebElement getWidgetType(String widgetType) {
        return widgetChooserElement.findElement(By.xpath(String.format(TEXT_TYPE, widgetType)));
    }

}
