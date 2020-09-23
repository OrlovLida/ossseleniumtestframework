package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;

public class Switcher extends Input {

    static Switcher create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Switcher(driver, wait, componentId);
    }

    static Switcher createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Switcher(parent, driver, wait, componentId);
    }

    private Switcher(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(driver, webDriverWait, componentId);
    }

    private Switcher(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(parent, driver, webDriverWait, componentId);
    }

    @Override
    public void setValue(Data value) {
        setSwitcherValue(value);
    }

    private void setSwitcherValue(Data value){
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if(!valueToSet.equals(isSwitched())) {
            this.webElement.findElement(By.className("switcher-inner")).click();
        }
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        return null;
    }


    private boolean isSwitched() {
        String switched = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return switched.equals("true");
    }

    @Override
    public void clear() {

    }

    @Override
    public String getLabel() {
        return "";
    }

}
