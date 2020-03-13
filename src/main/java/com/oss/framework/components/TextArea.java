package com.oss.framework.components;

import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TextArea extends Input {


    static TextArea create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextArea(driver, wait, componentId);
    }

    static TextArea createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextArea(parent, driver, wait, componentId);
    }

    private TextArea(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private TextArea(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//textarea"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(".//textarea"));
        return Data.createSingleData(input.getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(".//textarea"));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
