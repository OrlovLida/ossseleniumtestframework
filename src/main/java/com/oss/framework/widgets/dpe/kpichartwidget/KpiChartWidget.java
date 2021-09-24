package com.oss.framework.widgets.dpe.kpichartwidget;

import com.oss.framework.utils.CSSUtils;
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

import static com.oss.framework.logging.LoggerMessages.ELEMENT_PRESENT_AND_VISIBLE;
import static com.oss.framework.logging.LoggerMessages.MOVE_MOUSE_OVER;

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
    private static final String BARCHART_PATH = "//div[@class='chart']/div/*[name()='svg']//*[name()='g']/*[name()='g' and (@role='menu')]";

    private static final String EXPAND_DATA_VIEW_PATH =
            "//div[contains(@data-testid, '_Data_View')]//a[contains(@class, 'fullScreenButton')]";
    private static final String CHART_ACTIONS_BUTTON_ID = "context-action-panel";
    private static final String LEGEND_PATH = "//*[starts-with(@class,'amcharts-Container amcharts-Component amcharts-Legend')]";
    private static final String DATA_SERIES_COLOR_BUTTON_PATH = "//div[@class='colorPickerWrapper']";
    private static final String FIRST_COLOR_BUTTON_PATH = "//div[@class='color-picker__color-table-cell']";
    private static final String DATA_SERIES_POINT_PATH = "//*[@class='amcharts-Sprite-group amcharts-Circle-group' and @stroke-width='2']";

    private static final String HIDDEN_Y_AXIS_PATH = "//*[@display = 'none' and contains (@class,'amcharts-v')]";
    private static final String VISIBLE_Y_AXIS_PATH = "//*[not (contains(@display, 'none')) and contains (@class,'amcharts-v')]";
    private static final String Y_AXIS_VALUES_PATH = "//*[contains(@class, 'amcharts-AxisLabel' and @style, 'user-select')]"; // do zbadania wartości na Y axis

    //do przerobienia z datatest id
    private static final String LAST_SAMPLE_DISPLAYED_PATH = "//*[contains(text(),'Last sample:')]/ancestor::*[contains(@class,'amcharts-Sprite-group amcharts-Container-group amcharts-Label-group') and not (contains(@display, 'none'))]";
    private static final String DATA_COMPLETENESS_DISPLAYED_PATH = "//*[contains(text(), '%')]/ancestor::*[contains(@class, 'amcharts-Sprite-group amcharts-Container-group amcharts-Label-group') and contains(@style, 'pointer-events: none')]";
    private static final String OTHER_PERIOD_DISPLAYED_PATH = "//*[contains(text(), 'Other')]/ancestor::*[contains(@class, 'amcharts-Sprite-group amcharts-Container-group amcharts-Label-group') and contains(@style, 'pointer-events: none')]";

    // ponizsze DWA do przeniesienia do kpitreeWidget
    private static final String VISIBLE_INDICATORS_TREE_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Indicators' and not(contains(@style, 'display: none'))]";
    private static final String VISIBLE_DIMENSIONS_TREE_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Dimensions' and not(contains(@style, 'display: none'))]";
    private static final String VISIBLE_DATA_VIEW_PATH = "//div[@" + CSSUtils.TEST_ID + "='_Data_View' and not(contains(@style, 'display: none'))]";

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

    public boolean dataViewPanelVisibility() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_DATA_VIEW_PATH)).size();
        log.debug("Data View panel is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    public boolean indicatorsTreeVisibility() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_INDICATORS_TREE_PATH)).size();
        log.debug("Indicators tree is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    public boolean dimensionsTreeVisibility() {
        int visibleDataViewPanel = this.webElement.findElements(By.xpath(VISIBLE_DIMENSIONS_TREE_PATH)).size();
        log.debug("Dimension tree is visible: {}", visibleDataViewPanel);
        return visibleDataViewPanel == 1;
    }

    private WebElement findElementByXpath(String xpath) {
        return this.driver.findElement(By.xpath(xpath));
    }
}
