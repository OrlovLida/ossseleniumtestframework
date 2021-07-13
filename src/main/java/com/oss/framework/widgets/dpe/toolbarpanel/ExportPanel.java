package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;
import static com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel.KPI_TOOLBAR_PATH;

public class ExportPanel {

    private static final Logger log = LoggerFactory.getLogger(ExportPanel.class);

    private final static String DOWNLOAD_BUTTON_PATH = "//i[@aria-label='DOWNLOAD']";
    private final static String DOWNLOAD_BUTTON_ID = "export-button";
    private final static String EXPORT_BUTTON_ID = "export-menu-button";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public enum ExportType {
        JPG, PNG, PDF, XLSX
    }

    static ExportPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new ExportPanel(driver, webDriverWait, webElement);
    }

    private ExportPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public void exportKpiToFile(ExportType exportType) {
        DelayUtils.waitForClickability(wait, findElementByXpath(this.webElement, DOWNLOAD_BUTTON_PATH));
        DelayUtils.sleep();
        clickDownload();
        log.debug(CLICK_BTN + "Download");
        DelayUtils.waitForPageToLoad(driver, wait);
        getExportButtonWithExtension(exportType).click();
        log.debug(CLICK_BTN + exportType + "export type");
        clickExport();
        log.info("Exporting chart as {}", exportType);
    }

    private void clickDownload() {
        DelayUtils.waitForClickability(wait, webElement.findElement(By.xpath("//button[@" + CSSUtils.TEST_ID + "='" + DOWNLOAD_BUTTON_ID + "']")));
        Button downloadButton = Button.createById(driver, DOWNLOAD_BUTTON_ID);
        downloadButton.click();
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
