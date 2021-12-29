package com.oss.framework.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class LayoutPanel {

    private static final Logger log = LoggerFactory.getLogger(LayoutPanel.class);

    private static final String LAYOUT_PANEL_XPATH = "//div[@data-testid='layout-template-menu']";
    private static final String CHART_LAYOUT_BUTTON_ID = ".//*[@data-testid='chart-layout-";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement layoutPanelElement;

    public enum LayoutType {
        LAYOUT_1x1("1x1"),
        LAYOUT_2x1("2x1"),
        LAYOUT_2x2("2x2"),
        LAYOUT_3x2("3x2"),
        LAYOUT_3x3("3x3"),
        LAYOUT_4x4("4x4"),
        LAYOUT_AUTO("auto");

        public final String label;

        LayoutType(String label) {
            this.label = label;
        }
    }

    public static LayoutPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(LAYOUT_PANEL_XPATH));

        return new LayoutPanel(driver, webDriverWait, webElement);
    }

    private LayoutPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement layoutPanelElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.layoutPanelElement = layoutPanelElement;
    }

    public void changeLayout(LayoutType layout) {
        findElementByXpath(this.layoutPanelElement, CHART_LAYOUT_BUTTON_ID + layout.label + "']").click();

        log.debug(CLICK_BTN + layout.label + " layout");
        log.info("Changed layout to {}", layout.label);
    }

    public String chartLayoutButtonStatus(LayoutType layout) {
        String status = findElementByXpath(this.layoutPanelElement, CHART_LAYOUT_BUTTON_ID + layout.label + "']").getAttribute("class");
        log.debug("Layout {} button status: {}", layout.label, status);
        return status;
    }
}
