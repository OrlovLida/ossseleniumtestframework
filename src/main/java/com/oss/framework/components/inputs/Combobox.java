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

public class Combobox extends Input {

    private static final String INPUT_XPATH = ".//input";
    private static final String LABEL_XPATH = ".//label";
    private static final String COMBOBOX_INPUT_XPATH = ".//input[contains(@class,'oss-input__input')] | .//input[contains(@id,'domain-combobox-input')]";
    private static final String CLEAR_BUTTON_SELECTOR = "[aria-label='CLOSE']";

    private Combobox(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Combobox(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static Combobox create(WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(driver, wait, comboboxId);
    }

    static Combobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(parent, driver, wait, comboboxId);
    }

    @Override
    public void setValueContains(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, INPUT_XPATH);
        clear();
        WebElementUtils.clickWithRetry(driver, webElement, By.xpath(createDropdownList()));
        webElement.findElement(By.xpath(INPUT_XPATH)).sendKeys(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        WebElement input =
                webElement.findElement(By.xpath(COMBOBOX_INPUT_XPATH));
        return Data.createSingleData(input.getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, INPUT_XPATH);
        clear();
        WebElementUtils.clickWithRetry(driver, webElement, By.xpath(createDropdownList()));
        webElement.findElement(By.xpath(INPUT_XPATH)).sendKeys(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.selectOption(value.getStringValue());
    }

    @Override
    public void clear() {
        if (isClearIconPresent()) {
            webElement.findElement(By.cssSelector(CLEAR_BUTTON_SELECTOR)).click();
        } else {
            WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
            WebElementUtils.clickWebElement(driver, input);
            input.sendKeys(Keys.CONTROL + "a");
            input.sendKeys(Keys.DELETE);
        }
    }

    @Override
    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(LABEL_XPATH));
        return label.getText();
    }

    private boolean isClearIconPresent() {
        return !webElement.findElements(By.cssSelector(CLEAR_BUTTON_SELECTOR)).isEmpty();
    }

    private String createDropdownList() {
        return "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']";
    }

}
