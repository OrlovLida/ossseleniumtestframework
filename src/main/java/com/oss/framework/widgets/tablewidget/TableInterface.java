package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Map;

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

    void selectLinkInSpecificColumn(String columnName);

    int getRowNumber(String value, String attributeLabel);

    void selectRowByAttributeValueWithLabel(String attributeLabel, String value);

    String getCellValue(int index, String attributeLabel);

    void searchByAttribute(String attributeId, Input.ComponentType componentType, String value);

    void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value);

    void callAction(String actionId);

    void callActionByLabel(String actionLabel);

    void callAction(String groupId, String actionId);

    void selectTabByLabel(String tabLabel, String id);

    void callActionByLabel(String groupLabel, String actionLabel);

    void doRefreshWhileNoData(int waitTime, String refreshId);

    Multimap<String, String> getAppliedFilters();

    @Deprecated
        //TODO: this method does not belongs here, it should be moved to property panel implementation
    Map<String, String> getPropertyNamesToValues();

    List<TableRow> getSelectedRows();

    String getCellValueById(int row, String columnId);
}
