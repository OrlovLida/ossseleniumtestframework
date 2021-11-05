package com.oss.framework.components.inputs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.ComponentMessages;
import com.oss.framework.components.portals.Tooltip;
import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public abstract class Input {

    private static final String LABEL = ".//label";
    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final WebElement webElement;
    protected final String componentId;

    public enum ComponentType {
        TEXT_FIELD, TEXT_AREA, PASSWORD_FIELD, NUMBER_FIELD,
        DATE_TIME_RANGE, DATE_TIME, TIME, CHECKBOX, DATE,
        SWITCHER, SEARCH_FIELD, MULTI_SEARCH_FIELD, COMBOBOX,
        MULTI_COMBOBOX, FILE_CHOOSER, COORDINATES, PHONE_FIELD,
        RADIO_BUTTON, SCRIPT_COMPONENT, BPM_COMBOBOX, OBJECT_SEARCH_FIELD
    }

    static String createComponentPath(String componentId) {
        return "//*[@" + CSSUtils.TEST_ID + "='" + componentId + "']";
    }

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

    public final void hover() {
        Actions action = new Actions(this.driver);
        action.moveToElement(webElement).build().perform();
    }

    public final void click() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).click().build().perform();
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

    public String cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        return webElement.findElement(By.xpath(".//input")).getCssValue("cursor");
    }

    public final List<String> getHint() {
        Tooltip tooltip = Tooltip.create(driver, webDriverWait, componentId);
        return tooltip.getMessages();
    }

    public final List<String> getMessages() {
        ComponentMessages messages = new ComponentMessages(this.webElement);
        return messages.getMessages();
    }

    public abstract void setValue(Data value);

    public abstract void setValueContains(Data value);

    public abstract Data getValue();

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

}
