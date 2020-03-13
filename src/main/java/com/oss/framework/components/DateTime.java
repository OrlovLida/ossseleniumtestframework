package com.oss.framework.components;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class DateTime extends Input {

    private static final String XPATH_HOURS_MORE = "(//div[@class='timePicker-navButton timePicker-navButton--more btn'])[1]";
    private static final String XPATH_HOURS_LESS = "(//div[@class='timePicker-navButton timePicker-navButton--less btn'])[1]";
    private static final String XPATH_MINUTES_MORE = "(//div[@class='timePicker-navButton timePicker-navButton--more btn'])[2]";
    private static final String XPATH_MINUTES_LESS = "(//div[@class='timePicker-navButton timePicker-navButton--less btn'])[2]";
    private static final String XPATH_SECUNDES_MORE = "(//div[@class='timePicker-navButton timePicker-navButton--more btn'])[3]";
    private static final String XPATH_SECUNDES_LESS = "(//div[@class='timePicker-navButton timePicker-navButton--less btn'])[3]";
    private static final String XPATH_DATE_TODAY = "//div[contains(@class,'DayPicker-Day btn DayPicker-Day--selected DayPicker-Day--today')]";
    private static final String XPATH_NEXT_MONTH = "//span[@aria-label='Next Month']";
    private static final String XPATH_PREVIOUS_MONTH = "//span[@aria-label='Previous Month']";

    static DateTime create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTime(driver, wait, componentId);
    }

    static DateTime createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTime(parent,driver, wait, componentId);
    }

    private DateTime(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private DateTime(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        return Data.createSingleData(input.getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        //unfocus element
        input.sendKeys(Keys.TAB);
        DelayUtils.sleep();
    }

    static public String createPathDate(Calendar date){
        return  "//div[@aria-label='" + date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH) + ", " +
                date.get(Calendar.DAY_OF_MONTH) + " " +
                date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + date.get(Calendar.YEAR) + "']";
    }

    //TODO exceptions handling, duplicated code, var's names
    public void chooseDate(String dataK) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(dataK);
        } catch (ParseException e) {
            System.out.println("Problem with date parser");
        }
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(date);
        Calendar today = new GregorianCalendar();
        int dateDifference = givenDate.get(Calendar.MONTH) - today.get(Calendar.MONTH);
        if(dateDifference > 0) {
            WebElement we = driver.findElement(By.xpath(XPATH_NEXT_MONTH));
            for(int i =0; i<dateDifference; i++) we.click();
        } else if(dateDifference < 0) {
            WebElement we = driver.findElement(By.xpath(XPATH_PREVIOUS_MONTH));
            for(int i =0; i>dateDifference; i--) we.click();
        }
        selectDate(givenDate);
    }

    private void selectDate(Calendar givenDate) {
        String day = String.valueOf(givenDate.get(Calendar.DAY_OF_MONTH));
       // WebElement data = driver.findElement(By.xpath(createPathDay(day)));
        WebElement data = driver.findElement(By.xpath(createPathDate(givenDate)));
        data.click();
    }

    private void clickTime(){
        WebElement clock = this.webElement.findElement(By.xpath(".//i[@class='OSSIcon fa fa-clock-o']"));
        clock.click();
        DelayUtils.sleep();
    }

    public void clickCalendar(){
        WebElement calendar = this.webElement.findElement(By.xpath(".//i[@class='OSSIcon fa fa-clock-o']"));
        calendar.click();
        DelayUtils.sleep();
    }

    public void chooseTimeMore(int hours, int minutes, int seconds) {
        this.chooseTime(hours, minutes, seconds, true);
    }

    public void chooseTimeLess(int hours, int minutes, int seconds) {
        this.chooseTime(hours, minutes, seconds, false);
    }

    private void chooseTime(int hours, int minutes, int seconds, boolean isMore){
        clickTime();
        List<WebElement> moreButtons;

        if(isMore) {
            moreButtons = this.webElement
                    .findElements(By.xpath(".//div[@class='timePicker-navButton timePicker-navButton--more btn']"));
        } else {
            moreButtons = this.webElement
                    .findElements(By.xpath(".//div[@class='timePicker-navButton timePicker-navButton--less btn']"));
        }

        for(int i = 0; i < hours; i ++) {
            moreButtons.get(0).click();
            DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        }
        for(int i = 0; i < minutes; i ++) {
            moreButtons.get(1).click();
            DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        }
        for(int i = 0; i < seconds; i ++) {
            moreButtons.get(2).click();
            DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        }
    }
}