package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.CSSUtils;
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
    private static final String AGGREGATION_METHOD_CHOOSER_PATH = "//div[@data-testid = 'aggregation-method-button']";
    private static final String AGGREGATION_METHOD_CHOOSER_INPUT_PATH = "//*[contains(@class,'list-group')]//*[@data-testid='%s']";
    private static final String ACTIVE_AGGREGATION_METHOT_XPATH = "//*[contains(@class,'list-group')]//*[@class='list-group-item active']";
    private static final String Y_AXIS_SETTINGS_PATH = "//*[@data-testid = 'y-axis-options-button']";
    private static final String Y_AXIS_SETTINGS_INPUT_PATH = "//label[contains(@for,'%s')]";
    private static final String MISCELLANEOUS_OPTIONS_PATH = "//*[@data-testid = 'miscellaneous-options-common-form']";
    private static final String OPTIONS_INPUT_ID = "//*[@data-testid = '%s']";
    private static final String COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH = "//*[@data-testid = 'compare-with-period-common-form']";

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

    public enum AggregationMethodOption {
        MIN, MAX, AVG, SUM, COUNT
    }

    public enum YAxisOption {
        AUTO, MANUAL
    }

    public enum MiscellaneousOption {
        DATA_COMPLETENESS, LAST_SAMPLE_TIME
    }

    private void moveOverElement(String elementPath) {
        Actions action = new Actions(driver);
        action.moveToElement(this.driver.findElement(By.xpath(elementPath))).build().perform();
    }

    public void chooseTimePeriod() {
        clickOnOptionsButton();
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

    public void chooseAggregationMethodOption(AggregationMethodOption aggregationMethod) {
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (aggregationMethod) {
            case MIN: {
                input.findElement(By.xpath(createChooseAggregationMethodXPath("Min"))).click();
                break;
            }
            case MAX: {
                input.findElement(By.xpath(createChooseAggregationMethodXPath("Max"))).click();
                break;
            }
            case AVG: {
                input.findElement(By.xpath(createChooseAggregationMethodXPath("Avg"))).click();
                break;
            }
            case SUM: {
                input.findElement(By.xpath(createChooseAggregationMethodXPath("Sum"))).click();
                break;
            }
            case COUNT: {
                input.findElement(By.xpath(createChooseAggregationMethodXPath("Count"))).click();
                break;
            }
        }
    }

    private String createChooseAggregationMethodXPath(String option) {
        return String.format(AGGREGATION_METHOD_CHOOSER_INPUT_PATH, option);
    }

    public void chooseAggregationMethod() {
        clickOnOptionsButton();
        moveOverElement(AGGREGATION_METHOD_CHOOSER_PATH);
    }

    private void clickOnOptionsButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, OPTIONS_BUTTON_ID).click();
        log.debug("Click options button");
    }

    public String getActiveAggregationMethod() {
        chooseAggregationMethod();
        WebElement activeAggregationMethod = driver.findElement(By.xpath(ACTIVE_AGGREGATION_METHOT_XPATH));
        String activeAggMethod = activeAggregationMethod.getAttribute(CSSUtils.TEST_ID).toUpperCase();
        clickOnOptionsButton();

        return activeAggMethod;
    }

    public void chooseYAxisOption() {
        clickOnOptionsButton();
        moveOverElement(Y_AXIS_SETTINGS_PATH);
    }

    private String createChooseYAxisOptionXPath(String option) {
        return String.format(Y_AXIS_SETTINGS_INPUT_PATH, option);
    }

    private String createXPathByDataTestId(String option) {
        return String.format(OPTIONS_INPUT_ID, option);
    }

    public void setYAxisOption(YAxisOption yAxisOption) {
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (yAxisOption) {
            case MANUAL: {
                input.findElement(By.xpath(createChooseYAxisOptionXPath("manual"))).click();
                break;
            }
            case AUTO: {
                input.findElement(By.xpath(createChooseYAxisOptionXPath("auto"))).click();
                break;
            }
        }
    }

    public void chooseMiscellaneousOption() {
        clickOnOptionsButton();
        moveOverElement(MISCELLANEOUS_OPTIONS_PATH);
    }

    public void setMiscellaneousOption(MiscellaneousOption miscellaneousOption) {
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (miscellaneousOption) {
            case LAST_SAMPLE_TIME: {
                input.findElement(By.xpath(createXPathByDataTestId("ShowLastSampleChanged"))).click();
                break;
            }
            case DATA_COMPLETENESS: {
                input.findElement(By.xpath(createXPathByDataTestId("CompletenessChanged"))).click();
                break;
            }
        }
    }

    public void setOtherPeriodOption() {
        clickOnOptionsButton();
        moveOverElement(COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH);
        input.findElement(By.xpath(createXPathByDataTestId("OtherPeriodEnabled"))).click();
    }
}
