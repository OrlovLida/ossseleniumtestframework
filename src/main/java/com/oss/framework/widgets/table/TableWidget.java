package com.oss.framework.widgets.table;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.selectionbar.SelectionBarComponent;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TableWidget extends Widget implements TableInterface {
    public static final String TABLE_WIDGET_CLASS = "TableWidget";
    public static final String REFRESH_ACTION_ID = "refreshButton";
    public static final String EXPORT_ACTION_ID = "exportButton";
    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet";
    private static final String TABLE_CONTENT_CSS = ".sticky-table__content";
    private static final String TABLE_COMPONENT_PATTERN = "[" + CSSUtils.TEST_ID + "='%s'] " + TABLE_CONTENT_CSS;
    private static final int REFRESH_INTERVAL = 2000;

    private AdvancedSearch advancedSearch;
    private TableComponent tableComponent;

    private TableWidget(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        super(driver, wait, tableWidgetId);
    }

    public static TableWidget createById(WebDriver driver, String tableWidgetId, WebDriverWait webDriverWait) {
        Widget.waitForWidget(webDriverWait, TABLE_WIDGET_CLASS);
        DelayUtils.waitBy(webDriverWait, By.cssSelector(String.format(TABLE_COMPONENT_PATTERN, tableWidgetId)));
        return new TableWidget(driver, webDriverWait, tableWidgetId);
    }

    @Override
    public void selectRow(int row) {
        getTableComponent().selectRow(row);
    }

    @Override
    public int getColumnSize(int columnIndex) {
        return getTableComponent().getColumnSizeByPosition(columnIndex);
    }

    @Override
    public void resizeColumn(int column, int offset) {
        getTableComponent().resizeColumnByPosition(column, offset);
    }

    @Override
    public List<String> getActiveColumnHeaders() {
        return getTableComponent().getColumnHeaders();
    }

    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getAttributesChooser();
        attributesChooser.disableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        getTableComponent().changeColumnsOrder(columnLabel, position);
    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        getTableComponent().getRow(value, attributeId).selectRow();
    }

    @Override
    public boolean hasNoData() {
        return getTableComponent().hasNoData();
    }

    @Override
    public void clickLink(String columnName) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public int getRowNumber(String value, String columnId) {
        return getTableComponent().getRow(value, columnId).getIndex();
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public String getCellValue(int rowIndex, String columnId) {
        return getTableComponent().getCellValue(rowIndex, columnId);
    }

    public boolean isCellValueBold(int row, String columnId) {
        return getTableComponent().isCellValueBold(row, columnId);
    }

    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        openAdvancedSearch();
        if (!CSSUtils.isElementPresent(driver, attributeId)) {
            advancedSearch.selectAttributes(Lists.newArrayList(attributeId));
        }
        setFilterContains(attributeId, componentType, value);
        confirmFilter();
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void searchByAttribute(String attributeId, String value) {
        openAdvancedSearch();
        setFilterContains(attributeId, value);
        confirmFilter();
    }

    @Override
    public void callAction(String actionId) {
        getContextActions().callActionById(actionId);
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        getContextActions().callActionById(groupId, actionId);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        getContextActions().callActionByLabel(groupLabel, actionLabel);
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
    public Multimap<String, String> getAppliedFilters() {
        return getAdvancedSearch().getAppliedFilters();
    }

    @Override
    public List<TableRow> getSelectedRows() {
        return getTableComponent().getVisibleRows().stream()
                .filter(TableRow::isSelected).collect(Collectors.toList());
    }

    /**
     * @deprecated method will be removed in 3.0.x release, use instead getCellValue
     */
    @Override
    @Deprecated
    public String getCellValueById(int row, String columnId) {
        return getCellValue(row, columnId);
    }

    public void clickRow(int row) {
        getTableComponent().clickRow(row);
    }

    public void clickLink(int index, String columnId) {
        getTableComponent().clickLink(index, columnId);
    }

    public ActionsContainer getContextActions() {
        return ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
    }

    public void selectVisibilitySearchAttributes(List<String> attributeIds) {
        openAdvancedSearch();
        getAdvancedSearch().selectAttributes(attributeIds);
    }

    public void unselectVisibilitySearchAttributes(List<String> attributeIds) {
        openAdvancedSearch();
        getAdvancedSearch().unselectAttributes(attributeIds);
    }

    public List<String> getAllVisibleFilters() {
        openAdvancedSearch();
        List<String> filters = getAdvancedSearch().getAllVisibleFilters();
        getAdvancedSearch().clickCancel();
        return filters;
    }

    public List<String> getActiveColumnIds() {
        return getTableComponent().getColumnIds();
    }

    public void changeColumnsOrderById(String columnId, int position) {
        getTableComponent().changeColumnsOrderById(columnId, position);
    }

    public void sortColumnByASC(String columnId) {
        getTableComponent().sortColumnByASC(columnId);
    }

    public void sortColumnByDESC(String columnId) {
        getTableComponent().sortColumnByDESC(columnId);
    }

    public void turnOffSortingForColumn(String columnId) {
        getTableComponent().turnOffSorting(columnId);
    }

    public void setColumnWidth(String columnId, String columnWidth) {
        getTableComponent().setColumnWidth(columnId, columnWidth);
    }

    public void setLinkPattern(String columnId, String linkPattern) {
        getTableComponent().setLinkPattern(columnId, linkPattern);
    }

    public void clearAllFilters() {
        getAdvancedSearch().clearAllFilters();
    }

    public void clearFilter(String filterName) {
        getAdvancedSearch().clearFilter(filterName);
    }

    public void markFavoriteFilter(String label) {
        openAdvancedSearch();
        getAdvancedSearch().markFavoriteFilter(label);
        getAdvancedSearch().clickCancel();
    }

    public void chooseSavedFiltersByLabel(String label) {
        openAdvancedSearch();
        getAdvancedSearch().selectSavedFilterByLabel(label);
        getAdvancedSearch().clickApply();
    }

    public void saveAsNewFilter(String name) {
        openAdvancedSearch();
        getAdvancedSearch().saveAsNewFilter(name);
        getAdvancedSearch().clickCancel();
    }

    public AttributesChooser getAttributesChooser() {
        return getTableComponent().getAttributesChooser();
    }

    public int getRowsNumber() {
        return getTableComponent().getVisibleRows().size();
    }

    public PaginationComponent getPagination() {
        return getTableComponent().getPaginationComponent();
    }

    public void selectAllRows() {
        getTableComponent().selectAll();
    }

    public void unselectRow(int row) {
        getTableComponent().unselectRow(row);
    }

    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    public void setQuickFilter(String name) {
        getAdvancedSearch().setQuickFilter(name);
    }

    public void scrollHorizontally(int offset) {
        getTableComponent().scrollHorizontally(offset);
    }

    public void scrollVertically(int offset) {
        getTableComponent().scrollVertically(offset);
    }

    public void openSelectionBar() {
        getSelectionBarComponent().openSelectionBar();
    }

    public void hideSelectionBar() {
        getSelectionBarComponent().hideSelectionBar();
    }

    public void unselectAllRows() {
        getSelectionBarComponent().unselectAll();
    }

    public void showOnlySelectedRows() {
        getSelectionBarComponent().showSelected();
    }

    public void showAllRows() {
        getSelectionBarComponent().showAll();
    }

    public String getSelectedObjectCount() {
        return getSelectionBarComponent().getSelectedObjectsCount();
    }

    public AdvancedSearch getAdvancedSearch() {
        if (advancedSearch == null) {
            advancedSearch = AdvancedSearch.createByClass(driver, webDriverWait, AdvancedSearch.SEARCH_COMPONENT_CLASS);
        }
        return advancedSearch;
    }

    private TableComponent getTableComponent() {
        if (tableComponent == null) {
            tableComponent = TableComponent.create(this.driver, this.webDriverWait, this.id);
        }
        return tableComponent;
    }

    private SelectionBarComponent getSelectionBarComponent() {
        return SelectionBarComponent.create(this.driver, this.webDriverWait, id);
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
}
