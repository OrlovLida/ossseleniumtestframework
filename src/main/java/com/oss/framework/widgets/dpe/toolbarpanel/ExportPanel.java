package com.oss.framework.widgets.dpe.toolbarpanel;

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

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public enum ExportType {
        JPG, PNG, PDF, XLSX
    }

    static ExportPanel create(WebDriver driver, WebDriverWait webDriverWait){
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new ExportPanel(driver, webDriverWait, webElement);
    }

    private ExportPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public void exportKpiToFile(ExportType exportType){
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

    private void clickDownload(){
        DelayUtils.waitForClickability(wait, webElement.findElement(By.xpath(DOWNLOAD_BUTTON_PATH)));
        findElementByXpath(this.webElement, DOWNLOAD_BUTTON_PATH).click();
    }

    private WebElement getExportButtonWithExtension(ExportType exportType){
        return findElementByXpath(this.webElement, "//button[contains(text(),'" + exportType + "')]");
    }

    private void clickExport(){
        findElementByXpath(this.webElement, "//button[@class='btn export']").click();
        log.debug(CLICK_BTN + "Export");
    }
}