package com.oss.framework.widgets.dpe.toolbarpanel;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OptionsPanel {

    private static final Logger log = LoggerFactory.getLogger(OptionsPanel.class);

    private final static String OPTIONS_PANEL_XPATH = "//div[@data-testid='options-menu']";
    private static final String TIME_PERIOD_CHOOSER_PATH = "//div[@data-testid = 'time-period-chooser']";
    private static final String TIME_PERIOD_CHOOSER_INPUT_PATH = "//div[@class='md-input']//input[@label='%s']";
    private static final String AGGREGATION_METHOD_CHOOSER_PATH = "//div[@data-testid = 'aggregation-method-button']";
    private static final String AGGREGATION_METHOD_CHOOSER_INPUT_PATH = "//*[contains(@class,'list-group')]//*[@data-testid='%s']";
    private static final String ACTIVE_AGGREGATION_METHOT_XPATH = ".//*[@class='list-group-item active']";
    private static final String Y_AXIS_SETTINGS_PATH = "//*[@data-testid = 'y-axis-options-button']";
    private static final String Y_AXIS_SETTINGS_INPUT_PATH = "//label[contains(@for,'%s')]";
    private static final String MISCELLANEOUS_OPTIONS_PATH = "//*[@data-testid = 'miscellaneous-options-common-form']";
    private static final String OPTIONS_INPUT_ID = "//*[@data-testid = '%s']";
    private static final String COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH = "//*[@data-testid = 'compare-with-period-common-form']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement optionsPanel;

    public static OptionsPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement optionsPanel = driver.findElement(By.xpath(OPTIONS_PANEL_XPATH));

        return new OptionsPanel(driver, webDriverWait, optionsPanel);
    }

    private OptionsPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.optionsPanel = webElement;
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
        DATA_COMPLETENESS, LAST_SAMPLE_TIME
    }

    private void moveOverElement(String elementPath) {
        Actions action = new Actions(driver);
        action.moveToElement(this.driver.findElement(By.xpath(elementPath))).build().perform();
    }

    private void chooseTimePeriod() {
        moveOverElement(TIME_PERIOD_CHOOSER_PATH);
    }

    public void setLastPeriodOption(Integer days, Integer hours, Integer minutes) {
        chooseTimePeriod();
        DelayUtils.waitForPageToLoad(driver, wait);
        fillInput(days, "Days");
        fillInput(hours, "Hours");
        fillInput(minutes, "Minutes");
    }

    private void fillInput(Integer value, String label) {
        String timePeriodInputXpath = String.format(TIME_PERIOD_CHOOSER_INPUT_PATH, label);
        WebElement timePeriodInput = optionsPanel.findElement(By.xpath(timePeriodInputXpath));
        timePeriodInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        timePeriodInput.sendKeys(Keys.DELETE);
        timePeriodInput.sendKeys(value.toString());
    }

    public void chooseTimePeriodOption(TimePeriodChooserOption option) {
        chooseTimePeriod();
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (option) {
            case LAST: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("LAST_2"))).click();
                break;
            }
            case RANGE: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("RANGE_1"))).click();
                break;
            }
            case PERIOD: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("PERIOD_0"))).click();
                break;
            }
            case MIDDLE: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("MIDDLE_3"))).click();
                break;
            }
            case SMART: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("SMART_4"))).click();
                break;
            }
            case LATEST: {
                optionsPanel.findElement(By.xpath(createChooseOptionXPath("LATEST_5"))).click();
                break;
            }
        }
        log.debug("Setting time period option: {}", option);
    }

    private String createChooseOptionXPath(String option) {
        return "//div[contains(@class,'main-options common-options')]//label[@for='time-options_" + option + "']";
    }

    public void chooseAggregationMethodOption(AggregationMethodOption aggregationMethod) {
        chooseAggregationMethod();
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (aggregationMethod) {
            case MIN: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("Min"))).click();
                break;
            }
            case MAX: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("Max"))).click();
                break;
            }
            case AVG: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("Avg"))).click();
                break;
            }
            case SUM: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("Sum"))).click();
                break;
            }
            case COUNT: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("Count"))).click();
                break;
            }
            case NONE: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("None"))).click();
            }
            case AGG_STANDARD: {
                optionsPanel.findElement(By.xpath(createChooseAggregationMethodXPath("AGGStandard"))).click();
            }
        }
        log.debug("Selecting aggregation method: {}", aggregationMethod);
    }

    private String createChooseAggregationMethodXPath(String option) {
        return String.format(AGGREGATION_METHOD_CHOOSER_INPUT_PATH, option);
    }

    public List<AggregationMethodOption> getActiveAggregationMethods() {
        chooseAggregationMethod();
        List<WebElement> webElementsAgg = optionsPanel.findElements(By.xpath(ACTIVE_AGGREGATION_METHOT_XPATH));
        List<AggregationMethodOption> activeAggMethods = new ArrayList<>();
        for (String aggMethodId : activeAggMethodsIds(webElementsAgg)) {
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
            }
        }
        return activeAggMethods;
    }

    private List<String> activeAggMethodsIds(List<WebElement> webElementsAgg) {
        return webElementsAgg.stream().map(aggMethod -> CSSUtils.getAttributeValue("data-testid", aggMethod))
                .collect(Collectors.toList());
    }

    private void chooseAggregationMethod() {
        moveOverElement(AGGREGATION_METHOD_CHOOSER_PATH);
    }

    public void setYAxisOption(YAxisOption yAxisOption) {
        moveOverElement(Y_AXIS_SETTINGS_PATH);
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (yAxisOption) {
            case MANUAL: {
                optionsPanel.findElement(By.xpath(createChooseYAxisOptionXPath("manual"))).click();
                break;
            }
            case AUTO: {
                optionsPanel.findElement(By.xpath(createChooseYAxisOptionXPath("auto"))).click();
                break;
            }
        }
        log.debug("Setting Y axis option to: {}", yAxisOption);
    }

    private String createChooseYAxisOptionXPath(String option) {
        return String.format(Y_AXIS_SETTINGS_INPUT_PATH, option);
    }

    public void setMiscellaneousOption(MiscellaneousOption miscellaneousOption) {
        moveOverElement(MISCELLANEOUS_OPTIONS_PATH);
        DelayUtils.waitForPageToLoad(driver, wait);

        switch (miscellaneousOption) {
            case LAST_SAMPLE_TIME: {
                optionsPanel.findElement(By.xpath(createXPathByDataTestId("ShowLastSampleChanged"))).click();
                break;
            }
            case DATA_COMPLETENESS: {
                optionsPanel.findElement(By.xpath(createXPathByDataTestId("CompletenessChanged"))).click();
                break;
            }
        }
        log.debug("Setting show: {}", miscellaneousOption);
    }

    public void setOtherPeriodOption() {
        moveOverElement(COMPARE_WITH_OTHER_PERIOD_OPTIONS_PATH);
        optionsPanel.findElement(By.xpath(createXPathByDataTestId("OtherPeriodEnabled"))).click();
        log.debug("Setting compare with other period option");
    }

    private String createXPathByDataTestId(String option) {
        return String.format(OPTIONS_INPUT_ID, option);
    }
}
