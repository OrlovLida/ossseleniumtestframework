package com.oss.framework.widgets.propertypanel;

import com.google.common.collect.Maps;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OldPropertyPanel implements PropertyPanelInterface{

    public static final String PROPERTY_PANEL_CLASS = "propertyPanel";

    private static final String PROPERTY_PATH = ".//li[@class='row']";
    private static final String PROPERTY_VALUE_PATH = ".//div[@class='item-value']";
    private static final String PROPERTY_PANEL_PATH = "//div[@class='propertyPanel']";
    private final Map<String, String> properties = Maps.newHashMap();
    private static final String ITEM_NAME = ".//div[@class='item-label']";
    private static final String PROPERTY_NAME = ".//div[contains(@class,'OSSRichText')]";


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


    private List<WebElement> getProperties(){
        DelayUtils.waitByXPath(wait, PROPERTY_PATH);
        return this.webElement.findElements(By.xpath(PROPERTY_PATH));}

    @Override
    public String getPropertyValue(String propertyName) {
       getPropertiesMap();
       return properties.get(propertyName);
    }

    private void getPropertiesMap() {
        for(WebElement element : getProperties()){
            WebElement itemName = element.findElement(By.xpath(ITEM_NAME));
            WebElement propertyElement = itemName.findElement(By.xpath(PROPERTY_NAME));
            String propertyName = propertyElement.getText();
            WebElement itemValue = element.findElement(By.xpath(PROPERTY_VALUE_PATH));
            WebElement propertyElement2 = itemValue.findElement(By.xpath(PROPERTY_NAME));
            String propertyValue = propertyElement2.getText();
            properties.put(propertyName,propertyValue);
        }
    }
}
