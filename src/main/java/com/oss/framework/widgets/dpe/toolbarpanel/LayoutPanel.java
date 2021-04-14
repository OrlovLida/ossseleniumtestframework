package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.utils.WidgetUtils.findElementByXpath;
import static com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel.KPI_TOOLBAR_PATH;

public class LayoutPanel {

    private static final Logger log = LoggerFactory.getLogger(LayoutPanel.class);

    private final static String LAYOUT_BUTTON_PATH = "//i[@aria-label='LAYOUT']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

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

    static LayoutPanel create(WebDriver driver, WebDriverWait webDriverWait){
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new LayoutPanel(driver, webDriverWait, webElement);
    }

    private LayoutPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public void changeLayout(LayoutType layout){
        DelayUtils.waitForClickability(wait, webElement.findElement(By.xpath(LAYOUT_BUTTON_PATH)));
        DelayUtils.sleep();
        findElementByXpath(this.webElement, LAYOUT_BUTTON_PATH).click();

        log.debug("Clicking 'Layout' button");

        findElementByXpath(this.webElement, "//i[@class='OSSIcon ossfont-layout-" + layout.label + "']").click();

        log.debug("Clicking button for layout: {}", layout.label);
        log.info("Changed layout to {}", layout.label);
    }
}
