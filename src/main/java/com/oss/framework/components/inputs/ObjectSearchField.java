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
    private static final String NOT_DISABLED_INPUT_PATTERN = "[data-testid='%s'] input:not([disabled])";

    private ObjectSearchField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static ObjectSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitForPresence(wait, By.cssSelector(String.format(NOT_DISABLED_INPUT_PATTERN, componentId)));
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ObjectSearchField(driver, wait, webElement, componentId);
    }

    public static ObjectSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait,
                                                     String componentId) {
        DelayUtils.waitForNestedElements(wait, parent, By.cssSelector(String.format(NOT_DISABLED_INPUT_PATTERN, componentId)));
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new ObjectSearchField(driver, wait, webElement, componentId);
    }

    public void setValue(Data value, boolean isContains) {
        if (!isSingleComponent()) {
            WebElementUtils.clickWebElement(driver, webElement);
            WebElement innerInput = driver.findElement(By.xpath(OSF_INNER_INPUT));
            innerInput.sendKeys(value.getStringValue());
            DelayUtils.waitForSpinners(webDriverWait, webElement);
            DelayUtils.waitByXPath(webDriverWait, OSF_DROP_DOWN_LIST);
            chooseFirstResult();
            WebElementUtils.clickWebElement(driver, webElement);
        } else {
            clear();
            DelayUtils.sleep(1000);
            webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
            DelayUtils.waitForSpinners(webDriverWait, webElement);
            DelayUtils.waitByXPath(webDriverWait, OSF_DROP_DOWN_LIST);
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
        return !webElement.findElement(By.xpath(".//./ancestor::div[contains(@class,'component')]"))
                .findElements(By.className(OSF_SINGLE)).isEmpty();
    }

    private boolean isMultiComponentEmpty() {
        return !webElement.findElements(By.className("md-input-empty")).isEmpty();
    }

    private void chooseFirstResult() {
        DelayUtils.sleep(1500);
        List<WebElement> dropdownElement = driver.findElements(By.xpath(OSF_DROP_DOWN_LIST));
        dropdownElement.get(0).click();
    }

}
