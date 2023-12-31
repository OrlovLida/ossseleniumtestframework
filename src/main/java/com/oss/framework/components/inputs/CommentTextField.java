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

    private static final String TEXT_FIELD_XPATH = ".//div[starts-with(@class, 'textFieldCont')]";
    private static final String ACCEPT_BUTTON_CSS = ".fa-check";
    private static final String LINK_XPATH_PATTERN = ".//a[@href and text()='%s']";

    private CommentTextField(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }

    public static CommentTextField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new CommentTextField(driver, wait, webElement, componentId);
    }

    public static CommentTextField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new CommentTextField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(textField().getText());
    }

    @Override
    public void setValue(Data value) {
        clear();
        textField().sendKeys(value.getStringValue());
        acceptInputValue();
    }

    @Override
    public void clear() {
        WebElementUtils.clickWebElement(driver, textField());
        textField().sendKeys(Keys.CONTROL + "a");
        textField().sendKeys(Keys.DELETE);
    }

    private WebElement textField() {
        return webElement.findElement(By.xpath(TEXT_FIELD_XPATH));
    }

    private void acceptInputValue() {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(ACCEPT_BUTTON_CSS)));
    }

    public void clickLink(String linkText) {
        webElement.findElement(By.xpath(String.format(LINK_XPATH_PATTERN, linkText))).click();
    }
}
