package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class MultiSearchField extends Input {
    private static final String CLOSE_XPATH = ".//span[contains(@class, 'close')]";
    private static final String SEARCH_ID = "search-box__button-search";
    private static final String TAGS_INPUT_LABEL_CSS = ".tags-input__label";
    private static final String INPUT_LABEL_CSS = ".oss-input__input-label";

    private MultiSearchField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static MultiSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new MultiSearchField(driver, wait, webElement, componentId);
    }

    static MultiSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new MultiSearchField(driver, wait, webElement, componentId);
    }

    @Override
    public MouseCursor cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.className("oss-input__input")).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        search(value.getStringValue()).selectOptionContains(value.getStringValue());
        webElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, SEARCH_ID))).click();
    }

    @Override
    public Data getValue() {
        return Data.createMultiData(webElement.findElements(By.cssSelector(TAGS_INPUT_LABEL_CSS)).stream().map(value -> value.getAttribute("textContent")).collect(Collectors.toList()));
    }

    @Override
    public void setValue(Data value) {
        setValue(value, false);
    }

    @Override
    public void clear() {
        List<WebElement> closeButtons = webElement.findElements(By.xpath(CLOSE_XPATH));
        closeButtons.forEach(this::clearSingle);
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.cssSelector(INPUT_LABEL_CSS)).getText();
    }

    public void setValueCaseSensitive(Data value) {
        setValue(value, true);
    }

    private void clearSingle(WebElement closeButton) {
        WebElementUtils.clickWebElement(driver, closeButton);
    }

    private String createDropdownList() {
        return "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']";
    }

    private DropdownList search(String value) {
        WebElementUtils.clickWithRetry(driver, webElement, By.xpath(createDropdownList()));
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        return dropdownList;
    }

    private void setSingleValue(String value, boolean isSensitive) {
        if (isSensitive) {
            search(value).selectOptionCaseSensitive(value);
        } else {
            search(value).selectOption(value);
        }
    }

    private void setMultiValue(Data values, boolean isSensitive) {
        values.getStringValues().forEach(value -> setSingleValue(value, isSensitive));
    }

    private void setValue(Data value, boolean isCaseSensitive) {
        if (value.isList()) {
            setMultiValue(value, isCaseSensitive);
        } else {
            setSingleValue(value.getStringValue(), isCaseSensitive);
        }
        webElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, SEARCH_ID))).click();
    }
}
