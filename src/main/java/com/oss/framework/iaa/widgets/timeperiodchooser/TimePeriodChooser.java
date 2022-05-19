package com.oss.framework.iaa.widgets.timeperiodchooser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class TimePeriodChooser extends Widget {

    private static final String CALENDAR_XPATH = ".//i[contains(@class,'OSSIcon fa fa-calendar')]";
    private static final String DAYS_ID = "days-number-field";
    private static final String HOURS_ID = "hours-number-field";
    private static final String MINUTES_ID = "minutes-number-field";
    private static final String FROM_ID = "start-date";
    private static final String TO_ID = "end-date";
    private static final String CLOSE_ICON_CSS = ".ossfont-close";

    private TimePeriodChooser(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement webElement) {
        super(driver, webDriverWait, widgetId, webElement);
    }

    public static TimePeriodChooser create(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        String xPath = "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']";

        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new TimePeriodChooser(driver, webDriverWait, widgetId, webElement);
    }

    public void chooseOption(TimePeriodChooserOption option) {
        WebElement calendar = webElement.findElement(By.xpath(CALENDAR_XPATH));
        WebElementUtils.clickWebElement(driver, calendar);
        switch (option) {
            case LAST: {
                DropdownList.create(driver, webDriverWait).selectOption("Last");
                break;
            }
            case RANGE: {
                DropdownList.create(driver, webDriverWait).selectOption("Range");
                break;
            }
            case PERIOD: {
                DropdownList.create(driver, webDriverWait).selectOption("Period");
                break;
            }
        }
    }

    public void setLastPeriod(Integer days, Integer hours, Integer minutes) {
        setTextField(DAYS_ID, days.toString());
        setTextField(HOURS_ID, hours.toString());
        setTextField(MINUTES_ID, minutes.toString());
    }

    public void setRangePeriod(String fromDate, String toDate) {
        setTextField(FROM_ID, fromDate);
        setTextField(TO_ID, toDate);
    }

    public void clickClearValue() {
        webElement.findElement(By.cssSelector(CLOSE_ICON_CSS)).click();
    }

    public enum TimePeriodChooserOption {
        PERIOD, RANGE, LAST
    }

    private void setTextField(String componentId, String value) {
        ComponentFactory.create(componentId, Input.ComponentType.TEXT_FIELD, driver, webDriverWait)
                .setSingleStringValue(value);
    }
}