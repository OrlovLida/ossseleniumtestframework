package com.oss.framework.widgets.propertypanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.Widget;

public class OldPropertyPanel extends Widget implements PropertyPanelInterface {
    
    private static final String PROPERTY_NAME_PATH = ".//li[@class='row']//div[@class='item-label']//span[text()='%s']";
    private static final String PROPERTY_VALUE_PATH = "./ancestor::li[@class='row']//div[@class='item-value']";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView(true);";
    private static final String PROPERTY_ATTRIBUTES_NAME_CSS = ".Col_PropertyName";
    private static final String PROPERTY_VALUES_CSS = ".Col_PropertyValue";
    private static final String CELL_CSS = ".Cell";
    
    private OldPropertyPanel(WebDriver driver, WebDriverWait wait, String widgetId) {
        super(driver, wait, widgetId);
    }
    
    public static OldPropertyPanel createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new OldPropertyPanel(driver, wait, widgetId);
    }
    
    @Override
    public String getPropertyValue(String propertyName) {
        WebElement propertyNameWebElement = this.webElement.findElement(By.xpath(String.format(PROPERTY_NAME_PATH, propertyName)));
        ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, propertyNameWebElement);
        WebElement propertyValueElement = propertyNameWebElement.findElement(By.xpath(PROPERTY_VALUE_PATH));
        return propertyValueElement.getText();
    }
    
    public Map<String, String> getPropertyNamesToValues() {
        Map<String, String> properties = new HashMap<>();
        List<String> propertyNames = getPropertyNames();
        propertyNames.forEach(attribute -> properties.put(attribute, getPropertyValues().get(propertyNames.indexOf(attribute))));
        return properties;
    }
    
    private List<String> getPropertyNames() {
        WebElement attributes = webElement.findElement(By.cssSelector(PROPERTY_ATTRIBUTES_NAME_CSS));
        return getCellValue(attributes);
    }
    
    private List<String> getPropertyValues() {
        WebElement values = webElement.findElement(By.cssSelector(PROPERTY_VALUES_CSS));
        return getCellValue(values);
    }
    
    private List<String> getCellValue(WebElement parent) {
        return parent.findElements(By.cssSelector(CELL_CSS)).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
    
}
