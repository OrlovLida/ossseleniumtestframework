package com.oss.framework.widgets.tree;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.selectionbar.SelectionBarComponent;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidgetV2 extends Widget {

    private TreeComponent treeComponent;
    private AdvancedSearch advancedSearch;

    private TreeWidgetV2(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static TreeWidgetV2 create(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new TreeWidgetV2(driver, wait, widgetId);
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

    public void selectNode(int nodeNumber) {
        List<Node> nodes = getTreeComponent().getVisibleNodes();
        nodes.get(nodeNumber).toggleNode();
    }

    public Node getNode(int nodeNumber) {
        return getVisibleNodes().get(nodeNumber);
    }

    /**
     * @deprecated standardizing the nomenclature of methods, please use fullTextSearch(String text). Method will be removed in 3.0.x release
     */
    @Deprecated
    public void typeIntoSearch(String text) {
        fullTextSearch(text);
    }

    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    public void callActionByLabel(String groupLabel, String actionLabel) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionByLabel(groupLabel, actionLabel);
    }

    public void callActionById(String groupId, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(groupId, id);
    }

    public void callActionById(String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(id);
    }

    public String getGroupActionLabel(String id) {
        ActionsContainer actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        return actionsContainer.getGroupActionLabel(id);
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
        advancedSearch = getAdvancedSearch();
        advancedSearch.openSearchPanel();
        if (!CSSUtils.isElementPresent(driver, attributeId)) {
            advancedSearch.selectAttributes(Lists.newArrayList(attributeId));
        }
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

    public Optional<Node> findNodeByLabelsPath(String labels) {
        return getTreeComponent().findNodeByLabelsPath(labels);
    }

    public PaginationComponent getPagination() {
        return PaginationComponent.createFromParent(webElement);
    }

    public void openSelectionBar() {
        getSelectionBarComponent().openSelectionBar();
    }

    public void hideSelectionBar() {
        getSelectionBarComponent().hideSelectionBar();
    }

    public void unselectAllNodes() {
        getSelectionBarComponent().unselectAll();
    }

    public void showSelectedNodes() {
        getSelectionBarComponent().showSelected();
    }

    public void showAllInNodes() {
        getSelectionBarComponent().showAll();
    }

    public String getSelectedObjectCount() {
        return getSelectionBarComponent().getSelectedObjectsCount();
    }

    public Set<String> getNodeChildrenByLabelsPath(String labels) {
        return getTreeComponent().getNodeChildren(labels);
    }
    public Set<String> getNodeChildrenByPath(String path) {
        return getTreeComponent().getNodeChildrenByPath(path);
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
