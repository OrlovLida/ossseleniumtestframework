package com.oss.framework.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.data.Data;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MultiCombobox extends Input {

    static MultiCombobox create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(driver, wait, componentId);
    }

    static MultiCombobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(parent, driver, wait, componentId);
    }

    private MultiCombobox(WebDriver driver, WebDriverWait wait, String label) {
        super(driver, wait, label);
    }

    private MultiCombobox(WebElement parent, WebDriver driver, WebDriverWait wait, String label) {
        super(parent, driver, wait, label);
    }

    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
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

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//span")).getText();
    }
}