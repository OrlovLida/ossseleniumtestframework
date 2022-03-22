package com.oss.framework.iaa.widgets.table;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.iaa.widgets.components.Table2DComponent;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Table2DWidget {
    private static final String TABLE_2D_WIDGET_XPATH = "//div[@class='Table2DWidget']";
    private static final String TABLE_2D_TITLE_XPATH = ".//p[@class='title']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement widget;
    private final String tableName;

    public static Table2DWidget create(WebDriver driver, WebDriverWait wait, String tableName) {
        DelayUtils.waitBy(wait, By.xpath(TABLE_2D_WIDGET_XPATH));
        WebElement webElement = driver.findElements(By.xpath(TABLE_2D_WIDGET_XPATH)).stream()
                .filter(table -> table.findElement(By.xpath(TABLE_2D_TITLE_XPATH)).getText().equals(tableName)).findFirst()
                .orElseThrow(() -> new RuntimeException("Table with provided name doesn't exist"));
        return new Table2DWidget(driver, wait, tableName, webElement);
    }

    private Table2DWidget(WebDriver driver, WebDriverWait wait, String tableName, WebElement widget) {
        this.driver = driver;
        this.wait = wait;
        this.widget = widget;
        this.tableName = tableName;
    }

    private Table2DComponent getTable2DComponent() {
        return Table2DComponent.create(driver, wait, tableName);
    }

    public String getCellValue(String leftHeader, String columnName) {
        return getTable2DComponent().getCellValue(leftHeader, columnName);
    }

    public void selectCell(String leftHeader, String columnName) {
        getTable2DComponent().selectCell(leftHeader, columnName);
    }

    public List<Table2DComponent.Cell2D> getCells() {
        return getTable2DComponent().getCells();
    }

    public void selectRow(String rowName) {
        getTable2DComponent().selectRow(rowName);
    }

    public void selectColumn(String columnName) {
        getTable2DComponent().selectColumn(columnName);
    }

    public List<Table2DComponent.Cell2D> getSelectedCells() {
        return getTable2DComponent().getSelectedCells();
    }

    public void clearFilter() {
        getTable2DComponent().clearFilter();
    }

    public void callAction(String actionLabel) {
        ButtonContainer.createFromParent(widget, driver, wait).callActionByLabel(actionLabel);
    }
}
