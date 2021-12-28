package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;

public class NumberField extends Input {

    private static final String INPUT = ".//input";

    static NumberField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new NumberField(driver, wait, componentId);
    }

    static NumberField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new NumberField(parent, driver, wait, componentId);
    }

    private NumberField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private NumberField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        clear();
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        return Data.createSingleData(input.getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
