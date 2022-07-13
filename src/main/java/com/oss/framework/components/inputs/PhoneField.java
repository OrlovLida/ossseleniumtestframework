package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class PhoneField extends Input {

    private static final String INPUT = ".//input";

    private PhoneField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static PhoneField create(WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(phoneFieldId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PhoneField(driver, wait, webElement, phoneFieldId);
    }

    static PhoneField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(phoneFieldId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PhoneField(driver, wait, webElement, phoneFieldId);
    }

    @Override
    public void setValueContains(Data value) {
        setValue(value);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
