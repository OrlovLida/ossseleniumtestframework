package com.oss.framework.components;

import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MultiSearchField extends Input {

    static MultiSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(driver, wait, componentId);
    }

    static MultiSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(parent, driver, wait, componentId);
    }

    private MultiSearchField(WebDriver driver, WebDriverWait wait, String componentId) { super(driver, wait, componentId); }
    private MultiSearchField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId); }

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