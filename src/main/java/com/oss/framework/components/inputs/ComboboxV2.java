package com.oss.framework.components.inputs;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class ComboboxV2 extends Input {

    static ComboboxV2 create(WebDriver driver, WebDriverWait wait, String comboboxId) {
        return new ComboboxV2(driver, wait, comboboxId);
    }

    private ComboboxV2(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    @Override
    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(".//span"));
        return label.getText();
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, "//span");
        webElement.findElement(By.xpath(".//span")).click();
        WebElement input = driver.findElement(By.xpath("//div[@class='combo-box__dropdown']//input[@class='oss-input__input']"));
        input.sendKeys(value.getStringValue());
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.ESCAPE);
    }

    @Override
    public void setValueContains(Data value) {
        DelayUtils.waitForNestedElements(this.webDriverWait, webElement, "//span");
        webElement.findElement(By.xpath(".//span")).click();
        WebElement input = driver.findElement(By.xpath("//div[@class='combo-box__dropdown']//input[@class='oss-input__input']"));
        input.sendKeys(value.getStringValue());
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.ESCAPE);
    }

    @Override
    public Data getValue() {
        List<String> values = new ArrayList<String>();
        DelayUtils.waitByElement(webDriverWait, this.webElement);
        List<WebElement> comboboxValues = this.webElement.findElements(By.xpath(".//div[@class='tags-input__tag']"));
        for (WebElement comboboxValue : comboboxValues) {
            values.add(comboboxValue.getText());
        }
        return Data.createMultiData(values);
    }

    @Override
    public void clear() {
    }
}