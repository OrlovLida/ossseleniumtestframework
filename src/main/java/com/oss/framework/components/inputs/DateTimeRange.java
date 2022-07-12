package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.datetime.DatePicker;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;

public class DateTimeRange extends Input {
    
    private static final String INPUT_XPATH = ".//input";
    private static final String CLEAR_XPATH = ".//*[@class='OSSIcon ossfont-close']";
    private static final String CALENDAR_XPATH = ".//*[@aria-label='DATE']";
    private static final String DATE_TIME_PICKER_TO_ID = "dateTimePicker-to";
    private static final String DATE_TIME_PICKER_FROM_ID = "dateTimePicker-from";

    private DateTimeRange(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }
    
    static DateTimeRange create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new DateTimeRange(driver, wait, webElement, componentId);
    }
    
    static DateTimeRange createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new DateTimeRange(driver, wait, webElement, componentId);
    }
    
    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_EXCEPTION);
    }
    
    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.getAttribute("value"));
    }
    
    @Override
    public void setValue(Data value) {
        WebElementUtils.moveToElement(driver, webElement);
        clear();
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        WebElementUtils.clickWebElement(driver, input);
        input.sendKeys(value.getStringValue());
    }
    
    @Override
    public void clear() {
        if (WebElementUtils.isElementPresent(webElement, By.xpath(CLEAR_XPATH))) {
            webElement.findElement(By.xpath(CLEAR_XPATH)).click();
        }
    }
    
    @Override
    public String getLabel() {
        return webElement.getText();
    }
    
    public void chooseDatesFromCalendar(String fromDate, String toDate) {
        clickCalendar();
        DatePicker datePickerFrom = DatePicker.create(driver, webDriverWait, DATE_TIME_PICKER_FROM_ID);
        datePickerFrom.chooseDate(fromDate);
        DatePicker datePickerTo = DatePicker.create(driver, webDriverWait, DATE_TIME_PICKER_TO_ID);
        datePickerTo.chooseDate(toDate);
    }
    
    private void clickCalendar() {
        WebElement calendar = webElement.findElement(By.xpath(CALENDAR_XPATH));
        WebElementUtils.clickWebElement(driver, calendar);
    }
}
