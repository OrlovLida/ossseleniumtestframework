package com.oss.framework.widgets.propertypanel;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PropertyPanel {

    public static final String PROPERTY_PANEL_CLASS = "PropertyPanel";

    private static final String PROPERTY_PATH = ".//div[@class='propertyPanelRow']";
    private static final String PROPERTY_NAME_PATH = ".//div[@class='propertyPanelRow-label']";
    private static final String PROPERTY_VALUE_PATH = ".//div[@class='propertyPanelRow-value']";
    private final Map<String, WebElement> properties = Maps.newHashMap();

    protected final WebDriver driver;
    protected final WebElement webElement;


    public PropertyPanel(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(PROPERTY_PANEL_CLASS));
    }

    public static PropertyPanel create(WebDriver driver) {
        return new PropertyPanel(driver);
    }

    public List<WebElement> getProperties(){return this.webElement.findElements(By.xpath(PROPERTY_PATH));}

    public List<String> getPropertyLabels() {
        List<String> labels = new ArrayList<String>();
        for(WebElement element : this.webElement.findElements(By.xpath(PROPERTY_NAME_PATH))) {
            labels.add(element.getText());
        }
        return labels;
    }

    public String getNthPropertyLabel(int n) {
        List<String> propertyLabels = getPropertyLabels();
        return propertyLabels.get(n-1);
    }

    public Map<String, WebElement> getPropertiesMap() {
        for(WebElement element : getProperties()){
            properties.put(element.getAttribute("id"),element);
        }
        return properties;
    }

    public String getPropertyValue(String propertyName) {
       getPropertiesMap();
       return properties.get(propertyName).findElement(By.xpath(PROPERTY_VALUE_PATH)).getText();
    }
}
