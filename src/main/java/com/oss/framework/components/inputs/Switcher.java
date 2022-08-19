package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class Switcher extends Input {

    private static final String SWITCHER_CLASS = "switcher-inner";

    private Switcher(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }

    static Switcher create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Switcher(driver, wait, webElement, componentId);
    }

    static Switcher createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Switcher(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }

    @Override
    public Data getValue() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }

    @Override
    public void setValue(Data value) {
        setSwitcherValue(value);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }


    private void setSwitcherValue(Data value) {
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if (!valueToSet.equals(isSwitched())) {
            this.webElement.findElement(By.className(SWITCHER_CLASS)).click();
        }
    }

    private boolean isSwitched() {
        String switched = this.webElement.findElement(By.xpath(".//input")).getAttribute("value");
        return switched.equals("true");
    }
}
