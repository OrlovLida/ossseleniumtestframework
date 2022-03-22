package com.oss.framework.iaa.widgets.components;

import com.google.common.collect.Lists;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Table2DComponent {
    private static final String TABLE_2D_WIDGET_XPATH = "//div[@class='Table2DWidget']";
    private static final String TABLE_2D_COLUMN_XPATH = ".//div[@class='Table2DColumn center']";
    private static final String WIDGET_TITLE = ".//p[@class='title']";
    private static final String TABLE_COMPONENT_CLASS = "Table2DComponent";
    private static final String CELL_XPATH = ".//div[contains(@class,'Cell')]";
    private static final String ROW_HEADER_XPATH = ".//div[@class='Table2DColumn left']//div[contains(@class, 'Header')]";
    private static final String COLUMN_HEADER_XPATH = ".//div[@class='Table2DColumn center']//div[contains(@class, 'Header')]";
    private static final String CLEAR_FILTER_XPATH = ".//div[@class='Header filter-clear']";
    private static final String SELECTED_CELL_CLASS = "selected";
    private static final String ROW_XPATH = "//ancestor::div[contains(@class,'Table2DColumn left')]";
    private static final String COLUMN_XPATH = ".//ancestor::div[contains(@class,'Table2DColumn center')]";
    private static final String HEADER_XPATH = ".//div[@class='Header']";
    private static final String HEADER_CLASS = "Header";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement tableComponent;

    private Table2DComponent(WebDriver driver, WebDriverWait wait, WebElement tableComponent) {
        this.driver = driver;
        this.wait = wait;
        this.tableComponent = tableComponent;
    }

    public static Table2DComponent create(WebDriver driver, WebDriverWait wait, String widgetName) {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_WIDGET_XPATH));
        WebElement tableWidget = driver.findElements(By.xpath(TABLE_2D_WIDGET_XPATH)).stream()
                .filter(widget -> widget.findElement(By.xpath(WIDGET_TITLE)).getText().equals(widgetName)).findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find Widget with name " + widgetName));
        WebElement table2DComponent = tableWidget.findElement(By.className(TABLE_COMPONENT_CLASS));
        return new Table2DComponent(driver, wait, table2DComponent);
    }

    public String getCellValue(String leftHeader, String columnName) {
        Cell2D cell = getCell(leftHeader, columnName);
        return cell.getValue();
    }

    private Cell2D getCell(String leftHeader, String columnName) {
        return getCells().stream().filter(c -> c.getColumnName().equals(columnName) && c.getRowName().equals(leftHeader)).findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find cell with provided attributes"));
    }

    public void selectCell(String leftHeader, String columnName) {
        getCell(leftHeader, columnName).select();
    }

    public List<Cell2D> getCells() {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_COLUMN_XPATH));
        List<Cell2D> allCells = Lists.newArrayList();
        List<WebElement> columns = tableComponent.findElements(By.xpath(TABLE_2D_COLUMN_XPATH));
        for (WebElement column : columns) {
            int index = 0;
            List<WebElement> columnCells = column.findElements(By.xpath(CELL_XPATH));
            for (WebElement element : columnCells) {
                Cell2D cell = new Cell2D(element, index++);
                allCells.add(cell);
            }
        }
        return allCells;
    }

    public void selectRow(String rowName) {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_COLUMN_XPATH));
        List<WebElement> rows = tableComponent.findElements(By.xpath(ROW_HEADER_XPATH));
        WebElement chosenRow = rows.stream().filter(row -> row.getText().equals(rowName)).findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find row with provided attributes"));
        chosenRow.click();
    }

    public void selectColumn(String columnName) {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_COLUMN_XPATH));
        List<WebElement> columns = tableComponent.findElements(By.xpath(COLUMN_HEADER_XPATH));
        WebElement chosenColumn = columns.stream().filter(column -> column.getText().equals(columnName)).findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find column with provided attributes"));
        chosenColumn.click();
    }

    public List<Cell2D> getSelectedCells() {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_COLUMN_XPATH));
        List<Cell2D> allSelectedCells = Lists.newArrayList();
        List<WebElement> columns = tableComponent.findElements(By.xpath(TABLE_2D_COLUMN_XPATH));
        for (WebElement column : columns) {
            int index = 0;
            List<WebElement> columnCells = column.findElements(By.className(SELECTED_CELL_CLASS));
            for (WebElement element : columnCells) {
                Cell2D cell = new Cell2D(element, index++);
                allSelectedCells.add(cell);
            }
        }
        return allSelectedCells;
    }

    public void clearFilter() {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_COLUMN_XPATH));
        WebElement clearFilter = tableComponent.findElement(By.xpath(CLEAR_FILTER_XPATH));
        clearFilter.click();
    }

    public static class Cell2D {
        private final WebElement cell;
        private final int indexLeftHeader;

        private Cell2D(WebElement cell, int index) {
            this.cell = cell;
            this.indexLeftHeader = index;
        }

        public String getValue() {
            return cell.getText();
        }

        public void select() {
            if (!isSelected()) {
                cell.click();
            }
        }

        public void unselect() {
            if (isSelected()) {
                cell.click();
            }
        }

        public boolean isSelected() {
            return !cell.findElements(By.className(SELECTED_CELL_CLASS)).isEmpty();
        }

        public String getRowName() {
            WebElement row = cell.findElement(By.xpath(ROW_XPATH));
            return row.findElements(By.xpath(HEADER_XPATH)).get(indexLeftHeader).getText();
        }

        public String getColumnName() {
            WebElement column = cell.findElement(By.xpath(COLUMN_XPATH));
            return column.findElement(By.className(HEADER_CLASS)).getText();
        }
    }
}
