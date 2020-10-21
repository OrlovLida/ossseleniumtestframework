package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Multimap;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.ExpandedTextTooltip;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Widget;

public class TableWidget extends Widget implements TableInterface {
    public static final String TABLE_WIDGET_CLASS = "TableWidget";
    public static final String PAGINATION_COMPONENT_CLASS = "OSSPagination";
    private static final String ATTRIBUTES_MANAGEMENT_XPATH = "//div[@id='attributes-management']";
//    private static final String PATH = "//div[@class='TableWidget']";
//    private static final String FILTER_ICON_PATH =".//i[@class='fa fa-filter']";
//    private static final String filterTiles = ".//span[@class='md-input-value']";
//    private static final String typeTile = ".//span[@class='md-input-value']/span[contains(text(),'Type')]";
    private static final String checkboxes = ".//div[contains(@class,'stickyColumn')]//div[contains(@class, 'Row')]";
    private static final String tableRows = ".//div[@class='TableBody']//div[@class='custom-scrollbars']//div[contains(@class, 'Row')]";
    private static final String columnResizeGrips = ".//div[@class='resizeGrip']";
    private static final String headers = ".//div[@class='headerItem text-align']";
    private static final String gearIcon = ".//div[@id='management-btn']";
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
        DelayUtils.waitBy(webDriverWait, By.className(widgetClass)); //TODO: change to id
        //DelayUtils.waitBy(webDriverWait, By.className(PAGINATION_COMPONENT_CLASS));
        return new TableWidget(driver, widgetClass, webDriverWait);
    }

    private TableWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
        if (webElement.findElements(By.className(PAGINATION_COMPONENT_CLASS)).size() > 0) {
            this.paginationComponent = new PaginationComponent(this.webElement);
        }
    }

    public ActionsContainer getContextActions() {
        return ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
    }

    @Override
    public Multimap<String, String> getAppliedFilters() {
        if (this.advancedSearch == null) {
            throw new IllegalStateException("Advanced search has never been initialized");
        }
        return this.advancedSearch.getAppliedFilters();
    }

    @Override
    public void selectRow(int row) {
        selectTableRow(row);
    }

    @Override
    public int getColumnSize(int column) {
        return getColumns().get(column).getWidth();
    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isNoData() {
        return false;
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void selectLinkInSpecificColumn(String columnName) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        setFilterContains(attributeId, componentType, value);
        confirmFilter();
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callAction(String actionLabel) {
        if (contextActions == null) {
            this.contextActions = ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
        }
        contextActions.callAction(actionLabel);
        this.contextActions = null;
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callAction(String groupId, String actionId) {
        if (contextActions == null) {
            this.contextActions = ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
        }
        contextActions.callAction(groupId, actionId);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int getRowNumber(String value, String attributeLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public String getValueCell(int index, String attributeLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void refreshUntilNoData(int waitTime, String refreshLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Map<String, String> getPropertyNamesToValues() {
        throw new RuntimeException("Not implemented for TableWidget");
    }

    @Override
    public List<TableRow> getSelectedRows() {
        return getVisibleRows().stream().filter(Row::isSelected).collect(Collectors.toList());
    }

    @Override
    public List<String> getActiveColumnHeaders() {
        return getColumns().stream().map(Column::getText).collect(Collectors.toList());
    }

    @Override
    public void disableColumn(String columnId) {
        AttributesChooser attributesChooser = getAttributesChooser();
        //TODO: implement after adding columns ids
    }

    @Override
    public void disableColumnByLabel(String columnLabel) {
        AttributesChooser attributesChooser = getAttributesChooser();
        if (attributesChooser.isAttributeSelectedByLabel(columnLabel)) {
            attributesChooser.toggleAttributeByLabel(columnLabel);
        }
        attributesChooser.clickApply();
    }

    @Override
    public void enableColumnByLabel(String columnLabel) {
        AttributesChooser attributesChooser = getAttributesChooser();
        if (!attributesChooser.isAttributeSelectedByLabel(columnLabel)) {
            attributesChooser.toggleAttributeByLabel(columnLabel);
        }
        attributesChooser.clickApply();
    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        changeColumnsPosition(getColumnByLabel(columnLabel), getColumns().get(position).getWebElement());
    }

    public void changeColumnsOrder(String sourceColumnLabel, String targetColumnLabel) {
        changeColumnsPosition(getColumnByLabel(sourceColumnLabel), getColumnByLabel(targetColumnLabel));
    }

    public void changeColumnsOrder(int sourcePosition, int targetPosition) {
        changeColumnsPosition(getColumns().get(sourcePosition).getWebElement(), getColumns().get(targetPosition).getWebElement());
    }

    private void changeColumnsPosition(WebElement source, WebElement target) {
        Actions action = new Actions(driver);
        WebElement sourceBtn = source.findElement(By.xpath(".//div[@class = 'btn-drag']"));
        action.moveToElement(sourceBtn).perform();
        DragAndDrop.dragAndDrop(sourceBtn, target, driver);
    }

    public SaveConfigurationWizard openSaveConfigurationWizard() {
        clickOnKebabMenu();
        DropdownList.create(driver, webDriverWait).selectOptionWithId("saveNewConfig");
        return SaveConfigurationWizard.create(driver, webDriverWait);
    }

    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        clickOnKebabMenu();
        DropdownList.create(driver, webDriverWait).selectOptionWithId("chooseConfig");
        return ChooseConfigurationWizard.create(driver, webDriverWait);
    }

    public ChooseConfigurationWizard openDownloadConfigurationWizard() {
        clickOnKebabMenu();
        DropdownList.create(driver, webDriverWait).selectOptionWithId("table_gql_Download");
        return ChooseConfigurationWizard.create(driver, webDriverWait);
    }

    private List<Column> getColumns() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<WebElement> elements = this.webElement.findElements(By.xpath(headers));
        return elements.stream().map(Column::new).collect(Collectors.toList());
    }

    public AttributesChooser getAttributesChooser() {
        if (!(this.webElement.findElements(By.xpath(gearIcon + "[@class = 'open']")).size() > 0)) {
            this.webElement.findElement(By.xpath(gearIcon)).click();
        }
        return AttributesChooser.create(driver, webDriverWait);
    }

    public void clickOnKebabMenu() {
        getKebabMenuBtn().click();
    }

    public void clickOnAction(String actionName) {
    }

    private WebElement getKebabMenuBtn() {
        return this.webElement.findElement(By.xpath(kebabMenuBtn));
    }

    private WebElement getRowsCounter() {
        return this.webElement.findElement(By.xpath(rowsCounter));
    }

    //TODO: return checkbox class instead of WebElement
    public List<WebElement> getTableCheckboxes() {
        return this.webElement.findElements(By.xpath(checkboxes));
    }

    private List<WebElement> getTableRows() {
        return this.webElement.findElements(By.xpath(tableRows));
    }

    public List<WebElement> getTableCells() {
        return this.webElement.findElements(By.xpath(cells));
    }

    private WebElement getHorizontalTableScroller() {
        return this.webElement.findElement(By.xpath(horizontalTableScroller));
    }

    private WebElement getVerticalTableScroller() {
        return this.webElement.findElement(By.xpath(verticalTableScroller));
    }

    private List<WebElement> getColumnResizeGrips() {
        return this.webElement.findElements(By.xpath(columnResizeGrips));
    }

    private List<WebElement> getExpanders() {
        return this.webElement.findElements(By.xpath(expanders));
    }

    private WebElement getColumnByLabel(String label) {
        return driver.findElement(By.xpath(".//p[text()='" + label + "']/ancestor::div[@class='headerItem text-align']"));
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
    public WebElement getActivePageBtn() {
        return this.webElement.findElement(By.xpath(activePageBtn));
    }

    private WebElement getFirstPageBtn() {
        return this.webElement.findElement(By.xpath(firstPageBtn));
    }

    private WebElement getLastPageBtn() {
        return this.webElement.findElement(By.xpath(lastPageBtn));
    }

    private WebElement getPreviousPageBtn() {
        return this.webElement.findElement(By.xpath(previousPageBtn));
    }

    private WebElement getNextPageBtn() {
        return this.webElement.findElement(By.xpath(nextPageBtn));
    }

    public String getLabelOfActivePageBtn() {
        return getActivePageBtn().getText();
    }

    public String getLabelOfLastPageBtn() {
        return getLastPageBtn().getText();
    }

    public String getNumberOfAllRowsInTable() {
        return getRowsCounter().getText();
    }

    public String getExpandedText() {
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

    public void clickFirstPage() {
        getFirstPageBtn().click();
    }

    public void clickLastPage() {
        getLastPageBtn().click();
    }

    public void clickNextPage() {
        getNextPageBtn().click();
    }

    public void clickPreviousPage() {
        getPreviousPageBtn().click();
    }

    public void clickOnFirstExpander() {
        getExpanders().get(0).click();
        if (expandedTextTooltip == null) {
            expandedTextTooltip = new ExpandedTextTooltip(this.driver);
        }
    }

    public int howManyRowsOnFirstPage() {
        return driver.findElements(By.xpath(tableRows)).size();
    }

    private void selectTableRow(int row) {
        this.contextActions = null;
        if (!getTableRows().get(row).getAttribute("class").contains("selected"))
            getTableRows().get(row).click();
    }

    public void unselectTableRow(int row) {
        this.contextActions = null;
        if(getTableRows().get(row).getAttribute("class").contains("selected"))
            getTableRows().get(row).click();
    }

    public boolean checkIfTableIsEmpty(){
        return driver.findElements(By.xpath("//div[@class='TableBody']//*[@class='noDataWithColumns']")).size()>0;
    }

    private List<Row> getVisibleRows() {
        WebElement gridContainer = this.webElement.findElement(By.className("TableBody"))
                .findElement(By.xpath(".//div[@class='grid-container']/div"));
        List<Row> rows = gridContainer.findElements(By.xpath("./div")).stream().map(Row::new)
                .collect(Collectors.toList());
        return rows;
    }

    @Deprecated
    public ColumnsManagement getColumnsManagement() {
        DelayUtils.waitByXPath(this.webDriverWait, gearIcon);
        this.webElement.findElement(By.xpath(gearIcon)).click();
        return ColumnsManagement.create(this.driver);
    }

    private void openSearchPanel() {
        if (advancedSearch == null) {
            LocatingUtils.waitUsingBy(By.className(AdvancedSearch.SEARCH_COMPONENT_CLASS), this.webDriverWait);
            advancedSearch = new AdvancedSearch(this.driver, this.webDriverWait);
        }
        advancedSearch.openSearchPanel();
    }

    private void confirmFilter() {
        this.advancedSearch.clickApply();
    }

    private void setFilterContains(String componentId, ComponentType componentType, String value) {
        if (this.advancedSearch == null) {
            this.openSearchPanel();
        }
        Input input = advancedSearch.getComponent(componentId, componentType);
        input.setSingleStringValueContains(value);
    }

    public String getAttribute(int rowIndex, String attribute) {
        return getTableRows().get(rowIndex).getAttribute(attribute);
    }

    public void typeIntoSearch(String text) {
        getSearchInput().sendKeys(text);
    }

    public void scrollHorizontally(int offset) {
        getHorizontalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getHorizontalTableScroller()).clickAndHold();
        action.moveByOffset(offset, 0);
        action.release();
        action.perform();
    }

    public void scrollVertically(int offset) {
        getVerticalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getVerticalTableScroller()).clickAndHold();
        action.moveByOffset(0, offset);
        action.release();
        action.perform();
    }

    @Override
    public void resizeColumn(int column, int offset) {
        Actions action = new Actions(this.driver);
        action.dragAndDropBy(getColumnResizeGrips().get(column), offset, 0).perform();
    }

    public String getValueFromRowWithID(String columnLabel, String id) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath(".//div[@id='table-wrapper']/div[@class='TableBody']//div[@id='" + id + "' and contains(@class,'Row')]/div[@class='Cell']//div[contains(@class,'text-wrapper')]"));
        return valueCells.get(index).getText();
    }

    public String getValueFromNthRow(String columnLabel, String rowNumber) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath("(.//div[@id='table-wrapper']/div[@class='TableBody']//div[@class='Row' or @class='Row selected'])[" + rowNumber + "]/div[@class='Cell']/div/div"));
        return valueCells.get(index).getText();
    }

    public static class Column {
        private final WebElement webElement;

        private Column(WebElement webElement) {
            this.webElement = webElement;
        }

        public String getText() {
            return webElement.findElement(By.xpath(".//p")).getAttribute("title");
        }

        public int getWidth() {
            return CSSUtils.getWidthValue(webElement);
        }

        private WebElement getWebElement() {
            return webElement;
        }
    }

    public static class Row implements TableRow {
        private final WebElement webElement;

        private Row(WebElement webElement) {
            this.webElement = webElement;
        }

        public boolean isSelected() {
            return this.webElement.findElement(By.xpath("./div"))
                    .getAttribute("class").contains("selected");
        }

        public int getIndex() {
            int heightValue = CSSUtils.getHeightValue(this.webElement);
            int topValue = CSSUtils.getTopValue(this.webElement) + heightValue;
            return (topValue / heightValue);
        }
    }

    //TODO: create component for this
    public static class PaginationComponent {
        private final WebElement webElement;

        private PaginationComponent(WebElement webElement) {
            this.webElement = webElement.findElement(By.className(PAGINATION_COMPONENT_CLASS));
        }

        @Deprecated
        public int getStep() {
            String step = this.webElement.findElement(By.className("pageSize")).getText();
            return Integer.valueOf(step);
        }

        @Deprecated
        public int getCurrentPage() {
            WebElement pages = this.webElement.findElement(By.className("pagination"));
            String currentPage = pages.findElement(By.className("active")).getText();
            return Integer.valueOf(currentPage);
        }
    }
}
