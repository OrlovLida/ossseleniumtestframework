package com.oss.pages.platform;

import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class HierarchyViewPage extends BasePage {

    private TreeWidget mainTree;

    public HierarchyViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeWidget() {
        if(mainTree == null){
            Widget.waitForWidget(wait, "TreeWidget");
            mainTree = TreeWidget.createByClass(driver, "TreeWidget", wait);
        }
        return mainTree;
    }
}
