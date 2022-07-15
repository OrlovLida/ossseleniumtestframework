package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class KpiToolbarPanel extends Widget {

    static final String KPI_TOOLBAR_PATH = "//div[@class='toolbarPanel']";
    private static final String APPLY_BUTTON_ID = "apply-button";
    private static final String DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH = ".//div[@" + CSSUtils.TEST_ID + "='dropdown_list_type_display_data']";
    private static final String TOP_N_BUTTON_ID = "top-n-button";
    private static final String OPTIONS_BUTTON_ID = "options-menu-button";
    private static final String LAYOUT_BUTTON_ID = "layout-button";
    private static final String WINDOW_CONTENT_XPATH = "//div[@class='window']/div";
    private static final String OPENED_TOP_N_PANEL_XPATH = WINDOW_CONTENT_XPATH + "[@" + CSSUtils.TEST_ID + "='drill-down-menu']";
    private static final String OPENED_OPTIONS_PANEL_XPATH = WINDOW_CONTENT_XPATH + "[@" + CSSUtils.TEST_ID + "='options-menu']";
    private static final String OPENED_LAYOUT_PANEL_XPATH = WINDOW_CONTENT_XPATH + "[@" + CSSUtils.TEST_ID + "='layout-template-menu']";
    private static final String EXPORT_BUTTON_ID = "export-button";
    private static final String OPENED_EXPORT_PANEL_XPATH = "." + WINDOW_CONTENT_XPATH + "[@" + CSSUtils.TEST_ID + "='export-menu']";
    private static final String OPENED_FILTERS_PANEL_XPATH = "." + WINDOW_CONTENT_XPATH + "[@" + CSSUtils.TEST_ID + "='filter-menu']";
    private static final String FILTER_BUTTON_ID = "filter-button";
    private static final String WIDGET_ID = "_Data_View";
    private static final String OPENED_OPTIONS_SIDE_PANEL_CSS = ".expanded[" + CSSUtils.TEST_ID + "='dockedPanel-right']";
    private static final String OPTIONS_SIDE_PANEL_BUTTON_LABEL = "Options";
    private static final String INACTIVE_ELEMENT_CSS = ".inactive";

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
        return TopNPanel.create(driver);
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
        return FiltersPanel.create(driver);
    }

    public OptionsSidePanel openOptionsSidePanel() {
        if (!isOptionsSidePanelOpen()) {
            clickOptionsSidePanelButton();
        }
        return OptionsSidePanel.create(driver, webDriverWait);
    }

    private void clickOptionsSidePanelButton() {
        Button.createByLabel(driver, OPTIONS_SIDE_PANEL_BUTTON_LABEL).click();
    }

    private boolean isOptionsSidePanelOpen() {
        return WebElementUtils.isElementPresent(webElement, By.cssSelector(OPENED_OPTIONS_SIDE_PANEL_CSS));
    }

    public void clickApply() {
        DelayUtils.waitForElementDisappear(webDriverWait, By.cssSelector(INACTIVE_ELEMENT_CSS));
        Button.createById(driver, APPLY_BUTTON_ID).click();
    }

    public void selectDisplayType(String displayTypeId) {
        webElement.findElement(By.xpath(DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH)).click();
        DropdownList.create(driver, webDriverWait).selectOptionById(displayTypeId);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private boolean isTopNPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.xpath(OPENED_TOP_N_PANEL_XPATH));
    }

    private void clickTopNButton() {
        Button.createById(driver, TOP_N_BUTTON_ID).click();
    }

    private boolean isOptionsPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.xpath(OPENED_OPTIONS_PANEL_XPATH));
    }

    private void clickOptionsButton() {
        Button.createById(driver, OPTIONS_BUTTON_ID).click();
    }

    private boolean isLayoutPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.xpath(OPENED_LAYOUT_PANEL_XPATH));
    }

    private void clickLayoutButton() {
        Button.createById(driver, LAYOUT_BUTTON_ID).click();
    }

    private boolean isExportPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.xpath(OPENED_EXPORT_PANEL_XPATH));
    }

    private void clickExportButton() {
        Button.createById(driver, EXPORT_BUTTON_ID).click();
    }

    private boolean isFilterPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.xpath(OPENED_FILTERS_PANEL_XPATH));
    }

    private void clickFilterButton() {
        Button.createById(driver, FILTER_BUTTON_ID).click();
    }
}
