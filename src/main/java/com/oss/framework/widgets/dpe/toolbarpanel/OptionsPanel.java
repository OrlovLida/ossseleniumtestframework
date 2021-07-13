package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel.KPI_TOOLBAR_PATH;

public class OptionsPanel {

    private static final Logger log = LoggerFactory.getLogger(OptionsPanel.class);

    private final static String OPTIONS_BUTTON_ID = "options-menu-button";
    private final static String TIME_PERIOD_CHOOSER_PATH = "//div[@data-testid = 'time-period-chooser']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static OptionsPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new OptionsPanel(driver, webDriverWait, webElement);
    }

    private OptionsPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public enum TimePeriodChooserOption {
        PERIOD, RANGE, LAST, MIDDLE, SMART, LATEST
    }

    private void moveOverElement(String elementPath) {
        Actions action = new Actions(driver);
        action.moveToElement(this.driver.findElement(By.xpath(elementPath))).build().perform();
    }

    public void chooseTimePeriod() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, OPTIONS_BUTTON_ID).click();
        log.debug("Click options button");
        moveOverElement(TIME_PERIOD_CHOOSER_PATH);
    }

    public void setLastPeriodOption(Integer days, Integer hours, Integer minutes) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement daysInput = webElement.findElement(By.xpath("//div[@class='md-input']//input[@label='Days']"));
        daysInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        daysInput.sendKeys(Keys.DELETE);
        daysInput.sendKeys(days.toString());

        WebElement hoursInput = webElement.findElement(By.xpath("//div[@class='md-input']//input[@label='Hours']"));
        hoursInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        hoursInput.sendKeys(Keys.DELETE);
        hoursInput.sendKeys(hours.toString());

        WebElement minutesInput = webElement.findElement(By.xpath("//div[@class='md-input']//input[@label='Minutes']"));
        minutesInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        minutesInput.sendKeys(Keys.DELETE);
        minutesInput.sendKeys(minutes.toString());
    }

    public void chooseTimePeriodOption(TimePeriodChooserOption option) {
        DelayUtils.waitForPageToLoad(driver, wait);

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
            case MIDDLE: {
                webElement.findElement(By.xpath(createChooseOptionXPath("MIDDLE_3"))).click();
                break;
            }
            case SMART: {
                webElement.findElement(By.xpath(createChooseOptionXPath("SMART_4"))).click();
                break;
            }
            case LATEST: {
                webElement.findElement(By.xpath(createChooseOptionXPath("LATEST_5"))).click();
                break;
            }
        }
    }

    private String createChooseOptionXPath(String option) {
        return "//div[contains(@class,'main-options common-options')]//label[@for='time-options_" + option + "']";
    }
}
