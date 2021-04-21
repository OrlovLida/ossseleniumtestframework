package com.oss.framework.widgets.dpe.kpichartwidget;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;
import static com.oss.framework.logging.LoggerMessages.ELEMENT_PRESENT_AND_VISIBLE;
import static com.oss.framework.logging.LoggerMessages.MOVE_MOUSE_OVER;

public class KpiChartWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiChartWidget.class);
    private static final String KPI_CHART_WIDGET_PATH = "//*[starts-with(@class,'multipleChart ')]";

    private static final String GRAPH_LOCATOR_PATH = "//*[starts-with(@class,'multipleChart ')]";
    private static final String COLLAPSED_GRAPH_MENU_PATH = "//*[@class='fa fa-chevron-right']/..";
    private static final String EXPANDED_GRAPH_MENU_PATH = "//*[@class='fa fa-chevron-left']/..";

    private static final String RESIZE_CHART_PATH = "//a[@class='btn btn-default btn-sm btn-border']";
    private static final String MAXIMIZE_CHART_PATH = "//i[@aria-label='MAXIMIZE']";
    private static final String MINIMIZE_CHART_PATH = "//i[@aria-label='REDUCE']";

    private static final String CHART_COLUMN_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='menuitem')]";
    private static final String CHART_LINE_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='group')]";

    public KpiChartWidget(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiChartWidget create(WebDriver driver, WebDriverWait wait){
        DelayUtils.waitByXPath(wait, KPI_CHART_WIDGET_PATH);
        WebElement webElement = driver.findElement(By.xpath(KPI_CHART_WIDGET_PATH));

        return new KpiChartWidget(driver, wait, webElement);
    }

    public void waitForPresenceAndVisibility(){
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(KPI_CHART_WIDGET_PATH));
        log.debug(ELEMENT_PRESENT_AND_VISIBLE + "Chart");
    }

    public void hoverMouseOverPoint(){
        int size = getNumberOfSamples();
        log.trace("Number of samples: {}", size);
        String pointXpath = "(//*[starts-with(@class,'amcharts-Container amcharts-Bullet amcharts-CircleBullet')]//*[@class='amcharts-Sprite-group amcharts-Circle-group'])[" + (size / 2) + "]";

        moveOverElement(findElementByXpath(pointXpath));
        DelayUtils.sleep();
    }

    private int getNumberOfSamples() {
        List<WebElement> numberOfSamples = webElement.findElements(By.xpath("//*[starts-with(@class,'amcharts-Container amcharts-Bullet amcharts-CircleBullet')]//*[@class='amcharts-Sprite-group amcharts-Circle-group']"));
        return numberOfSamples.size() - 1;
    }

    private void moveOverElement(WebElement webElement){
        Actions action = new Actions(driver);
        action.moveToElement(webElement).click().build().perform();
        log.debug(MOVE_MOUSE_OVER + "point");
    }

    public void maximizeChart(){
        showChartActions();
        clickMaximize();
        hideChartActions();
        log.info("Chart maximized");
    }

    public void minimizeChart(){
        showChartActions();
        clickMinimize();
        hideChartActions();
        log.info("Chart minimized");
    }

    private void showChartActions(){
        clickChartActionsBar(COLLAPSED_GRAPH_MENU_PATH);
        log.debug(CLICK_BTN + "Show chart actions");
    }

    private void hideChartActions(){
        clickChartActionsBar(EXPANDED_GRAPH_MENU_PATH);
        log.debug(CLICK_BTN + "Hide chart actions");
    }

    private void clickChartActionsBar(String actionBarXpath){
        moveOverElement(GRAPH_LOCATOR_PATH);

        log.debug(MOVE_MOUSE_OVER + "first chart");

        WebElement graphMenu = findElementByXpath(actionBarXpath);
        DelayUtils.waitForClickability(webDriverWait, graphMenu);
        graphMenu.click();
        DelayUtils.sleep();
    }

    private void clickMaximize(){
        moveOverElement(RESIZE_CHART_PATH);
        DelayUtils.waitForPresence(webDriverWait, By.xpath(MAXIMIZE_CHART_PATH));
        findElementByXpath(RESIZE_CHART_PATH).click();

        log.debug(CLICK_BTN + "MAXIMIZE");
    }

    private void clickMinimize(){
        moveOverElement(RESIZE_CHART_PATH);
        DelayUtils.waitForPresence(webDriverWait, By.xpath(MINIMIZE_CHART_PATH));
        findElementByXpath(RESIZE_CHART_PATH).click();

        log.debug(CLICK_BTN + "MINIMIZE");
    }

    private void moveOverElement(String resizeChartPath) {
        Actions action = new Actions(driver);
        action.moveToElement(findElementByXpath(resizeChartPath)).build().perform();
    }

    public int countColumns(){
        int columnsCount = this.webElement.findElements(By.xpath(CHART_COLUMN_PATH)).size();
        log.debug("Columns count: {}", columnsCount);
        return columnsCount;
    }

    public int countLines(){
        int linesCount = this.webElement.findElements(By.xpath(CHART_LINE_PATH)).size();
        log.debug("Lines count: {}", linesCount);
        return linesCount;
    }

    private WebElement findElementByXpath(String xpath) {
        return this.driver.findElement(By.xpath(xpath));
    }
}
