package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class TextField extends Input {

    private static final String INPUT = ".//input";
    private static final String ASTERISK_XPATH = ".//span[@class='asterisk']";

    private TextField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static TextField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new TextField(driver, wait, webElement, componentId);
    }

    static TextField create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new TextField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.clear();
        WebElementUtils.clickWebElement(driver, input);
        webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        Actions action = new Actions(driver);
        action.moveToElement(input).build().perform();
        clear();
        DelayUtils.waitForClickability(webDriverWait, input);
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    public boolean isMandatory() {
        return !webElement.findElements(By.xpath(ASTERISK_XPATH)).isEmpty();
    }
}
