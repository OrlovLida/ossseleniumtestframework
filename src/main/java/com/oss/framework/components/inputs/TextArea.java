package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;

public class TextArea extends Input {

    private static final String TEXT_AREA = ".//textarea";

    static TextArea create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextArea(driver, wait, componentId);
    }

    static TextArea createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new TextArea(parent, driver, wait, componentId);
    }

    private TextArea(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private TextArea(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(TEXT_AREA));
        clear();
        input.sendKeys(value.getStringValue());
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
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(TEXT_AREA));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public MouseCursor cursor() {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.xpath(TEXT_AREA)).getCssValue("cursor");
        return getMouseCursor(cursor);
    }
}
