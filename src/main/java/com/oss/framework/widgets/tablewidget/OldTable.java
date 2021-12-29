package com.oss.framework.widgets.tablewidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.tabswidget.TabsWidget;

public class OldTable implements TableInterface {

    private static final Logger log = LoggerFactory.getLogger(OldTable.class);
    private static final String ROWS_COUNTER_SPANS_PATH = ".//div[@class='rowsCounter']//span";
    private static final int REFRESH_INTERVAL = 2000;
    private static final String PROPERTY_NAME_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyName')]/div[contains(@class,'Cell Row_%s')]";
    private static final String PROPERTY_VALUE_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyValue')]/div[contains(@class,'Cell Row_%s')]";
    private static final String EXPAND_PROPERTIES_BUTTON_PATH =
            "//div[contains(@class, 'tabWindow')]//a[contains(@class, 'fullScreenButton')]";
    private static final String FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN =
            "(//div[contains(@class, 'Col_ColumnId_Name')]//div[contains(text(), '%s')])[%d]";
    private static final String TABLE_COMPONENT = ".//div[contains(@class, 'OSSTableComponent')]";
    private static final String COLUMNS_WITHOUT_CHECKBOX =
            ".//div[contains(@class,'OSSTableColumn') and not(contains(@class,'Col_SELECTION'))]";
    private static final String CONTEXT_ACTIONS_CONTAINER = "//div[contains(@class, 'windowToolbar')] | //*[@class='actionsContainer']";
    private static final String INPUT = ".//input";
    private static final String HEADER = ".//div[contains(@class, 'Header')]";
    private static final String RICH_TEXT = ".//div[contains(@class, 'OSSRichText')]";
    private static final String TOGGLE_BUTTON = ".//span[contains(@class,'ToggleButton')]";
    private static final String CELL = ".//div[contains(@class, 'Cell')]";
    private static final String TABLE_IN_ACTIVE_TAB_XPATH =
            "//div[@data-attributename='TableTabsApp']//div[contains(@class,'tabsContainerSingleContent active')]//div[@class='AppComponentContainer']/div";
    private static final String NOT_IMPLEMENTED = "Not implemented method in OldTable";
    private static final String LABEL = ".//label";

    // to be removed after adding data-attributeName OSSWEB-8398
    @Deprecated
    public static OldTable createByOssWindow(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[@class='OssWindow']");
        WebElement table = driver.findElement(By.xpath("//div[@class='OSSTableContainer']"));
        WebElement window = driver.findElement(By.xpath("//div[@class='OssWindow']"));
        return new OldTable(driver, wait, null, table, window);
    }

