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

public class IaaTable implements TableInterface {
    private static final String NOT_IMPLEMENTED = "Not implemented method in IaaTable";
    private static final String HEADER = ".//div[contains(@class, 'table__header')]";


    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    private IaaTable(WebDriver driver, WebDriverWait wait, String widgetId) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
    }

    public static IaaTable createById(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        DelayUtils.waitBy(wait, By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + tableWidgetId + "']"));
        return new IaaTable(driver, wait, tableWidgetId);
    }

    @Override
    public void selectRow(int row) {
        List<WebElement> columns = driver.findElements(By.className("table-row"));
        if (row >= columns.size()) {
            columns.get(columns.size() - 1).click();
        } else {
            columns.get(row).click();
        }
    }

    public List<WebElement> getListOfCells(String columnNameId) {
        List<WebElement> cells = driver.findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='"+ columnNameId +"']//span[@title]"));
        return cells;
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
}
