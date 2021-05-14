package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;
import com.google.common.collect.Multimap;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Widget;

public class TableWidget extends Widget implements TableInterface {
    public static final String TABLE_WIDGET_CLASS = "TableWidget";
    public static final String REFRESH_ACTION_ID = "refreshButton";
    private static final int REFRESH_INTERVAL = 2000;
    public static final String EXPORT_ACTION_ID = "exportButton";

    private static final String headers = ".//div[@data-rbd-droppable-id='header-container']//div[contains(@class, 'headerItem')]";

    private static final String activePageBtn = ".//li[@class='page active']";
    private static final String rowsCounter = ".//div[@class='rowsCounter']/span[last()]";
    private static final String kebabMenuBtn = ".//div[@id='frameworkCustomButtonsGroup']";
    private static final String selectAllCheckbox = ".//input[@id='checkbox-checkbox']";

    private AdvancedSearch advancedSearch;

    @Deprecated
    public static TableWidget create(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(widgetClass)); // TODO: change to id
        return new TableWidget(driver, widgetClass, webDriverWait);
    }

    public static TableWidget createById(WebDriver driver, String tableWidgetId, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + tableWidgetId + "']"));
        return new TableWidget(driver, webDriverWait, tableWidgetId);
    }

    private TableWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    private TableWidget(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        super(driver, wait, tableWidgetId);
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(this.driver, this.webDriverWait, this.id);
    }

    public ActionsContainer getContextActions() {
        return ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
    }

    @Override
    public Multimap<String, String> getAppliedFilters() {
        return getAdvancedSearch().getAppliedFilters();
    }

    @Override
    public void selectRow(int row) {
        selectTableRow(row);
    }

    @Override
    public int getColumnSize(int columnIndex) {
        return getTableComponent().getColumnSizeByPosition(columnIndex);
    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean hasNoData() {
        return driver.findElements(By.xpath("//div[@class='TableBody']//*[@class='noDataWithColumns']")).size() > 0;
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
        openAdvancedSearch();
        setFilterContains(attributeId, componentType, value);
        confirmFilter();
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callAction(String actionId) {
        getContextActions().callActionById(actionId);
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callAction(String groupId, String actionId) {
        getContextActions().callAction(groupId, actionId);
    }

    @Override
    public void selectTabByLabel(String tabLabel, String id) {
        // TODO: remove that method
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        getContextActions().callActionByLabel(groupLabel, actionLabel);
    }

    @Override
    public int getRowNumber(String value, String attributeLabel)
    {
        Column x = getColumns().stream().filter(column -> column.getText().equals(attributeLabel)).findFirst().get();
    getColumns().indexOf(x);
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public String getCellValue(int rowIndex, String columnId) {
        return getTableComponent().getCellValue(rowIndex, columnId);
    }

    @Override
    public void doRefreshWhileNoData(int waitTime, String refreshId) {
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + waitTime;
        while (hasNoData() && (stopTime > System.currentTimeMillis())) {
            DelayUtils.sleep(REFRESH_INTERVAL);
            callAction(ActionsContainer.KEBAB_GROUP_ID, refreshId);
        }
    }

    @Override
    @Deprecated
    public Map<String, String> getPropertyNamesToValues() {
        // TODO: Remove that method
        throw new RuntimeException("Not implemented for TableWidget");
    }

    @Override
    public List<TableRow> getSelectedRows() {
        return getTableComponent().getVisibleRows().stream()
                .filter(TableRow::isSelected).collect(Collectors.toList());
    }

    @Override
    public List<String> getActiveColumnHeaders() {
        return getTableComponent().getColumnHeaders();
    }

    @Override
    public void disableColumn(String columnLabel) {
        getAttributesChooser().disableAttributeByLabel(columnLabel, "")
                .clickApply();
    }

    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {
        getAttributesChooser().disableAttributeByLabel(columnLabel, path)
                .clickApply();
    }

    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {
        getAttributesChooser()
                .enableAttributeByLabel(columnLabel, path)
                .clickApply();
    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        DragAndDrop.dragAndDrop(getDraggableElement(columnLabel), getDropElement(position), driver);
    }

    public void changeColumnsOrder(String sourceColumnLabel, String targetColumnLabel) {
        DragAndDrop.dragAndDrop(getDraggableElement(sourceColumnLabel), getDropElement(targetColumnLabel), driver);
    }

    public void changeColumnsOrder(int sourcePosition, int targetPosition) {
        DragAndDrop.dragAndDrop(getDraggableElement(sourcePosition), getDropElement(targetPosition), driver);
    }

    public void clearAllFilters() {
        getAdvancedSearch().clearAllFilters();
    }

    private DragAndDrop.DraggableElement getDraggableElement(String columnLabel) {
        WebElement source = getColumnByLabel(columnLabel).findElement(By.xpath(".//div[@class = 'btn-drag']"));
        Actions action = new Actions(driver);
        action.moveToElement(source).perform();
        return new DragAndDrop.DraggableElement(source);
    }

    private DragAndDrop.DraggableElement getDraggableElement(int sourcePosition) {
        WebElement source = getColumns().get(sourcePosition).getWebElement().findElement(By.xpath(".//div[@class = 'btn-drag']"));
        Actions action = new Actions(driver);
        action.moveToElement(source).perform();
        return new DragAndDrop.DraggableElement(source);
    }

    private DragAndDrop.DropElement getDropElement(int targetPosition) {
        return new DragAndDrop.DropElement(getColumns().get(targetPosition).getWebElement());
    }

    private DragAndDrop.DropElement getDropElement(String targetLabel) {
        return new DragAndDrop.DropElement(getColumnByLabel(targetLabel));
    }

    public SaveConfigurationWizard openSaveConfigurationWizard() {
        callAction(ActionsContainer.KEBAB_GROUP_ID, SaveConfigurationWizard.SAVE_CONFIG_ID);
        return SaveConfigurationWizard.create(driver, webDriverWait);
    }

    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        callAction(ActionsContainer.KEBAB_GROUP_ID, ChooseConfigurationWizard.CHOOSE_CONFIG_ID);
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
        return elements.stream().map(we -> new Column(driver, webDriverWait, we)).collect(Collectors.toList());
    }

    public AttributesChooser getAttributesChooser() {
        return getTableComponent().getAttributesChooser();
    }

    @Deprecated
    public void clickOnKebabMenu() {
        getKebabMenuBtn().click();
    }

    private WebElement getKebabMenuBtn() {
        return this.webElement.findElement(By.xpath(kebabMenuBtn));
    }

    private WebElement getRowsCounter() {
        return this.webElement.findElement(By.xpath(rowsCounter));
    }

    private WebElement getColumnByLabel(String label) {
        return driver.findElement(By.xpath(".//p[text()='" + label + "']/ancestor::div[@class='headerItem text-align']"));
    }

    // TODO: wrap WebElement
    public WebElement getActivePageBtn() {
        return this.webElement.findElement(By.xpath(activePageBtn));
    }

    public String getLabelOfActivePageBtn() {
        return getActivePageBtn().getText();
    }

    public int getRowsNumber() {
        return Integer.valueOf(getRowsCounter().getText());
    }

    public boolean isFirstPageActive() {
        return getLabelOfActivePageBtn().equals("1");
    }

    public int howManyRowsOnFirstPage() {
        return getTableComponent().getVisibleRows().size();
    }

    private void selectTableRow(int row) {
        getTableComponent().selectRow(row);
    }

    public void selectAllRows() {
        this.webElement.findElement(By.xpath(selectAllCheckbox)).click();
    }

    public void unselectTableRow(int row) {
        getTableComponent().unselectRow(row);
    }

    @Deprecated //use hasNoData()
    public boolean checkIfTableIsEmpty() {
        return getTableComponent().getVisibleRows().size() == 0;
    }

    private AdvancedSearch getAdvancedSearch() {
        if (advancedSearch == null) {
            advancedSearch = AdvancedSearch.createByClass(driver, webDriverWait, AdvancedSearch.SEARCH_COMPONENT_CLASS);
        }
        return advancedSearch;
    }

    private void openAdvancedSearch() {
        getAdvancedSearch().openSearchPanel();
    }

    private void confirmFilter() {
        getAdvancedSearch().clickApply();
    }

    private void setFilterContains(String componentId, ComponentType componentType, String value) {
        Input input = getAdvancedSearch().getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public void typeIntoSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    public void scrollHorizontally(int offset) {
       getTableComponent().scrollHorizontally(offset);
    }

    public void scrollVertically(int offset) {
        getTableComponent().scrollVertically(offset);
    }

    @Override
    public void resizeColumn(int column, int offset) {
        getTableComponent().resizeColumnByPosition(column, offset);
    }

    public static class Column {
        private final WebElement webElement;
        private final WebDriver driver;
        private final WebDriverWait webDriverWait;

        private Column(WebDriver driver, WebDriverWait wait, WebElement webElement) {
            this.driver = driver;
            this.webDriverWait = wait;
            this.webElement = webElement;
        }

        public String getText() {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
            return webElement.findElement(By.xpath(".//p")).getText();
        }

        private int getWidth() {
            return CSSUtils.getWidthValue(webElement);
        }

        private WebElement getWebElement() {
            return webElement;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Column column = (Column) o;
            return Objects.equal(getText(), column.getText());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getText());
        }
    }

}
