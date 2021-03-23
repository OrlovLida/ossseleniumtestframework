package com.oss.framework.widgets.tablewidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabswidget.TabsWidget;

public class OldTable implements TableInterface {

    private static final String kebabMenuBtn = ".//div[@id='frameworkCustomButtonsGroup']";
    private static final String rowsCounterSpansPath = ".//div[@class='rowsCounter']//span";
    private static final int REFRESH_INTERVAL = 2000;
    private static final String REFRESH_BUTTON_LABEL = "";

    private static final String PROPERTY_NAME_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyName')]/div[contains(@class,'Cell Row_%s')]";
    private static final String PROPERTY_VALUE_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyValue')]/div[contains(@class,'Cell Row_%s')]";
    private static final String EXPAND_PROPERTIES_BUTTON_PATH =
            "//div[contains(@class, 'tabWindow')]//a[contains(@class, 'fullScreenButton')]";

    private static final String FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN =
            "(//div[contains(@class, 'Col_ColumnId_Name')]//div[contains(text(), '%s')])[%d]";

    // to be removed after adding data-attributeName OSSWEB-8398
    @Deprecated
    public static OldTable createByOssWindow(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[@class='OssWindow']");
        WebElement table = driver.findElement(By.xpath("//div[@class='OSSTableContainer']"));
        WebElement window = driver.findElement(By.xpath("//div[@class='OssWindow']"));
        return new OldTable(driver, wait, null, table, window);
    }

    public static OldTable createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitByXPath(wait, "//div[contains(@id,'" + componentId + "')] | //div[@" + CSSUtils.TEST_ID + "='" + componentId + "']");
        WebElement table =
                driver.findElement(By.xpath("//div[@id='" + componentId + "'] | //div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(table).build().perform();
        return new OldTable(driver, wait, componentId, table);
    }

    public static OldTable createByComponentDataAttributeName(WebDriver driver, WebDriverWait wait, String dataAttributeName) {
        DelayUtils.waitByXPath(wait, "//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']");
        WebElement table = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(table).build().perform();
        WebElement window = table.findElement(
                By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + dataAttributeName + "']/ancestor::div[contains(@class,'OssWindow')]"));
        return new OldTable(driver, wait, dataAttributeName, table, window);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    @Deprecated // TODO:
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
        Lists.newArrayList(columns.values()).get(0).selectCell(row);
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
        getColumn(attributeLabel).selectCell(value);
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
        Column column = getColumn(attributeLabel);
        column.clear();
        column.setValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Override
    public void callAction(String actionId) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, window);
        actions.callActionById(actionId);
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

    public String getCellValue(int index, String attributeLabel) {
        return getColumn(attributeLabel).getValueCell(index);
    }

    private Column getColumn(String columnLabel) {
        Map<String, Column> columns = createColumnsFilters();
        if (columns.containsKey(columnLabel)) {
            return columns.get(columnLabel);
        } else {
            System.out.println("Available columns:");
            columns.forEach((key, value) -> System.out.println(key));
            throw new RuntimeException("Cannot find a column with label = " + columnLabel);
        }
    }

    /**
     * @param anyLabelInTable any column label existing in table
     * @return number of rows in table
     */
    public int getNumberOfRowsInTable(String anyLabelInTable) {
        return getColumn(anyLabelInTable).getNumberOfRows();
    }

    @Override
    public void clickOnKebabMenu() {
        WebElement foundedElement = table.findElement(By.xpath(kebabMenuBtn));
        foundedElement.click();
    }

