package com.oss.framework.widgets.gisMap;

public interface GisMapInterface {

    void callActionByLabel(String actionLabel);

    void searchFirstResult(String value);

    void searchResult(String value);

    void clickOnMapByCoordinates(int x, int y);

    void doubleClickOnMapByCoordinates(int x, int y);

    void clickOnMapByCoordinatesWithShift(int x, int y);

    void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination);
}
