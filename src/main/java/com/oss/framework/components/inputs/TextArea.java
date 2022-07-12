package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class TextArea extends Input {

    private static final String TEXT_AREA = ".//textarea";

    private TextArea(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
         super(driver, wait, webElement, componentId);
    }

    static TextArea create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new TextArea(driver, wait, webElement, componentId);
    }

    static TextArea createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new TextArea(driver, wait, webElement, componentId);
    }

    @Override
    public MouseCursor cursor() {
        WebElementUtils.moveToElement(driver, webElement);
        String cursor = webElement.findElement(By.xpath(TEXT_AREA)).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException("Set value contains is not supported in TextArea");
    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(TEXT_AREA));
        return Data.createSingleData(input.getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(TEXT_AREA));
        clear();
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(TEXT_AREA));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
