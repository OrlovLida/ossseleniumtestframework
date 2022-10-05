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
    private static final String TEXT_CONTENT = "textContent";
    private static final String SWITCHER_OFF_TEXT = ".switcher-offText";
    private static final String SWITCHER_ON_TEXT = ".switcher-onText";
    private static final String INPUT_XPATH = ".//input";
    private static final String VALUE = "value";

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
        return Data.createSingleData(this.webElement.findElement(By.xpath(INPUT_XPATH)).getAttribute(VALUE));
    }

    @Override
    public void setValue(Data value) {
        setSwitcherValue(value);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }

    public String getOffLabel() {
        return webElement.findElement(By.cssSelector(SWITCHER_OFF_TEXT)).getAttribute(TEXT_CONTENT);
    }

    public String getOnLabel() {
        return webElement.findElement(By.cssSelector(SWITCHER_ON_TEXT)).getAttribute(TEXT_CONTENT);
    }

    private void setSwitcherValue(Data value) {
        Boolean valueToSet = Boolean.valueOf(value.getStringValue());
        if (!valueToSet.equals(isSwitched())) {
            this.webElement.findElement(By.className(SWITCHER_CLASS)).click();
        }
    }

    private boolean isSwitched() {
        String switched = this.webElement.findElement(By.xpath(INPUT_XPATH)).getAttribute(VALUE);
        return switched.equals("true");
    }
}
