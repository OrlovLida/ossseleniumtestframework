package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.TimePicker;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class Time extends Input {

    private static final String CLOCK_ICON_XPATH = ".//i[@class='OSSIcon fa fa-clock-o']";
    private static final String INPUT = ".//input";

    private Time(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Time(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static Time create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Time(driver, wait, componentId);
    }

    static Time create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Time(parent, driver, wait, componentId);
    }

    public void chooseTime(String value) {
        clickTime();
        TimePicker timePicker = TimePicker.create(driver, webDriverWait);
        timePicker.chooseTime(value);
        clickTime();
    }

    @Override
    public void setValueContains(Data value) {
        webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement
                .findElement(By.xpath(INPUT))
                .getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        webElement.findElement(By.xpath(INPUT)).sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    private void clickTime() {
        WebElement clock = this.webElement.findElement(By.xpath(CLOCK_ICON_XPATH));
        clock.click();
        DelayUtils.sleep();
    }

}
