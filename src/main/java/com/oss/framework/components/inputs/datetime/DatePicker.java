package com.oss.framework.components.inputs.datetime;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class DatePicker {
    private static final By TODAY_BUTTON = By.xpath(".//button[@class='DayPicker-TodayButton btn btn-primary']");
    private static final By PREV_YEAR = By.xpath(".//span[@class='DayPicker-NavButton DayPicker-NavButton-year--prev']");
    private static final By PREV_MONTH = By.xpath(".//span[contains(@class, 'DayPicker-NavButton DayPicker-NavButton--prev')]");
    private static final By NEXT_YEAR = By.xpath(".//span[@class='DayPicker-NavButton DayPicker-NavButton-year--next']");
    private static final By NEXT_MONTH = By.xpath(".//span[contains(@class, 'DayPicker-NavButton DayPicker-NavButton--next')]");
    private static final String DATE_TIME_PICKER_ID = "dateTimePicker";

    private final WebElement webElement;
    private final String componentId;

    private DatePicker(WebElement webElement, String componentId) {
        this.webElement = webElement;
        this.componentId = componentId;
    }

    public static DatePicker create(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitForPresence(wait, By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        WebElement dayPicker = driver.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        return new DatePicker(dayPicker, componentId);
    }

    // yyyy-mm-dd
    public void chooseDate(String date) {
        List<String> dateList = Arrays.asList(date.split("-"));
        int day = Integer.parseInt(dateList.get(2));
        int month = Integer.parseInt(dateList.get(1));
        int year = Integer.parseInt(dateList.get(0));
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    private void setYear(int year) {
        while (getCalendarYear() != year) {
            if (getCalendarYear() < year) {
                clickArrow(getNextYearButton());
            } else {
                clickArrow(getPreviousYearButton());
            }
            DelayUtils.sleep(80);
        }
    }

    private void setMonth(int month) {
        while (getCalendarMonth() != month) {
            if (getCalendarMonth() < month) {
                clickArrow(NEXT_MONTH);
            } else {
                clickArrow(PREV_MONTH);
            }
            DelayUtils.sleep(80);
        }
    }

    private void clickArrow(By arrowBy) {
        webElement.findElement(arrowBy).click();
    }

    private void setDay(int day) {
        webElement.findElement(By.xpath(".//div[text()='" + day + "' and contains(@aria-disabled,'false')]")).click();
    }

    private int getCalendarYear() {
        return Integer.parseInt(getCurrentCalendarTitle()[1]);
    }

    private int getCalendarMonth() {
        Month month = Month.valueOf(getCurrentCalendarTitle()[0].toUpperCase());
        return month.getValue();
    }

    private String[] getCurrentCalendarTitle() {
        return webElement
                .findElement(By.xpath(".//div[contains(@class,'DayPicker-Caption')]")).getText().split(" ");
    }

    private void setToday() {
        webElement.findElement(TODAY_BUTTON).click();
    }

    private By getNextYearButton() {
        if (componentId.equals(DATE_TIME_PICKER_ID)) {
            return NEXT_MONTH;
        }
        return NEXT_YEAR;
    }

    private By getPreviousYearButton() {
        if (componentId.equals(DATE_TIME_PICKER_ID)) {
            return PREV_MONTH;
        }
        return PREV_YEAR;
    }
}