    public static OldTable createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitByXPath(wait, "//div[contains(@id,'" + componentId + "')] | //div[@" + CSSUtils.TEST_ID + "='" + componentId + "']");
        WebElement table =
                driver.findElement(By.xpath("//div[@id='" + componentId + "'] | //div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(table).build().perform();
        return new OldTable(driver, wait, componentId, table);
    }

    public static OldTable createByComponentDataAttributeName(WebDriver driver, WebDriverWait wait, String dataAttributeName) {
        DelayUtils.waitByXPath(wait, "//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']");
        WebElement table = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(table).build().perform();
        WebElement window = table.findElement(
                By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']//ancestor::div[contains(@class,'OssWindow')]"));
        return new OldTable(driver, wait, dataAttributeName, table, window);
    }

    public static OldTable createTableForActiveTab(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String tableIdFromActiveTab = driver.findElement(By.xpath(TABLE_IN_ACTIVE_TAB_XPATH)).getAttribute(CSSUtils.TEST_ID);
        return createByComponentDataAttributeName(driver, wait, tableIdFromActiveTab);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    @Deprecated // TODO:
    private final WebElement table;
    @Deprecated
    private WebElement window;

    private OldTable(WebDriver driver, WebDriverWait wait, String widgetId, WebElement table, WebElement window) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
        this.table = table;
        this.window = window;
    }

    private OldTable(WebDriver driver, WebDriverWait wait, String widgetId, WebElement table) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
        this.table = table;
    }

    @Override
    public void selectRow(int row) {
        Map<String, Column> columns = createColumnsFilters();
        Lists.newArrayList(columns.values()).get(0).selectCell(row);
    }

    @Override
    public int getColumnSize(int column) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void resizeColumn(int column, int offset) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public List<String> getActiveColumnHeaders() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {
        AttributeChooser attributeChooser = new AttributeChooser(driver, getColumnManager());
        attributeChooser.disableColumnByLabel(columnLabel);
        attributeChooser.acceptButton();
    }

    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {
        AttributeChooser attributeChooser = new AttributeChooser(driver, getColumnManager());
        attributeChooser.enabledColumnByLabel(columnLabel);
        attributeChooser.acceptButton();
    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        WebElement columnPosition = table.findElements(By.className("OSSTableColumn")).get(position);

        DragAndDrop.dragAndDrop(getColumn(columnLabel).getDragElement(), new DragAndDrop.DropElement(columnPosition), driver);

    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getColumn(attributeLabel).selectCell(value);
    }

    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        if (componentType != ComponentType.TEXT_FIELD) {
            throw new IllegalArgumentException("Old table widget supports" + ComponentType.TEXT_FIELD + "only");
        }
        clearColumnValue(attributeLabel).setValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public Column clearColumnValue(String attributeLabel) {
        Column column = getColumn(attributeLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
        column.clear();
        DelayUtils.waitForPageToLoad(driver, wait);
        return column;
    }

    @Override
    public void callAction(String actionId) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, window);
        actions.callActionById(actionId);
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, window);
        actions.callActionByLabel(actionLabel);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        getActionsInterface().callActionById(groupId, actionId);
    }

    @Override
    public void selectTabByLabel(String tabLabel, String id) {
        TabsWidget tabs = TabsWidget.createById(driver, wait, id);
        tabs.selectTabByLabel(tabLabel);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    public String getCellValue(int index, String attributeLabel) {
        return getColumn(attributeLabel).getValueCell(index);
    }

    private Column getColumn(String columnLabel) {
        Map<String, Column> columns = createColumnsFilters();
        if (columns.containsKey(columnLabel)) {
            return columns.get(columnLabel);
        } else {
            log.debug("Available columns:");
            columns.forEach((key, value) -> log.debug(key));
            throw new NoSuchElementException("Cannot find a column with label = " + columnLabel);
        }
    }

    /**
     * @param anyLabelInTable any column label existing in table
     * @return number of rows in table
     */
    public int getNumberOfRowsInTable(String anyLabelInTable) {
        return getColumn(anyLabelInTable).getNumberOfRows();
    }

    @Override
    public void doRefreshWhileNoData(int waitTime, String refreshId) {
        if (widgetId == null) {
            throw new IllegalArgumentException("widgetId property is missing");
        }
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + waitTime;
        while (hasNoData() && (stopTime > System.currentTimeMillis())) {
            DelayUtils.sleep(REFRESH_INTERVAL);
            callAction(OldActionsContainer.KEBAB_GROUP_ID, refreshId);
        }
    }

    @Override
    public Multimap<String, String> getAppliedFilters() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    public Map<String, String> getPropertyNamesToValues() {
        int index = 0;
        clickExpandPropertiesButton();
        String propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
        String propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
        Optional<WebElement> propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
        Optional<WebElement> propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        Map<String, String> namesToValues = new HashMap<>();
        while (propertyName.isPresent() && propertyValue.isPresent()) {
            index++;
            String propertyNameText = propertyName.get().getText();
            String propertyValueText = propertyValue.get().getText();
            namesToValues.put(propertyNameText, propertyValueText);
            propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
            propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
            propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
            propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        }
        clickExpandPropertiesButton();
        return namesToValues;
    }

    @Override
    public List<TableRow> getSelectedRows() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public String getCellValueById(int row, String columnId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    private void clickExpandPropertiesButton() {
        WebElement expandButton = driver.findElement(By.xpath(EXPAND_PROPERTIES_BUTTON_PATH));
        expandButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Override
    public boolean hasNoData() {
        List<WebElement> noData = driver
                .findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']//h3[contains(@class,'noDataWithColumns')]"));
        return !noData.isEmpty();
    }

    @Override
    public void selectLinkInSpecificColumn(String columnName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getColumn(columnName).selectLink();
    }

    public void selectRowByPartialNameAndIndex(String partialName, int index) {
        String xpath = String.format(FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN, partialName, index);
        driver.findElement(By.xpath(xpath)).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getTableObjectsCount() {
        List<WebElement> rowsCounterSpans = table
                .findElements(By.xpath(ROWS_COUNTER_SPANS_PATH));
        try {
            return Integer.parseInt(rowsCounterSpans.get(rowsCounterSpans.size() - 2).getText());
        } catch (NumberFormatException e) {
            log.debug("Problem with getting table object count. Value is not a number.");
            return 0;
        }
    }

    public void changeItemsPerPageValue(int pageOption) {
        WebElement pagination = table
                .findElement(By.className("OSSPagination"));
        pagination.findElement(By.xpath(".//button")).click();
        pagination.findElement(By.xpath(".//li[text()='" + pageOption + "']")).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getRowNumber(String value, String attributeLabel) {
        DelayUtils.waitForNestedElements(wait, table, "//*[contains(text(),'" + value + "')]");
        return getColumn(attributeLabel).indexOf(value);
    }

    public void selectPredefinedFilter(String filterName) {
        PredefinedFilter predefinedFilter = PredefinedFilter.createPredefinedFilter(driver, wait, filterName);
        predefinedFilter.selectPredefinedFilter();
    }

    private Map<String, Column> createColumnsFilters() {
        Map<String, Column> columns = Maps.newHashMap();
        DelayUtils.waitForNestedElements(wait, table, TABLE_COMPONENT);
        List<Column> columns2 =
                table.findElements(By.xpath(COLUMNS_WITHOUT_CHECKBOX))
                        .stream().map(columnElement -> new Column(columnElement, wait, driver)).collect(Collectors.toList());
        for (Column column : Lists.reverse(columns2)) {
            if (column.checkIfLabelExist()) {
                columns.put(column.getLabel(), column);
            }
        }
        return columns;
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForNestedElements(wait, window, CONTEXT_ACTIONS_CONTAINER);
        boolean isNewActionContainer = isElementPresent(window, By.className("actionsContainer"));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(window, driver, wait);
        } else {
            return OldActionsContainer.createFromParent(driver, wait, window);
        }
    }

    private static boolean isElementPresent(WebElement window, By by) {
        try {
            window.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private WebElement getColumnManager() {
        Actions actions = new Actions(driver);
        WebElement columnsSettingsIcon = table.findElement(By.className("OSSTableColumnsSettingsIcon"));
        actions.moveToElement(columnsSettingsIcon).click().build().perform();
        DelayUtils.waitBy(wait, By.xpath("//div[@class='OSSStickyColumnsSettings']"));
        return driver.findElement(By.className("OSSStickyColumnsSettings"));
    }

    private static class Column {
        private final WebElement columnElement;
        private final WebDriverWait wait;
        private final WebDriver driver;

        private Column(WebElement columnElement, WebDriverWait wait, WebDriver driver) {
            this.columnElement = columnElement;
            this.wait = wait;
            this.driver = driver;
        }

        private String getLabel() {
            WebElement header = moveToHeader();
            try {
                return header.findElement(By.xpath(INPUT)).getAttribute("label");
            } catch (NoSuchElementException e) {
                return header.getText();
            }
        }

        private WebElement moveToHeader() {
            WebElement header = columnElement.findElement(By.xpath(HEADER));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", header);
            return header;
        }

        private DragAndDrop.DraggableElement getDragElement() {
            WebElement dragButton = moveToHeader().findElement(By.className("flex"));
            return new DragAndDrop.DraggableElement(dragButton);
        }

        private boolean checkIfLabelExist() {
            try {
                return !columnElement.findElement(By.xpath(INPUT)).getAttribute("label").equals("");
            } catch (NoSuchElementException e) {
                return !columnElement.getText().isEmpty();
            }
        }

        private void selectCell(String value) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(".//div[contains(@class, 'Cell Row')]"));
            for (WebElement cell : cells) {
                DelayUtils.waitForNestedElements(wait, cell, RICH_TEXT);
                WebElement richText = cell.findElement(By.xpath(RICH_TEXT));
                if (richText.getText().equals(value)) {
                    Actions action = new Actions(driver);
                    action.click(cell).perform();
                    break;
                }
            }
        }

        public int indexOf(String value) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL));

            for (WebElement cell : cells) {

                DelayUtils.waitForNestedElements(wait, cell, RICH_TEXT);
                WebElement richText = cell.findElement(By.xpath(RICH_TEXT));
                if (richText.getText().equals(value)) {
                    return cells.indexOf(cell);
                }
            }
            throw new NoSuchElementException("Cannot find a row with the provided value");
        }

        public void selectCell(int index) {
            WebElement cell = getCellByIndex(index);
            Actions action = new Actions(driver);
            action.moveToElement(cell).click(cell).perform();
        }

        private String getValueCell(int index) {
            WebElement cell = getCellByIndex(index);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cell);
            Actions action = new Actions(driver);
            action.moveToElement(cell).build().perform();
            return cell.getText();
        }

        private WebElement getCellByIndex(int index) {
            moveToHeader();
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL));
            return cells.get(index);
        }

        private int getNumberOfRows() {
            List<WebElement> cells = columnElement.findElements(By.xpath(CELL));
            return cells.size();
        }

        private void setValue(String value) {
            WebElement input = columnElement.findElement(By.xpath(INPUT));
            Actions action = new Actions(driver);
            action.moveToElement(input).build().perform();
            input.sendKeys(value);
            input.sendKeys(Keys.ENTER);
        }

        private void clear() {
            WebElement input = columnElement.findElement(By.xpath(INPUT));
            Actions action = new Actions(driver);
            action.moveToElement(input).click(input).build()
                    .perform();
            input = columnElement.findElement(By.xpath(INPUT));
            input.sendKeys(Keys.CONTROL + "a");
            input.sendKeys(Keys.DELETE);
            DelayUtils.sleep();
        }

        private void selectLink() {
            DelayUtils.waitByXPath(wait, "//div[contains(@class, 'Cell')]//div[contains(@class, 'OSSRichText')]");
            DelayUtils.waitForNestedElements(wait, columnElement, ".//a[contains(@href, '#/')]");
            Actions action = new Actions(driver);
            action.click(columnElement.findElement(By.xpath(".//div[contains(@class, 'Cell')]//a[contains(@href, '#/')]"))).perform();
        }
    }

    private static class PredefinedFilter {
        private final WebDriver driver;
        private final WebElement predefinedFilterElement;

        private PredefinedFilter(WebDriver driver, WebElement predefinedFilterElement) {
            this.driver = driver;
            this.predefinedFilterElement = predefinedFilterElement;
        }

        private static PredefinedFilter createPredefinedFilter(WebDriver driver, WebDriverWait wait, String filterName) {
            DelayUtils.waitForPageToLoad(driver, wait);
            DelayUtils.waitByXPath(wait, TOGGLE_BUTTON);
            List<WebElement> filters = driver.findElements(By.xpath(TOGGLE_BUTTON));
            WebElement predefinedFilter = filters.stream().filter(filter -> filter.getText().equals(filterName)).findFirst()
                    .orElseThrow(() -> new RuntimeException("There is no Predefined Filter"));
            return new PredefinedFilter(driver, predefinedFilter);
        }

        private void selectPredefinedFilter() {
            if (!isFilterSelected()) {
                Actions action = new Actions(driver);
                action.moveToElement(predefinedFilterElement).click(predefinedFilterElement).perform();
            }
        }

        private void unselectPredefinedFilter() {
            if (isFilterSelected()) {
                Actions action = new Actions(driver);
                action.moveToElement(predefinedFilterElement).click(predefinedFilterElement).perform();
            }
        }

        private boolean isFilterSelected() {
            return predefinedFilterElement.getAttribute("class").contains("active");
        }

    }

    private static class AttributeChooser {
        private WebDriver driver;
        private WebElement columnManager;

        private AttributeChooser(WebDriver driver, WebElement columnManager) {
            this.driver = driver;
            this.columnManager = columnManager;
        }

        private void disableColumnByLabel(String attributeLabel) {
            WebElement node = getNode(attributeLabel, true);
            if (isSelected(node)) {
                node.findElement(By.xpath(LABEL)).click();
            }
        }

        private void enabledColumnByLabel(String attributeLabel) {
            Actions actions = new Actions(driver);
            WebElement node = getNode(attributeLabel, true);
            if (!isSelected(node)) {
                actions.moveToElement(node.findElement(By.xpath(LABEL))).click().perform();
            }
        }

        private void disableColumnById(String columnId) {
            WebElement node = getNode(columnId, false);
            if (isSelected(node)) {
                node.findElement(By.xpath(LABEL)).click();
            }
        }

        private WebElement getNode(String attribute, boolean isLabel) {
            if (isLabel) {

                return getNodes().stream().filter(n -> n.getText().equals(attribute)).findFirst()
                        .orElseThrow(() -> new RuntimeException("Cant find node " + attribute));
            } else {
                return columnManager.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + attribute + "']"));
            }
        }

        private List<WebElement> getNodes() {
            return columnManager.findElements(By.xpath(".//div[@class='form-element']"));
        }

        private boolean isSelected(WebElement node) {
            return !node.findElements(By.xpath(".//input[@checked]")).isEmpty();
        }

        private void acceptButton() {
            columnManager.findElement(By.xpath(".//button[text()='Accept']")).click();
        }

    }

}
