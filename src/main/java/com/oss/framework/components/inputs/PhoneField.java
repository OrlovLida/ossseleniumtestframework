package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;

public class PhoneField extends Input {

    private static final String INPUT = ".//input";

    private PhoneField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private PhoneField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static PhoneField create(WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        return new PhoneField(driver, wait, phoneFieldId);
    }

    static PhoneField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        return new PhoneField(parent, driver, wait, phoneFieldId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        setValueContains(value);
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
