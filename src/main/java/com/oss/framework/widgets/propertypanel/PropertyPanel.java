package com.oss.framework.widgets.propertypanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Widget;

public class PropertyPanel extends Widget implements PropertyPanelInterface {

    public static final String PROPERTY_PANEL_CLASS = "PropertyPanel";

    private static final String PROPERTY_PATH = ".//div[contains(@class, 'propertyPanelRow')]";
    private static final String PROPERTY_NAME_PATH = ".//div[@class='propertyPanelRow-label']";
    private static final String PROPERTY_VALUE_PATH = ".//div[@class='propertyPanelRow-value']";
    private final Map<String, WebElement> properties = Maps.newHashMap();


    @Deprecated
    public static PropertyPanel create(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait);
    }

    @Deprecated
    public static PropertyPanel createById(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait, id);
    }

    public static PropertyPanel createById(WebDriver driver, WebDriverWait wait, String testId) {
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait, testId);
    }

    private PropertyPanel(WebDriver driver, WebDriverWait wait) {
        super(driver, PROPERTY_PANEL_CLASS, wait);
    }

    private PropertyPanel(WebDriver driver, WebDriverWait wait, String id) {
        super(driver, wait, id);
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
