package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class OptionsPanel {

    private static final Logger log = LoggerFactory.getLogger(OptionsPanel.class);

    private static final String OPTIONS_PANEL_XPATH = "//div[@" + CSSUtils.TEST_ID + "='options-menu']";
    private static final String TIME_PERIOD_CHOOSER_PATH = "//div[@" + CSSUtils.TEST_ID + "='input_time-period-chooser']";
    private static final String TIME_PERIOD_CHOOSER_INPUT_PATH = "//div[@class='md-input']//input[@label='%s']";
    private static final String AGGREGATION_METHOD_CHOOSER_PATH = "//div[@" + CSSUtils.TEST_ID + "='aggregation-method-button']";
    private static final String AGGREGATION_METHOD_CHOOSER_INPUT_PATH = "//*[contains(@class,'list-group')]//*[@" + CSSUtils.TEST_ID + "='%s']";
    private static final String ACTIVE_AGGREGATION_METHOT_XPATH = ".//*[@class='list-group-item active']";
    private static final String Y_AXIS_SETTINGS_PATH = "//*[@" + CSSUtils.TEST_ID + "='y-axis-options-button']";
    private static final String Y_AXIS_SETTINGS_INPUT_PATH = "//label[contains(@for,'%s')]";
    private static final String MISCELLANEOUS_OPTIONS_PATH = "//*[@" + CSSUtils.TEST_ID + "='miscellaneous-options-common-form']";
    private static final String COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH = "//*[@" + CSSUtils.TEST_ID + "='compare-with-period-common-form']";
    private static final String AGGREGATION_METHODS_SELECT_XPATH = "//*[@" + CSSUtils.TEST_ID + "='aggregation-method-select']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement optionsPanelElement;

    private OptionsPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.optionsPanelElement = webElement;
    }

    public static OptionsPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement optionsPanel = driver.findElement(By.xpath(OPTIONS_PANEL_XPATH));

        return new OptionsPanel(driver, webDriverWait, optionsPanel);
    }

    public void setLastPeriodOption(Integer days, Integer hours, Integer minutes) {
        chooseTimePeriod();
        DelayUtils.waitForPageToLoad(driver, wait);
        fillInput(days, "Days");
        fillInput(hours, "Hours");
        fillInput(minutes, "Minutes");
    }

    public void chooseTimePeriodOption(TimePeriodChooserOption option) {
        chooseTimePeriod();
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (option) {
            case LAST: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("LAST_2"))).click();
                break;
            }
            case RANGE: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("RANGE_1"))).click();
                break;
            }
            case PERIOD: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("PERIOD_0"))).click();
                break;
            }
            case MIDDLE: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("MIDDLE_3"))).click();
                break;
            }
            case SMART: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("SMART_4"))).click();
                break;
            }
            case LATEST: {
                optionsPanelElement.findElement(By.xpath(createChooseOptionXPath("LATEST_5"))).click();
                break;
            }
        }
        log.debug("Setting time period option: {}", option);
    }

    public void chooseAggregationMethodOption(AggregationMethodOption aggregationMethod) {
        chooseAggregationMethod();
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (aggregationMethod) {
            case MIN: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("Min"))).click();
                break;
            }
            case MAX: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("Max"))).click();
                break;
            }
            case AVG: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("Avg"))).click();
                break;
            }
            case SUM: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("Sum"))).click();
                break;
            }
            case COUNT: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("Count"))).click();
                break;
            }
            case NONE: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("None"))).click();
                break;
            }
            case AGG_STANDARD: {
                optionsPanelElement.findElement(By.xpath(createChooseAggregationMethodXPath("AGGStandard"))).click();
                break;
            }
        }
        log.debug("Selecting aggregation method: {}", aggregationMethod);
    }

    public List<AggregationMethodOption> getActiveAggregationMethods() {
        chooseAggregationMethod();
        List<AggregationMethodOption> activeAggMethods = new ArrayList<>();
        for (String aggMethodId : getActiveAggMethodsIds()) {
            switch (aggMethodId) {
                case "Min": {
                    activeAggMethods.add(AggregationMethodOption.MIN);
                    break;
                }
                case "Max": {
                    activeAggMethods.add(AggregationMethodOption.MAX);
                    break;
                }
                case "Avg": {
                    activeAggMethods.add(AggregationMethodOption.AVG);
                    break;
                }
                case "Sum": {
                    activeAggMethods.add(AggregationMethodOption.SUM);
                    break;
                }
                case "Count": {
                    activeAggMethods.add(AggregationMethodOption.COUNT);
                    break;
                }
                case "None": {
                    activeAggMethods.add(AggregationMethodOption.NONE);
                    break;
                }
                case "AggStandard": {
                    activeAggMethods.add(AggregationMethodOption.AGG_STANDARD);
                    break;
                }
                default:
                    return activeAggMethods;
            }
        }
        return activeAggMethods;
    }

    public void setYAxisOption(YAxisOption yAxisOption) {
        moveOverElement(Y_AXIS_SETTINGS_PATH);
        DelayUtils.waitForPageToLoad(driver, wait);

        if (yAxisOption == YAxisOption.MANUAL) {
            optionsPanelElement.findElement(By.xpath(createChooseYAxisOptionXPath("manual"))).click();
        } else if (yAxisOption == YAxisOption.AUTO) {
            optionsPanelElement.findElement(By.xpath(createChooseYAxisOptionXPath("auto"))).click();
        }
        log.debug("Setting Y axis option to: {}", yAxisOption);
    }

    public void setMiscellaneousOption(MiscellaneousOption miscellaneousOption) {
        moveOverElement(MISCELLANEOUS_OPTIONS_PATH);
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (miscellaneousOption) {
            case LAST_SAMPLE_TIME: {
                setSwitcherOn("ShowLastSampleChanged");
                break;
            }
            case DATA_COMPLETENESS: {
                setSwitcherOn("CompletenessChanged");
                break;
            }
            case SHOW_TIME_ZONE: {
                setSwitcherOn("ShowTimeZone");
                break;
            }
        }
        log.debug("Setting show: {}", miscellaneousOption);
    }

    public void setOtherPeriodOption() {
        moveOverElement(COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH);
        setSwitcherOn("OtherPeriodEnabled");
        log.debug("Setting compare with other period option");
    }

    private void setSwitcherOn(String switcherId) {
        ComponentFactory.create(switcherId, Input.ComponentType.SWITCHER, driver, wait)
                .setValue(Data.createSingleData("true"));
    }

    private void moveOverElement(String elementPath) {
        Actions action = new Actions(driver);
        action.moveToElement(this.driver.findElement(By.xpath(elementPath))).build().perform();
    }

    private void chooseTimePeriod() {
        moveOverElement(TIME_PERIOD_CHOOSER_PATH);
    }

    private void fillInput(Integer value, String label) {
        String timePeriodInputXpath = String.format(TIME_PERIOD_CHOOSER_INPUT_PATH, label);
        WebElement timePeriodInput = optionsPanelElement.findElement(By.xpath(timePeriodInputXpath));
        timePeriodInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        timePeriodInput.sendKeys(Keys.DELETE);
        timePeriodInput.sendKeys(value.toString());
    }

    private String createChooseOptionXPath(String option) {
        return "//div[contains(@class,'main-options common-options')]//label[@for='time-options_" + option + "']";
    }

    private String createChooseAggregationMethodXPath(String option) {
        return String.format(AGGREGATION_METHOD_CHOOSER_INPUT_PATH, option);
    }

    private List<String> getActiveAggMethodsIds() {
        WebElement aggregationMethodList = driver.findElement(By.xpath(AGGREGATION_METHODS_SELECT_XPATH));
        List<WebElement> webElementsAgg = aggregationMethodList.findElements(By.xpath(ACTIVE_AGGREGATION_METHOT_XPATH));
        return webElementsAgg.stream().map(aggMethod -> CSSUtils.getAttributeValue("data-testid", aggMethod))
                .collect(Collectors.toList());
    }

    private void chooseAggregationMethod() {
        moveOverElement(AGGREGATION_METHOD_CHOOSER_PATH);
    }

    private String createChooseYAxisOptionXPath(String option) {
        return String.format(Y_AXIS_SETTINGS_INPUT_PATH, option);
    }

    public enum TimePeriodChooserOption {
        PERIOD, RANGE, LAST, MIDDLE, SMART, LATEST
    }

    public enum AggregationMethodOption {
        MIN, MAX, AVG, SUM, COUNT, NONE, AGG_STANDARD
    }

    public enum YAxisOption {
        AUTO, MANUAL
    }

    public enum MiscellaneousOption {
        DATA_COMPLETENESS, LAST_SAMPLE_TIME, SHOW_TIME_ZONE
    }
}
