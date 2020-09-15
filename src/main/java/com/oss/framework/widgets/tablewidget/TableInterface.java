package com.oss.framework.widgets.tablewidget;

import com.oss.framework.components.inputs.Input;

public interface TableInterface {

    void selectRow(int row);

    void selectRowByAttributeValue(String attributeId, String value);

    int getRowNumber(String value, String attributeLabel);

    void selectRowByAttributeValueWithLabel(String attributeLabel, String value);

    String getValueCell(int index, String attributeLabel);

    void searchByAttribute(String attributeId, Input.ComponentType componentType, String value);

    void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value);

    void callAction(String actionId);

    void callActionByLabel(String actionLabel);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String groupLabel, String actionLabel);

    void clickOnKebabMenu();

    void clickOnAction(String actionName);

    void refreshUntilNoData(int waitTime, String refreshLabel);

}
