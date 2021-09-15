package com.oss.framework.components.tree;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.components.selection_tab.SelectionBarComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treewidget.InlineMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

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

    private PaginationComponent paginationComponent;

    public static TreeComponent create(WebDriver driver, WebDriverWait webDriverWait, WebElement parent) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement treeComponent = parent.findElement(By.className(TREE_CLASS));
        return new TreeComponent(driver, webDriverWait, treeComponent);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement treeComponent;

    private TreeComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement treeComponent) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.treeComponent = treeComponent;
    }

    public PaginationComponent getPaginationComponent() {
        if (paginationComponent == null) {
            PaginationComponent.createFromParent(driver, webDriverWait, treeComponent);
        }
        return paginationComponent;
    }

    public void expandNodeByPath(String path) {
        Node node = getNodeByPath(path);
        node.expandNode();
    }

    public void toggleNodeByPath(String path) {
        Node node = getNodeByPath(path);
        node.toggleNode();
    }

    public Node getNodeByPath(String path) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(path));
        return getNodeByPath(pathElements, false);
    }

    public Node getNodeByLabelsPath(String labels) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(labels));
        return getNodeByPath(pathElements, true);
    }

    private Node getNodeByPath(List<String> pathElements, boolean isLabel) {
        StringBuilder currentPath = new StringBuilder();
        Node node = null;
        for (int i = 0; i < pathElements.size(); i++) {
            currentPath.append(pathElements.get(i));
            String tempPath = currentPath.toString();
            List<Node> nodes = getVisibleNodes();

            if (isLabel) {
                node = nodes.stream().filter(n -> n.getPathLabel().equals(tempPath))
                        .findFirst().orElseThrow(() -> new RuntimeException("Cant find node: " + tempPath));
            } else {
                node = nodes.stream().filter(n -> n.getPath().equals(tempPath))
                        .findFirst().orElseThrow(() -> new RuntimeException("Cant find node: " + tempPath));
            }

            if (i != pathElements.size() - 1) {
                node.expandNode();
                currentPath.append(".");
            }
        }

        return node;
    }

    public List<Node> getVisibleNodes() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return this.treeComponent.findElements(By.xpath("." + getNodeClassPath())).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    @Deprecated
    public List<Node> getNodes(int level) {
        DelayUtils.waitForNestedElements(webDriverWait, treeComponent, getNodeClassPath());
        String marginToString = level * LEFT_MARGIN_IN_PX + "px";
        return this.treeComponent
                .findElements(By.xpath(getNodeClassPath() + "[.//div[contains(@style, '" + marginToString + "')]]")).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    @Deprecated
    public List<Node> getNodesWithExpander(int level) {
        DelayUtils.waitForNestedElements(webDriverWait, treeComponent, getNodeClassPath());
        String marginToString = level * LEFT_MARGIN_IN_PX + "px";
        return this.treeComponent
                .findElements(By.xpath(getNodeClassPath() + "[.//i][.//div[contains(@style, '" + marginToString + "')]]"))
                .stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    private String getNodeClassPath() {
        return "//div[@class='" + NODE_CLASS + "']";
    }

    public static class Node {
        private static final String DATA_GUID_ATTR = "data-guid";
        private static final String DATA_PATH_LABEL_ATTR = "data-testid";

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement node;

        private Node create(WebDriver driver, WebDriverWait webDriverWait, WebElement node) {
            // TODO: add path to equals
            return new Node(driver, webDriverWait, node);
        }

        private Node(WebDriver driver, WebDriverWait webDriverWait, WebElement node) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.node = node;
        }

        public String getPath() {
            return CSSUtils.getAttributeValue(DATA_GUID_ATTR, node);
        }

        public String getPathLabel() {
            return CSSUtils.getAttributeValue(DATA_PATH_LABEL_ATTR, node);
        }

        public boolean isToggled() {
            WebElement input = node.findElement(By.xpath(NODE_CHECKBOX_XPATH));
            return input.isSelected();
        }

        public void toggleNode() {
            Actions action = new Actions(driver);
            action.moveToElement(node).perform();

            WebElement input = node.findElement(By.xpath(NODE_CHECKBOX_LABEL_XPATH));
            input.click();
        }

        public void expandNode() {
            if (!isExpanded()) {
                Actions action = new Actions(driver);
                action.moveToElement(node).perform();
                WebElement button = node.findElement(By.xpath(EXPANDER_BUTTON_XPATH));
                button.click();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }

        public void expandNextLevel() {
            if (isExpandNextLevelEnabled()) {
                Actions action = new Actions(driver);
                action.moveToElement(node).perform();
                WebElement expandNextLevelArrow = node.findElement(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH));
                action.click(expandNextLevelArrow).perform();
                WebElement button =
                        node.findElement(By.xpath("//a[contains(@" + CSSUtils.TEST_ID + ",'" + EXPAND_NEXT_LEVEL_BUTTON + "')]"));
                action.moveToElement(button).click().perform();
                DelayUtils.waitForElementDisappear(webDriverWait, node.findElement(By.xpath(SPIN_XPATH)));
            } else
                throw new RuntimeException("Expand Next Level is not available for Node " + getLabel());

        }

        public void collapseNode() {
            if (isExpanded()) {
                Actions action = new Actions(driver);
                action.moveToElement(node).perform();
                WebElement button = node.findElement(By.xpath(COLLAPSER_BUTTON_XPATH));
                button.click();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }

        public boolean isExpanded() {
            return node.findElements(By.xpath(EXPANDER_ICON_XPATH)).isEmpty();
        }

        private boolean isExpandNextLevelEnabled() {
            return !node.findElements(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH)).isEmpty();

        }

        public String getLabel() {
            WebElement richText = node.findElement(By.className(NODE_LABEL_CLASS));
            return richText.getText();
        }

        public void callAction(String groupId, String actionId) {
            Actions actions = new Actions(driver);
            actions.moveToElement(node).build().perform();

            InlineMenu menu = InlineMenu.create(node, driver, webDriverWait);
            menu.callAction(groupId, actionId);
        }
    }
}
