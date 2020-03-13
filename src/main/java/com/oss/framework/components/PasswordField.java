package com.oss.framework.components;

import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PasswordField extends Input {

    static PasswordField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new PasswordField(driver, wait, componentId);
    }

    static PasswordField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new PasswordField(parent, driver, wait, componentId);
    }

    private PasswordField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private PasswordField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getAttribute("value"));
    }

    @Override
    public void clear() {
        webElement.findElement(By.xpath(".//input")).clear();
    }
}
