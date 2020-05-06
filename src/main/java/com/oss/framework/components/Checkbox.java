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

    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        return null;
    }

    @Override
    public void clear() {

    }

    public By getCheckbox(String checkbox1){
        return By.xpath("//div[@class='checkbox-cont checkbox']//label[starts-with(@for,'"+checkbox1+"') and //input[@type='checkbox']]");
    }
}

