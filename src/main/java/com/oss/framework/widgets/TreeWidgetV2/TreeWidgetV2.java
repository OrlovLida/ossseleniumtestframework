package com.oss.framework.widgets.TreeWidgetV2;

import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.selectionbar.SelectionBarComponent;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

public class TreeWidgetV2 extends Widget {

    private TreeComponent treeComponent;
    private AdvancedSearch advancedSearch;

    public static TreeWidgetV2 create(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new TreeWidgetV2(driver, wait, widgetId);
    }

    private TreeWidgetV2(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public List<Node> getVisibleNodes() {
        return getTreeComponent().getVisibleNodes();
    }

    public Node getNode(String label) {
        Optional<Node> node = getVisibleNodes().stream().filter(n -> n.getLabel().equals(label)).findFirst();
        if (node.isPresent()) {
            return node.get();
        }
        throw new NoSuchElementException("Can't find node: " + label);
    }

    @Deprecated
    public void selectFirstNode() {
        List<Node> nodes = getTreeComponent().getVisibleNodes();
        nodes.get(0).toggleNode();
    }

    public void selectNode(int nodeNumber) {
        List<Node> nodes = getTreeComponent().getVisibleNodes();
        nodes.get(nodeNumber).toggleNode();
    }

    @Deprecated
    public Node getFirstNode() {
        return getVisibleNodes().get(0);
    }

    public Node getNode(int nodeNumber) {
        return getVisibleNodes().get(nodeNumber);
    }

    public void typeIntoSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    public void callActionById(String groupLabel, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(groupLabel, id);
    }

    public void selectNodeByLabel(String label) {
        getNode(label).toggleNode();
    }

    public void unselectNodeByLabel(String label) {
        Node node = getNode(label);
        if (node.isToggled()) {
            node.toggleNode();
        }
    }

    public void expandNodeWithLabel(String label) {
        getNode(label).expandNode();
    }

    public void clearAllFilters() {
        getAdvancedSearch().clearAllFilters();
    }

    public void clearFilter(String filterName) {
        getAdvancedSearch().clearFilter(filterName);
    }

    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        AdvancedSearch advancedSearch = getAdvancedSearch();
        advancedSearch.setFilter(attributeId, componentType, value);
        advancedSearch.clickApply();
    }

    public boolean isEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getVisibleNodes().isEmpty();
    }

    public Node getNodeByPath(String path) {
        return getTreeComponent().getNodeByPath(path);
    }

    public Node getNodeByLabelsPath(String labels) {
        return getTreeComponent().getNodeByLabelsPath(labels);
    }

    public PaginationComponent getPagination() {
        return PaginationComponent.createFromParent(driver, webDriverWait, webElement);
    }

    public void openSelectionBar() {
        getSelectionBarComponent().openSelectionBar(driver);
    }

    public void hideSelectionBar() {
        getSelectionBarComponent().hideSelectionBar(driver);
    }

    private TreeComponent getTreeComponent() {
        if (treeComponent == null) {
            treeComponent = TreeComponent.create(driver, webDriverWait, webElement);
        }
        return treeComponent;
    }

    private AdvancedSearch getAdvancedSearch() {
        if (advancedSearch == null) {
            advancedSearch = AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
        }
        return advancedSearch;
    }

    private SelectionBarComponent getSelectionBarComponent() {
        return SelectionBarComponent.create(driver, webDriverWait, id);
    }
}
