package com.oss.framework.widgets.tablewidget;

public interface TableRow {

    boolean isSelected();
    int getIndex();
    void clickRow();
    void selectRow();
    void unselectRow();
    void callAction(String groupId, String actionId);
}
