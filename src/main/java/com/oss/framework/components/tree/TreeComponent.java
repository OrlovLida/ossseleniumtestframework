package com.oss.framework.components.tree;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class TreeComponent {

    private static String TREE_CLASS = "tree-component";
    private static String NODE_CLASS = "tree-node";

    public static TreeComponent create(WebDriver driver, WebDriverWait webDriverWait, WebElement parent) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement treeComponent = parent.findElement(By.className(TREE_CLASS));
        return new TreeComponent(driver, webDriverWait, treeComponent);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement treeComponent;

    private TreeComponent(WebDriver driver, WebDriverWait webDriverWait,  WebElement treeComponent) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.treeComponent = treeComponent;
    }

    public List<Node> getNodes() {
        DelayUtils.waitForNestedElements(webDriverWait, treeComponent, "//div[@class='"+NODE_CLASS+"']");
        return this.treeComponent.findElements(By.className(NODE_CLASS)).stream()
                .map( node -> new Node(driver, webDriverWait, node)).collect(Collectors.toList());
    }

    public static class Node {

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement node;

        private Node(WebDriver driver, WebDriverWait webDriverWait,  WebElement node) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.node = node;
        }

        public boolean isToggled() {
            WebElement input = node.findElement(By.xpath(".//div[contains(@class,'tree-node-selection')]//input"));
            return input.isSelected();
        }

        public void toggleNode() {
            Actions action = new Actions(driver);
            action.moveToElement(node).perform();

            WebElement input = node.findElement(By.xpath(".//div[contains(@class,'tree-node-selection')]//label"));
            input.click();
        }

        public String getLabel() {
            WebElement richText = node.findElement(By.className(" OSSRichText"));
            return richText.getText();
        }
    }
}


