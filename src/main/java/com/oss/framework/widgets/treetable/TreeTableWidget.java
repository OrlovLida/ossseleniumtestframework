/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.treetable;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.selectionbar.SelectionBarComponent;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableRow;

/**
 * @author Gabriela Zaranek
 */
public class TreeTableWidget extends Widget implements TableInterface {

    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet";
    private static final String TREE_TABLE_WIDGET_CLASS = "common-treetablewidgetgraphql";
    private static final int REFRESH_INTERVAL = 2000;
    private static final String TABLE_CONTENT_CSS = ".sticky-table__content";
    private static final String TABLE_COMPONENT_PATTERN = "[" + CSSUtils.TEST_ID + "='%s'] " + TABLE_CONTENT_CSS;
    private AdvancedSearch advancedSearch;

    private TreeTableWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static TreeTableWidget createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidget(wait, TREE_TABLE_WIDGET_CLASS);
        DelayUtils.waitBy(wait, By.cssSelector(String.format(TABLE_COMPONENT_PATTERN, widgetId)));
        return new TreeTableWidget(driver, wait, widgetId);
    }

    public boolean isRowExpanded(int index) {
        return getTableComponent().getRow(index).isRowExpanded();
    }

    public void expandRow(int index) {
        getTableComponent().getRow(index).expandRow();
    }

    public void expandRow(String value, String columnId) {
        getTableComponent().getRow(value, columnId).expandRow();
    }

    public void unselectRow(int index) {
        getTableComponent().unselectRow(index);
    }

    public void collapseRow(int index) {
        getTableComponent().getRow(index).collapseRow();
    }

    public void collapseRow(String value, String columnId) {
        getTableComponent().getRow(value, columnId).collapseRow();
    }

    public void callActionById(String groupId, String actionId) {
        ActionsContainer.createFromParent(webElement, driver, webDriverWait).callActionByLabel(groupId, actionId);
    }

    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    @Override
    public void selectRow(int row) {
        getTableComponent().selectRow(row);
    }

    @Override
    public int getColumnSize(int column) {
        return getTableComponent().getColumnSizeByPosition(column);
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

    public void unselectRowByAttributeValue(String attributeId, String value) {
        getTableComponent().getRow(value, attributeId).unselectRow();
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
    public void selectRowByAttributeValueWithLabel(String columnLabel, String value) {
        TableComponent tableComponent = getTableComponent();
        int columnIndex = tableComponent.getColumnHeaders().indexOf(columnLabel);
        getTableComponent().getRow(value, tableComponent.getColumnIds().get(columnIndex)).selectRow();
    }

    public void clickLink(int index, String columnId) {
        getTableComponent().clickLink(index, columnId);
    }

    public String getSelectedObjectCount() {
        return getSelectionBarComponent().getSelectedObjectsCount();
    }

    public void showOnlySelectedRows() {
        getSelectionBarComponent().showSelected();
    }

    public void showAllRows() {
        getSelectionBarComponent().showAll();
    }

    public void unselectAllRows() {
        getSelectionBarComponent().unselectAll();
    }

    private SelectionBarComponent getSelectionBarComponent() {
        return SelectionBarComponent.create(this.driver, this.webDriverWait, id);
    }

    public boolean isExpandPresent(int index) {
        return getTableComponent().getRow(index).isExpandPresent();
    }

    @Override
    public String getCellValue(int rowIndex, String columnId) {
        return getTableComponent().getCellValue(rowIndex, columnId);
    }

    public TableComponent.Cell getCell(int index, String columnId) {
        return getTableComponent().getCell(index, columnId);
    }

    @Override
    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        openAdvancedSearch();
        if (!CSSUtils.isElementPresent(driver, attributeId)) {
            advancedSearch.selectAttributes(Lists.newArrayList(attributeId));
        }
        setFilterContains(attributeId, componentType, value);
        confirmFilter();
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void searchByAttribute(String attributeId, String value) {
        openAdvancedSearch();
        if (!CSSUtils.isElementPresent(driver, attributeId)) {
            advancedSearch.selectAttributes(Lists.newArrayList(attributeId));
        }
        setFilterContains(attributeId, value);
        confirmFilter();
    }

    public void clearAllFilters() {
        getAdvancedSearch().clearAllFilters();
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

    public boolean isActionVisible(String groupId, String actionId) {
        return getContextActions().isActionVisibleById(groupId, actionId);
    }

    public boolean isActionVisible(String actionId) {
        return getContextActions().isActionVisibleById(actionId);
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

    public List<TableRow> getAllRows() {
        return getTableComponent().getVisibleRows();
    }

    public TableComponent.Row getRow(int index) {
        return getTableComponent().getRow(index);
    }

    public void clickRow(int row) {
        getTableComponent().clickRow(row);
    }

    public PaginationComponent getPagination() {
        return getTableComponent().getPaginationComponent();
    }

    public List<String> getActiveColumnIds() {
        return getTableComponent().getColumnIds();
    }

    public AttributesChooser getAttributesChooser() {
        return getTableComponent().getAttributesChooser();
    }

    public void setColumnWidth(String columnId, String columnWidth) {
        getTableComponent().setColumnWidth(columnId, columnWidth);
    }

    public void changeColumnsOrderById(String columnId, int position) {
        getTableComponent().changeColumnsOrderById(columnId, position);
    }

    public ActionsContainer getContextActions() {
        return ActionsContainer.createFromParent(this.webElement, this.driver, this.webDriverWait);
    }

    public void setCellValue(int row, String columnId, String value) {
        getTableComponent().setCellValue(row, columnId, value);
    }

    public boolean isValuePresent(String value, String columnId) {
        return getTableComponent().isValuePresent(value, columnId);
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(driver, webDriverWait, id);
    }

    private void openAdvancedSearch() {
        getAdvancedSearch().openSearchPanel();
    }

    private void setFilterContains(String componentId, Input.ComponentType componentType, String value) {
        getAdvancedSearch().setFilter(componentId, componentType, value);
    }

    private void setFilterContains(String componentId, String value) {
        getAdvancedSearch().setFilter(componentId, value);
    }

    private void confirmFilter() {
        getAdvancedSearch().clickApply();
    }

    public AdvancedSearch getAdvancedSearch() {
        if (advancedSearch == null) {
            advancedSearch = AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
        }
        return advancedSearch;
    }

    public void selectAllRows() {
        getTableComponent().selectAll();
    }

    public Boolean isHeaderCheckboxSelected() {
        return getTableComponent().isHeaderCheckboxSelected();
    }
}