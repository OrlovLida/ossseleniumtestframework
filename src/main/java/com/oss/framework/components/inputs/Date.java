package com.oss.framework.components.inputs;

import com.oss.framework.components.portals.DatePicker;
import com.oss.framework.data.Data;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Date extends Input {

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

        return Data.createSingleData(this.webElement.findElement(By.xpath(".//input"))
                .getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = this.webElement.findElement(By.xpath(".//input"));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public void setValue(Data value) {
        clear();
        this.webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        this.webElement.findElement(By.xpath(".//input")).click();
        this.webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
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