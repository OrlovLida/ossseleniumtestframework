package com.oss.framework.widgets.toolbarwidget;

import com.oss.framework.widgets.Widget;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToolbarWidget extends Widget {

    private ToolbarWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }
    private final static String TOOLBAR_WIDGET_CLASS = "toolbarWidget";
}