    @Override
    public void doRefreshWhileNoData(int waitTime, String refreshId) {
        if (widgetId == null) {
            throw new RuntimeException("widgetId property is missing");
        }
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + waitTime;
        while (hasNoData() && (stopTime > System.currentTimeMillis())) {
            DelayUtils.sleep(REFRESH_INTERVAL);
            callAction(refreshId);
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
    public boolean hasNoData() {
        List<WebElement> noData = driver
                .findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']//h3[contains(@class,'noDataWithColumns')]"));
        return !noData.isEmpty();
    }

    @Override
    public void selectLinkInSpecificColumn(String columnName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getColumn(columnName).selectLink();
    }

    public void selectRowByPartialNameAndIndex(String partialName, int index) {
        String xpath = String.format(FIND_BY_PARTIAL_NAME_AND_INDEX_PATTERN, partialName, index);
        driver.findElement(By.xpath(xpath)).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getTableObjectsCount() {
        List<WebElement> rowsCounterSpans = table
                .findElements(By.xpath(rowsCounterSpansPath));
        try {
            return Integer.parseInt(rowsCounterSpans.get(rowsCounterSpans.size() - 1).getText());
        } catch (NumberFormatException e) {
            System.out.println("Problem with getting table object count. Value is not a number.");
            return 0;
        }
    }

    public void changeItemsPerPageValue(int pageOption) {
        WebElement pagination = table
                .findElement(By.className("OSSPagination"));
        pagination.findElement(By.xpath(".//button")).click();
        pagination.findElement(By.xpath(".//li[text()='" + pageOption + "']")).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getRowNumber(String value, String attributeLabel) {
        DelayUtils.waitForNestedElements(wait, table, "//*[contains(text(),'" + value + "')]");
        return getColumn(attributeLabel).indexOf(value);
    }

    public void selectPredefinedFilter(String filterName) {
        PredefinedFilter predefinedFilter = PredefinedFilter.createPredefinedFilter(driver, wait, filterName);
        predefinedFilter.selectPredefinedFilter();
    }

    private Map<String, Column> createColumnsFilters() {
        Map<String, Column> columns = Maps.newHashMap();
        DelayUtils.waitForNestedElements(wait, table, ".//div[contains(@class, 'OSSTableComponent')]");
        List<Column> columns2 =
                table.findElements(By.xpath(".//div[contains(@class,'OSSTableColumn')]"))
                        .stream().map(columnElement -> new Column(columnElement, wait, driver)).collect(Collectors.toList());

        for (Column column : Lists.reverse(columns2)) {
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
            WebElement header = column.findElement(By.xpath(".//div[contains(@class, 'Header')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", header);
            return header;
        }

        private boolean checkIfLabelExist() {
            WebElement header = moveToHeader();
            System.out.println("Header isDisplayed is " + header.isDisplayed());
            System.out.println("Header getText return : '" + header.getText() + "'");
            return !header.getText().isEmpty();
//            return !moveToHeader().getText().isEmpty();
        }

        private void selectCell(String value) {
            moveToHeader();
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell Row')]"));
            for (WebElement cell : cells) {
                DelayUtils.waitForNestedElements(wait, cell, ".//div[contains(@class, 'OSSRichText')]");
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

                DelayUtils.waitForNestedElements(wait, cell, ".//div[contains(@class, 'OSSRichText')]");
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cell);
            Actions action = new Actions(driver);
            action.moveToElement(cell).build().perform();
            return cell.getText();
        }

        private WebElement getCellByIndex(int index) {
            moveToHeader();
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            return cells.get(index);
        }

        private int getNumberOfRows() {
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            return cells.size();
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
            action.moveToElement(input).click(input).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build()
                    .perform();
            DelayUtils.sleep();
        }

        private void selectLink() {
            DelayUtils.waitByXPath(wait, "//div[contains(@class, 'Cell')]//div[contains(@class, 'OSSRichText')]");
            DelayUtils.waitForNestedElements(wait, column, ".//a[contains(@href, '#/')]");
            Actions action = new Actions(driver);
            action.click(column.findElement(By.xpath(".//div[contains(@class, 'Cell')]//a[contains(@href, '#/')]"))).perform();
        }
    }

    private static class PredefinedFilter {
        private final WebDriverWait wait;
        private final WebDriver driver;
        private final WebElement predefinedFilter;

        private PredefinedFilter(WebDriver driver, WebDriverWait wait, WebElement predefinedFilter) {
            this.driver = driver;
            this.wait = wait;
            this.predefinedFilter = predefinedFilter;
        }

        private static PredefinedFilter createPredefinedFilter(WebDriver driver, WebDriverWait wait, String filterName) {
            DelayUtils.waitForPageToLoad(driver, wait);
            DelayUtils.waitByXPath(wait, ".//span[contains(@class,'ToggleButton')]");
            List<WebElement> filters = driver.findElements(By.xpath(".//span[contains(@class,'ToggleButton')]"));
            WebElement predefinedFilter = filters.stream().filter(filter -> filter.getText().equals(filterName)).findFirst()
                    .orElseThrow(() -> new RuntimeException("There is no Predefined Filter"));
            return new PredefinedFilter(driver, wait, predefinedFilter);
        }

        private void selectPredefinedFilter() {
            if (!isFilterSelected()) {
                Actions action = new Actions(driver);
                action.moveToElement(predefinedFilter).click(predefinedFilter).perform();
            }
        }

        private void unselectPredefinedFilter() {
            if (isFilterSelected()) {
                Actions action = new Actions(driver);
                action.moveToElement(predefinedFilter).click(predefinedFilter).perform();
            }
        }

        private boolean isFilterSelected() {
            return predefinedFilter.getAttribute("class").contains("active");
        }

    }

}
