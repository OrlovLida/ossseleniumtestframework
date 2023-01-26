/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Gabriela Zaranek
 */
public class SearchBox extends Input {
    
    private static final String INPUT_CSS = "input";
    private static final String DROPDOWN_CSS = ".ExtendedSearchResults.open";
    private static final String DATA_VALUE = "data-value";
    
    SearchBox(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }
    
    public static SearchBox create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchBox(driver, wait, webElement, componentId);
    }
    
    public static SearchBox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchBox(driver, wait, webElement, componentId);
    }
    
    @Override
    public void setValueContains(Data value) {
        typeValue(value.getStringValue());
        if (isDropdownPresent()) {
            getDropdown().selectOptionContains(value.getStringValue());
        }
    }
    
    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.cssSelector(INPUT_CSS)).getAttribute(DATA_VALUE));
    }
    
    @Override
    public void setValue(Data value) {
        typeValue(value.getStringValue());
        if (isDropdownPresent()) {
            getDropdown().selectOptionByTitle(value.getStringValue());
        }
    }
    
    @Override
    public void clear() {
        DelayUtils.waitForClickability(webDriverWait, webElement);
        WebElement input = webElement.findElement(By.cssSelector(INPUT_CSS));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
    
    public void typeValue(String value) {
        clear();
        webElement.click();
        webElement.findElement(By.cssSelector(INPUT_CSS)).sendKeys(value);
        Actions action = new Actions(driver);
        action.moveToElement(webElement)
                .click()
                .sendKeys(Keys.ENTER)
                .perform();
        DelayUtils.waitForSpinners(webDriverWait, webElement);
    }
    
    private boolean isDropdownPresent() {
        return WebElementUtils.isElementPresent(driver, By.cssSelector(DROPDOWN_CSS));
    }
    
    private DropdownList getDropdown() {
        return DropdownList.create(driver, webDriverWait);
    }
}
