package com.oss.framework.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;

public class Checkbox extends Input {

    static Checkbox create(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        return new Checkbox(driver, webDriverWait, componentId);
    }

    static Checkbox createFromParent(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        return new Checkbox(parent, driver, webDriverWait, componentId);
    }

    private Checkbox(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(driver, webDriverWait, componentId);
    }

    private Checkbox(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(parent, driver, webDriverWait, componentId);
    }

    @Override
    public void setValue(Data value) {
        setCheckBoxValue(value);
    }

    @Override
    public void setValueContains(Data value) {
        setCheckBoxValue(value);
    }

    private void setCheckBoxValue(Data value) {
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if(valueToSet && !isChecked()) {
            this.webElement.findElement(By.className("checkbox-cont")).click();
        }
        if(!valueToSet && isChecked()) {
            this.webElement.findElement(By.className("checkbox-cont")).click();
        }
    }

    @Override
    public Data getValue() {
        String value = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return Data.createSingleData(value);
    }

    private boolean isChecked() {
        String checked = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return checked.equals("true");
    }

    @Override
    public void clear() {
        setCheckBoxValue(Data.createSingleData("false"));
    }
}

