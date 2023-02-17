package com.oss.framework.widgets.gismap;

import com.oss.framework.components.tree.TreeComponent;

public interface GisMapInterface {

    void callActionByLabel(String actionLabel);

    void setValueContains(String value);

    void setValue(String value);

    void clickMapByCoordinates(int x, int y);

    void doubleClickMapByCoordinates(int x, int y);

    void clickMapByCoordinatesWithShift(int x, int y);

    void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination);

    TreeComponent getLayersTree();

    boolean isCanvasPresent();

    String getCanvasObject();

    void setMap(String mapLabel);

    String getScale();

    void zoomIn();

    void zoomOut();
}
