package com.oss.framework.components.inputs;

import java.util.Calendar;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.datetime.DatePicker;
import com.oss.framework.components.inputs.datetime.TimePicker;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class DateTime extends Input {
    
    private static final String XPATH_CLOCK_ICON = ".//button//i[@class='OSSIcon fa fa-calendar']";
    private static final String INPUT = ".//input";
    private static final String DATE_TIME_PICKER_ID = "dateTimePicker";
    
    private DateTime(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }
    
    public static String createPathDate(Calendar date) {
        return "//div[@aria-label='" + date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH) + ", " +
                date.get(Calendar.DAY_OF_MONTH) + " " +
                date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + date.get(Calendar.YEAR) + "']";
    }
    
    static DateTime create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new DateTime(driver, wait, webElement, componentId);
    }
    
    static DateTime createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new DateTime(driver, wait, webElement, componentId);
    }
    
    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }
    
    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        return Data.createSingleData(input.getAttribute("value"));
    }
    
    @Override
    public void setValue(Data value) {
        clear();
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }
    
    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        // unfocus element
        input.sendKeys(Keys.TAB);
        DelayUtils.sleep();
    }
    
    public void chooseDate(String date) {
        clickCalendar();
        DatePicker.create(driver, webDriverWait, DATE_TIME_PICKER_ID).chooseDate(date);
    }
    
    public void clickCalendar() {
        Actions actions = new Actions(driver);
        WebElement calendar = webElement.findElement(By.xpath(XPATH_CLOCK_ICON));
        actions.moveToElement(webElement).click(calendar).build().perform();
        DelayUtils.sleep();
    }
    
    public void chooseTime(String time) {
        clickTime();
        TimePicker.create(driver, webDriverWait).chooseTime(time);
        clickTime();
    }
    
    private void clickTime() {
        WebElement clock = this.webElement.findElement(By.xpath(XPATH_CLOCK_ICON));
        clock.click();
        DelayUtils.sleep();
    }
}
