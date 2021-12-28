package com.oss.framework.components.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class DatePicker {
    private final WebElement webElement;
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By prev = By.xpath(".//span[contains(@class,'prev')]");
    private final By next = By.xpath(".//span[contains(@class,'next')]");
    private static final String TODAY_BUTTON_XPATH = ".//button[text()='Today']";

    public static DatePicker create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement dayPicker = driver.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        DelayUtils.waitByXPath(wait, ".//span[contains(@class,'next')]");

        return new DatePicker(driver, wait, dayPicker);
    }

    public DatePicker(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.webElement = webElement;
        this.wait = wait;
    }

    // yyyy-mm-dd
    public void chooseDate(String date) {
        List<String> dateList = Arrays.asList(date.split("-"));
        int day = Integer.parseInt(dateList.get(2));
        int month = Integer.parseInt(dateList.get(1));
        int year = Integer.parseInt(dateList.get(0));

        if (year == getCalendarYear()) {
            if (month > getCalendarMonth()) {
                int monthDifference = month - getCalendarMonth();
                for (int i = 0; i < monthDifference; i++) {
                    next().click();
                }
            } else if (month < getCalendarMonth()) {
                int monthDifference = getCalendarMonth() - month;
                for (int i = 0; i < monthDifference; i++) {
                    prev().click();
                }
            }
        } else if (year > getCalendarYear()) {
            setYear(year);
            for (int i = 0; i < month - 1; i++) {
                next().click();
            }
        } else if (year < getCalendarYear()) {
            setYear(year);
            for (int i = 0; i < 12 - month; i++) {
                prev().click();
            }
        }
        setDay(day);
    }

    private void setYear(int year) {
        while (getCalendarYear() != year) {
            if (getCalendarYear() < year) {
                next().click();
            } else {
                prev().click();
            }
            DelayUtils.sleep(80);
        }
    }

    private WebElement prev() {
        return webElement.findElement(prev);
    }

    private WebElement next() {
        return webElement.findElement(next);
    }

    private void setDay(int day) {
        webElement.findElement(By.xpath(".//div[text()='" + day + "' and contains(@aria-disabled,'false')]")).click();
    }

    private int getCalendarYear() {
        String calendarTitle = webElement
                .findElement(By.xpath(".//div[contains(@class,'DayPicker-Caption')]")).getText();
        return Integer.parseInt(calendarTitle.substring(calendarTitle.length() - 4));
    }

    private int getCalendarMonth() {
        WebElement selectedDay = webElement.findElements(By.className("DayPicker-Day")).stream()
                .filter(day -> day.getAttribute("aria-selected").equals("true")).findFirst().get();
        String selectedDate = selectedDay.getAttribute("aria-label");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = simpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse date");
        }
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(date);

        return givenDate.get(Calendar.MONTH) + 1;
    }

    private void setToday() {
        webElement.findElement(By.xpath(TODAY_BUTTON_XPATH)).click();
    }
}
