package com.oss.framework.widgets.tablewidget;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;

import java.util.List;
import java.util.Map;

public interface TableInterface {

    void selectRow(int row);

    default void selectFirstRow() {
        selectRow(0);
    }

    int getColumnSize(int column);

    default int getFirstColumnSize() {
        return getColumnSize(0);
    }

    void resizeColumn(int column, int offset);

    default void resizeFirstColumn(int offset) {
        resizeColumn(0, offset);
    }

    List<String> getActiveColumnHeaders();

    void disableColumn(String columnId);

    void disableColumnByLabel(String columnLabel);

    void enableColumnByLabel(String columnLabel);

    void changeColumnsOrder(String columnLabel, int position);

    void selectRowByAttributeValue(String attributeId, String value);

    boolean isNoData();

    int getRowNumber(String value, String attributeLabel);

    void selectRowByAttributeValueWithLabel(String attributeLabel, String value);

    String getValueCell(int index, String attributeLabel);

    void searchByAttribute(String attributeId, Input.ComponentType componentType, String value);

    void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value);

    void callAction(String actionId);

    void callActionByLabel(String actionLabel);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String groupLabel, String actionLabel);

    @Deprecated //TODO: we can treat kebab menu as a special group, so this method should be removed
    void clickOnKebabMenu();

    @Deprecated //TODO we already have callAction method
    void clickOnAction(String actionName);

    void refreshUntilNoData(int waitTime, String refreshLabel);

    Multimap<String, String> getAppliedFilters();

    @Deprecated //TODO: this method does not belongs here, it should be moved to property panel implementation
    Map<String, String> getPropertyNamesToValues();

    List<TableRow> getSelectedRows();
}
