package com.oss.framework.iaa.widgets.dpe.kpichartwidget;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class KpiChartWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiChartWidget.class);

    private static final String KPI_CHART_WIDGET_ID = "am-chart-wrapper";
    private static final String KPI_CHART_WIDGET_PATH = "//*[@" + CSSUtils.TEST_ID + "='" + KPI_CHART_WIDGET_ID + "']";
    private static final String CHART_COLUMN_PATH = ".//*[name()='g' and (@role='listitem')]";
    private static final String LINE_CHART_TYPE_XPATH = ".//*[@data-series-type='line']";
    private static final String PARTIAL_CHART_ID = ".//*[starts-with(@" + CSSUtils.TEST_ID + ", 'amchart-series') and not(contains(@" + CSSUtils.TEST_ID + ", 'shadow'))]";
    private static final String PIE_CHART_PATH = ".//*[contains(@class, 'amcharts-PieChart-group')]";
    private static final String TOP_N_NAVIGATION_BAR_PATH = ".//*[@class='amcharts-Container amcharts-Component amcharts-NavigationBar']";
    private static final String TOP_N_BAR_CHART = ".//*[@" + CSSUtils.TEST_ID + "='amchart-series-y-selected']";
    private static final String TOP_N_FIRST_BAR = TOP_N_BAR_CHART + "//*[@role='menuitem'][1]";
    private static final String LEGEND_PATH = "//*[starts-with(@class,'amcharts-Container amcharts-Component amcharts-Legend')]";
    private static final String COMMON_LEGEND_CONTAINER_CSS = ".common-legend-container";
    private static final String DATA_SERIES_POINT_PATH = "//*[@class='amcharts-Sprite-group amcharts-Circle-group' and @stroke-width='2']";
    private static final String HIDDEN_Y_AXIS_PATH = "//*[@display = 'none' and contains (@class,'amcharts-v')]";
    private static final String VISIBLE_Y_AXIS_PATH = "//*[not (contains(@display, 'none')) and contains (@class,'amcharts-v')]";
    private static final String VISIBLE_Y_AXIS_VALUES_XPATH = "//*[@" + CSSUtils.TEST_ID + "='amchart-label-y' and not (contains(@display, 'none')) and not (contains(@opacity, '0'))]";
    private static final String LAST_SAMPLE_DISPLAYED_PATH = ".//*[@" + CSSUtils.TEST_ID + "='last-sample-time' and not(contains(@display, 'none'))]";
    private static final String TIME_ZONE_DISPLAYED_PATH = ".//*[@" + CSSUtils.TEST_ID + "='timezone' and not(contains(@display, 'none'))]";
    private static final String ZOOM_OUT_BUTTON_PATH = ".//*[@" + CSSUtils.TEST_ID + "='amchart-zoomout-button']";
    private static final String ZOOM_OUT_HIDDEN_BUTTON_PATH = ".//*[@" + CSSUtils.TEST_ID + "='amchart-zoomout-button' and @visibility='hidden']";
    private static final String MOVE_MOUSE_OVER = "Moving mouse over: ";
    private static final String ELEMENT_PRESENT_AND_VISIBLE = "Element is present and visible: ";
    private static final String LEGEND_WITH_TXT_XPATH = ".//div[@class='legendWrapper']//*[contains(text(), '%s')]";
    private static final String TREND_LINE_ON_CHART_XPATH = ".//*[contains(@class, 'amcharts-trendline')]";
    private static final String SAMPLE_XPATH = "//*[contains(@class,'amcharts-Container amcharts-Series-bullets')]//*[@class='amcharts-Sprite-group amcharts-Circle-group']";
    private static final String CHARTS_XPATH = ".//*[@data-testid='am-chart']";

    private KpiChartWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement widget) {
        super(driver, webDriverWait, widgetId, widget);
    }

    public static KpiChartWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_CHART_WIDGET_PATH);
        WebElement widget = driver.findElement(By.xpath(KPI_CHART_WIDGET_PATH));

        return new KpiChartWidget(driver, wait, KPI_CHART_WIDGET_ID, widget);
    }

    public void waitForPresenceAndVisibility() {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(KPI_CHART_WIDGET_PATH));
        log.debug(ELEMENT_PRESENT_AND_VISIBLE + "Chart");
    }

    public int countColumns() {
        int columnsCount = this.webElement.findElements(By.xpath(CHART_COLUMN_PATH)).size();
        log.debug("Columns count: {}", columnsCount);
        return columnsCount;
    }

    public int countLines() {
        int linesCount = this.webElement.findElements(By.xpath(LINE_CHART_TYPE_XPATH)).size();
        log.debug("Lines count: {}", linesCount);
        return linesCount;
    }

    public int countTrendLines() {
        return this.webElement.findElements(By.xpath(TREND_LINE_ON_CHART_XPATH)).size();
    }

    public int countPieCharts() {
        int pieChartsCount = this.webElement.findElements(By.xpath(PIE_CHART_PATH)).size();
        log.debug("Number of visible Pie Charts is: {}", pieChartsCount);
        return pieChartsCount;
    }

    public int countVisiblePoints() {
        int pointsCount = this.webElement.findElements(By.xpath(DATA_SERIES_POINT_PATH)).size();
        log.debug("Visible points count: {}", pointsCount);
        return pointsCount;
    }

    public List<String> allYaxisVisibleValues() {
        return this.webElement.findElements(By.xpath(VISIBLE_Y_AXIS_VALUES_XPATH))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int countVisibleLastSampleTime() {
        int visibleLastSampleTime = this.webElement.findElements(By.xpath(LAST_SAMPLE_DISPLAYED_PATH)).size();
        log.debug("Visible last sample time count: {}", visibleLastSampleTime);
        return visibleLastSampleTime;
    }

    public int countVisibleDataCompleteness() {
        int visibleDataCompleteness = getLegendContainsText("%]").size();
        log.debug("Visible data completeness in legend count: {}", visibleDataCompleteness);
        return visibleDataCompleteness;
    }

    public boolean isTimeZonePresent() {
        return WebElementUtils.isElementPresent(driver, By.xpath(TIME_ZONE_DISPLAYED_PATH));
    }

    public boolean isLegendPresent(String partialText) {
        return WebElementUtils.isElementPresent(driver, By.xpath(String.format(LEGEND_WITH_TXT_XPATH, partialText)));
    }

    public boolean isCommonLegendPresent() {
        return WebElementUtils.isElementPresent(driver, By.cssSelector(COMMON_LEGEND_CONTAINER_CSS));
    }

    private List<WebElement> getLegendContainsText(String text) {
        return this.webElement.findElements(By.xpath(String.format(LEGEND_WITH_TXT_XPATH, text)));
    }

    public String getDataSeriesLineWidth() {
        String strokeWidth = findElementByXpath(LINE_CHART_TYPE_XPATH).getCssValue("stroke-width");
        log.debug("Line width: {}", strokeWidth);
        return strokeWidth;
    }

    public String getDataSeriesVisibility() {
        String dataSeriesVisibility = findElementByXpath(LINE_CHART_TYPE_XPATH).getCssValue("visibility");
        log.debug("Data Series visibility: {}", dataSeriesVisibility);
        return dataSeriesVisibility;
    }

    public String getDataSeriesType() {
        return driver.findElement(By.xpath(PARTIAL_CHART_ID)).getAttribute("data-series-type");
    }

    public String getDataSeriesColor() {
        String strokeColor = findElementByXpath(LINE_CHART_TYPE_XPATH).getCssValue("stroke");
        log.debug("Data Series color: {}", strokeColor);
        return strokeColor;
    }

    public void clickDataSeriesLegend() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement legend = findElementByXpath(LEGEND_PATH);
        clickElementWithOffset(legend, 0, -5);
        log.debug("Clicking first data series on legend");
    }

    public boolean isTopNBarChartIsPresent() {
        return WebElementUtils.isElementPresent(this.webElement, By.xpath(TOP_N_BAR_CHART));
    }

    public void doubleClickTopNBar() {
        WebElement barInTopNBarChart = this.webElement.findElement(By.xpath(TOP_N_FIRST_BAR));
        Actions action = new Actions(driver);
        action.moveToElement(barInTopNBarChart).click(barInTopNBarChart).build().perform();
        action.doubleClick(barInTopNBarChart).build().perform();
        log.debug("Double clicking on bar in TopN BarChart");
    }

    public boolean isTopNNavigationBarPresent() {
        return WebElementUtils.isElementPresent(this.webElement, By.xpath(TOP_N_NAVIGATION_BAR_PATH));
    }

    public void zoomDataView() {
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(this.webElement, 100, 100).build().perform();
        log.debug("Zooming Data View with offset x = 100, y = 100");
    }

    public boolean isZoomOutButtonHidden() {
        return WebElementUtils.isElementPresent(this.webElement, By.xpath(ZOOM_OUT_HIDDEN_BUTTON_PATH));
    }

    public void clickZoomOut() {
        clickElement(this.webElement.findElement(By.xpath(ZOOM_OUT_BUTTON_PATH)));
        log.debug("Clicking Zoom Out button");
    }

    public int countCharts() {
        return driver.findElements(By.xpath(CHARTS_XPATH)).size();
    }

    private int countSamples() {
        List<WebElement> numberOfSamples = webElement.findElements(By.xpath(SAMPLE_XPATH));
        return numberOfSamples.size() - 1;
    }

    private void clickElement(WebElement webElement) {
        WebElementUtils.clickWebElement(driver, webElement);
        log.debug(MOVE_MOUSE_OVER + "point");
    }

    private void clickElementWithOffset(WebElement webElement, int offsetX, int offsetY) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement, offsetX, offsetY).click().build().perform();
        log.debug(MOVE_MOUSE_OVER + "point");
    }

    private WebElement findElementByXpath(String xpath) {
        return this.driver.findElement(By.xpath(xpath));
    }
}
