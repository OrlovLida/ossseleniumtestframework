package com.oss.framework.components;

import com.oss.framework.components.portals.DatePicker;
import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Date extends Input {
    private final WebElement input = this.webElement.findElement(By.xpath(".//input"));

    static Date create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Date(driver, wait, componentId);
    }

    static Date createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Date(parent, driver, wait, componentId);
    }

    private Date(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Date(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(this.input
                .getAttribute("value"));
    }

    @Override
    public void clear() {
        this.input.clear();
    }

    @Override
    public void setValue(Data value) {
        this.input.click();
        this.input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        this.input.click();
        this.input.sendKeys(value.getStringValue());
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//label")).getText();
    }

    public void chooseDate(String date) {
        DatePicker datePicker = DatePicker.create(webDriverWait, webElement);
        datePicker.chooseDate(date);
    }
}