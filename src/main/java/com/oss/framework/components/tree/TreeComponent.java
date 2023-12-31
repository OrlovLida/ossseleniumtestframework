package com.oss.framework.components.tree;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oss.framework.components.alerts.ComponentMessage;
import com.oss.framework.components.alerts.ElementMessage;
import com.oss.framework.components.alerts.Message;
import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.scrolls.CustomScrolls;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
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
    private static final String CUSTOM_SCROLLBARS_CSS = ".custom-scrollbars";
    private static final String TREE_COMPONENT_NOT_TREE_COMPONENT_LOADER_CSSS = ".tree-component:not(.tree-component--loader)";
    private static final String CANNOT_FIND_NODE_EXCEPTION = "Cannot find Node ";
    private static final String DATA_PATH_LABEL_ATTR = "data-label-path";
    private static final String DATA_PATH_ATTR = "data-path";
    private static final String HAS_NO_DATA_CLASS = "tree_no_data";
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement treeComponentElement;

    private TreeComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement treeComponentElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.treeComponentElement = treeComponentElement;
    }

    public static TreeComponent create(WebDriver driver, WebDriverWait webDriverWait, WebElement parent) {
        DelayUtils.waitForNestedElements(webDriverWait, parent, By.cssSelector(TREE_COMPONENT_NOT_TREE_COMPONENT_LOADER_CSSS));
        WebElement treeComponent = parent.findElement(By.className(TREE_CLASS));
        return new TreeComponent(driver, webDriverWait, treeComponent);
    }

    public boolean hasNoData() {
        return WebElementUtils.isElementPresent(treeComponentElement, By.className(HAS_NO_DATA_CLASS));
    }

    public void expandNodeByPath(String path) {
        getNodeByPath(path).expandNode();
    }

    public void toggleNodeByPath(String path) {
        getNodeByPath(path).toggleNode();
    }

    public Node getNodeByPath(String path) {
        return findNodeByPath(path).orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_NODE_EXCEPTION + path));
    }

    public Node getNodeByLabelsPath(String labels) {
        return findNodeByLabelsPath(labels).orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_NODE_EXCEPTION + labels));
    }

    public List<Node> getVisibleNodes() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return this.treeComponentElement.findElements(By.xpath("." + getNodeClassPath())).stream()
                .map(node -> Node.create(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    public Set<String> getNodeChildren(String path, boolean isLabel) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(path));
        Node root = getNodeByPath(pathElements, isLabel).orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_NODE_EXCEPTION + path));
        root.expandNode();
        List<Node> visibleChildren = getNodesByPathContains(path, isLabel);
        Set<String> allChildren = visibleChildren.stream().map(Node::getLabel).collect(Collectors.toCollection(HashSet::new));
        allChildren.addAll(getAllVisibleChildren(visibleChildren, path, isLabel));
        return allChildren;
    }

    public Optional<Node> findNodeByLabelsPath(String labels) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(labels));
        return getNodeByPath(pathElements, true);
    }

    public Optional<Node> findNodeByPath(String path) {
        List<String> pathElements = Lists.newArrayList(Splitter.on(".").split(path));
        return getNodeByPath(pathElements, false);
    }

    public void dropAsChild(DragAndDrop.DraggableElement source, Node parent) {
        DragAndDrop.dragAndDrop(source, parent.getDropElement(), 0, 30, driver);
    }

    public void dropAsRoot(DragAndDrop.DraggableElement source) {
        Node node = getVisibleNodes().get(0);
        DragAndDrop.dragAndDrop(source, node.getDropElement(), driver);
    }

    private List<Node> getNodesByPathContains(String path, boolean isLabel) {
        if (isLabel) {
            return treeComponentElement.findElements(By.cssSelector("[" + DATA_PATH_LABEL_ATTR + "^='" + path + ".']")).stream()
                    .map(child -> Node.create(driver, webDriverWait, child))
                    .collect(Collectors.toList());
        } else {
            return treeComponentElement.findElements(By.cssSelector("[" + DATA_PATH_ATTR + "^='" + path + ".']")).stream()
                    .map(child -> Node.create(driver, webDriverWait, child))
                    .collect(Collectors.toList());
        }
    }

    public List<Message> getMessages() {
        return ComponentMessage.create(driver, treeComponentElement.getAttribute(CSSUtils.TEST_ID)).getMessages();
    }

    private Node getLastVisibleNode(List<Node> visibleNodes) {
        return visibleNodes.get(visibleNodes.size() - 1);
    }

    private Optional<Node> getNodeByPath(List<String> pathElements, boolean isLabel) {
        scrollToFirstNode();
        StringBuilder currentPath = new StringBuilder();
        Optional<Node> node = Optional.empty();
        for (int i = 0; i < pathElements.size(); i++) {
            currentPath.append(pathElements.get(i));
            String tempPath = currentPath.toString();
            List<Node> nodes = getVisibleNodes();
            node = getNode(isLabel, tempPath, nodes);
            if (!node.isPresent()) {
                node = scrollToNode(isLabel, node, tempPath);
            }
            if (i != pathElements.size() - 1) {
                node.ifPresent(Node::expandNode);
                currentPath.append(".");
            }
        }
        return node;
    }

    private Optional<Node> scrollToNode(boolean isLabel, Optional<Node> node, String tempPath) {
        List<Node> nodes = getVisibleNodes();
        Node lastNode = getLastVisibleNode(nodes);
        Node tempNode = null;
        while (!node.isPresent() && !lastNode.equals(tempNode)) {
            lastNode.moveToNode();
            nodes = getVisibleNodes();
            node = getNode(isLabel, tempPath, nodes);
            tempNode = lastNode;
            lastNode = getLastVisibleNode(nodes);
        }
        return node;
    }

    private Set<String> getAllVisibleChildren(List<Node> nodes, String path, boolean isLabel) {
        Node lastNode = getLastVisibleNode(nodes);
        lastNode.moveToNode();
        Set<String> allChildren = Sets.newHashSet();
        while (!lastNode.equals(getLastVisibleNode(getNodesByPathContains(path, isLabel)))) {
            allChildren.addAll(getNodesByPathContains(path, isLabel).stream().map(Node::getLabel).collect(Collectors.toList()));
            lastNode = getLastVisibleNode(getNodesByPathContains(path, isLabel));
            lastNode.moveToNode();
        }
        return allChildren;
    }

    private Optional<Node> getNode(boolean isLabel, String tempPath, List<Node> nodes) {
        Optional<Node> node;
        if (isLabel) {
            node = nodes.stream().filter(n -> n.getPathLabel().equals(tempPath))
                    .findFirst();
        } else {
            node = nodes.stream().filter(n -> n.getPath().equals(tempPath))
                    .findFirst();
        }
        return node;
    }

    private void scrollToFirstNode() {
        if (!isScrollPresent())
            return;
        CustomScrolls scrolls = getCustomScrolls();
        if (scrolls.getVerticalBarHeight() == 0)
            return;

        int translateY = scrolls.getTranslateYValue();
        if (translateY == 0)
            return;

        scrolls.scrollVertically(-translateY);
    }

    private boolean isScrollPresent() {
        return WebElementUtils.isElementPresent(treeComponentElement, By.cssSelector(CUSTOM_SCROLLBARS_CSS));
    }

    private CustomScrolls getCustomScrolls() {
        return CustomScrolls.create(driver, webDriverWait, treeComponentElement);
    }

    private String getNodeClassPath() {
        return "//div[@class='" + NODE_CLASS + "']";
    }

    public static class Node {
        private static final String FILTERS_BUTTON_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='filters-panel-button']";
        private static final String ADVANCED_SEARCH_PANEL_ID = "advanced-search_panel";
        private static final String DECORATOR_ICON_CSS = ".icon-wrapper";
        private static final String GREEN = "color: rgb(0, 166, 102);";
        private static final String PURPLE = "color: rgb(150, 54, 139);";
        private static final String RED = "color: rgb(199, 19, 69);";
        private static final String CANNOT_MAP_TO_COLOR_EXCEPTION = "Cannot map to color";
        private static final String TREE_NODE_BADGE_CSS = ".tree-node-badge";
        private static final String POPUP_CONTAINER_CSS = ".popupContainer";
        private static final String ARIA_LABEL_MINUS_CSS = "[aria-label='MINUS']";
        private static final String ARIA_LABEL_ADD_CSS = "[aria-label='ADD']";
        private static final String LABEL_NODE_CSS = ".OSSRichText,.more-button";
        private static final String OSS_ICON_CLASS = "OSSIcon";
        private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
        private static final String TREE_NODE_COMPONENT_CONTAINER_CSS = ".tree-node-component-container";
        private static final String NODE_DOESN_T_HAVE_MESSAGE_TEXT_EXCEPTION = "Node doesn't have Message Text";
        private static final String NODE_LABEL_CSS = ".tree-node-default-component-label";
        private static final String NODE_SELECTED_CLASS_CSS = ".tree-node-default-component--selected";
        private static final String TREE_COMPONENT_XPATH = ".//ancestor::div[@class='tree-component']";
        private static final String PATERN_DATA_TEST_ID_PATTERN = "%s_%s";

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement nodeElement;
        private final String nodeId;

        private Node(WebDriver driver, WebDriverWait webDriverWait, WebElement node, String nodeId) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.nodeElement = node;
            this.nodeId = nodeId;
        }

        private static Node create(WebDriver driver, WebDriverWait webDriverWait, WebElement nodeElement) {
            DelayUtils.waitForNestedElements(webDriverWait, nodeElement, By.cssSelector(LABEL_NODE_CSS));
            String nodeId = CSSUtils.getAttributeValue(DATA_PATH_ATTR, nodeElement);
            return new Node(driver, webDriverWait, nodeElement, nodeId);
        }

        public String getPath() {
            return nodeId;
        }

        String getPathLabel() {
            return CSSUtils.getAttributeValue(DATA_PATH_LABEL_ATTR, nodeElement);
        }

        public boolean isToggled() {
            return !nodeElement.findElements(By.cssSelector(NODE_SELECTED_CLASS_CSS)).isEmpty();
        }

        public void toggleNode() {
            moveToNode();
            if (isCheckboxPresent()) {
                WebElement input = nodeElement.findElement(By.xpath(NODE_CHECKBOX_LABEL_XPATH));
                input.click();
            } else {
                clickLabel();
            }
        }

        public void expandNode() {
            if (!isExpanded()) {
                moveToNode();
                WebElement button = nodeElement.findElement(By.xpath(EXPANDER_BUTTON_XPATH));
                WebElementUtils.clickWebElement(driver, button);
                DelayUtils.waitBy(webDriverWait, By.cssSelector("[" + DATA_PATH_ATTR + "='" + nodeId + "'] " + ARIA_LABEL_MINUS_CSS));
            }
        }

        public Optional<Popup> expandNextLevel() {
            if (isExpandNextLevelPresent()) {
                moveToNode();
                WebElement expandNextLevelArrow = nodeElement.findElement(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH));
                WebElementUtils.clickWebElement(driver, expandNextLevelArrow);
                WebElement button =
                        nodeElement.findElement(By.xpath("//a[contains(@" + CSSUtils.TEST_ID + ",'" + EXPAND_NEXT_LEVEL_BUTTON + "')]"));
                WebElementUtils.clickWebElement(driver, button);
                List<WebElement> popups = driver.findElements(By.cssSelector(POPUP_CONTAINER_CSS));
                if (!popups.isEmpty()) {
                    return Optional.of(Popup.create(driver, webDriverWait));
                }
                DelayUtils.waitForNestedElements(webDriverWait, nodeElement, By.cssSelector(ARIA_LABEL_MINUS_CSS));
                return Optional.empty();
            } else
                throw new NoSuchElementException("Expand Next Level is not available for Node " + getLabel());
        }

        public void collapseNode() {
            if (isExpanded()) {
                moveToNode();
                WebElement button = nodeElement.findElement(By.xpath(COLLAPSER_BUTTON_XPATH));
                button.click();
                DelayUtils.waitForNestedElements(webDriverWait, nodeElement, By.cssSelector(ARIA_LABEL_ADD_CSS));
            }
        }

        public boolean isExpanded() {
            return nodeElement.findElements(By.xpath(EXPANDER_ICON_XPATH)).isEmpty();
        }

        public String getLabel() {
            WebElement richText = nodeElement.findElement(By.className(NODE_LABEL_CLASS));
            return richText.getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }

        public void callAction(String groupId, String actionId) {
            WebElementUtils.moveToElement(driver, nodeElement);
            InlineMenu menu = InlineMenu.create(nodeElement, driver, webDriverWait);
            menu.callAction(groupId, actionId);
        }

        public void callAction(String actionId) {
            moveToNode();
            InlineMenu menu = InlineMenu.create(nodeElement, driver, webDriverWait);
            menu.callAction(actionId);
        }

        public AdvancedSearch openAdvancedSearch() {
            clickFilter();
            return AdvancedSearch.createById(driver, webDriverWait, ADVANCED_SEARCH_PANEL_ID);
        }

        public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
            AdvancedSearch advancedSearch = openAdvancedSearch();
            advancedSearch.setFilter(attributeId, componentType, value);
            advancedSearch.clickApply();
            DelayUtils.waitForNestedElements(webDriverWait, nodeElement, By.className(OSS_ICON_CLASS));
        }

        public void searchByAttribute(String attributeId, String value) {
            AdvancedSearch advancedSearch = openAdvancedSearch();
            advancedSearch.setFilter(attributeId, value);
            advancedSearch.clickApply();
            DelayUtils.waitForNestedElements(webDriverWait, nodeElement, By.className(OSS_ICON_CLASS));
        }

        public int countDecorators() {
            return nodeElement.findElements(By.cssSelector(DECORATOR_ICON_CSS)).size();
        }

        public DecoratorStatus getDecoratorStatus() {
            if (countDecorators() != 0) {
                String style = nodeElement.findElement(By.cssSelector(DECORATOR_ICON_CSS)).getAttribute("style");
                switch (style) {
                    case GREEN: {
                        return DecoratorStatus.GREEN;
                    }
                    case PURPLE: {
                        return DecoratorStatus.PURPLE;
                    }
                    case RED: {
                        return DecoratorStatus.RED;
                    }
                    default:
                }
                throw new IllegalArgumentException(CANNOT_MAP_TO_COLOR_EXCEPTION);
            }
            return DecoratorStatus.NONE;
        }

        public String getBadge() {
            return nodeElement.findElement(By.cssSelector(TREE_NODE_BADGE_CSS)).getText();
        }

        public boolean isBadgePresent() {
            return !nodeElement.findElements(By.cssSelector(TREE_NODE_BADGE_CSS)).isEmpty();
        }

        public boolean isExpandNextLevelPresent() {
            return !nodeElement.findElements(By.className(EXPAND_NEXT_LEVEL_ARROW_XPATH)).isEmpty();
        }

        public Optional<Message> getMessage() {
            if (driver.findElements(By.cssSelector("[" + CSSUtils.DATA_PARENT_TEST_ID + "='" + getParentDataPath() + "']")).isEmpty()) {
                return Optional.empty();
            }
            List<String> allClasses = CSSUtils.getAllClasses(nodeElement.findElement(By.cssSelector(TREE_NODE_COMPONENT_CONTAINER_CSS)));
            return Optional.of(Message.create(getMessageText(), allClasses));
        }

        public DragAndDrop.DraggableElement getDraggableElement() {
            WebElement source = nodeElement.findElement(By.cssSelector(".btn-drag"));
            return new DragAndDrop.DraggableElement(source);
        }

        private DragAndDrop.DropElement getDropElement() {
            return new DragAndDrop.DropElement(nodeElement.findElement(By.cssSelector(NODE_LABEL_CSS)));
        }

        private String getMessageText() {
            return ElementMessage.create(driver, getParentDataPath()).getMessagesText().stream()
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(NODE_DOESN_T_HAVE_MESSAGE_TEXT_EXCEPTION));
        }

        private String getParentDataPath() {
            String componentId = nodeElement.findElement(By.xpath(TREE_COMPONENT_XPATH)).getAttribute(CSSUtils.TEST_ID);
            return String.format(PATERN_DATA_TEST_ID_PATTERN, componentId, nodeId);
        }

        private void clickFilter() {
            if (isFilterButtonPresent()) {
                WebElement filterButton = nodeElement.findElement(By.xpath(FILTERS_BUTTON_XPATH));
                WebElementUtils.clickWebElement(driver, filterButton);
            } else
                throw new NoSuchElementException("Filter Node is not available for Node " + getLabel());
        }

        private boolean isFilterButtonPresent() {
            return !nodeElement.findElements(By.xpath(FILTERS_BUTTON_XPATH)).isEmpty();
        }

        private void moveToNode() {
            WebElementUtils.moveToElement(driver, nodeElement);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node node = (Node) o;
            return Objects.equals(nodeId, node.nodeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId);
        }

        private void clickLabel() {
            nodeElement.findElement(By.cssSelector(NODE_LABEL_CSS)).click();
        }

        private boolean isCheckboxPresent() {
            return !nodeElement.findElements(By.xpath(NODE_CHECKBOX_LABEL_XPATH)).isEmpty();
        }

        public enum DecoratorStatus {
            GREEN, PURPLE, RED, NONE
        }

    }
}
