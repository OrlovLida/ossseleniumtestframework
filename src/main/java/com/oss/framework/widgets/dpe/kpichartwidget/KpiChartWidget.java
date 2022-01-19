package com.oss.framework.widgets.dpe.kpichartwidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

import static com.oss.framework.logging.LoggerMessages.ELEMENT_PRESENT_AND_VISIBLE;
import static com.oss.framework.logging.LoggerMessages.MOVE_MOUSE_OVER;

public class KpiChartWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiChartWidget.class);

    private static final String KPI_CHART_WIDGET_PATH = "//*[@" + CSSUtils.TEST_ID + "='am-chart-wrapper']";
    private static final String KPI_CHART_WIDGET_ID = "am-chart-wrapper";

    private static final String CHART_COLUMN_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='menuitem')]";
    private static final String CHART_LINE_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='group')]";
    private static final String BARCHART_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='list')]";
    private static final String PIE_CHART_PATH = ".//*[contains(@class, 'amcharts-PieChart-group')]";

    private static final String FIRST_TOP_N_COLUMN_PATH = ".//*[@class='amcharts-Sprite-group amcharts-Container-group' and @role='menuitem'][1]";
    private static final String TOP_N_NAVIGATION_BAR_PATH = ".//*[@class='amcharts-Container amcharts-Component amcharts-NavigationBar']";

    private static final String LEGEND_PATH = "//*[starts-with(@class,'amcharts-Container amcharts-Component amcharts-Legend')]";
    private static final String DATA_SERIES_POINT_PATH = "//*[@class='amcharts-Sprite-group amcharts-Circle-group' and @stroke-width='2']";

    private static final String HIDDEN_Y_AXIS_PATH = "//*[@display = 'none' and contains (@class,'amcharts-v')]";
    private static final String VISIBLE_Y_AXIS_PATH = "//*[not (contains(@display, 'none')) and contains (@class,'amcharts-v')]";
    private static final String Y_AXIS_VALUES_PATH = "//*[contains(@class, 'amcharts-AxisLabel' and @style, 'user-select')]"; // do zbadania wartości na Y axis

    private static final String LAST_SAMPLE_DISPLAYED_PATH = ".//*[contains(@data-testid, 'last-sample-time-chart')]";
    private static final String DATA_COMPLETENESS_DISPLAYED_PATH = ".//*[@data-testid='amchart-legend-selected']//*[contains(text(), '%')]";
    private static final String TIME_ZONE_DISPLAYED_PATH = ".//*[starts-with(@data-testid, 'timezone-chart')]";
    private static final String OTHER_PERIOD_DISPLAYED_PATH = ".//*[@data-testid='amchart-legend-other-period']";

    private static final String VISIBLE_INDICATORS_TREE_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Indicators' and not(contains(@style, 'display: none'))]";
    private static final String VISIBLE_DIMENSIONS_TREE_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Dimensions' and not(contains(@style, 'display: none'))]";
    private static final String VISIBLE_DATA_VIEW_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Data_View' and not(contains(@style, 'display: none'))]";

    private static final String ZOOM_OUT_BUTTON_PATH = ".//*[@data-testid='amchart-zoomout-button']";
    private static final String ZOOM_OUT_HIDDEN_BUTTON_PATH = ".//*[@data-testid='amchart-zoomout-button' and @visibility='hidden']";

    private KpiChartWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement widget) {
        super(driver, webDriverWait, widgetId, widget);
    }

    public static KpiChartWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, KPI_CHART_WIDGET_PATH);
        WebElement widget = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + KPI_CHART_WIDGET_ID + "']"));

        return new KpiChartWidget(driver, wait, KPI_CHART_WIDGET_ID, widget);
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

    public int countVisibleYAxis() {
        int visibleYAxis = this.webElement.findElements(By.xpath(VISIBLE_Y_AXIS_PATH)).size();
        log.debug("Visible Y axis count: {}", visibleYAxis);
        return visibleYAxis;
    }

    public int countHiddenYAxis() {
        int hiddenYAxis = this.webElement.findElements(By.xpath(HIDDEN_Y_AXIS_PATH)).size();
        log.debug("Hidden Y axis count: {}", hiddenYAxis);
        return hiddenYAxis;
    }

    public int countVisibleLastSampleTime() {
        int visibleLastSampleTime = this.webElement.findElements(By.xpath(LAST_SAMPLE_DISPLAYED_PATH)).size();
        log.debug("Visible last sample time count: {}", visibleLastSampleTime);
        return visibleLastSampleTime;
    }

    public int countVisibleDataCompleteness() {
        int visibleDataCompleteness = this.webElement.findElements(By.xpath(DATA_COMPLETENESS_DISPLAYED_PATH)).size();
        log.debug("Visible data completeness in legend count: {}", visibleDataCompleteness);
        return visibleDataCompleteness;
    }

    public boolean isTimeZonePresent() {
        return !this.webElement.findElements(By.xpath(TIME_ZONE_DISPLAYED_PATH)).isEmpty();
    }

    public int countVisibleOtherPeriod() {
        int visibleOtherPeriod = this.webElement.findElements(By.xpath(OTHER_PERIOD_DISPLAYED_PATH)).size();
        log.debug("Visible other period in legend count: {}", visibleOtherPeriod);
        return visibleOtherPeriod;
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

    public boolean isDataViewPanelPresent() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_DATA_VIEW_PATH)).size();
        log.debug("Data View panel is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    public boolean isIndicatorsTreePresent() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_INDICATORS_TREE_PATH)).size();
        log.debug("Indicators tree is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    public boolean isDimensionsTreePresent() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_DIMENSIONS_TREE_PATH)).size();
        log.debug("Dimension tree is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    public boolean isTopNBarChartIsPresent(String barChartId) {
        return !this.webElement.findElements(By.xpath(".//*[@data-testid='" + barChartId + "']")).isEmpty();
    }

    public void doubleClickOnTopNBar(String barChartId) {
        WebElement barInTopNBarChart = this.webElement.findElement(By.xpath(".//*[@data-testid='" + barChartId + "']//*[@role='menuitem'][1]"));
        Actions action = new Actions(driver);
        action.moveToElement(barInTopNBarChart).click(barInTopNBarChart).build().perform();
        action.doubleClick(barInTopNBarChart).build().perform();
        log.debug("Double clicking on bar in TopN BarChart");
    }

    public boolean isTopNNavigationBarPresent() {
        return !this.webElement.findElements(By.xpath(TOP_N_NAVIGATION_BAR_PATH)).isEmpty();
    }

    public void zoomDataView() {
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(this.webElement, 100, 100).build().perform();
        log.debug("Zooming Data View with offset x = 100, y = 100");
    }

    public boolean isZoomOutButtonPresent() {
        return this.webElement.findElements(By.xpath(ZOOM_OUT_HIDDEN_BUTTON_PATH)).isEmpty();
    }

    public void clickZoomOutButton() {
        this.webElement.findElement(By.xpath(ZOOM_OUT_BUTTON_PATH)).click();
        log.debug("Clicking Zoom Out button");
    }

    public int countCharts() {
        return driver.findElements(By.xpath(".//*[@data-testid='am-chart']")).size();
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

    private WebElement findElementByXpath(String xpath) {
        return this.driver.findElement(By.xpath(xpath));
    }
}
