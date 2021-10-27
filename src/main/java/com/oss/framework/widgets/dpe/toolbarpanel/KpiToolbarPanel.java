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
        Button applyButton = Button.createById(driver, APPLY_BUTTON_ID);
        DelayUtils.sleep(5000);
        applyButton.click();

        log.debug(CLICK_BTN + "Apply");
    }

    public void selectDisplayType(String displayTypeLabel) {
        webElement.findElement(By.xpath(DISPLAY_TYPE_DROPDOWN_BUTTON_XPATH)).click();
        DropdownList.create(driver, webDriverWait).selectOptionContains(displayTypeLabel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
