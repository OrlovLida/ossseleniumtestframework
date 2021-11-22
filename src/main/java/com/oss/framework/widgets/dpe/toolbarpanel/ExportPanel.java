package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;

public class ExportPanel {

    private static final Logger log = LoggerFactory.getLogger(ExportPanel.class);

    private final static String EXPORT_BUTTON_ID = "export-menu-button";
    private final static String EXPORT_PANEL_XPATH = ".//div[@data-testid='export-menu']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement exportPanel;

    public enum ExportType {
        JPG, PNG, PDF, XLSX
    }

    static ExportPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(EXPORT_PANEL_XPATH));

        return new ExportPanel(driver, webDriverWait, webElement);
    }

    private ExportPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement exportPanel) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.exportPanel = exportPanel;
    }

    public void exportKpiToFile(ExportType exportType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getExportButtonWithExtension(exportType).click();
        log.debug(CLICK_BTN + exportType + "export type");
        clickExport();
        log.info("Exporting chart as {}", exportType);
    }

    private Button getExportButtonWithExtension(ExportType exportType) {
        Button exportButtonWithExtension = Button.createById(driver, EXPORT_BUTTON_ID + "-" + exportType);
        return exportButtonWithExtension;
    }

    private void clickExport() {
        Button.createById(driver, EXPORT_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Export");
    }
}
