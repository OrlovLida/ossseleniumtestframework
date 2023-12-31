package com.oss.framework.widgets.table;

import java.util.List;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;

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

    void disableColumnByLabel(String columnLabel, String... path);

    void enableColumnByLabel(String columnLabel, String... path);

    void changeColumnsOrder(String columnLabel, int position);

    void selectRowByAttributeValue(String attributeId, String value);

    boolean hasNoData();

    void clickLink(String columnName);

    int getRowNumber(String value, String columnId);

    void selectRowByAttributeValueWithLabel(String attributeLabel, String value);

    String getCellValue(int index, String columnId);

    void searchByAttribute(String attributeId, Input.ComponentType componentType, String value);

    /**
     * @Depracated Method will be removed in 5.0.x release, use method searchByColumn(String attributeLabel, String value) -> OldTable instead
     */
    @Deprecated
    void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value);

    void searchByAttribute(String attributeId, String value);

    void callAction(String actionId);

    void callActionByLabel(String actionLabel);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String groupLabel, String actionLabel);

    void doRefreshWhileNoData(int waitTime, String refreshId);

    Multimap<String, String> getAppliedFilters();

    List<TableRow> getSelectedRows();

}
