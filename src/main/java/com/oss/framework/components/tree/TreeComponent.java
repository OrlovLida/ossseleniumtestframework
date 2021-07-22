package com.oss.framework.components.tree;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class TreeComponent {

    private static final String TREE_CLASS = "tree-component";
    private static final String NODE_CLASS = "tree-node";
    private static final String NODE_LABEL_CLASS = "OSSRichText";
    private static final String EXPANDER_ICON_XPATH = ".//i[@class='OSSIcon fa fa-plus']";
    private static final String EXPANDER_BUTTON_XPATH = ".//div[@class = 'tree-node-expand-icon tree-node-expand-icon--collapsed']";
    private static final String COLLAPSER_BUTTON_XPATH = ".//div[@class = 'tree-node-expand-icon tree-node-expand-icon--expanded']";
    private static final String NODE_CHECKBOX_XPATH = ".//div[contains(@class,'tree-node-selection')]//input";
    private static final String NODE_CHECKBOX_LABEL_XPATH = ".//div[contains(@class,'tree-node-selection')]//label";

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
        if(paginationComponent == null) {
            PaginationComponent.createFromParent(driver, webDriverWait, treeComponent);
        }
        return paginationComponent;
    }

    private Node getNodeByPath(String ... path) {
        StringBuilder currentPath = new StringBuilder();
        Node node = null;
        for(String element : path) {
            currentPath.append(element);
            String tempPath = currentPath.toString();
            List<Node> nodes = getVisibleNodes();
            node = nodes.stream().filter(n -> n.getPath().equals(tempPath))
                    .findFirst().orElseThrow(() ->  new RuntimeException("Cant find node: " + tempPath));
            //if last element we can skip
            node.expandNode();
            currentPath.append(".");
        }
        return node;
    }

    private List<Node> getVisibleNodes() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return this.treeComponent.findElements(By.xpath(".//div[@class='" + NODE_CLASS + "']")).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    @Deprecated
    public List<Node> getNodes(int level) {
        DelayUtils.waitForNestedElements(webDriverWait, treeComponent, "//div[@class='" + NODE_CLASS + "']");
        String marginToString = level * LEFT_MARGIN_IN_PX + "px";
        return this.treeComponent.findElements(By.xpath("//div[@class='" + NODE_CLASS + "'][.//div[contains(@style, '" + marginToString + "')]]")).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    @Deprecated
    public List<Node> getNodesWithExpander(int level) {
        DelayUtils.waitForNestedElements(webDriverWait, treeComponent, "//div[@class='" + NODE_CLASS + "']");
        String marginToString = level * LEFT_MARGIN_IN_PX + "px";
        return this.treeComponent.findElements(By.xpath("//div[@class='" + NODE_CLASS + "'][.//i][.//div[contains(@style, '" + marginToString + "')]]")).stream()
                .map(node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    public static class Node {

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement node;

        private Node(WebDriver driver, WebDriverWait webDriverWait, WebElement node) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.node = node;
        }

        public String getPath() {
            return CSSUtils.getAttributeValue("data-guid", node);
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

        public String getLabel() {
            WebElement richText = node.findElement(By.className(NODE_LABEL_CLASS));
            return richText.getText();
        }
    }
}


