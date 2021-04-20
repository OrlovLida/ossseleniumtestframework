package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;

public class TextField extends Input {

    static TextField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextField(driver, wait, componentId);
    }

    static TextField create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextField(parent, driver, wait, componentId);
    }

    private TextField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private TextField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        Actions action = new Actions(driver);
        action.moveToElement(input).build().perform();
        clear();
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.clear();
        input.click(); //before click element is not reachable by keyboard and after the click element is refreshed
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
