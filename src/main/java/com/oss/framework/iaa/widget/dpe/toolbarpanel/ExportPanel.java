package com.oss.framework.iaa.widget.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;

public class ExportPanel {

    private static final Logger log = LoggerFactory.getLogger(ExportPanel.class);

    private static final String EXPORT_BUTTON_ID = "export-menu-button";
    private static final String EXPORT_PANEL_XPATH = ".//div[@data-testid='export-menu']";
    private static final String EXPORT_BUTTON_XPATH = "//*[data-testid='export-menu-button']";
    private static final String CLICK_BTN = "Clicking button: ";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement exportPanelElement;

    private ExportPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement exportPanelElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.exportPanelElement = exportPanelElement;
    }

    static ExportPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(EXPORT_PANEL_XPATH));

        return new ExportPanel(driver, webDriverWait, webElement);
    }

    public void exportKpiToFile(ExportType exportType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getExportButtonWithExtension(exportType).click();
        log.debug(CLICK_BTN + "{} export type", exportType);
        clickExport();
        log.info("Exporting chart as {}", exportType);
    }

    private Button getExportButtonWithExtension(ExportType exportType) {
        return Button.createById(driver, EXPORT_BUTTON_ID + "-" + exportType);
    }

    private void clickExport() {
        Button.createById(driver, EXPORT_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Export");
        DelayUtils.waitForButtonDisappear(driver, EXPORT_BUTTON_XPATH);
    }

    public enum ExportType {
        JPG, PNG, PDF, XLSX
    }
}
