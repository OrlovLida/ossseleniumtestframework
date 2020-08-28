package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;

import com.oss.framework.data.Data;

import static java.lang.Thread.sleep;

public class DateTimeRange extends Input {

    private static final String XPATH_MONTH_NEXT = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--next')]";
    private static final String XPATH_MONTH_NEXT_1 = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--next')][1]";
    private static final String XPATH_MONTH_NEXT_2 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--next')])[2]";
    private static final String XPATH_MONTH_PREV = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--prev')]";
    private static final String XPATH_MONTH_PREV_1 = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--prev')][1]";
    private static final String XPATH_MONTH_PREV_2 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton--prev')])[2]";
    private static final String XPATH_DATE_TODAY = "//div[contains(@class,'DayPicker-Day btn DayPicker-Day--selected DayPicker-Day--today')]";
    private static final String XPATH_NEXT_MONTH = "//span[@aria-label='Next Month']";
    private static final String XPATH_PREVIOUS_MONTH = "//span[@aria-label='Previous Month']";
    private static final String XPATH_YEAR_NEXT = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--next')]";
    private static final String XPATH_YEAR_NEXT_1 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--next')])[1]";
    private static final String XPATH_YEAR_NEXT_2 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--next')])[2]";
    private static final String XPATH_YEAR_PREV = "//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--prev')]";
    private static final String XPATH_YEAR_PREV_1 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--prev')])[1]";
    private static final String XPATH_YEAR_PREV_2 = "(//span[contains(@class,'DayPicker-NavButton DayPicker-NavButton-year--prev')])[2]";

    static DateTimeRange create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTimeRange(driver, wait, componentId);
    }

    static DateTimeRange createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new DateTimeRange(parent, driver, wait, componentId);
    }

    private DateTimeRange(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private DateTimeRange(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.getAttribute("value"));
    }

    @Override
    public void clear() {

    }

    @Override
    public void setValue(Data value) {
        webElement.click();
        webElement.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public String getLabel() {
        return webElement.getText();
    }

    static public String createPath(String label){
        return  "//input[@label='" + label + "']";
    }

    static public String createPathComponent(String label){
        return  "//input[@label='" + label + "']";
    }

    static public String createPathDay(String day){
        return  "//div[@class='DayPicker-Day btn '][contains(.,'"+ day + "')]";
    }

    static public String createPathDate(Calendar date){
        return  "//div[@aria-label='" + date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH) + ", " + date.get(Calendar.DAY_OF_MONTH) + " " + date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + date.get(Calendar.YEAR) + "']";
    }

    public void chooseDate(String dataK){
        WebElement data = driver.findElement(By.xpath(dataK));
        data.click();
    }

    private Calendar formatDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(date);
        return givenDate;
    }

    //TODO: method's name, var's names, too complicated logic ?
    private void calcDateP(int month1, int month2, Calendar givenDate) {
        if (driver.findElements(By.xpath(createPathDate(givenDate))).size() != 0) {
            WebElement we = driver.findElement(By.xpath(createPathDate(givenDate)));
            we.click();
        } else {
            int diff = month2 - month1;
            if (diff > 0) {
                diff--;
                WebElement we = null;
                if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_2)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                else
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT));
                for (int i = 0; i < diff; i++) {
                    we.click();
                    if (i > 0) {
                        if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_2)).size() != 0)
                            we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                    }
                }
            } else if (diff < 0) {
                WebElement we = null;
                if (driver.findElements(By.xpath(XPATH_MONTH_PREV_1)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_PREV_1));
                else
                    we = driver.findElement(By.xpath(XPATH_MONTH_PREV));
                for (int i = 0; i > diff; i--) {
                    we.click();
                    if (i < 0) {
                        if (driver.findElements(By.xpath(XPATH_MONTH_PREV_1)).size() != 0)
                            we = driver.findElement(By.xpath(XPATH_MONTH_PREV_1));
                    }
                }
            }
            selectDate(givenDate);
        }
    }

    //TODO fix method name, duplicated code, fix var's names, whole method looks too complicated
    private void calcDateK(int month1, int month2, int month3, Calendar givenDate){
        if(driver.findElements(By.xpath(createPathDate(givenDate))).size() != 0) {
            WebElement we = driver.findElement(By.xpath(createPathDate(givenDate)));
            we.click();
        }
        else {
            int diff = 0;
            if (month2 <= month1) diff = month2 - month3;
            else diff = month3 - month2;
            if (diff > 0) {
                WebElement we = null;
                if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_2)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                else
                    if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_1)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_1));
                else
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT));
                for (int i = 0; i < diff; i++) {
                    we.click();
                    if (i > 0 && month3 > month1) {
                        if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_2)).size() != 0)
                            we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                    }
                }
            } else if (diff < 0) {
                if(-diff>month1) diff++;
                WebElement we = null;
                boolean n2 = false;
                if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_1)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_1));
                else if (driver.findElements(By.xpath(XPATH_MONTH_NEXT_2)).size() != 0)
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                else
                    we = driver.findElement(By.xpath(XPATH_MONTH_NEXT));
                int j=0;
                int d = month1 - month2;
                for (int i = 0; i > diff; i--) {
                    if(n2) we = driver.findElement(By.xpath(XPATH_MONTH_NEXT_2));
                    if(d==j) {
                        we = driver.findElement(By.xpath(XPATH_MONTH_NEXT));
                        n2 = true;

                    }
                    we.click();
                    j++;
                }
            }
            selectDate(givenDate);
        }
    }

    //TODO, do we need this ? fix method's name, var's names
    public void chooseDate2(String dataP, String dataK) {
        Calendar today = new GregorianCalendar();
        Calendar dateP = formatDate(dataP);
        Calendar dateK = formatDate(dataK);

        int month1 = today.get(Calendar.MONTH);
        int month2 = dateP.get(Calendar.MONTH);
        int month3 = dateK.get(Calendar.MONTH);
        calcDateP(month1, month2, dateP);
        calcDateK(month1, month2, month3, dateK);
    }

    public void selectDate(Calendar givenDate){
        WebElement data = driver.findElement(By.xpath(createPathDate(givenDate)));
        data.click();
    }

    public void clickCalendar(String pathB){
        WebElement calendar = driver.findElement(By.xpath(pathB));
        calendar.click();
    }

}