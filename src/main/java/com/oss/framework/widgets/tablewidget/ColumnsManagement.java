package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@Deprecated
public class ColumnsManagement {

    private static final String APPLY_BTN_PATH =".//a[@class='CommonButton btn btn-primary btn-md']";
    private static final String DEFAULT_SETTINGS_BTN_PATH = ".//a[@class='CommonButton btn btn-flat btn-md']";
    private static final String COLUMNS_PATH = ".//div[@class='items']";

    private final WebDriver driver;
    private final WebElement webElement;
    private List<Column> columns;

    public static ColumnsManagement create(WebDriver driver) {
        return new ColumnsManagement(driver);
    }

    private ColumnsManagement (WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.id("columns-management"));
    }

    public void clickApply(){
        WebElement applyBtn = this.webElement.findElement(By.xpath(APPLY_BTN_PATH));
        applyBtn.click();
        columns = null;
    }

    public void clickDefaultSettings(){
        WebElement defaultSettingsBtn = this.webElement.findElement(By.xpath(DEFAULT_SETTINGS_BTN_PATH));
        defaultSettingsBtn.click();
        columns = null;
    }

    public void toggleColumn(String columnLabel) {
        initColumns();
        Column column = findColumn(columnLabel);
        column.toggleColumn();
    }

    public void changeColumnPosition(String columnLabel, int xOffset, int yOffset) {
        initColumns();
        Column column = findColumn(columnLabel);
        column.changePosition(xOffset, yOffset);
        columns = null;
    }

    public List<String> getColumnLabels() {
        initColumns();
        return columns.stream().map(Column::getLabel)
                .collect(Collectors.toList());
    }

    public String getFirstUnselectedColumnLabel() {
        initColumns();
        Column column = findFirstUnselectedColumn();
        return column.getLabel();
    }

    public boolean isColumnEnable(String columnLabel) {
        initColumns();
        Column column = findColumn(columnLabel);
        return column.isEnabled();
    }

    private void initColumns() {
        if(columns == null) {
            columns = this.webElement.findElements(By.xpath(COLUMNS_PATH)).stream()
                    .map(column -> Column.create(column, this.driver))
                    .collect(Collectors.toList());
        }
    }

    private Column findColumn(String columnLabel) {
        initColumns();
        Optional<Column> column = this.columns.stream().filter(c -> c.getLabel()
                .equals(columnLabel)).findFirst();
        if(column.isPresent()) {
            return column.get();
        }

        throw new NoSuchElementException("Cant find column: " + columnLabel);
    }

    private Column findFirstUnselectedColumn() {
        initColumns();
        Optional<Column> column = this.columns.stream().filter(c -> !c.isEnabled()).findFirst();
        if(column.isPresent()) {
            return column.get();
        }

        throw new NoSuchElementException("Cant find unselected column");
    }

    private static class Column {

        private static final String DRAG_ICONS = ".//div[contains(@class,'btn-drag')]";
        private static final String CHECKBOXES = ".//input";
        private static final String CHECKBOX_LABELS = ".//label";

        private final WebElement webElement;
        private final WebDriver driver;

        static Column create(WebElement webElement, WebDriver driver) {
            return new Column(webElement, driver);
        }

        private Column(WebElement webElement, WebDriver driver) {
            this.webElement = webElement;
            this.driver = driver;
        }

        //TODO: needs verification
        private boolean isEnabled() {
            WebElement checkBoxInput = webElement.findElement(By.xpath(CHECKBOXES));
            return checkBoxInput.getAttribute("checked") != null;
        }

        private String getLabel() {
            WebElement checkBoxLabel = webElement.findElement(By.xpath(CHECKBOX_LABELS));
            return checkBoxLabel.getText();
        }

        private void toggleColumn() {
            WebElement checkBoxInput = webElement.findElement(By.xpath(CHECKBOXES));
            checkBoxInput.click();
        }

        private void changePosition(int xOffset, int yOffset) {
            Actions action = new Actions(this.driver);
            WebElement dragIcon = this.webElement.findElement(By.xpath(DRAG_ICONS));
            action.clickAndHold(dragIcon).moveByOffset(xOffset, yOffset).perform();
            action.release(dragIcon).perform();
        }
    }
}
