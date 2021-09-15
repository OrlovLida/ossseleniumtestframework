package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class ObjectSearchField extends Input {
    private static final String OSF_LABEL = ".//span[@class='md-input-label-text']";
    private static final String OSF_INNER_INPUT = ".//*[@class='object-input-component__multi__dropdown']//input";
    private static final String OSF_DROP_DOWN_LIST = ".//div[@class='dropdown-element__label']";
    private static final String OSF_VALUE_LIST = ".//div[@class='md-input-multi']";
    private static final String OSF_VALUE_CLEAR_BTN = ".//div[@class='md-input-multi']";
    
    static ObjectSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new ObjectSearchField(driver, wait, componentId);
    }
    
    private ObjectSearchField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }
    
    @Override
    public void setValue(Data value) {
        setValue(value, false);
    }
    
    public void setValue(Data value, boolean isContains) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        
        DelayUtils.waitByXPath(webDriverWait, ".//div[@class='dropdown-list']");
        if (!isSingleComponent()) {
            WebElement innerInput = driver.findElement(By.xpath(OSF_INNER_INPUT));
            innerInput.sendKeys(value.getStringValue());
        }
        
        DelayUtils.sleep(1500);
        
        List<WebElement> dropdownElement = driver.findElements(By.xpath(OSF_DROP_DOWN_LIST));
        dropdownElement.get(0).click();
        actions.moveToElement(webElement).click().build().perform();
    }
    
    @Override
    public void setValueContains(Data value) {
        setValue(value, true);
    }
    
    @Override
    public Data getValue() {
        if (isSingleComponent()) {
            return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getAttribute("value"));
        }
        if (!isMultiComponentEmpty()) {
            List<WebElement> values = webElement.findElements(By.xpath(OSF_VALUE_LIST + "//span//span"));
            return Data.createMultiData(values.stream().map(WebElement::getText).collect(Collectors.toList()));
        }
        return Data.createSingleData("");
    }
    
    @Override
    public void clear() {
        List<WebElement> closeButtons = webElement.findElements(By.xpath(OSF_VALUE_CLEAR_BTN));
        closeButtons.forEach(WebElement::click);
    }
    
    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(OSF_LABEL)).getText();
    }
    
    private boolean isSingleComponent() {
        return webElement.findElements(By.className("object-input-component__single__dropdown")).size() > 0;
    }
    
    private boolean isMultiComponentEmpty() {
        return webElement.findElements(By.className("md-input-empty")).size() > 0;
    }
}
