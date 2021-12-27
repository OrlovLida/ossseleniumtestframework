package com.oss.framework.components.inputs;

import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhoneField extends Input {

    static PhoneField create(WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        return new PhoneField(driver, wait, phoneFieldId);
    }

    static PhoneField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        return new PhoneField(parent, driver, wait, phoneFieldId);
    }

    private PhoneField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private PhoneField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
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
        //webElement.findElement(By.xpath(".//input")).clear();
    }


}
