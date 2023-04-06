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

public class Tags extends Input {
    private static final String CLOSE_XPATH = ".//span[contains(@class, 'close')]";
    private static final String SEARCH_CSS = "[aria-label='SEARCH']";
    private static final String TEXT_CONTENT = "textContent";

    private Tags(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static Tags create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Tags(driver, wait, webElement, componentId);
    }

    static Tags createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Tags(driver, wait, webElement, componentId);
    }

    @Override
    public MouseCursor cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.className("md-input")).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(SEARCH_CSS)));
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOptionByDataValueContains(value.getStringValue());
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(SEARCH_CSS)));
    }

    @Override
    public Data getValue() {
        return Data.createMultiData(webElement.findElements(By.xpath(".//span//span")).stream().map(value -> value.getAttribute(TEXT_CONTENT)).collect(Collectors.toList()));
    }

    @Override
    public void setValue(Data value) {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(SEARCH_CSS)));
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOptionByDataValue(value.getStringValue());
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(SEARCH_CSS)));
    }

    @Override
    public void clear() {
        List<WebElement> closeButtons = webElement.findElements(By.xpath(CLOSE_XPATH));
        closeButtons.forEach(this::clearSingle);
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//span")).getText();
    }

    private void clearSingle(WebElement closeButton) {
        WebElementUtils.clickWebElement(driver, closeButton);
    }
}
