package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class ScriptComponent extends Input {

    private static final String XPATH = ".//div[@class='CodeMirror-code']";

    private ScriptComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }

    static ScriptComponent create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ScriptComponent(driver, wait, webElement, componentId);
    }

    static ScriptComponent create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ScriptComponent(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(XPATH))
                .getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        clear();
        WebElement input = webElement.findElement(By.xpath(XPATH));
        Actions action = new Actions(driver);
        action.moveToElement(input).click()
                .sendKeys(value.getStringValue())
                .perform();
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(XPATH));
        Actions action = new Actions(driver);
        action.moveToElement(input)
                .click()
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .build()
                .perform();

    }
}
