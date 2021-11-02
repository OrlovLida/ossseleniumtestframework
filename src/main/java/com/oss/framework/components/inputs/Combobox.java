package com.oss.framework.components.inputs;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.data.Data;
import com.oss.framework.data.Data.DataWrapper;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;

public class Combobox extends Input {
    private static final String INPUT_XPATH = ".//input";
    private static final String LABEL_XPATH = ".//label";
    private static final String LIST_ITEM_XPATH = "//div[@class='list-item']";
    private static final String CLOSE_BUTTON_XPATH = ".//i[contains(@class,'OSSIcon ossfont-close button-close')]";
    private static final String COMBOBOX_INPUT_XPATH = ".//input[contains(@class,'oss-input__input')] | .//input[contains(@id,'domain-combobox-input')]";


    // TODO: remove after resolving OSSSD-2035 - setting data-testId in status Combobox
    public static Combobox createServiceDeskStatusComboBox(WebDriver driver, WebDriverWait webDriverWait) {
        String xPath = "//div[contains(@class, 'most-wanted__inputs')]//div[contains(@class, 'combo-box')]";
        WebElement webElement = driver.findElement(By.xpath(xPath));
        return new Combobox(driver, webDriverWait, webElement);
    }

    static Combobox create(WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(driver, wait, comboboxId);
    }

    static Combobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(parent, driver, wait, comboboxId);
    }

    private Combobox(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webDriverWait, webElement);
    }

    private Combobox(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Combobox(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(LABEL_XPATH));
        return label.getText();
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, INPUT_XPATH);
        DataWrapper wrapper = value.getWrapper();

        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        clear();
        input.sendKeys(wrapper.getReadableValue());

        if (wrapper.isFindFirst()) {
            DelayUtils.sleep(); //TODO: wait for spinners
            input.sendKeys(Keys.DOWN);
            input.sendKeys(Keys.RETURN);
            return;
        }

        DelayUtils.waitByXPath(webDriverWait, LIST_ITEM_XPATH + "//*[text()='" + wrapper.getReadableValue() + "']");
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

    public Data getSelectedValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT_XPATH)).getText());
    }

    @Override
    public void clear() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<WebElement> closeButtons = this.webElement.findElements(By.xpath(CLOSE_BUTTON_XPATH));
        closeButtons.forEach(WebElement::click);
    }
}



