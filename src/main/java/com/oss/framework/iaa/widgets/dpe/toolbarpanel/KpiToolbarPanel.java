package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class KpiToolbarPanel extends Widget {

    static final String KPI_TOOLBAR_PATH = "//div[@class='toolbarPanel']";
    private static final Logger log = LoggerFactory.getLogger(KpiToolbarPanel.class);
    private static final String APPLY_BUTTON_ID = "apply-button";
    private static final String DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH = ".//div[@data-testid='dropdown_list_type_display_data']";
    private static final String TOP_N_BUTTON_ID = "top-n-button";
    private static final String OPTIONS_BUTTON_ID = "options-menu-button";
    private static final String LAYOUT_BUTTON_ID = "layout-button";
    private static final String OPENED_TOP_N_PANEL_XPATH = "//div[@class='window']/div[@data-testid='drill-down-menu']";
    private static final String OPENED_OPTIONS_PANEL_XPATH = "//div[@class='window']/div[@data-testid='options-menu']";
    private static final String OPENED_LAYOUT_PANEL_XPATH = "//div[@class='window']/div[@data-testid='layout-template-menu']";
    private static final String EXPORT_BUTTON_ID = "export-button";
    private static final String OPENED_EXPORT_PANEL_XPATH = ".//div[@class='window']/div[@data-testid='export-menu']";
    private static final String OPENED_FILTERS_PANEL_XPATH = ".//div[@class='window']/div[@data-testid='filter-menu']";
    private static final String FILTER_BUTTON_ID = "filter-button";
    private static final String CLICK_BTN = "Clicking button: ";
    private static final String WIDGET_ID = "_Data_View";

    private KpiToolbarPanel(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static KpiToolbarPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_TOOLBAR_PATH);
        return new KpiToolbarPanel(driver, wait, WIDGET_ID);
    }

    public LayoutPanel openLayoutPanel() {
        if (!isLayoutPanelOpen()) {
            clickLayoutButton();
        }
        return LayoutPanel.create(driver, webDriverWait);
    }

    public OptionsPanel openOptionsPanel() {
        if (!isOptionsPanelOpen()) {
            clickOptionsButton();
        }
        return OptionsPanel.create(driver, webDriverWait);
    }

    public void closeOptionsPanel() {
        if (isOptionsPanelOpen()) {
            clickOptionsButton();
        }
    }

    public TopNPanel openTopNPanel() {
        if (!isTopNPanelOpen()) {
            clickTopNButton();
        }
        return TopNPanel.create(driver, webDriverWait);
    }

    public ExportPanel openExportPanel() {
        if (!isExportPanelOpen()) {
            clickExportButton();
        }
        return ExportPanel.create(driver, webDriverWait);
    }

    public FiltersPanel openFilterPanel() {
        if (!isFilterPanelOpen()) {
            clickFilterButton();
        }
        return FiltersPanel.create(driver, webDriverWait);
    }

    public void clickApply() {
        Button applyButton = Button.createById(driver, APPLY_BUTTON_ID);
        DelayUtils.sleep(5000);
        applyButton.click();

        log.debug(CLICK_BTN + "Apply");
    }

    public void selectDisplayType(String displayTypeId) {
        webElement.findElement(By.xpath(DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH)).click();
        DropdownList.create(driver, webDriverWait).selectOptionById(displayTypeId);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private boolean isTopNPanelOpen() {
        return !driver.findElements(By.xpath(OPENED_TOP_N_PANEL_XPATH)).isEmpty();
    }

    private void clickTopNButton() {
        Button.createById(driver, TOP_N_BUTTON_ID).click();
        log.debug(CLICK_BTN + "TopN");
    }

    private boolean isOptionsPanelOpen() {
        return !driver.findElements(By.xpath(OPENED_OPTIONS_PANEL_XPATH)).isEmpty();
    }

    private void clickOptionsButton() {
        Button.createById(driver, OPTIONS_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Options");
    }

    private boolean isLayoutPanelOpen() {
        return !driver.findElements(By.xpath(OPENED_LAYOUT_PANEL_XPATH)).isEmpty();
    }

    private void clickLayoutButton() {
        Button.createById(driver, LAYOUT_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Layout");
    }

    private boolean isExportPanelOpen() {
        return !driver.findElements(By.xpath(OPENED_EXPORT_PANEL_XPATH)).isEmpty();
    }

    private void clickExportButton() {
        Button.createById(driver, EXPORT_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Export");
    }

    private boolean isFilterPanelOpen() {
        return !driver.findElements(By.xpath(OPENED_FILTERS_PANEL_XPATH)).isEmpty();
    }

    private void clickFilterButton() {
        Button.createById(driver, FILTER_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Filters");
    }
}
