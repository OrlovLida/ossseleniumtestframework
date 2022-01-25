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
import com.oss.framework.utils.DelayUtils;

public class MultiSearchField extends Input {
    private static final String CLOSE_XPATH = ".//span[contains(@class, 'close')]";

    private MultiSearchField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private MultiSearchField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static MultiSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(driver, wait, componentId);
    }

    static MultiSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(parent, driver, wait, componentId);
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
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createMultiData(webElement.findElements(By.xpath(".//span//span")).stream().map(value -> value.getAttribute("textContent")).collect(Collectors.toList()));
    }

    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.search(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        dropdownList.selectOption(value.getStringValue());
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
        Actions actions = new Actions(driver);
        actions.moveToElement(closeButton).click().build().perform();
    }
}
