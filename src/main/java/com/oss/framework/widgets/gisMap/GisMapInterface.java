package com.oss.framework.widgets.gisMap;

public interface GisMapInterface {

    void callActionById(String actionId);

    void callActionByLabel(String group, String label);

    void searchFirstResult(String value);

    void clickOnMapByCoordinates(int x, int y);

    void doubleClickOnMapByCoordinates(int x, int y);
}
