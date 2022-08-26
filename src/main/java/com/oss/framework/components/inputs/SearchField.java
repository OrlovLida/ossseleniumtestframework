package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class SearchField extends Input {

    private static final String INPUT_XPATH = ".//input";

    private SearchField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static SearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchField(driver, wait, webElement, componentId);
    }

    static SearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        typeValue(value);
        DropdownList.create(driver, webDriverWait).selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT_XPATH)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        typeValue(value);
        DropdownList.create(driver, webDriverWait).selectOption(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }


    private void typeValue(Data value) {
        DelayUtils.waitForClickability(webDriverWait, webElement);
        webElement.click();
        DelayUtils.sleep();// wait for cursor
        clear();
        webElement.findElement(By.xpath(INPUT_XPATH)).sendKeys(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
    }
}
