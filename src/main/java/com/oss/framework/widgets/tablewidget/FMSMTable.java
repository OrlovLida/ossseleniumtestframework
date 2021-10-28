package com.oss.framework.widgets.tablewidget;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;

/**
 * @author Bartosz Nowak
 */
public class FMSMTable implements TableInterface {
    private static final String NOT_IMPLEMENTED = "Not implemented method in IaaTable";
    private static final String OSS_ICON_CLASS = "OSSIcon";
    private static final String OSS_ICON_VALUE = "title";
    private static final String TABLE_ROW_XPATH = "//div[@role='row']['table-row']";
    private static final String CELL_CONTENT = "cell__content";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tableWidget;

    private FMSMTable(WebDriver driver, WebDriverWait wait, WebElement tableWidget) {
        this.driver = driver;
        this.wait = wait;
        this.tableWidget = tableWidget;
    }

    public static FMSMTable createById(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        DelayUtils.waitBy(wait, By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + tableWidgetId + "']"));
        WebElement tableWidget = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + tableWidgetId + "']"));
        return new FMSMTable(driver, wait, tableWidget);
    }

    @Override
    public void selectRow(int row) {
        DelayUtils.waitForPresence(wait, By.className(CELL_CONTENT));
        List<WebElement> columns = tableWidget.findElements(By.xpath(TABLE_ROW_XPATH));
        System.out.println(columns);
        if (row >= columns.size()) {
            columns.get(columns.size() - 1).click();
        } else {
            columns.get(row).click();
        }
    }

    @Override
    public String getCellValueById(int row, String columnId) {
        Cell cell = Cell.create(tableWidget, row, columnId);
        return cell.getTextValue();
    }

    @Override
    public int getColumnSize(int column) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void resizeColumn(int column, int offset) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public List<String> getActiveColumnHeaders() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void disableColumn(String columnId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void disableColumnByLabel(String columnLabel, String... path) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void enableColumnByLabel(String columnLabel, String... path) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void changeColumnsOrder(String columnLabel, int position) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public boolean hasNoData() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void selectLinkInSpecificColumn(String columnName) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public int getRowNumber(String value, String attributeLabel) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public String getCellValue(int index, String attributeLabel) {
        return null;
    }

    @Override
    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, Input.ComponentType componentType, String value) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void callAction(String actionId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void callActionByLabel(String actionLabel) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void callAction(String groupId, String actionId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void selectTabByLabel(String tabLabel, String id) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public void doRefreshWhileNoData(int waitTime, String refreshId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);

    }

    @Override
    public Multimap<String, String> getAppliedFilters() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Map<String, String> getPropertyNamesToValues() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public List<TableRow> getSelectedRows() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    private static class Cell {
        private final WebElement cell;
        private final String columnNameId;

        private Cell(WebElement cell, String columnNameId) {
            this.cell = cell;
            this.columnNameId = columnNameId;
        }

        private static Cell create(WebElement tableWidget, int index, String columnNameId) {
            List<WebElement> cells = tableWidget.findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + columnNameId + "']"));
            return new Cell(cells.get(index), columnNameId);
        }

        private boolean isIcon(String att) {
            return !cell.findElements(By.className(OSS_ICON_CLASS)).isEmpty() && !cell.findElements(By.xpath(".//span[@" + att + "]")).isEmpty();
        }

        private String getAttributeValue(String att) {
            return cell.findElement(By.xpath(".//span[@" + att + "]")).getAttribute(att);
        }

        public String getTextValue() {
            if (isIcon(OSS_ICON_VALUE)) {
                return getAttributeValue(OSS_ICON_VALUE);
            } else {
                return cell.getText();
            }
        }

    }
}
