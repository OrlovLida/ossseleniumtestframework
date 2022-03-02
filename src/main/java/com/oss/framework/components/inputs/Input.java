package com.oss.framework.components.inputs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.alerts.InputMessage;
import com.oss.framework.components.data.Data;
import com.oss.framework.components.tooltip.Tooltip;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public abstract class Input {
    
    static final String DEFAULT = "default";
    static final String TEXT = "text";
    static final String POINTER = "pointer";
    static final String NOT_ALLOWED = "not-allowed";
    static final String CANNOT_FIND_MOUSE_COURSE_EXCEPTION = "Cannot find mouse course for your input";
    private static final String LABEL = ".//label";
    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final WebElement webElement;
    protected final String componentId;
    
    Input(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = webElement;
        this.componentId = null;
    }
    
    Input(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = driver.findElement(By.xpath(createComponentPath(componentId)));
        this.componentId = componentId;
    }
    
    Input(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = parent.findElement(By.xpath("." + createComponentPath(componentId)));
        this.componentId = componentId;
    }
    
    static String createComponentPath(String componentId) {
        return "//*[@" + CSSUtils.TEST_ID + "='" + componentId + "']";
    }
    
    public final void hover() {
        Actions action = new Actions(this.driver);
        action.moveToElement(webElement).build().perform();
    }
    
    public final void click() {
        WebElementUtils.clickWebElement(driver, webElement);
        DelayUtils.sleep();
    }
    
    public final void doubleClick() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).doubleClick().perform();
        DelayUtils.sleep();
    }
    
    public final void clearByAction() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).doubleClick().sendKeys(Keys.DELETE).perform();
        DelayUtils.sleep();
    }
    
    public MouseCursor cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.xpath(".//input")).getCssValue("cursor");
        return getMouseCursor(cursor);
        
    }
    
    public final List<String> getHint() {
        Tooltip tooltip = Tooltip.create(driver, webDriverWait, componentId);
        return tooltip.getMessages();
    }
    
    public final List<String> getMessages() {
        InputMessage messages = new InputMessage(this.webElement);
        return messages.getMessages();
    }
    
    public abstract void setValueContains(Data value);
    
    public abstract Data getValue();
    
    public abstract void setValue(Data value);
    
    public abstract void clear();
    
    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(LABEL));
        return label.getText();
    }
    
    public final void setMultiStringValue(List<String> values) {
        this.setValue(Data.createMultiData(values));
    }
    
    public final void setMultiStringValueContains(List<String> values) {
        this.setValueContains(Data.createMultiData(values));
    }
    
    public final void setSingleStringValue(String value) {
        this.setValue(Data.createSingleData(value));
    }
    
    public final void setSingleStringValueContains(String value) {
        this.setValueContains(Data.createSingleData(value));
    }
    
    public final String getStringValue() {
        return getValue().getStringValue();
    }
    
    public final List<String> getStringValues() {
        return getValue().getStringValues();
    }
    
    MouseCursor getMouseCursor(String cursor) {
        switch (cursor) {
        case DEFAULT: {
            return MouseCursor.DEFAULT;
        }
        case TEXT: {
            return MouseCursor.TEXT;
        }
        case POINTER: {
            return MouseCursor.POINTER;
        }
        case NOT_ALLOWED: {
            return MouseCursor.NOT_ALLOWED;
        }
        default:
        }
        throw new IllegalArgumentException(CANNOT_FIND_MOUSE_COURSE_EXCEPTION);
    }
    
    public enum ComponentType {
        TEXT_FIELD, TEXT_AREA, PASSWORD_FIELD, NUMBER_FIELD,
        DATE_TIME_RANGE, DATE_TIME, TIME, CHECKBOX, DATE,
        SWITCHER, SEARCH_FIELD, MULTI_SEARCH_FIELD, COMBOBOX,
        MULTI_COMBOBOX, FILE_CHOOSER, COORDINATES, PHONE_FIELD,
        RADIO_BUTTON, SCRIPT_COMPONENT, BPM_COMBOBOX, OBJECT_SEARCH_FIELD, SEARCH_BOX
    }
    
    public enum MouseCursor {
        NOT_ALLOWED, DEFAULT, TEXT, POINTER
    }
    
}
