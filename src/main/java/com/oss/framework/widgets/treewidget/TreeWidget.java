package com.oss.framework.widgets.treewidget;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidget extends Widget {
    //TODO: fix order of variables and methods

    private final static String CHECKBOXES = ".//div[@class='tree-node-selection']";
    private final static String INLINE_ACTIONS_MENU_BTN = ".//div[@class='actionsGroup-inline']";

    private InlineMenu inlineMenu;

    public static TreeWidget createByClass(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        return new TreeWidget(driver,
                widgetClass, webDriverWait);
    }

    private TreeWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    public List<WebElement> getCheckboxes() {
        return this.webElement.findElements(By.xpath(CHECKBOXES));
    }

    //TODO: create class that represents inline menu
    private List<WebElement> getInlineActionsMenuButtons() {
        return this.webElement.findElements(By.xpath(INLINE_ACTIONS_MENU_BTN));
    }

    public List<Node> getVisibleNodes() {
        return this.webElement.findElements(By.className("tree-node")).stream()
                .map(Node::new).collect(Collectors.toList());
    }

    public void selectTreeRow(String name) {
        this.webElement.findElement(By.xpath("//p[@class='TreeViewLabel'][text()='" + name + "']")).click();
    }

    public void selectTreeRow(String name, int parentTreeItemId, String parentName) {
        WebElement webElement = this.webElement.findElement(By.xpath("//p[text()='"+parentName+"']//..//..//..//li[@id='"
                +parentTreeItemId+"']//p[text()='"+name+"']"));
        webElement.click();
    }

    public List<TreeRow> getVisibleTreeRow() {
        return this.webElement.findElements(By.className("TreeRow")).stream()
                .map(TreeRow::new).collect(Collectors.toList());
    }

    public List<Node> getRootNodeWithDescendants() {
        return this.webElement.findElements(By.xpath("(//div[contains(@style,'margin-left: 0px;')])[2]/../." +
                "./preceding-sibling::div")).stream()
                .map(Node::new).collect(Collectors.toList());
    }

    public List<Node> getSelectedNodes() {
        return getVisibleNodes().stream().filter(n -> n.isSelected()).collect(Collectors.toList());
    }

    public Node findByLabel(String label) {
        Optional<Node> node = getVisibleNodes().stream().filter(n -> n.getLabel().equals(label)).findFirst();
        if (node.isPresent()) {
            return node.get();
        }
        throw new NoSuchElementException("Can't find node: " + label);
    }

    public String getFirstNodeLabel() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        return getVisibleNodes().get(0).getLabel();
    }

    public List<Node> getNodesWithExpandState(String expandState) {
        return getVisibleNodes().stream().filter(n -> n.getExpandNodeClass().contains(expandState)).collect(Collectors.toList());
    }

    public List<Node> getDescendantNodesWithExpandState(String expandState) {
        return getVisibleNodes().stream().filter(n -> n.getDescendantNodeExpandClass().contains(expandState)).collect(Collectors.toList());
    }

    public TreeWidget selectNode() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        getVisibleNodes().get(0).click();
        return this;
    }

    public void waitForTreeExpansion() {
        DelayUtils.waitForElementDisapear(webDriverWait, this.webElement.findElement(By.xpath("//i[contains(@class, 'list-plus')]")));
    }

    public TreeWidget selectTreeRowByText(String text) {
        getVisibleTreeRow()
                .stream()
                .filter(treeRow -> treeRow.getLabel()
                        .equals(text)).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find TreeRow: " + text)).click();
        return this;
    }

    public void selectTreeRowByOrder(Integer order) {
        getVisibleTreeRow().get(order).click();
    }

    public Boolean isNodeSelected() {
        return getVisibleNodes().get(0).isSelected();
    }

    public TreeWidget expandNode() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        getNodesWithExpandState("collapsed").get(0).changeExpandState();
        return this;
    }

    public void expandLastTreeRow() {
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(".//i[@class='fa-li fa list-plus fa-border']"))));
        List<TreeRow> treeRows = getVisibleTreeRow();
        treeRows.get(treeRows.size() - 1).expandTreeRow();
        webDriverWait.until(ExpectedConditions.not(ExpectedConditions
                .attributeToBe(driver.findElement(By.xpath("//div[@class='TreeRow']/i")), "class", "fa-li fa fa-border fa-spinner fa-spin")));
    }

    public void expandTreeRow(int treeItemId, String parentTreeRowName){
        WebElement childTreeRowElement= this.webElement.findElement(By.xpath("(//p[text()='"+parentTreeRowName+"']//..//..//..//li[@id='"
                +treeItemId+"']//div[@class='TreeRow'])"));
        TreeRow treeRow2 = new TreeRow(childTreeRowElement);
        treeRow2.expandTreeRow();
    }

    public TreeWidget selectRootCheckbox() {
        DelayUtils.sleep();
        getCheckboxes().get(0).click();
        DelayUtils.sleep();
        return this;
    }

    public TreeWidget selectExpandAllIcon() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        getVisibleNodes().get(0).clickExpandAllBtn();
        return this;
    }

    public Boolean areNodesSelected() {
        return (getRootNodeWithDescendants().size()) == getSelectedNodes().size();
    }

    public InlineMenu selectInlineActionsBtn() {
        DelayUtils.waitForVisibility(webDriverWait, getSearchInput());
        Actions builder = new Actions(driver);
        builder.moveToElement(this.webElement.findElement(By.className("tree-node"))).perform();
        DelayUtils.waitForBy(webDriverWait, By.xpath(INLINE_ACTIONS_MENU_BTN));
        builder.moveToElement(getInlineActionsMenuButtons().get(0)).perform();
        builder.click().perform();
        return inlineMenu.create(driver);
    }

    public TreeWidget performSearch(String inputText) {
        getSearchInput().sendKeys(inputText);
        return this;
    }

    public TreeWidget performSearchWithEnter(String inputText) {
        WebElement input = getSearchInput();
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public Boolean isTreeWidgetEmpty() {
        return getVisibleNodes().size() == 0;
    }

    //TODO: ??
    public Boolean isSearchingCorrect(String searchInput) {
        int count = 0;
        for (Node e : getVisibleNodes()) {
            if (e.getLabel().contains(searchInput)) {
                count++;
            }
        }
        return getVisibleNodes().size() == count;
    }

    private static class Node {

        // (tip: nodes are rendering in the virtual scroll, so they gonna disappear from DOM when invisible)

        private final static String TREE_NODE_LABEL = ".//div[@class='tree-node-default-component-label']";
        private final static String TREE_NODE_SELECTION = ".//div[contains(@class, 'tree-node-default-component')]";
        private final static String TREE_NODE_EXPAND = ".//div[contains(@class, 'tree-node-expand-icon')]";
        private final static String EXPAND_ALL_ICON = ".//i[contains(@class, 'expandAllIcon')]";
        private final static String DESCENDANT_TREE_NODE = "//div[not(contains(@style,'margin-left: 0px'))" +
                "]/div/div[contains(@class, 'tree-node-expand-icon')]";

        private final WebElement webElement;

        private Node(WebElement webElement) {
            this.webElement = webElement;
        }

        private String getLabel() {
            return this.webElement.findElement(By.xpath(TREE_NODE_LABEL)).getText();
        }

        private void click() {
            this.webElement.click();
        }

        private String getExpandNodeClass() {
            return this.webElement.findElement(By.xpath(TREE_NODE_EXPAND)).getAttribute("class");
        }

        private String getDescendantNodeExpandClass() {
            return this.webElement.findElement(By.xpath(DESCENDANT_TREE_NODE)).getAttribute("class");
        }

        private void changeExpandState() {
            this.webElement.findElement(By.xpath(TREE_NODE_EXPAND)).click();
        }

        private void clickExpandAllBtn() {
            this.webElement.findElement(By.xpath(EXPAND_ALL_ICON)).click();
        }

        private boolean isSelected() {
            return this.webElement.findElement(By.xpath(TREE_NODE_SELECTION)).getAttribute("class").contains(
                    "selected");
        }
    }

    private static class TreeRow {
        private final static String TREE_ROW_LABEL = ".//p[contains(@class,'TreeViewLabel')]";
        private final static String EXPAND_TREE_ROW = ".//i[@class='fa-li fa list-plus fa-border']";

        private final WebElement webElement;

        private TreeRow(WebElement webElement) {
            this.webElement = webElement;
        }

        private String getLabel() {
            return this.webElement.findElement(By.xpath(TREE_ROW_LABEL)).getText();
        }

        private void click() {
            this.webElement.click();
        }

        public void expandTreeRow() {
            this.webElement.findElement(By.xpath(EXPAND_TREE_ROW)).click();
        }

    }
}
