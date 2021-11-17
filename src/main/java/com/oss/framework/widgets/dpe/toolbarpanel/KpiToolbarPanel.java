package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;

public class KpiToolbarPanel extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiToolbarPanel.class);

    final static String KPI_TOOLBAR_PATH = "//div[@class='toolbarPanel']";
    private final static String APPLY_BUTTON_ID = "apply-button";
    private static final String DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH = ".//div[@data-testid='dropdown_list_type_display_data']";
    private final static String TOP_N_BUTTON_ID = "top-n-button";
    private final static String OPTIONS_BUTTON_ID = "options-menu-button";
    private final static String OPENED_TOP_N_PANEL_XPATH = "//div[@class='window']/div[@data-testid='drill-down-menu']";
    private final static String OPENED_OPTIONS_PANEL_XPATH = "//div[@class='window']/div[@data-testid='options-menu']";

    private KpiToolbarPanel(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiToolbarPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_TOOLBAR_PATH);
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new KpiToolbarPanel(driver, webElement, wait);
    }

    public FiltersPanel getFiltersPanel() {
        return FiltersPanel.create(driver, webDriverWait);
    }

    public ExportPanel getExportPanel() {
        return ExportPanel.create(driver, webDriverWait);
    }

    public LayoutPanel getLayoutPanel() {
        return LayoutPanel.create(driver, webDriverWait);
    }

    public OptionsPanel openOptionsPanel() {
        if (!isOptionsPanelOpen()) {
            Button.createById(driver, OPTIONS_BUTTON_ID).click();
            log.debug(CLICK_BTN + "Options");
        }
        return OptionsPanel.create(driver, webDriverWait);
    }

    public void closeOptionsPanel() {
        if (isOptionsPanelOpen()) {
            Button.createById(driver, OPTIONS_BUTTON_ID).click();
            log.debug(CLICK_BTN + "Options");
        }
    }

    public TopNPanel openTopNPanel() {
        if (!isTopNPanelOpen()) {
            Button.createById(driver, TOP_N_BUTTON_ID).click();
            log.debug(CLICK_BTN + "TopN");
        }
        return TopNPanel.create(driver, webDriverWait);
    }

    public void clickApply() {
        Button applyButton = Button.createById(driver, APPLY_BUTTON_ID);
        DelayUtils.sleep(5000);
        applyButton.click();

        log.debug(CLICK_BTN + "Apply");
    }

    public void selectDisplayType(String displayTypeId) {
        webElement.findElement(By.xpath(DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH)).click();
        DropdownList.create(driver, webDriverWait).selectOptionWithId(displayTypeId);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private boolean isTopNPanelOpen() {
        return driver.findElements(By.xpath(OPENED_TOP_N_PANEL_XPATH)).size() > 0;
    }

    private boolean isOptionsPanelOpen() {
        return driver.findElements(By.xpath(OPENED_OPTIONS_PANEL_XPATH)).size() > 0;
    }
}
