package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;

public class ObjectSearchField extends Input {
    private static final String OSF_LABEL = ".//span[@class='md-input-label-text']";
    private static final String OSF_INNER_INPUT = ".//*[@class='object-input-component__multi__dropdown']//input";
    private static final String OSF_DROP_DOWN_LIST = ".//div[@class='dropdown-element__label']";
    private static final String OSF_VALUE_LIST = ".//div[@class='md-input-multi']";
    private static final String OSF_VALUE_CLEAR_BTN = ".//div[@class='md-input-multi']";
    private static final String OSF_SINGLE = "object-input-component__single__dropdown";
    private static final String SEARCH_PLUS_ICON_XPATH = ".//button[@id='btn-as-modal']";
    private static final String INPUT = ".//input";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String OSF_NOT_DISABLED_CSS = ".md-input-cont:not(.md-input-disabled)";

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
            setValueForMultiComponent(value);
        } else {
            setSingleValue(value.getStringValue(), webElement.findElement(By.xpath(INPUT)));
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
            List<WebElement> values = webElement.findElements(By.xpath(OSF_VALUE_LIST + "//span//span"));
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
        searchPlus.click();
        return AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
    }

    private boolean isSingleComponent() {
        return !webElement.findElement(By.xpath(".//./ancestor::div[contains(@class,'component')]")).findElements(By.className(OSF_SINGLE)).isEmpty();
    }

    private boolean isMultiComponentEmpty() {
        return !webElement.findElements(By.className("md-input-empty")).isEmpty();
    }

    private void chooseFirstResult() {
        DelayUtils.waitByXPath(webDriverWait, OSF_DROP_DOWN_LIST);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        List<WebElement> dropdownElement = driver.findElements(By.xpath(OSF_DROP_DOWN_LIST));
        dropdownElement.get(0).click();
    }

    private void setValueForMultiComponent(Data value) {
        WebElementUtils.clickWebElement(driver, webElement);
        WebElement search = driver.findElement(By.xpath(OSF_INNER_INPUT));
        if (value.isList()) {
            setMultiValues(value, search);
        } else {
            setSingleValue(value.getStringValue(), search);
        }
        WebElementUtils.clickWebElement(driver, webElement);
    }

    private void setMultiValues(Data values, WebElement search) {
        values.getStringValues().forEach(value -> setSingleValue(value, search));
    }

    private void setSingleValue(String singleValue, WebElement input) {
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(singleValue);
        chooseFirstResult();
    }
}
