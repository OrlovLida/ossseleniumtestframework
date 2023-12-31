package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LayoutPanel {

    private static final Logger log = LoggerFactory.getLogger(LayoutPanel.class);

    private static final String LAYOUT_PANEL_XPATH = "//div[@data-testid='layout-template-menu']";
    private static final String CHART_LAYOUT_BUTTON_ID = ".//*[@data-testid='chart-layout-";
    private static final String CLICK_BTN = "Clicking button: ";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement layoutPanelElement;

    private LayoutPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement layoutPanelElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.layoutPanelElement = layoutPanelElement;
    }

    public static LayoutPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement webElement = driver.findElement(By.xpath(LAYOUT_PANEL_XPATH));

        return new LayoutPanel(driver, webDriverWait, webElement);
    }

    public void changeLayout(LayoutType layout) {
        this.layoutPanelElement.findElement(By.xpath(CHART_LAYOUT_BUTTON_ID + layout.label + "']")).click();

        log.debug("{}{} layout", CLICK_BTN, layout.label);
        log.info("Changed layout to {}", layout.label);
    }

    public String chartLayoutButtonStatus(LayoutType layout) {
        String status = this.layoutPanelElement.findElement(By.xpath(CHART_LAYOUT_BUTTON_ID + layout.label + "']")).getAttribute("class");
        log.debug("Layout {} button status: {}", layout.label, status);
        return status;
    }

    public enum LayoutType {
        LAYOUT_1X1("1x1"),
        LAYOUT_2X1("2x1"),
        LAYOUT_2X2("2x2"),
        LAYOUT_3X2("3x2"),
        LAYOUT_3X3("3x3"),
        LAYOUT_4X4("4x4"),
        LAYOUT_AUTO("auto");

        public final String label;

        LayoutType(String label) {
            this.label = label;
        }
    }
}
