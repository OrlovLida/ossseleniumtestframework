package com.oss.framework.widgets.treewidget;

import java.util.List;
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
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidget extends Widget {
    // TODO: fix order of variables and methods

    private static final String CHECKBOXES = ".//div[@class='tree-node-selection']";
    private static final String CLASS = "class";
    private static final String INLINE_ACTIONS_MENU_BTN = ".//div[@class='actionsGroup-inline']";
    private static final String PATH_TO_TREE_ROW =
            "//p[contains(@class,'TreeViewLabel')][text()='%s']//..//..//div[contains(@class,'TreeRow')]";
    private static final String PATH_TO_TREE_ROW_CONTAINS =
            "//p[contains(@class,'TreeViewLabel')][contains(text(), '%s')]//..//..//div[@class='TreeRow']";

    // move to get method
    private InlineMenu inlineMenu;


    @Deprecated
    public static TreeWidget createByClass(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        return new TreeWidget(driver,
                widgetClass, webDriverWait);
    }

    public static TreeWidget createById(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        Widget.waitForWidgetById(webDriverWait,widgetId);
        return new TreeWidget(driver, webDriverWait, widgetId);
    }

    private TreeWidget(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    private TreeWidget(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        super(driver, webDriverWait, dataAttributeName);
    }

    @Deprecated
    public void setValueOnCheckboxByNodeLabel(String nodeLabel, boolean checkboxValue) {
        Actions action = new Actions(driver);
        action.moveToElement(getNodeByLabel(nodeLabel)).perform();
        if (!getNodeByLabel(nodeLabel).findElements(By.xpath(".//input[@checked]")).isEmpty() == !checkboxValue)
            getNodeByLabel(nodeLabel).findElement(By.xpath(".//input/..")).click();
    }

    @Deprecated
    private WebElement getNodeByLabel(String nodeLabel) {
        DelayUtils.waitForVisibility(webDriverWait,
                webElement.findElement(By.xpath(".//div[text()='" + nodeLabel + "']/ancestor::div[@class='tree-node']")));
        return this.webElement.findElement(By.xpath(".//div[text()='" + nodeLabel + "']/ancestor::div[@class='tree-node']"));
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

    public void expandLastTreeRow() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(TreeRow.EXPAND_TREE_ROW))));
        List<TreeRow> treeRows = getVisibleTreeRow();
        treeRows.get(treeRows.size() - 1).expandTreeRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    public TreeWidget performSearchWithEnter(String inputText) {
        WebElement input = getSearchInput();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public void selectNodeByPosition(int position) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        getVisibleNodes().get(position).toggleNode();
    }

    public void callActionById(String groupLabel, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callAction(groupLabel, id);
    }

    private static class TreeRow {
        private static final String TREE_ROW_LABEL = ".//p[contains(@class,'TreeViewLabel')]";
        private static final String EXPAND_TREE_ROW = ".//div[@class='tree-view-icon tree-view-close']";

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
            return this.webElement.getAttribute(CLASS).contains(
                    "selected");
        }

    }
}
