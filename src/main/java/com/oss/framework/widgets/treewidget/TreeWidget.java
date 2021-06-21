package com.oss.framework.widgets.treewidget;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;

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

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidget extends Widget {
    // TODO: fix order of variables and methods
    
    private final static String CHECKBOXES = ".//div[@class='tree-node-selection']";
    private final static String INLINE_ACTIONS_MENU_BTN = ".//div[@class='actionsGroup-inline']";
    private final static String PATH_TO_TREE_ROW =
            "//p[contains(@class,'TreeViewLabel')][text()='%s']//..//..//div[contains(@class,'TreeRow')]";
    private final static String PATH_TO_TREE_ROW_CONTAINS =
            "//p[contains(@class,'TreeViewLabel')][contains(text(), '%s')]//..//..//div[@class='TreeRow']";
    
    private InlineMenu inlineMenu;
    
    public static TreeWidget createByClass(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        return new TreeWidget(driver,
                widgetClass, webDriverWait);
    }
    
    public static TreeWidget createByDataAttributeName(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        return new TreeWidget(driver, webDriverWait, dataAttributeName);
    }
    
    private TreeWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }
    
    private TreeWidget(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        super(driver, webDriverWait, dataAttributeName);
    }
    
    public List<WebElement> getCheckboxes() {
        return this.webElement.findElements(By.xpath(CHECKBOXES));
    }
    
    public List<Node> getVisibleNodes() {

        List<WebElement> nodes = webElement.findElements(By.xpath(".//div[@class='ReactVirtualized__Grid__innerScrollContainer']/div"));

       return nodes.stream().map(element -> {WebElement node = element.findElement(By.className("tree-node"));
            return new Node(driver, webDriverWait, node);}).collect(Collectors.toList());

    }
    
    @Deprecated
    public void setValueOnCheckboxByNodeLabel(String nodeLabel, boolean checkboxValue) {
        Actions action = new Actions(driver);
        action.moveToElement(getNodeByLabel(nodeLabel)).perform();
        if (getNodeByLabel(nodeLabel).findElements(By.xpath(".//input[@checked]")).size() > 0 == !checkboxValue)
            getNodeByLabel(nodeLabel).findElement(By.xpath(".//input/..")).click();
    }
    
    @Deprecated
    private WebElement getNodeByLabel(String nodeLabel) {
        DelayUtils.waitForVisibility(webDriverWait,
                webElement.findElement(By.xpath(".//div[text()='" + nodeLabel + "']/ancestor::div[@class='tree-node']")));
        return this.webElement.findElement(By.xpath(".//div[text()='" + nodeLabel + "']/ancestor::div[@class='tree-node']"));
    }
    
    @Deprecated
    private WebElement getCheckboxByNodeLabel(String nodeLabel) {
        getNodeByLabel(nodeLabel).findElement(By.xpath(".//input"));
        ComponentFactory.createFromParent("a", CHECKBOX, driver, webDriverWait, getNodeByLabel(nodeLabel));
        return getNodeByLabel(nodeLabel).findElement(By.xpath(".//input"));
    }
    
    public void selectFirstTreeRow() {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//div[@class='TreeRow'][1]"));
        List<TreeRow> treeRows = getVisibleTreeRow();
        treeRows.get(0).click();
    }
    
    public void selectTreeRow(String name) {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//p[@class='TreeViewLabel'][text()='" + name + "']"));
        this.webElement.findElement(By.xpath("//p[@class='TreeViewLabel'][text()='" + name + "']")).click();
    }
    
    public void selectTreeRowContains(String name) {
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath("//p[@class='TreeViewLabel'][contains(text(), '" + name + "')]"));
        this.webElement.findElement(By.xpath("//p[@class='TreeViewLabel'][contains(text(), '" + name + "')]")).click();
    }
    
    public void selectTreeRow(String name, int parentTreeItemId, String parentName) {
        WebElement webElement = this.webElement.findElement(By.xpath("//p[text()='" + parentName + "']//..//..//..//li[@id='"
                + parentTreeItemId + "']//p[text()='" + name + "']"));
        webElement.click();
    }
    
    public List<TreeRow> getVisibleTreeRow() {
        return this.webElement.findElements(By.className("TreeRow")).stream()
                .map(webElement -> new TreeRow(webElement, webDriverWait)).collect(Collectors.toList());
    }
    
    public List<Node> getRootNodeWithDescendants() {
        return this.webElement.findElements(By.xpath("(//div[contains(@style,'margin-left: 0px;')])[2]/../." +
                "./preceding-sibling::div")).stream()
                .map(node -> new Node(driver, webDriverWait, webElement)).collect(Collectors.toList());
    }
    
    public List<Node> getSelectedNodes() {
        return getVisibleNodes().stream().filter(n -> n.isSelected()).collect(Collectors.toList());
    }
    
    public Node getNode(String label) {
        Optional<Node> node = getVisibleNodes().stream().filter(n -> n.getLabel().equals(label)).findFirst();
        if (node.isPresent()) {
            return node.get();
        }
        throw new NoSuchElementException("Can't find node: " + label);
    }
    
    public String getFirstNodeLabel() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        return getVisibleNodes().get(0).getLabel();
    }
    
    public Node getNode(int index) {
        return getVisibleNodes().get(index);
    }
    
    public List<Node> getNodesWithExpandState(String expandState) {
        return getVisibleNodes().stream().filter(n -> n.getExpandNodeClass().contains(expandState)).collect(Collectors.toList());
    }
    
    public List<Node> getDescendantNodesWithExpandState(String expandState) {
        return getVisibleNodes().stream().filter(n -> n.getDescendantNodeExpandClass().contains(expandState)).collect(Collectors.toList());
    }
    
    public TreeWidget selectNode() {
        Node firstNode = getFirstNode();
        if(!firstNode.isSelected()){
            firstNode.click();
        }
        return this;
    }
    
    public Node getFirstNode() {
        return getVisibleNodes().get(0);
    }
    
    public void selectNodeByPosition(int position) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getVisibleNodes().get(position).click();
    }
    
    public void selectNodeByLabel(String label) {
        getNode(label).click();
    }
    
    public void waitForTreeExpansion() {
        DelayUtils.waitForElementDisappear(webDriverWait, this.webElement.findElement(By.xpath("//i[contains(@class, 'list-plus')]")));
    }
    
    public TreeWidget selectTreeRowByText(String text) {
        getVisibleTreeRow()
                .stream()
                .filter(treeRow -> treeRow.getLabel()
                        .equals(text))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Can't find TreeRow: " + text)).click();
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
    
    public void expandNode(String nodeLabel) {
        getNode(nodeLabel).changeExpandState();
    }
    
    public TreeWidget expandNodeWithLabel(String label) {
        getNode(label).changeExpandState();
        return this;
    }
    
    public void expandLastTreeRow() {
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(".//i[@class='fa-li fa list-plus fa-border']"))));
        List<TreeRow> treeRows = getVisibleTreeRow();
        treeRows.get(treeRows.size() - 1).expandTreeRow();
        webDriverWait.until(ExpectedConditions.not(ExpectedConditions
                .attributeToBe(driver.findElement(By.xpath("//div[@class='TreeRow']/i")), "class",
                        "fa-li fa fa-border fa-spinner fa-spin")));
    }
    
    public void expandTreeRow(int treeItemId, String parentTreeRowName) {
        WebElement childTreeRowElement =
                this.webElement.findElement(By.xpath("(//p[text()='" + parentTreeRowName + "']//..//..//..//li[@id='"
                        + treeItemId + "']//div[@class='TreeRow'])"));
        TreeRow treeRow = new TreeRow(childTreeRowElement, webDriverWait);
        treeRow.expandTreeRow();
    }
    
    public void expandTreeRow(String treeRowName) {
        DelayUtils.waitByXPath(webDriverWait, String.format(PATH_TO_TREE_ROW, treeRowName));
        DelayUtils.waitForVisibility(webDriverWait, this.webElement.findElement(By.xpath(String.format(PATH_TO_TREE_ROW, treeRowName))));
        WebElement treeRowElement = this.webElement.findElement(By.xpath(String.format(PATH_TO_TREE_ROW, treeRowName)));
        TreeRow treeRow = new TreeRow(treeRowElement, webDriverWait);
        treeRow.expandTreeRow();
    }
    
    public void expandTreeRowContains(String treeRowName) {
        DelayUtils.waitForVisibility(webDriverWait,
                this.webElement.findElement(By.xpath(String.format(PATH_TO_TREE_ROW_CONTAINS, treeRowName))));
        WebElement treeRowElement = this.webElement.findElement(By.xpath(String.format(PATH_TO_TREE_ROW_CONTAINS, treeRowName)));
        TreeRow treeRow = new TreeRow(treeRowElement, webDriverWait);
        treeRow.expandTreeRow();
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

    public TreeWidget performSearch(String inputText) {
        fullSearchText(inputText);
        return this;
    }
    
    public TreeWidget performSearchWithEnter(String inputText) {
        WebElement input = getSearchInput();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
        return this;
    }
    
    public Boolean isTreeWidgetEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getVisibleNodes().size() == 0;
    }
    
    // TODO: ??
    public Boolean isSearchingCorrect(String searchInput) {
        int count = 0;
        for (Node e: getVisibleNodes()) {
            if (e.getLabel().contains(searchInput)) {
                count++;
            }
        }
        return getVisibleNodes().size() == count;
    }
    
    public void callActionById(String groupLabel, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callAction(groupLabel, id);
    }
    
    public static class Node {
        
        // (tip: nodes are rendering in the virtual scroll, so they gonna disappear from DOM when invisible)
        
        private final static String TREE_NODE_LABEL =
                ".//div[@class='tree-node-default-component-label']//div[contains(@class, 'OSSRichText')]";
        private final static String TREE_NODE_SELECTION = ".//div[contains(@class, 'tree-node-default-component')]";
        private final static String TREE_NODE_EXPAND = ".//div[contains(@class, 'tree-node-expand-icon')]";
        private final static String EXPAND_ALL_ICON = ".//i[contains(@class, 'expandAllIcon')]";
        private final static String DESCENDANT_TREE_NODE = "//div[not(contains(@style,'margin-left: 0px'))" +
                "]/div/div[contains(@class, 'tree-node-expand-icon')]";
        private final static String ICON_PLUS = ".//i[@aria-label='ADD']";
        private final static String ICON_DISABLED =".//div[@class='tree-node-expand tree-node-expand--disabled']";
        private final static String ICON_EXPAND = "tree-node-expand";

        private final WebElement webElement;
        private final WebDriver driver;
        private final WebDriverWait wait;
        
        private Node(WebDriver driver, WebDriverWait wait, WebElement webElement) {
            this.webElement = webElement;
            this.driver = driver;
            this.wait = wait;
        }
        
        private String getLabel() {
            return webElement.findElement(By.xpath(TREE_NODE_LABEL)).getText();
        }
        
        public void click() {
            WebElement checkbox = webElement.findElement(By.className("tree-node-selection"));
            Actions actions = new Actions(driver);
            actions.moveToElement(checkbox).click(checkbox).build().perform();
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
        
        public boolean isExpanded() {
            if (hasExpandButton()) {
                return webElement.findElements(By.xpath(ICON_PLUS)).isEmpty();
            }
            throw new NoSuchElementException("Node doesn't have node expand");
        }
        
        private boolean hasExpandButton() {
            return webElement.findElements(By.xpath(ICON_DISABLED)).stream().allMatch(node -> node.getText().isEmpty());
        }
        
        public void expandNode() {
            
            if (!isExpanded()) {
                webElement.findElement(By.className(ICON_EXPAND)).click();
            }
        }
        
        public void callAction(String actionId) {
            InlineMenu.create(webElement, driver, wait).callAction(actionId);
        }
        
        public void callAction(String groupId, String actionId) {
            InlineMenu.create(webElement, driver, wait).callAction(groupId, actionId);
        }
        
        public boolean isInlineActionPresent() {
            return InlineMenu.create(webElement, driver, wait).isActionListDisplayed();
        }
    }
    
    private static class TreeRow {
        private final static String TREE_ROW_LABEL = ".//p[contains(@class,'TreeViewLabel')]";
        private final static String EXPAND_TREE_ROW = ".//i[@class='fa-li fa list-plus fa-border']";
        
        private final WebElement webElement;
        private final WebDriverWait webDriverWait;
        
        private TreeRow(WebElement webElement, WebDriverWait webDriverWait) {
            this.webElement = webElement;
            this.webDriverWait = webDriverWait;
        }
        
        private String getLabel() {
            return this.webElement.findElement(By.xpath(TREE_ROW_LABEL)).getText();
        }
        
        private void click() {
            this.webElement.click();
        }
        
        public void expandTreeRow() {
            DelayUtils.waitForClickability(webDriverWait, this.webElement.findElement(By.xpath(EXPAND_TREE_ROW)));
            this.webElement.findElement(By.xpath(EXPAND_TREE_ROW)).click();
        }
        
        private boolean isSelected() {
            return this.webElement.getAttribute("class").contains(
                    "selected");
        }
        
    }
}
