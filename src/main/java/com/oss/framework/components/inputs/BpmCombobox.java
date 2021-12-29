package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class BpmCombobox extends Input {

    private static final String OPTION = ".//option[@value='%s']";
    private static final String OPTION_CONTAINS = ".//option[contains(@value, '%s')]";
    private static final String SELECT = ".//select";
    private static final String METHOD_NOT_IMPLEMENTED = "Method not implemented for BpmCombobox";

    private BpmCombobox(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private BpmCombobox(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static BpmCombobox create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new BpmCombobox(driver, wait, componentId);
    }

    static BpmCombobox create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new BpmCombobox(parent, driver, wait, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        WebElement input = webElement.findElement(By.xpath(SELECT));
        Actions action = new Actions(driver);
        action.moveToElement(input).build().perform();
        input.click();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        input.findElement(By.xpath(String.format(OPTION_CONTAINS, value.getStringValue()))).click();
    }

    @Override
    public Data getValue() {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(SELECT));
        Actions action = new Actions(driver);
        action.moveToElement(input).build().perform();
        input.click();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        input.findElement(By.xpath(String.format(OPTION, value.getStringValue()))).click();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);

    }
}