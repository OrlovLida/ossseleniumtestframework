package com.oss.framework.components.inputs;


import com.oss.framework.components.portals.ComponentMessages;
import com.oss.framework.components.portals.Tooltip;
import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class Input {

    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final WebElement webElement;

    public enum ComponentType {
        TEXT_FIELD, TEXT_AREA, PASSWORD_FIELD, NUMBER_FIELD,
        DATE_TIME_RANGE, DATE_TIME, TIME, CHECKBOX, DATE,
        SWITCHER, SEARCH_FIELD, MULTI_SEARCH_FIELD, COMBOBOX, COMBOBOXV2,
        MULTI_COMBOBOX, FILE_CHOOSER, COORDINATES, PHONE_FIELD,
        RADIO_BUTTON
    }

    static String createComponentPath(String componentId) {
        return "//*[@"+ CSSUtils.TEST_ID +"='" + componentId + "']";
    }

    Input(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = driver.findElement(By.xpath(createComponentPath(componentId)));
    }

    Input(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = parent.findElement(By.xpath(createComponentPath(componentId)));
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
        action.moveToElement(webElement.findElement(By.xpath(".//label"))).build().perform();
        return webElement.findElement(By.xpath(".//label")).getCssValue("cursor");
    }

    public final List<String> getHint() {
        WebElement hint = this.webElement.findElement(By.xpath(".//span[contains(@class,'form-hint-tooltip')]"));
        hint.click();
        Tooltip tooltip = new Tooltip(this.driver);
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
        WebElement label = webElement.findElement(By.xpath(".//label"));
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
