package com.oss.framework.widgets.tablewidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.oss.framework.widgets.tabswidget.TabsWidget;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;

public class OldTable implements TableInterface {

    private static final String kebabMenuBtn = ".//div[@id='frameworkCustomButtonsGroup']";
    private static final int REFRESH_INTERVAL = 2000;
    private static final String REFRESH_BUTTON_LABEL = "";

    private static final String PROPERTY_NAME_PATTERN = "//div[contains(@class, 'OSSTableColumn Col_PropertyName')]/div[contains(@class,'Cell Row_%s')]";
    private static final String PROPERTY_VALUE_PATTERN = "//div[contains(@class, 'OSSTableColumn Col_PropertyValue')]/div[contains(@class,'Cell Row_%s')]";
    private static final String EXPAND_PROPERTIES_BUTTON_PATH = "//div[contains(@class, 'tabWindow')]//a[contains(@class, 'fullScreenButton')]";

    //to be removed after adding data-attributeName OSSWEB-8398
    @Deprecated
    public static OldTable createByOssWindow(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[@class='OssWindow']");
        WebElement table = driver.findElement(By.xpath("//div[@class='OSSTableContainer']"));
        WebElement window = driver.findElement(By.xpath("//div[@class='OssWindow']"));
        return new OldTable(driver, wait, null, table, window);
    }

