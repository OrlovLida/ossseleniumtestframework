package com.oss.framework.widgets.chartwidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

import static com.oss.framework.logging.LoggerMessages.ELEMENT_PRESENT_AND_VISIBLE;

public class ChartWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(ChartWidget.class);

    private ChartWidget(WebDriver driver, WebDriverWait webDriverWait, String windowId, WebElement webElement) {
        super(driver, webDriverWait, windowId, webElement);
    }

    public static ChartWidget create(WebDriver driver, WebDriverWait webDriverWait, String windowId) {
        String xPath = createXPath(windowId);
        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new ChartWidget(driver, webDriverWait, windowId, webElement);
    }

    private static String createXPath(String windowId) {
        return "//div[@" + CSSUtils.TEST_ID + "='" + windowId + "']//*[@class='chartContainer']";
    }

    public void waitForPresenceAndVisibility(String windowId) {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(createXPath(windowId)));
        log.debug(ELEMENT_PRESENT_AND_VISIBLE + "Chart");
        log.info("chart is visible");
    }
}
