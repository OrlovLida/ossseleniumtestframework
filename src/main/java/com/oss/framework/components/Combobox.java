package com.oss.framework.components;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Combobox extends Input {

    static Combobox create(WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(driver, wait, comboboxId);
    }

    static Combobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new Combobox(parent, driver, wait, comboboxId);
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
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.RETURN);

    }

    @Override
    public void setValueContains(Data value) {
        click();
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(".//input[contains(@class,'md-input-clickable')] | .//input[contains(@id,'domain-combobox-input')]"));
        return Data.createSingleData(input.getAttribute("value"));
    }

    public Data getSelectedValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getText());
    }

    @Override
    public void clear(){
        try {
            webElement.findElement(By.xpath(".//input")).clear();
        } catch (Exception e) {
            List<WebElement> closeButtons = this.webElement.findElements(By.xpath(".//i[@class='OSSIcon fa fa-close']"));
            closeButtons.forEach(WebElement::click);
        }
    }
}



