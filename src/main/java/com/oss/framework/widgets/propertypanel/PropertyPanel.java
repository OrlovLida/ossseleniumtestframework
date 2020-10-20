package com.oss.framework.widgets.propertypanel;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oss.framework.utils.DragAndDrop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PropertyPanel implements PropertyPanelInterface {

    public static final String PROPERTY_PANEL_CLASS = "PropertyPanel";

    private static final String PROPERTY_PATH = ".//div[contains(@class, 'propertyPanelRow')]";
    private static final String PROPERTY_NAME_PATH = ".//div[@class='propertyPanelRow-label']";
    private static final String PROPERTY_VALUE_PATH = ".//div[@class='propertyPanelRow-value']";
    private final Map<String, WebElement> properties = Maps.newHashMap();

    protected final WebDriver driver;
    protected final WebElement webElement;


    private PropertyPanel(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(PROPERTY_PANEL_CLASS));
    }

    private PropertyPanel(WebDriver driver, String id) {
        this.driver = driver;
        this.webElement = driver.findElement(By.xpath("//*[@data-attributename = '" + id + "']"));
    }

    public static PropertyPanel create(WebDriver driver) {
        return new PropertyPanel(driver);
    }

    public static PropertyPanel createById(WebDriver driver, String id) {
        return new PropertyPanel(driver, id);
    }

    private List<WebElement> getProperties() {
        return this.webElement.findElements(By.xpath(PROPERTY_PATH));
    }

    public List<String> getPropertyLabels() {
        List<String> labels = new ArrayList<String>();
        for (WebElement element : this.webElement.findElements(By.xpath(PROPERTY_NAME_PATH))) {
            labels.add(element.getText());
        }
        return labels;
    }

    public String getNthPropertyLabel(int n) {
        List<String> propertyLabels = getPropertyLabels();
        return propertyLabels.get(n - 1);
    }

    private Map<String, WebElement> getPropertiesMap() {
        for (WebElement element : getProperties()) {
            properties.put(element.getAttribute("id"), element);
        }
        return properties;
    }

    private WebElement getPropertyById(String id) {
        return this.webElement.findElement(By.id(id));
    }

    public void changePropertyOrder(String id, int position) {
        DragAndDrop.dragAndDrop(getPropertyById(id).findElement(By.xpath(".//div[@class = 'btn-drag']")),
                this.webElement.findElements(By.xpath(PROPERTY_NAME_PATH)).get(position), driver);
    }

    @Override
    public String getPropertyValue(String propertyName) {
        getPropertiesMap();
        return properties.get(propertyName).findElement(By.xpath(PROPERTY_VALUE_PATH)).getText();
    }
}
