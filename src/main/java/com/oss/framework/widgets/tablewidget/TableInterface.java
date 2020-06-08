package com.oss.framework.widgets.tablewidget;

import com.oss.framework.components.Input;

public interface TableInterface {

    void selectRow(int row);

    void selectRowByAttributeValue(String attributeId, String value);

    void selectRowByAttributeValueWithLabel(String attributeLabel, String value);

    void searchByAttribute(String attributeId, Input.ComponentType componentType, String value);

    void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value);

    void callAction(String actionId);

    void callActionByLabel(String actionLabel);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String groupLabel, String actionLabel);

}
