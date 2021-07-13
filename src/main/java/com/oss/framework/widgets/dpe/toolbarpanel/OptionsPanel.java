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

    private static final String OPTIONS_BUTTON_ID = "options-menu-button";
    private static final String TIME_PERIOD_CHOOSER_PATH = "//div[@data-testid = 'time-period-chooser']";
    private static final String TIME_PERIOD_CHOOSER_INPUT_PATH = "//div[@class='md-input']//input[@label='%s']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement input;

    public static OptionsPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new OptionsPanel(driver, webDriverWait, webElement);
    }

    private OptionsPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.input = webElement;
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
        fillInput(days, "Days");
        fillInput(hours, "Hours");
        fillInput(minutes, "Minutes");
    }

    private void fillInput(Integer value, String label) {
        String timePeriodInputXpath = String.format(TIME_PERIOD_CHOOSER_INPUT_PATH, label);
        WebElement timePeriodInput = input.findElement(By.xpath(timePeriodInputXpath));
        timePeriodInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        timePeriodInput.sendKeys(Keys.DELETE);
        timePeriodInput.sendKeys(value.toString());
    }

    public void chooseTimePeriodOption(TimePeriodChooserOption option) {
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (option) {
            case LAST: {
                input.findElement(By.xpath(createChooseOptionXPath("LAST_2"))).click();
                break;
            }
            case RANGE: {
                input.findElement(By.xpath(createChooseOptionXPath("RANGE_1"))).click();
                break;
            }
            case PERIOD: {
                input.findElement(By.xpath(createChooseOptionXPath("PERIOD_0"))).click();
                break;
            }
            case MIDDLE: {
                input.findElement(By.xpath(createChooseOptionXPath("MIDDLE_3"))).click();
                break;
            }
            case SMART: {
                input.findElement(By.xpath(createChooseOptionXPath("SMART_4"))).click();
                break;
            }
            case LATEST: {
                input.findElement(By.xpath(createChooseOptionXPath("LATEST_5"))).click();
                break;
            }
        }
    }

    private String createChooseOptionXPath(String option) {
        return "//div[contains(@class,'main-options common-options')]//label[@for='time-options_" + option + "']";
    }
}
