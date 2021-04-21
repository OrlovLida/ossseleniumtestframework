package com.oss.framework.components.portals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class DatePicker {
    private final WebElement webElement;

    private final LocalDate currentDate = LocalDate.now();
    private final int currentMonth = currentDate.getMonthValue();
    private final int currentYear = currentDate.getYear();

    private final By prev = By.xpath(".//span[contains(@class,'prev')]");
    private final By next = By.xpath(".//span[contains(@class,'next')]");
    private final By calendarIcon = By.xpath(".//i[contains(@class,'fa-calendar')]");

    public static DatePicker create(WebDriverWait wait, WebElement webElement) {
        webElement.findElement(By.xpath(".//i[contains(@class,'fa-calendar')]")).click();
        DelayUtils.waitByXPath(wait, ".//span[contains(@class,'next')]");
        return new DatePicker(webElement);
    }

    public DatePicker(WebElement webElement) {
        this.webElement = webElement;
    }

    //yyyy-mm-dd
    public void chooseDate(String date) {
        List<String> dateList = Arrays.asList(date.split("-"));
        int day = Integer.parseInt(dateList.get(2));
        int month = Integer.parseInt(dateList.get(1));
        int year = Integer.parseInt(dateList.get(0));

        setToday();
        clickCalendar();

        if (year == currentYear) {
            if (month > currentMonth) {
                int monthDifference = month - currentMonth;
                for (int i = 0; i < monthDifference; i++) {
                    next().click();
                }
            } else if (month < currentMonth) {
                int monthDifference = currentMonth - month;
                for (int i = 0; i < monthDifference; i++) {
                    prev().click();
                }
            }
        } else if (year > currentYear) {
            setYear(year);
            for (int i = 0; i < month - 1; i++) {
                next().click();
            }
        } else if (year < currentYear) {
            setYear(year);
            for (int i = 0; i < 12 - month; i++) {
                prev().click();
            }
        }

        setDay(day);
    }

    public void clickCalendar() {
        DelayUtils.sleep();
        WebElement calendarIcon = this.webElement
                .findElement(this.calendarIcon);
        calendarIcon.click();
        DelayUtils.sleep(200);
    }

    private void setYear(int year) {
        while (getCalendarTitleYear() != year) {
            if (getCalendarTitleYear() < year) {
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

    private int getCalendarTitleYear() {
        String calendarTitle = this.webElement
                .findElement(By.xpath(".//div[contains(@class,'DayPicker-Caption')]")).getText();
        return Integer.parseInt(calendarTitle.substring(calendarTitle.length() - 4));
    }

    private void setToday() {
        webElement.findElement(By.xpath(".//button[text()='Today']")).click();
    }
}
