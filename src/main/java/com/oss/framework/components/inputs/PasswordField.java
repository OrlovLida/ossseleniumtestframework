package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class PasswordField extends Input {

    private static final String INPUT = ".//input";

    private PasswordField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static PasswordField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PasswordField(driver, wait, webElement, componentId);
    }

    static PasswordField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PasswordField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        clear();
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
