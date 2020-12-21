package com.oss.framework.widgets;

import com.oss.framework.widgets.Widget.WidgetType;

public class WidgetFactory {


    public static Widget getWidget(String widgetId, WidgetType widgetType) {
        switch(widgetType) {
            case TABLE_WIDGET: {

            }
            case OLD_TABLE_WIDGET: {

            }
        }
        throw new RuntimeException("Widget does not exist: " + widgetType + " id: " + widgetId);
    }

}
