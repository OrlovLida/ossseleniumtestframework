package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class NumberField extends Input {
    
    private static final String INPUT = ".//input";
    
    private NumberField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }
    
    static NumberField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new NumberField(driver, wait, webElement, componentId);
    }
    
    static NumberField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new NumberField(driver, wait, webElement, componentId);
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
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        clear();
        input.sendKeys(value.getStringValue());
    }
    
    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
