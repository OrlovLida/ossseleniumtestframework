package com.oss.framework.components.common;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimePeriodChooser extends Widget {

    private final String componentId;

    public enum TimePeriodChooserOption {
        PERIOD, RANGE, LAST
    }

    private TimePeriodChooser(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement webElement) {
        super(driver, webDriverWait, widgetId, webElement);

        this.componentId = widgetId;
    }

    public static TimePeriodChooser create(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        String xPath = "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']";

        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new TimePeriodChooser(driver, webDriverWait, widgetId, webElement);
    }

    public void chooseOption(TimePeriodChooserOption option) {

        WebElement calendar = webElement.findElement(By.xpath("//i[contains(@class,'OSSIcon fa fa-calendar')]"));

        Actions actions = new Actions(driver);
        actions.moveToElement(calendar).click().build().perform();

        switch (option) {
            case LAST: {
                webElement.findElement(By.xpath(createChooseOptionXPath("LAST_2"))).click();
                break;
            }
            case RANGE: {
                webElement.findElement(By.xpath(createChooseOptionXPath("RANGE_1"))).click();
                break;
            }
            case PERIOD: {
                webElement.findElement(By.xpath(createChooseOptionXPath("PERIOD_0"))).click();
                break;
            }
        }
    }

    public void setLastPeriod(Integer days, Integer hours, Integer minutes) {
        WebElement daysInput = webElement.findElement(By.xpath("//input[@label='Days']"));
        daysInput.sendKeys(days.toString());

        WebElement hoursInput = webElement.findElement(By.xpath("//input[@label='Hours']"));
        hoursInput.sendKeys(hours.toString());

        WebElement minutesInput = webElement.findElement(By.xpath("//input[@label='Minutes']"));
        minutesInput.sendKeys(minutes.toString());
    }

    public void clickClearValue() {
        webElement.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']//i[contains(@class,'OSSIcon ossfont-close')]")).click();
    }

    private String createChooseOptionXPath(String option) {

        return "//div[contains(@class,'main-options common-options')]//label[@for='time-period-options_" + option + "']";
    }

}