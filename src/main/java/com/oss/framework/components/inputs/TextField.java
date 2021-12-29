package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class TextField extends Input {

    private static final String INPUT = ".//input";

    private TextField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private TextField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static TextField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextField(driver, wait, componentId);
    }

    static TextField create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextField(parent, driver, wait, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.clear();
        Actions action = new Actions(driver);
        action.moveToElement(input).click().build().perform(); //before click element is not reachable by keyboard and after the click element is refreshed
        webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        Actions action = new Actions(driver);
        action.moveToElement(input).build().perform();
        clear();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    public boolean isMandatory() {
        return !webElement.findElements(By.xpath(".//span[@class='asterisk']")).isEmpty();
    }
}
