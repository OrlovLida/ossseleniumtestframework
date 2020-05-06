package com.oss.framework.components;

import com.oss.framework.components.portals.TimePicker;
import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Time extends Input {

    static Time create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Time(driver, wait, componentId);
    }

    static Time create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Time(parent, driver, wait, componentId);
    }

    private Time(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Time(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement
                .findElement(By.xpath(".//input"))
                .getAttribute("value"));
    }

    @Override
    public void clear() {
        webElement.findElement(By.xpath(".//input")).clear();
    }

    @Override
    public void setValue(Data value) {
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
    }

    @Override
    public String getLabel() {
        return webElement.getText();
    }

    public void chooseTime(String value) {
        TimePicker timePicker = TimePicker.create(driver, webElement);
        timePicker.chooseTime(value);
    }
}