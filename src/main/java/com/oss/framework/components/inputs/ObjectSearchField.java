package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

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
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;

public class ObjectSearchField extends Input {
    private static final String OSF_LABEL = ".//span[@class='md-input-label-text']";
    private static final String OSF_INNER_INPUT = ".//*[@class='object-input-component__multi__dropdown']//input";
    private static final String OSF_DROP_DOWN_LIST = ".//div[@class='dropdown-element__label']";
    private static final String OSF_VALUE_LIST_CSS = ".md-input-value";
    private static final String OSF_VALUE_CLEAR_BTN = ".//div[@class='md-input-multi']";
    private static final String OSF_SINGLE = "object-input-component__single__dropdown";
    private static final String SEARCH_PLUS_ICON_XPATH = ".//button[@id='btn-as-modal']";
    private static final String INPUT = ".//input";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String OSF_NOT_DISABLED_CSS = ".md-input-cont:not(.md-input-disabled)";
    private static final String ICON_CHEVRON_UP_CSS = "[data-icon='chevron-up']";
    private static final String CURSOR = "cursor";
    private static final String INPUT_MULTI_CSS = ".md-input-multi";

    private ObjectSearchField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static ObjectSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ObjectSearchField(driver, wait, webElement, componentId);
    }

    public static ObjectSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ObjectSearchField(driver, wait, webElement, componentId);
    }

    public void setValue(Data value, boolean isContains) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.cssSelector(OSF_NOT_DISABLED_CSS));
        if (!isSingleComponent()) {
            setValueForMultiComponent(value, isContains);
        } else {
            setSingleValueWebElement(value.getStringValue(), By.xpath(INPUT), isContains);
        }
    }

    public void setFirstResult(String value) {
        if (!isSingleComponent()) {
            WebElementUtils.clickWebElement(driver, webElement);
            typeValueForMultiComponent(value, By.xpath(OSF_INNER_INPUT));
            chooseFirstResult();
            closeDropdown();
        } else {
            typeValueForSingleComponent(value, By.xpath(INPUT));
            chooseFirstResult();
        }
    }

    @Override
    public void setValueContains(Data value) {
        setValue(value, true);
    }

    @Override
    public Data getValue() {
        if (isSingleComponent()) {
            return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
        }
        if (!isMultiComponentEmpty()) {
            List<WebElement> values = webElement.findElements(By.cssSelector(OSF_VALUE_LIST_CSS));
            return Data.createMultiData(values.stream().map(WebElement::getText).collect(Collectors.toList()));
        }
        return Data.createSingleData("");
    }

    @Override
    public void setValue(Data value) {
        setValue(value, false);
    }

    @Override
    public void clear() {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.cssSelector(OSF_NOT_DISABLED_CSS));
        if (isSingleComponent()) {
            WebElement input = webElement.findElement(By.xpath(INPUT));
            input.sendKeys(Keys.CONTROL + "a");
            input.sendKeys(Keys.DELETE);
        }
        List<WebElement> closeButtons = webElement.findElements(By.xpath(OSF_VALUE_CLEAR_BTN));
        closeButtons.forEach(WebElement::click);
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(OSF_LABEL)).getText();
    }

    public AdvancedSearchWidget openAdvancedSearchWidget() {
        WebElement searchPlus = webElement.findElement(By.xpath(SEARCH_PLUS_ICON_XPATH));
        WebElementUtils.clickWithRetry(driver, searchPlus, By.className(ADVANCED_SEARCH_ID));
        return AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
    }

    @Override
    public MouseCursor cursor() {
        String cursor;
        if (isSingleComponent()) {
            cursor = webElement.findElement(By.xpath(INPUT)).getCssValue(CURSOR);
        } else {
            cursor = webElement.findElement(By.cssSelector(INPUT_MULTI_CSS)).getCssValue(CURSOR);
        }
        return getMouseCursor(cursor);
    }

    private boolean isSingleComponent() {
        return !webElement.findElements(By.className(OSF_SINGLE)).isEmpty();
    }

    private boolean isMultiComponentEmpty() {
        return !webElement.findElements(By.className("md-input-empty")).isEmpty();
    }

    private void setValueForMultiComponent(Data value, boolean isContains) {
        WebElementUtils.clickWebElement(driver, webElement);
        if (value.isList()) {
            setMultiValues(value, By.xpath(OSF_INNER_INPUT), isContains);
        } else {
            setSingleValue(value.getStringValue(), By.xpath(OSF_INNER_INPUT), isContains);
        }
        closeDropdown();
    }

    private void closeDropdown() {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(ICON_CHEVRON_UP_CSS)));//TODO change to ESC after OSSWEB-18557
    }

    private void setMultiValues(Data values, By by, boolean isContains) {
        values.getStringValues().forEach(value -> setSingleValue(value, by, isContains));
    }

    private void setSingleValue(String singleValue, By by, boolean isContains) {
        typeValueForMultiComponent(singleValue, by);
        chooseResult(singleValue, isContains);
    }

    private void typeValueForMultiComponent(String singleValue, By by) {
        DelayUtils.waitBy(webDriverWait, by);
        DelayUtils.waitForClickability(webDriverWait, driver.findElement(by));
        driver.findElement(by).sendKeys(Keys.CONTROL + "a");
        driver.findElement(by).sendKeys(Keys.DELETE);
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.cssSelector(OSF_NOT_DISABLED_CSS));
        driver.findElement(by).sendKeys(singleValue);
    }

    private void setSingleValueWebElement(String singleValue, By by, boolean isContains) {
        typeValueForSingleComponent(singleValue, by);
        chooseResult(singleValue, isContains);
    }

    private void typeValueForSingleComponent(String singleValue, By by) {
        DelayUtils.waitForClickability(webDriverWait, webElement.findElement(by));
        webElement.findElement(by).sendKeys(Keys.CONTROL + "a");
        webElement.findElement(by).sendKeys(Keys.DELETE);
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.cssSelector(OSF_NOT_DISABLED_CSS));
        webElement.findElement(by).sendKeys(singleValue);
    }

    private void chooseResult(String singleValue, boolean isContains) {
        DelayUtils.waitByXPath(webDriverWait, OSF_DROP_DOWN_LIST);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        if (isContains) {
            dropdownList.selectOptionByDataValueContains(singleValue);
            return;
        }
        dropdownList.selectOptionByDataValue(singleValue);
    }

    private void chooseFirstResult() {
        DelayUtils.waitByXPath(webDriverWait, OSF_DROP_DOWN_LIST);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        List<WebElement> dropdownElement = driver.findElements(By.xpath(OSF_DROP_DOWN_LIST));
        dropdownElement.get(0).click();
    }
}
