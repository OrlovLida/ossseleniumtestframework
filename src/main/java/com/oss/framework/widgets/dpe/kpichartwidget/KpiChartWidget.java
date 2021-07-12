package com.oss.framework.widgets.dpe.kpichartwidget;

import com.oss.framework.components.inputs.Button;
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

import static com.oss.framework.logging.LoggerMessages.*;

public class KpiChartWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiChartWidget.class);
    private static final String KPI_CHART_WIDGET_PATH = "//*[starts-with(@class,'amcharts-Rectangle')]";

    private static final String GRAPH_LOCATOR_PATH = "//*[starts-with(@class,'multipleChart ')]";
    private static final String COLLAPSED_GRAPH_MENU_PATH = "//*[@class='fa fa-chevron-right']/..";
    private static final String EXPANDED_GRAPH_MENU_PATH = "//*[@class='fa fa-chevron-left']/..";

    private static final String RESIZE_CHART_PATH = "//a[@class='fullScreenButton']";
    private static final String MAXIMIZE_CHART_PATH = "//i[@aria-label='Expand']";
    private static final String MINIMIZE_CHART_PATH = "//i[@aria-label='Collapse']";

    private static final String CHART_COLUMN_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='menuitem')]";
    private static final String CHART_LINE_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='group')]";
    private static final String BARCHART_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='list')]";

    private static final String EXPAND_DATA_VIEW_PATH =
            "//div[contains(@data-testid, '_Data_View')]//a[contains(@class, 'fullScreenButton')]";
    private static final String CHART_ACTIONS_BUTTON_ID = "context-action-panel";
    private static final String LEGEND_PATH = "//*[starts-with(@class,'amcharts-Container amcharts-Component amcharts-Legend')]";
    private static final String AREA_CHART_BUTTON_PATH = "//i[@aria-label='AREA_CHART']";
    private static final String LINE_CHART_BUTTON_PATH = "//i[@aria-label='LINE_CHART']";
    private static final String BAR_CHART_BUTTON_PATH = "//i[@aria-label='BAR_CHART']";
    private static final String CHART_COLOR_BUTTON_ID = "chart-color-button";
    private static final String DATA_SERIES_COLOR_BUTTON_PATH = "//div[@class='colorPickerWrapper']";
    private static final String FIRST_COLOR_BUTTON_PATH = "//div[@class='color-picker__color-table-cell']";

    public KpiChartWidget(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiChartWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_CHART_WIDGET_PATH);
        WebElement webElement = driver.findElement(By.xpath(KPI_CHART_WIDGET_PATH));

        return new KpiChartWidget(driver, wait, webElement);
    }

    public void waitForPresenceAndVisibility() {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(KPI_CHART_WIDGET_PATH));
        log.debug(ELEMENT_PRESENT_AND_VISIBLE + "Chart");
    }

    public void hoverMouseOverPoint() {
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

    private void moveOverElement(WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).click().build().perform();
        log.debug(MOVE_MOUSE_OVER + "point");
    }

    private void clickElementWithOffset(WebElement webElement, int offsetX, int offsetY) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement, offsetX, offsetY).click().build().perform();
        log.debug(MOVE_MOUSE_OVER + "point");
    }

    //TODO: it should be removed after changing DFE-SQM tests
    public void maximizeChart() {
        showChartActions();
        clickMaximize(); // to tak nie bedzie dzialac - niepotrzebne chart actions
        hideChartActions();
        log.info("Chart maximized");
    }

    //TODO: it should be removed after changing DFE-SQM tests
    public void minimizeChart() {
        showChartActions();
        clickMinimize();
        hideChartActions();
        log.info("Chart minimized");
    }

    //TODO: it should be removed after changing DFE-SQM tests
    private void showChartActions() {
        clickChartActionsBar(COLLAPSED_GRAPH_MENU_PATH);
        log.debug(CLICK_BTN + "Show chart actions");
    }

    //TODO: it should be removed after changing DFE-SQM tests
    private void hideChartActions() {
        clickChartActionsBar(EXPANDED_GRAPH_MENU_PATH);
        log.debug(CLICK_BTN + "Hide chart actions");
    }

    public void clickChartActions() {
        moveOverElement(GRAPH_LOCATOR_PATH);

        log.debug(MOVE_MOUSE_OVER + "first chart");
        Button graphMenuButton = Button.createById(driver, CHART_ACTIONS_BUTTON_ID);
        graphMenuButton.click();
        log.debug("Click chart actions button");
        DelayUtils.sleep();
    }

    //TODO: it should be removed after changing DFE-SQM tests
    private void clickChartActionsBar(String actionBarXpath) {
        moveOverElement(GRAPH_LOCATOR_PATH);

        log.debug(MOVE_MOUSE_OVER + "first chart");

        Button graphMenuButton = Button.createById(driver, CHART_ACTIONS_BUTTON_ID);
        graphMenuButton.click();
        DelayUtils.sleep();
    }

    //TODO: it should be removed after changing DFE-SQM tests
    private void clickMaximize() {
//        moveOverElement(RESIZE_CHART_PATH);
        DelayUtils.waitForPresence(webDriverWait, By.xpath(MAXIMIZE_CHART_PATH)); // omija to - do zobaczenia czemu
//        findElementByXpath(RESIZE_CHART_PATH).click();
        WebElement expandButton = driver.findElement(By.xpath(EXPAND_DATA_VIEW_PATH));
        expandButton.click();

        log.debug(CLICK_BTN + "MAXIMIZE");
    }

    //TODO: it should be removed after changing DFE-SQM tests
    private void clickMinimize() {
        moveOverElement(RESIZE_CHART_PATH);
        DelayUtils.waitForPresence(webDriverWait, By.xpath(MINIMIZE_CHART_PATH));
        findElementByXpath(RESIZE_CHART_PATH).click();
        log.debug(CLICK_BTN + "MINIMIZE");
    }

    //TODO: move later to KpiViewPage - when data-testid will be added
    public void clickAreaChartButton() {
        findElementByXpath(AREA_CHART_BUTTON_PATH).click();
        log.debug(CLICK_BTN + "AREA CHART TYPE");
    }

    public void clickBarChartButton() {
        findElementByXpath(BAR_CHART_BUTTON_PATH).click();
        log.debug(CLICK_BTN + "BAR CHART TYPE");
    }

    public void clickLineChartButton() {
        findElementByXpath(LINE_CHART_BUTTON_PATH).click();
        log.debug(CLICK_BTN + "LINE CHART TYPE");
    }

    public void clickChartColorButton() {
        Button chartColorButton = Button.createById(driver, CHART_COLOR_BUTTON_ID);
        chartColorButton.click();
        log.debug(CLICK_BTN + "Choose chart series color");
    }

    public void pickDataSeriesColorButton() {
        findElementByXpath(DATA_SERIES_COLOR_BUTTON_PATH).click();
        log.debug(CLICK_BTN + "DATA SERIES COLOR");
        findElementByXpath(FIRST_COLOR_BUTTON_PATH).click();
        log.debug("First color from the list is chosen");
    }

    private void moveOverElement(String resizeChartPath) {
        Actions action = new Actions(driver);
        action.moveToElement(findElementByXpath(resizeChartPath)).build().perform();
    }

    public int countColumns() {
        int columnsCount = this.webElement.findElements(By.xpath(CHART_COLUMN_PATH)).size();
        log.debug("Columns count: {}", columnsCount);
        return columnsCount;
    }

    public int countLines() {
        int linesCount = this.webElement.findElements(By.xpath(CHART_LINE_PATH)).size();
        log.debug("Lines count: {}", linesCount);
        return linesCount;
    }

    public String dataSeriesLineWidth() {
        String strokeWidth = findElementByXpath(CHART_LINE_PATH).getCssValue("stroke-width");
        log.debug("Line width: {}", strokeWidth);
        return strokeWidth;
    }

    public String dataSeriesVisibility() {
        String dataSeriesVisibility = findElementByXpath(CHART_LINE_PATH).getCssValue("visibility");
        log.debug("Data Series visibility: {}", dataSeriesVisibility);
        return dataSeriesVisibility;
    }

    public String dataSeriesFillOpacity() {
        String fillOpacity = findElementByXpath(CHART_LINE_PATH).getCssValue("fill-opacity");
        log.debug("Data Series fill opacity: {}", fillOpacity);
        return fillOpacity;
    }

    public String barDataSeriesFillOpacity() {
        String fillOpacity = findElementByXpath(BARCHART_PATH).getCssValue("fill-opacity");
        log.debug("Bar chart Data Series fill opacity: {}", fillOpacity);
        return fillOpacity;
    }

    public String dataSeriesColor() {
        String strokeColor = findElementByXpath(CHART_LINE_PATH).getCssValue("stroke");
        log.debug("Data Series color: {}", strokeColor);
        return strokeColor;
    }

    public void clickDataSeriesLegend() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement legend = findElementByXpath(LEGEND_PATH);
        clickElementWithOffset(legend, 0, -5);
        log.debug("Clicking first data series on legend");
    }

    private WebElement findElementByXpath(String xpath) {
        return this.driver.findElement(By.xpath(xpath));
    }
}
