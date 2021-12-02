package com.oss.framework.widgets.propertypanel;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OldPropertyPanel implements PropertyPanelInterface{

    private static final String PROPERTY_PANEL_CLASS = "propertyPanel";
    private static final String PROPERTY_PANEL_PATH = "//div[@class='propertyPanel']";
    private static final String PROPERTY_NAME_PATH = ".//li[@class='row']//div[@class='item-label']//span[text()='%s']";
    private static final String PROPERTY_VALUE_PATH = "./ancestor::li[@class='row']//div[@class='item-value']";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView(true);";


    protected final WebDriverWait wait;
    protected final WebDriver driver;
    protected final WebElement webElement;


    private OldPropertyPanel(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        DelayUtils.waitByXPath(wait, PROPERTY_PANEL_PATH);
        this.webElement = driver.findElement(By.className(PROPERTY_PANEL_CLASS));
    }

    public static OldPropertyPanel create(WebDriver driver, WebDriverWait wait) {
        return new OldPropertyPanel(driver, wait);
    }

    @Override
    public String getPropertyValue(String propertyName) {
        WebElement propertyNameWebElement = this.webElement.findElement(By.xpath(String.format(PROPERTY_NAME_PATH, propertyName)));
        ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, propertyNameWebElement);
        WebElement propertyValueElement = propertyNameWebElement.findElement(By.xpath(PROPERTY_VALUE_PATH));
        return propertyValueElement.getText();
    }
}
