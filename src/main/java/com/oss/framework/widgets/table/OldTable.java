package com.oss.framework.widgets.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.pagination.OldPaginationComponent;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class OldTable extends Widget implements TableInterface {
    
    private static final Logger log = LoggerFactory.getLogger(OldTable.class);
    private static final String TABLE_COLUMN_CLASS = "OSSTableColumn";
    private static final String ACTIONS_CONTAINER_CSS = ".actionsContainer";
    private static final String TABLE_COLUMNS_SETTINGS_ICON_CLASS = "OSSTableColumnsSettingsIcon";
    private static final String STICKY_COLUMNS_SETTINGS_CLASS = "OSSStickyColumnsSettings";
    private static final int REFRESH_INTERVAL = 2000;
    private static final String TABLE_COMPONENT_XPATH = ".//div[contains(@class, 'OSSTableComponent')]";
    private static final String COLUMNS_WITHOUT_CHECKBOX_CSS = ".OSSTableColumn:not(.Col_SELECTION)";
    private static final String HREF_CSS = "[href]";
    private static final String CONTEXT_ACTIONS_CONTAINER_CSS = ".windowToolbar," + ACTIONS_CONTAINER_CSS;
    private static final String TABLE_IN_ACTIVE_TAB_XPATH =
            "//div[@data-attributename='TableTabsApp']//div[contains(@class,'tabsContainerSingleContent active')]//div[@class='AppComponentContainer']/div";
    private static final String ANCESTOR_XPATH = ".//ancestor::div[contains(@class,'card-shadow')]";
    private static final String NO_DATA_XPATH = ".//h3[contains(@class,'noDataWithColumns')]";
    private static final String AVAILABLE_COLUMNS_LOG = "Available columns:";
    private static final String NUMBER_OF_COLUMNS_LOG_PATTERN = "Number of columns: %s";
    private static final String NUMBER_OF_COLUMNS_WITH_LABEL_LOG_PATTERN = "Number of columns with label: %s";
    private static final String NOT_SUPPORTED_TYPE_EXCEPTION = "Old table widget supports TEXT_FIELD only.";
    private static final String NOT_IMPLEMENTED_EXCEPTION = "Not implemented method in OldTable";
    private static final String NO_AVAILABLE_ROWS = "There are no available rows";
    private static final String CANNOT_FIND_COLUMN_EXCEPTION_PATTERN = "Cannot find a column with label = %s";
    private static final String CONTAINS_TEXT_PATTERN = "//*[contains(text(),'%s')]";
    private static final String FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN =
            "(//div[contains(@class, 'Col_ColumnId_Name')]//div[contains(text(), '%s')])[%d]";
    private static final String TABLE_PATTERN = "div[" + CSSUtils.TEST_ID + "='%s']";
    private static final String TEXT_ICON_CLASS = "OSSRichTextIcon";
    private static final String SEARCH_SELECTOR = "[aria-label='SEARCH']";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String HEADER_SELECTION_CSS = ".Header_SELECTION";
    private static final String SELECT_ALL_OBJECTS_IS_NOT_AVAILABLE_EXCEPTION = "Select All Objects is not available";
    
    private AdvancedSearch advancedSearch;
    private Map<String, Column> columns;
    
    private OldTable(WebDriver driver, WebDriverWait wait, String widgetId) {
        super(driver, wait, widgetId);
    }
    
    public static OldTable createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        WebElement table = driver.findElement(By.cssSelector(String.format(TABLE_PATTERN, widgetId)));
        Actions actions = new Actions(driver);
        actions.moveToElement(table).build().perform();
        return new OldTable(driver, wait, widgetId);
    }
    
    public static OldTable createTableForActiveTab(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String tableIdFromActiveTab = driver.findElement(By.xpath(TABLE_IN_ACTIVE_TAB_XPATH)).getAttribute(CSSUtils.TEST_ID);
        return createById(driver, wait, tableIdFromActiveTab);
    }
    
    private static boolean isElementPresent(WebElement window, By by) {
        return WebElementUtils.isElementPresent(window, by);
    }
    
    @Override
    public void selectRow(int row) {
        webElement.findElements(By.cssSelector(COLUMNS_WITHOUT_CHECKBOX_CSS))
                .stream()
                .filter(webElement -> webElement.findElements(By.cssSelector(HREF_CSS)).isEmpty())
                .map(columnElement -> new Column(columnElement, webDriverWait, driver))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_AVAILABLE_ROWS)).selectCellByIndex(row);
    }
    
    @Override
    public int getColumnSize(int column) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public void resizeColumn(int column, int offset) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public List<String> getActiveColumnHeaders() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {
        AttributeChooser attributeChooser = new AttributeChooser(driver, getColumnManager());
        attributeChooser.disableColumnByLabel(columnLabel);
        attributeChooser.clickAccept();
    }
    
    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {
        AttributeChooser attributeChooser = new AttributeChooser(driver, getColumnManager());
        attributeChooser.enabledColumnByLabel(columnLabel);
        attributeChooser.clickAccept();
    }
    
    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        WebElement columnPosition = webElement.findElements(By.className(TABLE_COLUMN_CLASS)).get(position);
        DragAndDrop.dragAndDrop(getColumn(columnLabel).getDragElement(), new DragAndDrop.DropElement(columnPosition), driver);
    }
    
    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public boolean hasNoData() {
        return WebElementUtils.isElementPresent(webElement, By.xpath(NO_DATA_XPATH));
    }
    
    @Override
    public void clickLink(String columnName) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getColumn(columnName).clickLink();
    }
    
    public int getRowNumber(String value, String attributeLabel) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, String.format(CONTAINS_TEXT_PATTERN, value));
        return getColumn(attributeLabel).indexOf(value);
    }
    
    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getColumn(attributeLabel).selectCellByValue(value);
    }
    
    public String getCellValue(int index, String attributeLabel) {
        return getColumn(attributeLabel).getValueCell(index);
    }
    
    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        openAdvancedSearch();
        setFilterContains(attributeId, componentType, value);
        confirmFilter();
    }
    
    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        if (componentType != ComponentType.TEXT_FIELD) {
            throw new IllegalArgumentException(NOT_SUPPORTED_TYPE_EXCEPTION);
        }
        clearColumnValue(attributeLabel).setValue(value);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    @Override
    public void searchByAttribute(String attributeId, String value) {
        openAdvancedSearch();
        setFilterContains(attributeId, value);
        confirmFilter();
    }
    
    @Override
    public void callAction(String actionId) {
        getActionsInterface().callActionById(actionId);
    }
    
    @Override
    public void callActionByLabel(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
    }
    
    @Override
    public void callAction(String groupId, String actionId) {
        getActionsInterface().callActionById(groupId, actionId);
    }
    
    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        getActionsInterface().callActionByLabel(groupLabel, actionLabel);
    }
    
    @Override
    public void doRefreshWhileNoData(int waitTime, String refreshId) {
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + waitTime;
        while (hasNoData() && (stopTime > System.currentTimeMillis())) {
            DelayUtils.sleep(REFRESH_INTERVAL);
            callAction(OldActionsContainer.KEBAB_GROUP_ID, refreshId);
        }
    }
    
    @Override
    public Multimap<String, String> getAppliedFilters() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public List<TableRow> getSelectedRows() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    @Override
    public String getCellValueById(int row, String columnId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION);
    }
    
    public void unselectRow(String attributeLabel, String value) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getColumn(attributeLabel).unselectCellByValue(value);
    }
    
    public Column clearColumnValue(String attributeLabel) {
        Column column = getColumn(attributeLabel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        column.clear();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return column;
    }
    
    public void clearAllColumnValues() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<Column> columns2 = Lists.newArrayList(getColumns().values());
        for (Column column: columns2) {
            if (column.isColumnWithSearchPresent()) {
                column.clear();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }
    }
    
    public int countRows(String anyLabelInTable) {
        return getColumn(anyLabelInTable).countRows();
    }

    public void selectRowByPartialNameAndIndex(String partialName, int index) {
        String xpath = String.format(FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN, partialName, index);
        driver.findElement(By.xpath(xpath)).click();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    public int getTotalCount() {
        return getPagination().getTotalCount();
    }

    public void goToNextPage() {
        getPagination().goOnNextPage();
    }

    public void setPageSize(int pageOption) {
        getPagination().changeRowsCount(pageOption);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private PaginationComponent getPagination() {
        return OldPaginationComponent.createFromParent(this.webElement);
    }

    public void selectPredefinedFilter(String filterName) {
        PredefinedFilter predefinedFilter = PredefinedFilter.createPredefinedFilter(driver, webDriverWait, filterName);
        predefinedFilter.selectPredefinedFilter();
    }

    public List<String> getSelectedPredefinedFilters() {
        List<PredefinedFilter> predefinedFilters = driver.findElements(By.cssSelector(".ToggleButton")).stream()
                .map(element -> PredefinedFilter.createPredefinedFilter(driver, webDriverWait, element))
                .filter(PredefinedFilter::isFilterSelected).collect(Collectors.toList());
        return predefinedFilters.stream().map(PredefinedFilter::getLabel).collect(Collectors.toList());
    }
    
    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }
    
    public List<String> getColumnsHeaders() {
        return new ArrayList<>(getColumns().keySet());
    }
    
    public void selectAllRows() {
        Column headerSelectionColumn = getHeaderSelectionColumn();
        headerSelectionColumn.selectAllRows();
    }
    
    public void unselectAllRows() {
        Column headerSelectionColumn = getHeaderSelectionColumn();
        headerSelectionColumn.unselectAllRows();
    }
    
    public String getGroupActionLabel(String groupId) {
        return getActionsInterface().getGroupActionLabel(groupId);
    }
    
    public String getActionLabel(String actionId) {
        return getActionsInterface().getActionLabel(actionId);
    }
    
    public String getActionLabel(String groupId, String actionId) {
        return getActionsInterface().getActionLabel(groupId, actionId);
    }
    
    private Column getHeaderSelectionColumn() {
        return webElement.findElements(By.cssSelector(HEADER_SELECTION_CSS)).stream()
                .map(headerSelection -> new Column(headerSelection, webDriverWait, driver)).findFirst()
                .orElseThrow(() -> new NoSuchElementException(SELECT_ALL_OBJECTS_IS_NOT_AVAILABLE_EXCEPTION));
    }
    
    private Map<String, Column> getColumns() {
        if (columns == null) {
            columns = createColumnsFilters();
        }
        return columns;
    }
    
    private Column getColumn(String columnLabel) {
        Map<String, Column> columnsMap = getColumns();
        if (columnsMap.containsKey(columnLabel)) {
            return columnsMap.get(columnLabel);
        } else {
            log.debug(AVAILABLE_COLUMNS_LOG);
            columnsMap.forEach((key, value) -> log.debug(key));
            throw new NoSuchElementException(String.format(CANNOT_FIND_COLUMN_EXCEPTION_PATTERN, columnLabel));
        }
    }
    
    private Map<String, Column> createColumnsFilters() {
        Map<String, Column> columnMap = Maps.newLinkedHashMap();
        DelayUtils.waitForNestedElements(webDriverWait, webElement, TABLE_COMPONENT_XPATH);
        List<Column> columns2 =
                webElement.findElements(By.cssSelector(COLUMNS_WITHOUT_CHECKBOX_CSS))
                        .stream().map(columnElement -> new Column(columnElement, webDriverWait, driver)).collect(Collectors.toList());
        String columnsNumberLog = String.format(NUMBER_OF_COLUMNS_LOG_PATTERN, columns2.size());
        log.debug(columnsNumberLog);
        for (Column column: columns2) {
            if (column.isLabelPresent()) {
                columnMap.put(column.getLabel(), column);
            }
        }
        String columnsWithLabelsNumberLog = String.format(NUMBER_OF_COLUMNS_WITH_LABEL_LOG_PATTERN, columnMap.size());
        log.debug(columnsWithLabelsNumberLog);
        return columnMap;
    }
    
    private ActionsInterface getActionsInterface() {
        WebElement window = webElement.findElement(By.xpath(ANCESTOR_XPATH));
        DelayUtils.waitForNestedElements(webDriverWait, window, By.cssSelector(CONTEXT_ACTIONS_CONTAINER_CSS));
        boolean isNewActionContainer = isElementPresent(window, By.cssSelector(ACTIONS_CONTAINER_CSS));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(window, driver, webDriverWait);
        } else {
            return OldActionsContainer.createFromParent(driver, webDriverWait, window);
        }
    }
    
    private WebElement getColumnManager() {
        WebElement columnsSettingsIcon = webElement.findElement(By.className(TABLE_COLUMNS_SETTINGS_ICON_CLASS));
        WebElementUtils.clickWebElement(driver, columnsSettingsIcon);
        DelayUtils.waitBy(webDriverWait, By.className(STICKY_COLUMNS_SETTINGS_CLASS));
        return driver.findElement(By.className(STICKY_COLUMNS_SETTINGS_CLASS));
    }
    
    private void openAdvancedSearch() {
        getAdvancedSearch().openSearchPanel();
    }
    
    private void confirmFilter() {
        getAdvancedSearch().clickApply();
    }
    
    private void setFilterContains(String componentId, ComponentType componentType, String value) {
        getAdvancedSearch().setFilter(componentId, componentType, value);
    }
    
    private void setFilterContains(String componentId, String value) {
        getAdvancedSearch().setFilter(componentId, value);
    }
    
    private AdvancedSearch getAdvancedSearch() {
        if (advancedSearch == null) {
            advancedSearch = AdvancedSearch.createById(driver, webDriverWait, id);
        }
        return advancedSearch;
    }
    
    private static class Column {
        private static final String FLEX_CLASS = "flex";
        private static final String LABEL_ATTRIBUTE = "label";
        private static final String CELL_ROW_XPATH = ".//div[contains(@class, 'Cell Row')]";
        private static final String HREF_XPATH = "//a[contains(@href, '#/')]";
        private static final String INPUT_XPATH = ".//input";
        private static final String HEADER_XPATH = ".//div[contains(@class, 'Header')]";
        private static final String RICH_TEXT_XPATH = ".//div[contains(@class, 'OSSRichText')]/span";
        private static final String CELL_XPATH = ".//div[contains(@class, 'Cell')]";
        private static final String CANNOT_FIND_ROW_EXCEPTION = "Cannot find a row with the provided value.";
        private static final String CANNOT_FIND_CELL_EXCEPTION = "Cannot find a cell with the provided value.";
        private static final String CELL_ALREADY_SELECTED_LOG = "The cell you want to select is already selected.";
        private static final String CELL_ALREADY_UNSELECTED_LOG = "The cell you want to unselect is already unselected.";
        private static final String CHECKBOX_ALL_CSS = ".checkbox";
        
        private final WebElement columnElement;
        private final WebDriverWait wait;
        private final WebDriver driver;
        
        private Column(WebElement columnElement, WebDriverWait wait, WebDriver driver) {
            this.columnElement = columnElement;
            this.wait = wait;
            this.driver = driver;
        }
        
        public int indexOf(String value) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL_XPATH));
            for (WebElement cell: cells) {
                if (getCellText(cell).equals(value)) {
                    return cells.indexOf(cell);
                }
            }
            throw new NoSuchElementException(CANNOT_FIND_ROW_EXCEPTION);
        }
        
        private void selectCellByIndex(int index) {
            WebElement cell = getCellByIndex(index);
            selectCell(cell);
        }
        
        private void selectCellByValue(String value) {
            selectCell(getCell(value));
        }
        
        private void unselectCellByValue(String value) {
            unselectCell(getCell(value));
        }
        
        private void unselectCell(WebElement cell) {
            if (!isSelected(cell)) {
                log.debug(CELL_ALREADY_UNSELECTED_LOG);
                return;
            }
            WebElementUtils.clickWebElement(driver, cell);
        }
        
        private void selectCell(WebElement cell) {
            if (isSelected(cell)) {
                log.debug(CELL_ALREADY_SELECTED_LOG);
                return;
            }
            WebElementUtils.clickWebElement(driver, cell);
        }
        
        private boolean isSelected(WebElement cell) {
            String cellClass = cell.getAttribute(CLASS_ATTRIBUTE);
            return cellClass.endsWith(" selected");
        }
        
        private String getCellText(WebElement cell) {
            if (WebElementUtils.isElementPresent(cell, By.xpath(RICH_TEXT_XPATH))) {
                return cell.findElement(By.xpath(RICH_TEXT_XPATH)).getText();
            } else
                return "";
        }
        
        private String getLabel() {
            WebElement header = moveToHeader();
            try {
                return header.findElement(By.xpath(INPUT_XPATH)).getAttribute(LABEL_ATTRIBUTE);
            } catch (NoSuchElementException e) {
                return header.getText();
            }
        }
        
        private WebElement moveToHeader() {
            WebElement header = columnElement.findElement(By.xpath(HEADER_XPATH));
            WebElementUtils.moveToElement(driver, header);
            return header;
        }
        
        private DragAndDrop.DraggableElement getDragElement() {
            WebElement dragButton = moveToHeader().findElement(By.className(FLEX_CLASS));
            return new DragAndDrop.DraggableElement(dragButton);
        }
        
        private boolean isLabelPresent() {
            try {
                moveToHeader();
                return !columnElement.findElement(By.xpath(INPUT_XPATH)).getAttribute(LABEL_ATTRIBUTE).equals("");
            } catch (NoSuchElementException e) {
                return !columnElement.getText().isEmpty();
            }
        }
        
        private boolean isColumnWithSearchPresent() {
            moveToHeader();
            return WebElementUtils.isElementPresent(columnElement, By.cssSelector(SEARCH_SELECTOR));
        }
        
        private String getValueCell(int index) {
            WebElement cell = getCellByIndex(index);
            WebElementUtils.moveToElement(driver, cell);
            if (isIconPresent(cell)) {
                return (getCellText(cell) + " " + getIconTitles(index)).trim();
            }
            return getCellText(cell);
        }
        
        private boolean isIconPresent(WebElement cell) {
            return !cell.findElements(By.xpath(".//i")).isEmpty();
        }
        
        private String getIconTitles(int cellIndex) {
            List<WebElement> textIcons = getCellByIndex(cellIndex).findElements(By.className(TEXT_ICON_CLASS));
            List<String> iconTitles = textIcons.stream().map(icon -> icon.getAttribute("title")).collect(Collectors.toList());
            
            return String.join(",", iconTitles);
        }
        
        private WebElement getCellByIndex(int index) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL_XPATH));
            return cells.get(index);
        }
        
        private WebElement getCell(String value) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL_ROW_XPATH));
            return cells.stream().filter(cell -> cell.findElement(By.xpath(RICH_TEXT_XPATH)).getText().equals(value)).findFirst()
                    .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_CELL_EXCEPTION));
        }
        
        private int countRows() {
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL_XPATH));
            return cells.size();
        }
        
        private void setValue(String value) {
            WebElement input = columnElement.findElement(By.xpath(INPUT_XPATH));
            Actions action = new Actions(driver);
            action.moveToElement(input).build().perform();
            input.sendKeys(value);
            input = columnElement.findElement(By.xpath(INPUT_XPATH));
            input.sendKeys(Keys.ENTER);
        }
        
        private void clear() {
            List<WebElement> inputs = columnElement.findElements(By.xpath(INPUT_XPATH));
            if (!inputs.isEmpty()) {
                WebElement input = inputs.get(0);
                WebElementUtils.clickWebElement(driver, input);
                input = columnElement.findElement(By.xpath(INPUT_XPATH));
                input.sendKeys(Keys.CONTROL + "a");
                input.sendKeys(Keys.DELETE);
                DelayUtils.sleep();
            }
        }
        
        private void clickLink() {
            DelayUtils.waitForNestedElements(wait, columnElement, CELL_XPATH + HREF_XPATH);
            Actions action = new Actions(driver);
            action.click(columnElement.findElement(By.xpath(CELL_XPATH + HREF_XPATH))).build().perform();
        }
        
        private void selectAllRows() {
            WebElement selectAllCheckbox = getSelectAllCheckbox();
            if (!isAllRowsSelected(selectAllCheckbox)) {
                selectAllCheckbox.click();
            }
        }
        
        private void unselectAllRows() {
            WebElement selectAllCheckbox = getSelectAllCheckbox();
            if (isAllRowsSelected(selectAllCheckbox)) {
                selectAllCheckbox.click();
            }
        }
        
        private boolean isAllRowsSelected(WebElement selectAllCheckbox) {
            return selectAllCheckbox.getAttribute(CLASS_ATTRIBUTE).endsWith("minus");
        }
        
        private WebElement getSelectAllCheckbox() {
            return columnElement.findElement(By.cssSelector(CHECKBOX_ALL_CSS));
        }
    }
    
    private static class PredefinedFilter {
        private static final String TOGGLE_BUTTON_XPATH = ".//span[contains(@class,'ToggleButton')]";
        private static final String NO_PREDEFINED_FILTER_EXCEPTION = "There is no Predefined Filter";
        
        private final WebDriver driver;
        private final WebElement predefinedFilterElement;
        
        private PredefinedFilter(WebDriver driver, WebElement predefinedFilterElement) {
            this.driver = driver;
            this.predefinedFilterElement = predefinedFilterElement;
        }
        
        private static PredefinedFilter createPredefinedFilter(WebDriver driver, WebDriverWait wait, String filterName) {
            DelayUtils.waitForPageToLoad(driver, wait);
            DelayUtils.waitByXPath(wait, TOGGLE_BUTTON_XPATH);
            List<WebElement> filters = driver.findElements(By.xpath(TOGGLE_BUTTON_XPATH));
            WebElement predefinedFilter = filters.stream().filter(filter -> filter.getText().equals(filterName)).findFirst()
                    .orElseThrow(() -> new RuntimeException(NO_PREDEFINED_FILTER_EXCEPTION));
            return new PredefinedFilter(driver, predefinedFilter);
        }
        
        private static PredefinedFilter createPredefinedFilter(WebDriver driver, WebDriverWait wait, WebElement predefinedFilter) {
            DelayUtils.waitForPageToLoad(driver, wait);
            DelayUtils.waitByXPath(wait, TOGGLE_BUTTON_XPATH);
            return new PredefinedFilter(driver, predefinedFilter);
        }
        
        private void selectPredefinedFilter() {
            if (!isFilterSelected()) {
                Actions action = new Actions(driver);
                action.moveToElement(predefinedFilterElement).click(predefinedFilterElement).perform();
            }
        }
        
        private boolean isFilterSelected() {
            return predefinedFilterElement.getAttribute(CLASS_ATTRIBUTE).contains("active");
        }

        private String getLabel(){
         return    predefinedFilterElement.findElement(By.cssSelector(".btnLabel")).getText();
        }
    }
    
    private static class AttributeChooser {
        private static final String LABEL_XPATH = ".//label";
        private static final String ACCEPT_XPATH = ".//button[text()='Accept']";
        private static final String CHECKED_XPATH = ".//input[@checked]";
        private static final String FORM_ELEMENT_XPATH = ".//div[@class='form-element']";
        private static final String CANT_FIND_NODE_EXCEPTION_PATTERN = "Cant find node %s";
        
        private final WebDriver driver;
        private final WebElement columnManager;
        
        private AttributeChooser(WebDriver driver, WebElement columnManager) {
            this.driver = driver;
            this.columnManager = columnManager;
        }
        
        private void disableColumnByLabel(String attributeLabel) {
            WebElement node = getNode(attributeLabel);
            if (isSelected(node)) {
                node.findElement(By.xpath(LABEL_XPATH)).click();
            }
        }
        
        private void enabledColumnByLabel(String attributeLabel) {
            WebElement node = getNode(attributeLabel);
            if (!isSelected(node)) {
                WebElementUtils.clickWebElement(driver, node.findElement(By.xpath(LABEL_XPATH)));
            }
        }
        
        private WebElement getNode(String attribute) {
            return getNodes().stream().filter(n -> n.getText().equals(attribute)).findFirst()
                    .orElseThrow(() -> new RuntimeException(String.format(CANT_FIND_NODE_EXCEPTION_PATTERN, attribute)));
        }
        
        private List<WebElement> getNodes() {
            return columnManager.findElements(By.xpath(FORM_ELEMENT_XPATH));
        }
        
        private boolean isSelected(WebElement node) {
            return !node.findElements(By.xpath(CHECKED_XPATH)).isEmpty();
        }
        
        private void clickAccept() {
            columnManager.findElement(By.xpath(ACCEPT_XPATH)).click();
        }
    }
}
