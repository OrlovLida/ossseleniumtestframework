package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;

public class Switcher extends Input {

    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet";

    private Switcher(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(driver, webDriverWait, componentId);
    }

    private Switcher(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(parent, driver, webDriverWait, componentId);
    }

    static Switcher create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Switcher(driver, wait, componentId);
    }

    static Switcher createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Switcher(parent, driver, wait, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public Data getValue() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void setValue(Data value) {
        setSwitcherValue(value);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    private void setSwitcherValue(Data value) {
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if (!valueToSet.equals(isSwitched())) {
            this.webElement.findElement(By.className("switcher-inner")).click();
        }
    }

    private boolean isSwitched() {
        String switched = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return switched.equals("true");
    }
}
