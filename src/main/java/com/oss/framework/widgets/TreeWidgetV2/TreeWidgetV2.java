package com.oss.framework.widgets.TreeWidgetV2;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treewidget.TreeWidget;

public class TreeWidgetV2 extends Widget {

    private TreeComponent treeComponent;

    public static TreeWidgetV2 create(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new TreeWidgetV2(driver, wait, widgetId);
    }

    private TreeWidgetV2(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    private List<Node> getVisibleNodes() {
        return getTreeComponent().getNodes(0);
    }

    public TreeComponent.Node getNode(String label) {
        Optional<Node> node = getVisibleNodes().stream().filter(n -> n.getLabel().equals(label)).findFirst();
        if (node.isPresent()) {
            return node.get();
        }
        throw new NoSuchElementException("Can't find node: " + label);
    }

    public void selectFirstNode() {
        List<TreeComponent.Node> nodes = getTreeComponent().getNodes(0);
        nodes.get(0).toggleNode();
    }

    public TreeComponent.Node getFirstNode() {
        return getVisibleNodes().get(0);
    }

    public void selectNodeByPosition(int position) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getVisibleNodes().get(position).toggleNode();
    }

    public void callActionById(String groupLabel, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callAction(groupLabel, id);
    }

    public void selectNodeByLabel(String label) {
        getNode(label).toggleNode();
    }

    @Deprecated
    public void expandNode() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        getVisibleNodes().get(0).expandNode();
    }

    @Deprecated
    public void expandNode(String nodeLabel) {
        getNode(nodeLabel).expandNode();
    }

    public void expandNodeWithLabel(String label) {
        getNode(label).expandNode();
    }

    public void performSearch(String inputText) {
        fullSearchText(inputText);
    }

    public Boolean isTreeWidgetEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getVisibleNodes().isEmpty();
    }

    private TreeComponent getTreeComponent() {
        if(treeComponent == null) {
            treeComponent = TreeComponent.create(driver, webDriverWait, webElement);
        }
        return treeComponent;
    }

}
