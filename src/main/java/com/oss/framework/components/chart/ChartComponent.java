package com.oss.framework.components.chart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class ChartComponent {

    private static final Logger log = LoggerFactory.getLogger(ChartComponent.class);
    private static final String ELEMENT_PRESENT_AND_VISIBLE = "Element is present and visible: ";
    private static final String CHART_PATTERN = "//div[@" + CSSUtils.TEST_ID + "='%s']//*[@class='chartContainer']";
    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private String windowId;
    private WebElement webElement;

    private ChartComponent(WebDriver driver, WebDriverWait webDriverWait, String windowId, WebElement webElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.windowId = windowId;
        this.webElement = webElement;
    }

    public static ChartComponent create(WebDriver driver, WebDriverWait webDriverWait, String windowId) {
        String chartXpath = String.format(CHART_PATTERN, windowId);
        waitForPresenceAndVisibility(webDriverWait, chartXpath);
        DelayUtils.waitByXPath(webDriverWait, chartXpath);
        WebElement webElement = driver.findElement(By.xpath(CHART_PATTERN));
        return new ChartComponent(driver, webDriverWait, windowId, webElement);
    }

    private static void waitForPresenceAndVisibility(WebDriverWait webDriverWait, String xpath) {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(xpath));
        log.debug(ELEMENT_PRESENT_AND_VISIBLE + "Chart");
        log.info("chart is visible");
    }
}
