package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.dpe.kpitoolbarpanel.ExportPanel;
import com.oss.framework.components.dpe.kpitoolbarpanel.FiltersPanel;
import com.oss.framework.components.dpe.kpitoolbarpanel.LayoutPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Filter;

import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class KpiToolbarPanel extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiToolbarPanel.class);

    private final static String KPI_TOOLBAR_PATH = "//div[@class='toolbarPanel']";
    private final static String APPLY_BUTTON_TEXT = "Apply";

    private KpiToolbarPanel(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiToolbarPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_TOOLBAR_PATH);
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new KpiToolbarPanel(driver, webElement, wait);
    }

    public FiltersPanel getFiltersPanel(){
        return FiltersPanel.create(driver, webDriverWait, webElement);
    }

    public ExportPanel getExportPanel(){
        return ExportPanel.create(driver, webDriverWait, webElement);
    }

    public LayoutPanel getLayoutPanel(){
        return LayoutPanel.create(driver, webDriverWait, webElement);
    }

    public void clickApply(){
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//div[contains(text(),'" + APPLY_BUTTON_TEXT + "')]"));
        WebElement applyButton = findElementByXpath(this.webElement, "//div[contains(text(),'" + APPLY_BUTTON_TEXT + "')]");
        DelayUtils.waitForClickability(webDriverWait, applyButton);
        DelayUtils.sleep();
        applyButton.click();
        log.debug("Clicking 'Apply' button");
    }
}