    public static OldTable createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitByXPath(wait, "//div[contains(@id,'" + componentId + "')]");
        WebElement table = driver.findElement(By.xpath("//div[@id='" + componentId + "']"));
        return new OldTable(driver, wait, componentId, table);
    }

    public static OldTable createByComponentDataAttributeName(WebDriver driver, WebDriverWait wait, String dataAttributeName) {
        DelayUtils.waitByXPath(wait, "//div[@data-attributename='" + dataAttributeName + "']");
        WebElement table = driver.findElement(By.xpath("//div[@data-attributename='" + dataAttributeName + "']"));
        WebElement window = table.findElement(By.xpath("//div[@data-attributename='" + dataAttributeName + "']/ancestor::div[contains(@class,'OssWindow')]"));
        return new OldTable(driver, wait, dataAttributeName, table, window);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    @Deprecated //TODO:
    private final WebElement table;
    @Deprecated
    private WebElement window;

    private OldTable(WebDriver driver, WebDriverWait wait, String widgetId, WebElement table, WebElement window) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
        this.table = table;
        this.window = window;
    }

    private OldTable(WebDriver driver, WebDriverWait wait, String widgetId, WebElement table) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
        this.table = table;
    }

    @Override
    public void selectRow(int row) {
        Map<String, Column> columns = createColumnsFilters();
        Lists.newArrayList(columns.values()).get(0).selectCell(0);
    }

    @Override
    public int getColumnSize(int column) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void resizeColumn(int column, int offset) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public List<String> getActiveColumnHeaders() {
        return null;
    }

    @Override
    public void disableColumn(String columnId) {

    }

    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {

    }

    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {

    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {

    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        column.selectCell(value);
    }

    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        if (componentType != ComponentType.TEXT_FIELD) {
            throw new RuntimeException("Old table widget supports" + ComponentType.TEXT_FIELD + "only");
        }
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        column.clear();
        column.setValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Override
    public void callAction(String actionId) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, window);
        actions.callActionByLabel(actionLabel);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        getActionsInterface().callAction(groupId, actionId);
    }

    @Override
    public void selectTabByLabel(String tabLabel, String id) {
        TabsWidget tabs = TabsWidget.createById(driver, wait, id);
        tabs.selectTabByLabel(tabLabel);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    public String getValueCell(int index, String attributeLabel) {
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        return column.getValueCell(index);
    }

    @Override
    public void clickOnKebabMenu() {
        WebElement foundedElement = this.table.findElement(By.xpath(kebabMenuBtn));
        foundedElement.click();
    }

    @Override
    public void clickOnAction(String actionName) {
        WebElement foundedElement = this.table.findElement(By.xpath("//a[text()='" + actionName + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(foundedElement));
        foundedElement.click();
    }

    @Override
    public void refreshUntilNoData(int waitTime, String refreshLabel) {
        if (this.widgetId == null) {
            throw new RuntimeException("widgetId property is missing");
        }
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + waitTime;
        while (isNoData() && stopTime > System.currentTimeMillis()) {
            DelayUtils.sleep(REFRESH_INTERVAL);
            callActionByLabel(refreshLabel);
        }
    }

    @Override
    public Multimap<String, String> getAppliedFilters() {
        throw new RuntimeException("Not implemented yet");
    }

    public Map<String, String> getPropertyNamesToValues() {
        int index = 0;
        clickExpandPropertiesButton();
        String propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
        String propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
        Optional<WebElement> propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
        Optional<WebElement> propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        Map<String, String> namesToValues = new HashMap<>();
        while (propertyName.isPresent() && propertyValue.isPresent()) {
            index++;
            String propertyNameText = propertyName.get().getText();
            String propertyValueText = propertyValue.get().getText();
            namesToValues.put(propertyNameText, propertyValueText);
            propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
            propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
            propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
            propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        }
        clickExpandPropertiesButton();
        return namesToValues;
    }

    @Override
    public List<TableRow> getSelectedRows() {
        throw new RuntimeException("not implemented yet");
    }

    private void clickExpandPropertiesButton() {
        WebElement expandButton = driver.findElement(By.xpath(EXPAND_PROPERTIES_BUTTON_PATH));
        expandButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
    @Override
    public boolean isNoData() {
        List<WebElement> noData = this.driver.findElements(By.xpath("//div[@data-attributename='" + this.widgetId + "']//h3[contains(@class,'noDataWithColumns')]"));
        return !noData.isEmpty();
    }

    @Override
    public void selectLinkInSpecificColumn(String columnName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(columnName);
        column.selectLink();
    }

    public int getRowNumber(String value, String attributeLabel) {
        DelayUtils.waitForNestedElements(wait, this.table, "//*[contains(text(),'" + value + "')]");
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        return column.indexOf(value);
    }

    private Map<String, Column> createColumnsFilters() {
        Map<String, Column> columns = Maps.newHashMap();
        DelayUtils.waitForNestedElements(wait, this.table, ".//div[contains(@class, 'OSSTableComponent')]");
        List<Column> columns2 =
                this.table.findElements(By.xpath(".//div[contains(@class,'OSSTableColumn')]"))
                        .stream().map(columnElement -> new Column(columnElement, wait, driver)).collect(Collectors.toList());

        for (Column column :  Lists.reverse(columns2)) {
            if (column.checkIfLabelExist()) {
                columns.put(column.getLabel(), column);
            } else {
                columns.put("", column);
            }
        }
        return columns;
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForNestedElements(wait, window, "//div[contains(@class, 'windowToolbar')] | //*[@class='actionsContainer']");
        boolean isNewActionContainer = isElementPresent(driver, By.className("actionsContainer"));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(window, driver, wait);
        } else {
            return OldActionsContainer.createFromParent(driver, wait, window);
        }
    }

    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static class Column {
        private final WebElement column;
        private final WebDriverWait wait;
        private final WebDriver driver;

        private Column(WebElement column, WebDriverWait wait, WebDriver driver) {
            this.column = column;
            this.wait = wait;
            this.driver = driver;
        }

        private String getLabel() {
            return moveToHeader().getText();
        }

        private WebElement moveToHeader() {
            WebElement header = this.column.findElement(By.xpath(".//div[contains(@class, 'Header')]"));
            Actions action = new Actions(driver);
            action.moveToElement(header).perform();
            return header;
        }

        private boolean checkIfLabelExist() {
            return this.column.findElements(By.xpath(".//span")).size() > 0;
        }

        private void selectCell(String value) {
            moveToHeader();
            DelayUtils.waitByXPath(this.wait, "//div[contains(@class, 'Cell')]//div[contains(@class, 'OSSRichText')]");
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell Row')]"));
            for (WebElement cell : cells) {
                DelayUtils.waitForNestedElements(this.wait, cell, ".//div[contains(@class, 'OSSRichText')]");
                WebElement richText = cell.findElement(By.xpath(".//div[contains(@class, 'OSSRichText')]"));
                if (richText.getText().equals(value)) {
                    Actions action = new Actions(driver);
                    action.click(cell).perform();
                    break;
                }
            }
        }

        public int indexOf(String value) {
            moveToHeader();
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));

            for (WebElement cell : cells) {

                DelayUtils.waitForNestedElements(this.wait, cell, ".//div[contains(@class, 'OSSRichText')]");
                WebElement richText = cell.findElement(By.xpath(".//div[contains(@class, 'OSSRichText')]"));
                if (richText.getText().equals(value)) {
                    return cells.indexOf(cell);
                }
            }
            throw new RuntimeException("Cannot find a row with the provided value");
        }

        public void selectCell(int index) {
            WebElement cell = getCellByIndex(index);
            Actions action = new Actions(driver);
            action.moveToElement(cell).click(cell).perform();
        }

        private String getValueCell(int index) {
            WebElement cell = getCellByIndex(index);
            Actions action = new Actions(driver);
            action.moveToElement(cell).build().perform();
            return cell.getText();
        }

        private WebElement getCellByIndex(int index) {
            moveToHeader();
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            return cells.get(index);
        }

        private void setValue(String value) {
            WebElement input = column.findElement(By.xpath(".//input"));
            Actions action = new Actions(driver);
            action.moveToElement(input).build().perform();
            input.sendKeys(value);
        }

        private void clear() {
            WebElement input = column.findElement(By.xpath(".//input"));
            Actions action = new Actions(driver);
            action.moveToElement(input).click(input).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build().perform();
            DelayUtils.sleep();
        }

        private void selectLink() {
            DelayUtils.waitByXPath(this.wait, "//div[contains(@class, 'Cell')]//div[contains(@class, 'OSSRichText')]");
            DelayUtils.waitForNestedElements(this.wait, column, ".//a[contains(@href, '#/')]");
            Actions action = new Actions(driver);
            action.click(column.findElement(By.xpath(".//div[contains(@class, 'Cell')]//a[contains(@href, '#/')]"))).perform();
        }
    }

}
