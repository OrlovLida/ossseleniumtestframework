package com.oss.framework.components.tree;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class TreeComponent {

    private static final String TREE_CLASS = "tree-component";
    private static final String NODE_CLASS = "tree-node";
    private static final String NODE_LABEL_CLASS = "OSSRichText";
    private static final String EXPANDER_ICON_XPATH = ".//i[@class='OSSIcon fa fa-plus']";
    private static final String EXPANDER_BUTTON_XPATH = ".//div[@class = 'tree-node-expand-icon tree-node-expand-icon--collapsed']";
    private static final String EXPAND_NEXT_LEVEL_ARROW_XPATH = "tree-node-expand--caret";
    private static final String EXPAND_NEXT_LEVEL_BUTTON = "nextLevelButton";
    private static final String COLLAPSER_BUTTON_XPATH = ".//div[@class = 'tree-node-expand-icon tree-node-expand-icon--expanded']";
    private static final String NODE_CHECKBOX_XPATH = ".//div[contains(@class,'tree-node-selection')]//input";
    private static final String NODE_CHECKBOX_LABEL_XPATH = ".//div[contains(@class,'tree-node-selection')]//label";
    private static final String SPIN_XPATH = ".//i[contains(@class,'fa-spin')]";

    private static final int LEFT_MARGIN_IN_PX = 24;
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement treeComponentElement;

    private TreeComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement treeComponentElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.treeComponentElement = treeComponentElement;
    }

    public static TreeComponent create(WebDriver driver, WebDriverWait webDriverWait, WebElement parent) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement treeComponent = parent.findElement(By.className(TREE_CLASS));
        return new TreeComponent(driver, webDriverWait, treeComponent);
    }

    public void expandNodeByPath(String path) {
        getNodeByPath(path).expandNode();
    }

    public void toggleNodeByPath(String path) {
        getNodeByPath(path).toggleNode();

    }

    public Node getNodeByPath(String path) {
        return findNodeByPath(path).orElseThrow(() -> new NoSuchElementException("Cannot find Node " + path));
    }

    public Node getNodeByLabelsPath(String labels) {
        return findNodeByLabelsPath(labels).orElseThrow(() -> new NoSuchElementException("Cannot find Node " + labels));
    }

    public List<Node> getVisibleNodes() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return this.treeComponentElement.findElements(By.xpath("." + getNodeClassPath())).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    public Optional<Node> findNodeByLabelsPath(String labels) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(labels));
        return getNodeByPath(pathElements, true);
    }

    public Optional<Node> findNodeByPath(String path) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(path));
        return getNodeByPath(pathElements, false);

    }

    private Optional<Node> getNodeByPath(List<String> pathElements, boolean isLabel) {
        StringBuilder currentPath = new StringBuilder();
        Optional<Node> node = Optional.empty();
        for (int i = 0; i < pathElements.size(); i++) {
            currentPath.append(pathElements.get(i));
            String tempPath = currentPath.toString();
            List<Node> nodes = getVisibleNodes();

            if (isLabel) {
                node = nodes.stream().filter(n -> n.getPathLabel().equals(tempPath))
                        .findFirst();
            } else {
                node = nodes.stream().filter(n -> n.getPath().equals(tempPath))
                        .findFirst();
            }
            if (i != pathElements.size() - 1) {
                node.ifPresent(Node::expandNode);
                currentPath.append(".");
            }
        }
        return node;
    }

    private String getNodeClassPath() {
        return "//div[@class='" + NODE_CLASS + "']";
    }

    public static class Node {
        private static final String DATA_GUID_ATTR = "data-guid";
        private static final String DATA_PATH_LABEL_ATTR = "data-label-path";

        private static final String FILTERS_BUTTON_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='filters-panel-button']";
        private static final String ADVANCED_SEARCH_PANEL_ID = "advanced-search_panel";

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement nodeElement;

        private Node(WebDriver driver, WebDriverWait webDriverWait, WebElement node) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.nodeElement = node;
        }

        public String getPath() {
            return CSSUtils.getAttributeValue(DATA_GUID_ATTR, nodeElement);
        }

        public String getPathLabel() {
            return CSSUtils.getAttributeValue(DATA_PATH_LABEL_ATTR, nodeElement);
        }

        public boolean isToggled() {
            WebElement input = nodeElement.findElement(By.xpath(NODE_CHECKBOX_XPATH));
            return input.isSelected();
        }

        public void toggleNode() {
            Actions action = new Actions(driver);
            action.moveToElement(nodeElement).perform();

            WebElement input = nodeElement.findElement(By.xpath(NODE_CHECKBOX_LABEL_XPATH));
            input.click();
        }

        public void expandNode() {
            if (!isExpanded()) {
                Actions action = new Actions(driver);
                action.moveToElement(nodeElement).perform();
                WebElement button = nodeElement.findElement(By.xpath(EXPANDER_BUTTON_XPATH));
                button.click();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }

        public void expandNextLevel() {
            if (isExpandNextLevelPresent()) {
                Actions action = new Actions(driver);
                action.moveToElement(nodeElement).perform();
                WebElement expandNextLevelArrow = nodeElement.findElement(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH));
                action.click(expandNextLevelArrow).perform();
                WebElement button =
                        nodeElement.findElement(By.xpath("//a[contains(@" + CSSUtils.TEST_ID + ",'" + EXPAND_NEXT_LEVEL_BUTTON + "')]"));
                WebElementUtils.clickWebElement(driver, button);
                DelayUtils.waitForElementDisappear(webDriverWait, nodeElement.findElement(By.xpath(SPIN_XPATH)));
            } else
                throw new NoSuchElementException("Expand Next Level is not available for Node " + getLabel());

        }

        public void collapseNode() {
            if (isExpanded()) {
                Actions action = new Actions(driver);
                action.moveToElement(nodeElement).perform();
                WebElement button = nodeElement.findElement(By.xpath(COLLAPSER_BUTTON_XPATH));
                button.click();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }

        public boolean isExpanded() {
            return nodeElement.findElements(By.xpath(EXPANDER_ICON_XPATH)).isEmpty();
        }

        public String getLabel() {
            WebElement richText = nodeElement.findElement(By.className(NODE_LABEL_CLASS));
            return richText.getText();
        }

        public void callAction(String groupId, String actionId) {
            Actions actions = new Actions(driver);
            actions.moveToElement(nodeElement).build().perform();

            InlineMenu menu = InlineMenu.create(nodeElement, driver, webDriverWait);
            menu.callAction(groupId, actionId);
        }

        public void callAction(String actionId) {
            Actions actions = new Actions(driver);
            actions.moveToElement(nodeElement).build().perform();

            InlineMenu menu = InlineMenu.create(nodeElement, driver, webDriverWait);
            menu.callAction(actionId);
        }

        public AdvancedSearch openAdvancedSearch() {
            clickFilter();
            return AdvancedSearch.createById(driver, webDriverWait, ADVANCED_SEARCH_PANEL_ID);
        }

        boolean isExpandNextLevelPresent() {
            return !nodeElement.findElements(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH)).isEmpty();
        }

        private void clickFilter() {
            if (isFilterButtonPresent()) {
                Actions actions = new Actions(driver);
                WebElement filterButton = nodeElement.findElement(By.xpath(FILTERS_BUTTON_XPATH));
                actions.moveToElement(filterButton).click(filterButton).build().perform();
            } else
                throw new NoSuchElementException("Filter Node is not available for Node " + getLabel());
        }

        private boolean isFilterButtonPresent() {
            return !nodeElement.findElements(By.xpath(FILTERS_BUTTON_XPATH)).isEmpty();
        }
    }
}
