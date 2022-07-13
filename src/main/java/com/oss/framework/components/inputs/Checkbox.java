package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class Checkbox extends Input {
    
    private static final String CHECKBOX_CSS = ".checkbox-cont,.oss-checkbox-content";
    
    private Checkbox(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }
    
    static Checkbox create(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Checkbox(driver, webDriverWait, webElement, componentId);
    }
    
    static Checkbox createFromParent(WebElement parent, WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Checkbox(driver, webDriverWait, webElement, componentId);
    }
    
    @Override
    public void setValueContains(Data value) {
        setCheckBoxValue(value);
    }
    
    @Override
    public Data getValue() {
        String value = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return Data.createSingleData(value);
    }
    
    @Override
    public void setValue(Data value) {
        setCheckBoxValue(value);
    }
    
    @Override
    public void clear() {
        setCheckBoxValue(Data.createSingleData("false"));
    }
    
    private void setCheckBoxValue(Data value) {
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if (!valueToSet.equals(isChecked())) {
            this.webElement.findElement(By.cssSelector(CHECKBOX_CSS)).click();
        }
    }
    
    private boolean isChecked() {
        return this.webElement.findElement(By.xpath(".//input")).isSelected();
    }
}
