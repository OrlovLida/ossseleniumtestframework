package com.oss.framework.components.inputs;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class Coordinates extends Input {
    
    private static final String VALUE = "value";
    private final WebElement labelN = webElement.findElement(By.xpath(".//label[contains(@for,'-N')]"));
    private final WebElement inputN = webElement.findElement(By.xpath(".//input[contains(@id,'-N')]"));
    private final WebElement labelS = webElement.findElement(By.xpath(".//label[contains(@for,'-S')]"));
    private final WebElement inputS = webElement.findElement(By.xpath(".//input[contains(@id,'-S')]"));
    private final WebElement inputDegrees = webElement.findElement(By.xpath(".//input[@name='degrees']"));
    private final WebElement inputMinutes = webElement.findElement(By.xpath(".//input[@name='minutes']"));
    private final WebElement inputSeconds = webElement.findElement(By.xpath(".//input[@name='seconds']"));
    
    private Coordinates(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }
    
    static Coordinates create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Coordinates(driver, wait, webElement, componentId);
    }
    
    static Coordinates create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Coordinates(driver, wait, webElement, componentId);
    }
    
    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Data getValue() {
        List<String> getList = new ArrayList<>();
        
        if (inputN.isSelected()) {
            getList.add(labelN.getText());
        } else if (inputS.isSelected()) {
            getList.add(labelS.getText());
        }
        
        getList.add(inputDegrees.getAttribute(VALUE));
        getList.add(inputMinutes.getAttribute(VALUE));
        getList.add(inputSeconds.getAttribute(VALUE));
        return Data.createMultiData(getList);
    }
    
    @Override
    public void setValue(Data value) {
        WebElementUtils.clickWebElement(driver, webElement);
        if (value.getStringValues().get(0).equals("N")) {
            labelN.click();
        } else if (value.getStringValues().get(0).equals("S")) {
            labelS.click();
        }
        clear();
        inputDegrees.sendKeys(value.getStringValues().get(1));
        inputMinutes.sendKeys(value.getStringValues().get(2));
        inputSeconds.sendKeys(value.getStringValues().get(3));
    }
    
    @Override
    public void clear() {
        inputDegrees.sendKeys(Keys.CONTROL + "a");
        inputDegrees.sendKeys(Keys.DELETE);
        inputMinutes.sendKeys(Keys.CONTROL + "a");
        inputMinutes.sendKeys(Keys.DELETE);
        inputSeconds.sendKeys(Keys.CONTROL + "a");
        inputSeconds.sendKeys(Keys.DELETE);
    }
    
    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//label")).getText();
    }
    
}
