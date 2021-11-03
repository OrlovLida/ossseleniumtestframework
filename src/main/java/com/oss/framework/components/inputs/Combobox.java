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

public class Combobox extends Input {

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
        WebElement label = webElement.findElement(By.xpath(".//label"));
        return label.getText();
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, "//input");
        DataWrapper wrapper = value.getWrapper();

        WebElement input = webElement.findElement(By.xpath(".//input"));
        clear();
        input.sendKeys(wrapper.getReadableValue());

        if (wrapper.isFindFirst()) {
            DelayUtils.sleep(); //TODO: wait for spinners
            input.sendKeys(Keys.DOWN);
            input.sendKeys(Keys.RETURN);
            return;
        }

        DelayUtils.waitByXPath(webDriverWait, "//div[contains(@class,'list-item')]//*[text()='" + wrapper.getReadableValue() + "']");
        List<WebElement> results = driver.findElements(By.xpath("//div[@class='list-item'] | //div[@class='combo-box__list-item']"));
        for (WebElement element : results) {
            if (wrapper.getReadableValue().equals(element.getText())) {
                element.click();
                return;
            }
        }
        throw new RuntimeException("Cant find element: " + wrapper.getReadableValue());
    }

    @Override
    public void setValueContains(Data value) {
        webElement.click();
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        WebElement input =
                webElement.findElement(By.xpath(".//input[contains(@class,'oss-input__input')] | .//input[contains(@id,'domain-combobox-input')]"));
        return Data.createSingleData(input.getAttribute("value"));

    }

    public Data getSelectedValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getText());
    }

    @Override
    public void clear() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        List<WebElement> closeButtons = this.webElement.findElements(By.xpath(".//i[contains(@class,'OSSIcon ossfont-close combo-box__close')]"));
        closeButtons.forEach(WebElement::click);
    }
}



