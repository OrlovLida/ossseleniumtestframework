package com.oss.framework.bpm.bpmpropertypanel;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class BpmPropertyPanel {

    private static final String PROPERTY_CSS = ".row";
    private static final String BPM_PROPERTY_PANEL_CSS = ".propertyPanel";
    private static final String ITEM_LABEL_CSS = ".item-label";
    private static final String ITEM_VALUE_CSS = ".item-value";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";

    private final WebElement propertyPanel;

    private BpmPropertyPanel(WebElement propertyPanel) {
        this.propertyPanel = propertyPanel;
    }

    public static BpmPropertyPanel createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitBy(wait, By.cssSelector(BPM_PROPERTY_PANEL_CSS));
        WebElement propertyPanel = driver.findElement(By.cssSelector(BPM_PROPERTY_PANEL_CSS + "[" + CSSUtils.TEST_ID + "='" + id + "']"));
        return new BpmPropertyPanel(propertyPanel);
    }

    public String getPropertyValue(String propertyName) {
        Map<String, WebElement> properties = getPropertiesMap();
        if (!properties.get(propertyName).findElements(By.cssSelector(ITEM_VALUE_CSS)).isEmpty()) {
            return properties.get(propertyName).findElement(By.cssSelector(ITEM_VALUE_CSS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        } else {
            return "";
        }
    }

    private Map<String, WebElement> getPropertiesMap() {
        Map<String, WebElement> properties = Maps.newHashMap();
        for (WebElement element : getProperties()) {
            properties.put(element.findElement(By.cssSelector(ITEM_LABEL_CSS)).getAttribute(TEXT_CONTENT_ATTRIBUTE), element);
        }
        return properties;
    }

    private List<WebElement> getProperties() {
        return this.propertyPanel.findElements(By.cssSelector(PROPERTY_CSS));
    }
}
