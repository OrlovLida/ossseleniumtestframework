package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class CustomSelect extends Input {
    private static final String ROLL_UP_CSS = "[aria-label='ROLL_UP']";
    private static final String TEXT_CONTENT = "textContent";
    private static final String TEXT_VALUE_XPATH = ".//span//span";
    private static final String CLOSE_XPATH = ".//span[contains(@class, 'close')]";

    private CustomSelect(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        super(driver, webDriverWait, webElement, componentId);
    }

    static CustomSelect create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new CustomSelect(driver, wait, webElement, componentId);
    }

    static CustomSelect createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new CustomSelect(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElementUtils.clickWebElement(driver, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOptionContains(value.getStringValue());
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(ROLL_UP_CSS)));
    }

    @Override
    public Data getValue() {
        return Data.createMultiData(webElement.findElements(By.xpath(TEXT_VALUE_XPATH)).stream().map(value -> value.getAttribute(TEXT_CONTENT)).collect(Collectors.toList()));
    }

    @Override
    public void setValue(Data value) {
        WebElementUtils.clickWebElement(driver, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOption(value.getStringValue());
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(ROLL_UP_CSS)));
    }

    @Override
    public void clear() {
        while (!elementsToClose().isEmpty()) {
            WebElementUtils.clickWebElement(driver, elementsToClose().get(0));
        }
    }

    private List<WebElement> elementsToClose() {
        return webElement.findElements(By.xpath(CLOSE_XPATH));
    }
}
