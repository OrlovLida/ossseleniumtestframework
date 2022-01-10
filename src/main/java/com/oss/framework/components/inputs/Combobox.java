package com.oss.framework.components.inputs;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.data.Data;
import com.oss.framework.data.Data.DataWrapper;
import com.oss.framework.utils.DelayUtils;

public class Combobox extends Input {

    private static final String INPUT_XPATH = ".//input";
    private static final String LABEL_XPATH = ".//label";
    private static final String COMBOBOX_INPUT_XPATH = ".//input[contains(@class,'oss-input__input')] | .//input[contains(@id,'domain-combobox-input')]";
    private static final String COMBOBOX_CLOSE_XPATH = ".//i[@aria-label ='CLOSE']";
    private static final String LIST_ITEM_XPATH = "//div[@class='list-item'] | //div[@class='combo-box__list-item']";

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
        webElement.click();
        webElement.findElement(By.xpath(INPUT_XPATH)).sendKeys(value.getStringValue());
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
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, "//input");
        DataWrapper wrapper = value.getWrapper();

        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        clear();
        input.sendKeys(wrapper.getReadableValue());

        if (wrapper.isFindFirst()) {
            DelayUtils.waitForSpinners(webDriverWait, webElement);
            input.sendKeys(Keys.DOWN);
            input.sendKeys(Keys.RETURN);
            return;
        }

        DelayUtils.waitByXPath(webDriverWait, "//div[contains(@class,'list-item')]//*[text()='" + wrapper.getReadableValue() + "']");
        List<WebElement> results = driver.findElements(By.xpath(LIST_ITEM_XPATH));
        for (WebElement element : results) {
            if (wrapper.getReadableValue().equals(element.getText())) {
                element.click();
                return;
            }
        }
        throw new NoSuchElementException("Cant find element: " + wrapper.getReadableValue());
    }

    @Override
    public void clear() {
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        List<WebElement> closeButtons = this.webElement.findElements(By.xpath(COMBOBOX_CLOSE_XPATH));
        closeButtons.forEach(WebElement::click);
    }

    @Override
    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(LABEL_XPATH));
        return label.getText();
    }
}
