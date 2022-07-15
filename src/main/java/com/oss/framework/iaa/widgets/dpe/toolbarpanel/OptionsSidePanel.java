package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;

public class OptionsSidePanel {

    private static final Logger log = LoggerFactory.getLogger(OptionsSidePanel.class);

    private static final String DOCKED_PANEL_ID = "dockedPanel-right";
    private static final String RADIO_BUTTONS_ID = "selection-mode";
    private static final String SELECT_CHARTS_ID = "chart-series-combobox";
    private static final String TREND_LINE_COMBO_BOX_ID = "trendLine";
    private static final String MISSING_DATA_COMBO_BOX_ID = "breakDuration";
    private static final String CUSTOM_BRAKE_DURATION = "Custom";
    private static final String MAXIMUM_DISTANCE_FIELD_ID = "maximumDistance";
    private static final String Y_AXIS_COMBO_BOX_ID = "chart-axis-combobox";
    private static final String Y_AXIS_MODE_COMBO_BOX_ID = "mode";
    private static final String Y_AXIS_MAX_VALUE_ID = "maxValue";
    private static final String Y_AXIS_MIN_VALUE_ID = "minValue";
    private static final String MANUAL_YAXIS_OPTION_LABEL = "Manual";
    private static final String SOFT_LIMITS_SWITCHER_ID = "enableSoftLimits";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement optionsPanelElement;

    private OptionsSidePanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.optionsPanelElement = webElement;
    }

    public static OptionsSidePanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement optionsSidePanel = driver.findElement(By.cssSelector("div[" + CSSUtils.TEST_ID + "='" + DOCKED_PANEL_ID + "']"));

        return new OptionsSidePanel(driver, webDriverWait, optionsSidePanel);
    }

    public void selectRadioButton(String value) {
        setValueInComponent(RADIO_BUTTONS_ID, value);
    }

    public void selectChartSeries(String chartOrSeries) {
        setValueInComponent(SELECT_CHARTS_ID, chartOrSeries);
    }

    public void selectTrendLineOption(String optionLabel) {
        Input trendLineCombobox = ComponentFactory.createFromParent(TREND_LINE_COMBO_BOX_ID, driver, wait, optionsPanelElement);
        trendLineCombobox.clear();
        trendLineCombobox.setSingleStringValue(optionLabel);
    }

    public void selectMissingDataOption(String optionLabel) {
        ComponentFactory.createFromParent(MISSING_DATA_COMBO_BOX_ID, driver, wait, optionsPanelElement).clearByAction();
        DropdownList.create(driver, wait).selectOption(optionLabel);
    }

    public String getMissingDataOption() {
        return ComponentFactory.createFromParent(MISSING_DATA_COMBO_BOX_ID, driver, wait, optionsPanelElement).getStringValue();
    }

    public void setMaximumDistance(String maxDistance) {
        if (isMissingDataOptionCustom()) {
            setValueInComponent(MAXIMUM_DISTANCE_FIELD_ID, maxDistance);
        } else {
            log.debug("Setting Maximum Distance is available only in Custom option");
        }
    }

    private boolean isMissingDataOptionCustom() {
        return getMissingDataOption().equals(CUSTOM_BRAKE_DURATION);
    }

    public void selectYAxis(String yaxis) {
        setValueInComponent(Y_AXIS_COMBO_BOX_ID, yaxis);
    }

    public void setYaxisMode(String mode) {
        setValueInComponent(Y_AXIS_MODE_COMBO_BOX_ID, mode);
    }

    public String getYaxisMode() {
        return ComponentFactory.createFromParent(Y_AXIS_MODE_COMBO_BOX_ID, driver, wait, optionsPanelElement).getStringValue();
    }

    public void setYaxisMaxAndMinValues(String maxValue, String minValue) {
        if (isManualYaxisMode()) {
            setValueInComponent(Y_AXIS_MAX_VALUE_ID, maxValue);
            setValueInComponent(Y_AXIS_MIN_VALUE_ID, minValue);
        } else {
            log.debug("Setting min and max Yaxis value is available only in Manual mode");
        }
    }

    public void setOnSoftLimits() {
        setValueInComponent(SOFT_LIMITS_SWITCHER_ID, "true");
    }

    private boolean isManualYaxisMode() {
        return getYaxisMode().equals(MANUAL_YAXIS_OPTION_LABEL);
    }

    private void setValueInComponent(String componentId, String value) {
        ComponentFactory.createFromParent(componentId, driver, wait, optionsPanelElement).setSingleStringValue(value);
    }
}
