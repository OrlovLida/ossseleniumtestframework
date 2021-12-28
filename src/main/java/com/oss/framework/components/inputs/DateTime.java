package com.oss.framework.components.inputs;

import java.util.Calendar;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.datetime.DatePicker;
import com.oss.framework.components.portals.TimePicker;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class DateTime extends Input {

    private static final String XPATH_CLOCK_ICON = ".//button//i[@class='OSSIcon fa fa-calendar']";
    private static final String INPUT = ".//input";

    static DateTime create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTime(driver, wait, componentId);
    }

    static DateTime createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTime(parent, driver, wait, componentId);
    }

    private DateTime(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private DateTime(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        clear();
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        return Data.createSingleData(input.getAttribute("value"));
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

    public static String createPathDate(Calendar date) {
        return "//div[@aria-label='" + date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH) + ", " +
                date.get(Calendar.DAY_OF_MONTH) + " " +
                date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + date.get(Calendar.YEAR) + "']";
    }

    public void chooseDate(String date) {
        clickCalendar();
        DatePicker.create(driver, webDriverWait, "dateTimePicker").chooseDate(date);
    }

    private void selectDate(Calendar givenDate) {
        String day = String.valueOf(givenDate.get(Calendar.DAY_OF_MONTH));
        WebElement data = driver.findElement(By.xpath(createPathDate(givenDate)));
        data.click();
    }

    private void clickTime() {
        WebElement clock = this.webElement.findElement(By.xpath(XPATH_CLOCK_ICON));
        clock.click();
        DelayUtils.sleep();
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
}
