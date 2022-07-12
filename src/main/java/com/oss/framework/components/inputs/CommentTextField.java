package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class CommentTextField extends Input {

    private static final String TEXT_FIELD_XPATH = ".//ancestor::div[@class='TextField clearfix']";
    private static final String ACCEPT_BUTTON_CSS = ".fa-check";

    private CommentTextField(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }

    static CommentTextField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new CommentTextField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.getText());
    }

    @Override
    public void setValue(Data value) {
        clear();
        webElement.sendKeys(value.getStringValue());
        acceptInputValue();
    }

    @Override
    public void clear() {
        WebElementUtils.clickWebElement(driver, webElement);
        webElement.sendKeys(Keys.CONTROL + "a");
        webElement.sendKeys(Keys.DELETE);
    }

    private WebElement textField() {
        return webElement.findElement(By.xpath(TEXT_FIELD_XPATH));
    }

    private void acceptInputValue() {
        WebElementUtils.clickWebElement(driver, textField().findElement(By.cssSelector(ACCEPT_BUTTON_CSS)));
    }
}
