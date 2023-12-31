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

public class SearchBoxV2 extends Input {

    private static final String INPUT_XPATH = ".//input";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String SPAN_TAG = "span";

    private SearchBoxV2(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static SearchBoxV2 create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchBoxV2(driver, wait, webElement, componentId);
    }

    static SearchBoxV2 createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchBoxV2(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        typeValue(value);
        DropdownList.create(driver, webDriverWait).selectOptionByDataValueContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT_XPATH)).getAttribute(VALUE_ATTRIBUTE));
    }

    @Override
    public void setValue(Data value) {
        typeValue(value);
        DropdownList.create(driver, webDriverWait).selectOptionByDataValue(value.getStringValue());

    }

    @Override
    public void clear() {
        DelayUtils.waitForElementToLoad(webDriverWait, webElement);
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.tagName(SPAN_TAG)).getText();
    }

    private void typeValue(Data value) {
        DelayUtils.waitForClickability(webDriverWait, webElement);
        webElement.click();
        clear();
        webElement.findElement(By.xpath(INPUT_XPATH)).sendKeys(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
    }

}
