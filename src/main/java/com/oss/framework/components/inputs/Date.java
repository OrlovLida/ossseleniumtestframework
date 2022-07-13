package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.datetime.DatePicker;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class Date extends Input {

    private static final String CLOCK_ICON_XPATH = ".//i[contains(@class,'fa-calendar')]";
    private static final String INPUT = ".//input";
    private static final String DATE_TIME_PICKER_ID = "dateTimePicker";

    private Date(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static Date create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Date(driver, wait,webElement, componentId);
    }

    static Date createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new Date(driver, wait, webElement, componentId);
    }

    public void chooseDate(String date) {
        webElement.findElement(By.xpath(CLOCK_ICON_XPATH)).click();
        DatePicker datePicker = DatePicker.create(driver, webDriverWait, DATE_TIME_PICKER_ID);
        datePicker.chooseDate(date);
    }

    @Override
    public void setValueContains(Data value) {
        this.webElement.findElement(By.xpath(INPUT)).click();
        this.webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(this.webElement.findElement(By.xpath(INPUT))
                .getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        clear();
        this.webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = this.webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//label")).getText();
    }

}
