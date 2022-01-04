package com.oss.framework.widgets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.Widget.WidgetType;
import com.oss.framework.widgets.propertypanel.PropertyPanel;

public class WidgetFactory {

    public static Widget getWidget(String widgetId, WidgetType widgetType, WebDriver webDriver, WebDriverWait wait) {
        switch (widgetType) {
            case TABLE_WIDGET: {
                break;
            }
            case OLD_TABLE_WIDGET: {
                break;
            }
            case PROPERTY_PANEL:
                return PropertyPanel.createById(webDriver, wait, widgetId);
        }
        throw new RuntimeException("Widget does not exist: " + widgetType + " id: " + widgetId);
    }

}
