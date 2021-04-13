package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KpiToolbarPanel extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiToolbarPanel.class);

    private final static String KPI_TOOLBAR_PATH = "//div[@class='toolbarPanel']";
    private final static String FILTERS_BUTTON_PATH = "//i[@aria-label='SETTINGS']";

    private final static String DOWNLAOD_BUTTON_PATH = "//i[@aria-label='DOWNLOAD']";
    private final static String LAYOUT_BUTTON_PATH = "//i[@aria-label='LAYOUT']";

    private final static String FILTERS_CLEAR_BUTTON_TEXT = "Clear All";
    private final static String FILTERS_CONFIRM_BUTTON_TEXT = "Confirm";
    private final static String APPLY_BUTTON_TEXT = "Apply";

    private final static String FILTER_WRAPPER = "onOffWrapper";

    public enum ExportType {
        JPG, PNG, PDF, XLSX
    }

    public enum LayoutType {
        LAYOUT_1x1("1x1"),
        LAYOUT_2x1("2x1"),
        LAYOUT_2x2("2x2"),
        LAYOUT_3x2("3x2"),
        LAYOUT_3x3("3x3"),
        LAYOUT_4x4("4x4"),
        LAYOUT_AUTO("auto");

        public final String label;
        private LayoutType(String label){
            this.label = label;
        }
    }

    private KpiToolbarPanel(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiToolbarPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_TOOLBAR_PATH);
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new KpiToolbarPanel(driver, webElement, wait);
    }

    public void openFilters() {
        DelayUtils.waitForPresence(webDriverWait, By.className(FILTER_WRAPPER));
        WebElement filters = findElementByXpath(FILTERS_BUTTON_PATH);
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(FILTERS_BUTTON_PATH));
        DelayUtils.waitForClickability(webDriverWait, filters);
        DelayUtils.sleep();
        filters.click();
        log.debug("Clicking 'Filters' button");
    }

    public void clearFilters() {
        Button.create(driver, FILTERS_CLEAR_BUTTON_TEXT).click();
        log.debug("Clicking 'Clear All' button");
    }

    public void turnOnFilters(List<String> filtersToEnable) {
        List<WebElement> filterElements = this.webElement.findElements(By.className(FILTER_WRAPPER))
                .stream()
                .filter(filter -> filtersToEnable.contains(filter.getText()))
                .collect(Collectors.toList());
        
        if(filterElements.size() == 0){
            log.warn("Can't find any filters");
            throw new NoSuchElementException("Can't find any filters");
        } else if(filterElements.size() != filtersToEnable.size()){
            log.warn("Can't find some filters to enable from list: " + filtersToEnable);
            throw new NoSuchElementException("Can't find some filters to enable from list: " + filtersToEnable);
        }
        filterElements
                .forEach(WebElement::click);
    }

    public void clickConfirm(){
        Button.create(driver, FILTERS_CONFIRM_BUTTON_TEXT).click();
    }

    public void clickApply(){
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//div[contains(text(),'" + APPLY_BUTTON_TEXT + "')]"));
        WebElement applyButton = findElementByXpath("//div[contains(text(),'" + APPLY_BUTTON_TEXT + "')]");
        DelayUtils.waitForClickability(webDriverWait, applyButton);
        DelayUtils.sleep();
        applyButton.click();
        log.debug("Clicking 'Apply' button");
    }

    public void exportKpiToFile(ExportType exportType){
        DelayUtils.waitForClickability(webDriverWait, findElementByXpath(DOWNLAOD_BUTTON_PATH));
        DelayUtils.sleep();
        clickDownload();
        log.debug("Clicking 'Download' button");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getExportButtonWithExtension(exportType).click();
        log.debug("Clicking {} export type button", exportType);
        clickExport();
        log.info("Exporting chart as {}", exportType);
    }

    private void clickDownload(){
        DelayUtils.waitForClickability(webDriverWait, webElement.findElement(By.xpath(DOWNLAOD_BUTTON_PATH)));
        findElementByXpath(DOWNLAOD_BUTTON_PATH).click();
    }

    private WebElement getExportButtonWithExtension(ExportType exportType){
        return findElementByXpath("//button[contains(text(),'" + exportType + "')]");
    }

    private void clickExport(){
        findElementByXpath("//button[@class='btn export']").click();
        log.debug("Clicking 'Export' button");
    }

    public void changeLayout(LayoutType layout){
        DelayUtils.waitForClickability(webDriverWait, webElement.findElement(By.xpath(LAYOUT_BUTTON_PATH)));
        DelayUtils.sleep();
        findElementByXpath(LAYOUT_BUTTON_PATH).click();

        log.debug("Clicking 'Layout' button");

        findElementByXpath("//i[@class='OSSIcon ossfont-layout-" + layout.label + "']").click();

        log.debug("Clicking button for layout: {}", layout.label);
        log.info("Changed layout to {}", layout.label);
    }

    private WebElement findElementByXpath(String xpath) {
        return this.webElement.findElement(By.xpath(xpath));
    }
}
