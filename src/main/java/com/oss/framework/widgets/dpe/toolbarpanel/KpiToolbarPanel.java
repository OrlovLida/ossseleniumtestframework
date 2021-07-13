package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.CSSUtils;
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

    public TopNPanel getTopNPanel() {
        return TopNPanel.create(driver, webDriverWait, webElement);
    }

    public void clickApply() {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//button[@" + CSSUtils.TEST_ID + "='" + APPLY_BUTTON_ID + "']"));
        Button applyButton = Button.createById(driver, APPLY_BUTTON_ID);
        DelayUtils.waitForClickability(webDriverWait, webElement.findElement(By.xpath("//button[@" + CSSUtils.TEST_ID + "='" + APPLY_BUTTON_ID + "']")));
        DelayUtils.sleep(5000);

        applyButton.click();

        log.debug(CLICK_BTN + "Apply");
    }
}
