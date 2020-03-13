package com.oss.framework.widgets.tablewidget;

import com.google.common.collect.Multimap;
import com.oss.framework.components.ActionsContainer;
import com.oss.framework.components.AdvancedSearch;
import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.portals.ActionsDropdownList;
import com.oss.framework.components.portals.ExpandedTextTooltip;
import com.oss.framework.components.portals.Popup;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class TableWidget extends Widget {
    public static final String TABLE_WIDGET_CLASS = "TableWidget";
    public static final String PAGINATION_COMPONENT_CLASS = "OSSPagination";

//    private static final String PATH = "//div[@class='TableWidget']";
//    private static final String FILTER_ICON_PATH =".//i[@class='fa fa-filter']";
//    private static final String filterTiles = ".//span[@class='md-input-value']";
//    private static final String typeTile = ".//span[@class='md-input-value']/span[contains(text(),'Type')]";
    private static final String checkboxes = ".//div[contains(@class,'stickyColumn')]//div[contains(@class, 'Row')]";
    private static final String tableRows = ".//div[@class='TableBody']//div[@class='custom-scrollbars']//div[contains(@class, 'Row')]";
    private static final String columnResizeGrips = ".//div[@class='resizeGrip']";
    private static final String headers = ".//div[@class='headerItem']";
    private static final String gearIcon = ".//i[contains(@class,'fa-cog')]";
    private static final String horizontalTableScroller = ".//div[contains(@style,'position: relative; display: block; height: 100%; cursor: pointer;')]";

    private static final String verticalTableScroller = ".//div[contains(@style,'position: relative; display: block; width: 100%; cursor: pointer;')]";
    private static final String cells = ".//div[@class='Cell']//*//div[contains(@class,'OSSRichText')]";
    private static final String expanders = ".//button[@class='btn-show-long-text-box']";
    private static final String firstPageBtn = ".//li[contains(@class,'page')][2]";
    private static final String nextPageBtn = ".//li[contains(@class,'pagesNavigation btn')][last()]";
    private static final String previousPageBtn = ".//li[contains(@class,'page')][2]/..//li[1]";
    private static final String lastPageBtn = ".//li[contains(@class,'page')][last()-1]";
    private static final String activePageBtn = ".//li[@class='page active']";
    private static final String rowsCounter = ".//div[@class='rowsCounter']/span[last()]";
    private static final String kebabMenuBtn = ".//div[@id='frameworkCustomButtonsGroup']";

    private AdvancedSearch advancedSearch;
    private ActionsContainer contextActions;
    private PaginationComponent paginationComponent;

    private ExpandedTextTooltip expandedTextTooltip;

    public static TableWidget create(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(TABLE_WIDGET_CLASS)); //TODO: change to id
        DelayUtils.waitBy(webDriverWait, By.className(PAGINATION_COMPONENT_CLASS));
        return new TableWidget(driver, widgetClass, webDriverWait);
    }

    private TableWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
        this.paginationComponent = new PaginationComponent(this.webElement);
    }

    public WebElement getKebabMenuBtn(){
        return this.webElement.findElement(By.xpath(kebabMenuBtn));
    }

    public WebElement getRowsCounter(){
        return this.webElement.findElement(By.xpath(rowsCounter));
    }

    //TODO: return checkbox class instead of WebElement
    public List<WebElement> getTableCheckboxes(){
        return this.webElement.findElements(By.xpath(checkboxes));
    }

    private List<WebElement> getTableRows(){
        return this.webElement.findElements(By.xpath(tableRows));
    }

    public List<WebElement> getTableCells(){
        return this.webElement.findElements(By.xpath(cells));
    }

    public WebElement getHorizontalTableScroller(){
        return this.webElement.findElement(By.xpath(horizontalTableScroller));
    }

    public WebElement getVerticalTableScroller(){
        return this.webElement.findElement(By.xpath(verticalTableScroller));
    }

    public List<WebElement> getColumnResizeGrips(){
        return this.webElement.findElements(By.xpath(columnResizeGrips));
    }

    public List<WebElement> getExpanders(){
        return this.webElement.findElements(By.xpath(expanders));
    }

    //TODO:
    private List<WebElement> getColumnHeaders(){
        return this.webElement.findElements(By.xpath(headers));
    }

    public List<String> getActiveColumns() {
        return this.webElement.findElements(By.xpath(headers)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public String getActiveColumnLabel(int column) {
        List<String> columnLabels = getActiveColumns();
        return columnLabels.get(column);
    }

    public String getFirstColumnLabel() {
        return getActiveColumnLabel(0);
    }

    //TODO: wrap WebElement
    public WebElement getActivePageBtn(){
        return this.webElement.findElement(By.xpath(activePageBtn));
    }

    private WebElement getFirstPageBtn(){
        return this.webElement.findElement(By.xpath(firstPageBtn));
    }

    private WebElement getLastPageBtn(){
        return this.webElement.findElement(By.xpath(lastPageBtn));
    }

    private WebElement getPreviousPageBtn(){
        return this.webElement.findElement(By.xpath(previousPageBtn));
    }

    private WebElement getNextPageBtn(){
        return this.webElement.findElement(By.xpath(nextPageBtn));
    }

    public String getLabelOfActivePageBtn(){
        return getActivePageBtn().getText();
    }

    public String getLabelOfLastPageBtn(){
        return getLastPageBtn().getText();
    }

    public String getNumberOfAllRowsInTable(){
        return getRowsCounter().getText();
    }

    public String getExpandedText(){
        return expandedTextTooltip.getExpandedText();
    }

    public String getPopupTitle() {
        return "";
    }

    public boolean isFirstPageActive() {
        return getLabelOfActivePageBtn().equals("1");
    }

    public boolean isMoreThanOnePage() {
        return !getLabelOfLastPageBtn().equals("1");
    }

    public boolean isPreviousPageClicable() {
        return !getPreviousPageBtn().getAttribute("class").contains("disabled");
    }

    public boolean isNextPageClicable() {
        return !getNextPageBtn().getAttribute("class").contains("disabled");
    }

    public void clickFirstPage(){
        getFirstPageBtn().click();
    }

    public void clickLastPage(){
        getLastPageBtn().click();
    }

    public void clickNextPage(){
        getNextPageBtn().click();
    }

    public void clickPreviousPage(){
        getPreviousPageBtn().click();
    }

    public void clickOnFirstExpander(){
        getExpanders().get(0).click();
        if(expandedTextTooltip == null){
            expandedTextTooltip = new ExpandedTextTooltip(this.driver);
        }
    }

    public void selectFirstRow(){
        selectTableRow(0);
    }

    public void selectTableRow(int row) {
        this.contextActions = null;
        getTableRows().get(row).click();
    }

    //TODO: Due to virtual scrolls returns only selected visible rows.
    public List<Row> getSelectedVisibleRows() {
        return getVisibleRows().stream().filter(Row::isSelected).collect(Collectors.toList());
    }

    public List<Row> getVisibleRows() {
        int currentPage = this.paginationComponent.getCurrentPage();
        int step = this.paginationComponent.getStep();
        WebElement gridContainer = this.webElement.findElement(By.className("TableBody"))
                .findElement(By.xpath(".//div[@class='grid-container']/div"));
        List<Row> rows = gridContainer.findElements(By.xpath("./div")).stream().map(we -> new Row(we, currentPage, step))
                .collect(Collectors.toList());
        return rows;
    }

    public ColumnsManagement getColumnsManagement(){
        DelayUtils.waitByXPath(this.webDriverWait, gearIcon);
        this.webElement.findElement(By.xpath(gearIcon)).click();
        return ColumnsManagement.create(this.driver);
    }

    private void openSearchPanel(){
        if(advancedSearch == null){
            LocatingUtils.waitUsingBy(By.className(AdvancedSearch.SEARCH_COMPONENT_CLASS), this.webDriverWait);
            advancedSearch = new AdvancedSearch(this.driver, this.webDriverWait);
        }
        advancedSearch.openSearchPanel();
    }

    public void confirmFilter(){
        this.advancedSearch.clickApply();
    }

    public Multimap<String, String> getAppliedFilters() {
        if(this.advancedSearch == null) {
            throw new IllegalStateException("Advanced search has never been initialized");
        }
        return this.advancedSearch.getAppliedFilters();
    }

    public void setFilterContains(String componentId, ComponentType componentType, String value) {
        if(this.advancedSearch == null) {
            this.openSearchPanel();
        }
        Input input = advancedSearch.getComponent(componentId, componentType);
        input.setSingleStringValueContains(value);
    }

    public void callAction(String actionLabel){
        if(contextActions == null) {
            this.contextActions = ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
        }
        contextActions.callAction(actionLabel);
        this.contextActions = null;
    }

    public void callAction(String groupId, String actionId){
        if(contextActions == null) {
            this.contextActions = ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
        }
        contextActions.callAction(groupId, actionId);
    }

    public void clickOnKebabMenu(){
        getKebabMenuBtn().click();
    }

    public void clickOnAction(String actionName){
    }

    public void typeIntoSearch(String text){ getSearchInput().sendKeys(text); }

    public void scrollHorizontally(int offset){
        getHorizontalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getHorizontalTableScroller()).clickAndHold();
        action.moveByOffset(offset,0);
        action.release();
        action.perform();
    }

    public void scrollVertically(int offset){
        getVerticalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getVerticalTableScroller()).clickAndHold();
        action.moveByOffset(0,offset);
        action.release();
        action.perform();
    }

    public int getFirstColumnSize() {
        return CSSUtils.getWidthValue(getColumnHeaders().get(0));
    }

    public void resizeFirstColumn(int offset){
        Actions action = new Actions(this.driver);
        action.dragAndDropBy(getColumnResizeGrips().get(0), offset,0).perform();
    }

    public String getValueFromRowWithID(String columnLabel, String id) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath(".//div[@id='table-wrapper']/div[@class='TableBody']//div[@id='"+id+"' and contains(@class,'Row')]/div[@class='Cell']//div[contains(@class,'text-wrapper')]"));
        return valueCells.get(index).getText();
    }

    public String getValueFromNthRow(String columnLabel, String rowNumber) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath("(.//div[@id='table-wrapper']/div[@class='TableBody']//div[@class='Row' or @class='Row selected'])["+rowNumber+"]/div[@class='Cell']/div/div"));
        return valueCells.get(index).getText();
    }

    public static class Row {
        private final WebElement webElement;
        private final int pageNumber;
        private final int paginationStep;

        private Row(WebElement webElement, int pageNumber, int paginationStep) {
            this.webElement = webElement;
            this.pageNumber = pageNumber;
            this.paginationStep = paginationStep;
        }

        public boolean isSelected() {
            return this.webElement.findElement(By.xpath("./div"))
                    .getAttribute("class").contains("selected");
        }

        public int getIndex() {
            int topValue = CSSUtils.getTopValue(this.webElement);
            int heightValue = CSSUtils.getHeightValue(this.webElement);
            return ((topValue + heightValue) / heightValue) +
                    ((pageNumber * paginationStep) - paginationStep);
        }
    }

    public static class PaginationComponent {
        private final WebElement webElement;

        private PaginationComponent(WebElement webElement) {
            this.webElement = webElement.findElement(By.className(PAGINATION_COMPONENT_CLASS));
        }

        public int getStep() {
            String step = this.webElement.findElement(By.className("pageSize")).getText();
            return Integer.valueOf(step);
        }

        public int getCurrentPage() {
            WebElement pages = this.webElement.findElement(By.className("pagination"));
            String currentPage = pages.findElement(By.className("active")).getText();
            return Integer.valueOf(currentPage);
        }
    }
}
