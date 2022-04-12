/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.list;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.categorylist.CategoryList;
import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.InlineForm;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

/**
 * @author Gabriela Kasza
 */
public class EditableList extends Widget {

    private static final String LIST_WIDGET_CLASS = "ExtendedList";
    private static final String XPATH_ADD_ROW = "//button[contains(@class, 'add-row-button')]";
    private static final String XPATH_ROWS_OF_LIST = ".//li[contains(@class,'list_row--editable')]";
    private static final String EMPTY_RESULTS_XPATH =
            "//div[contains(@class, '" + LIST_WIDGET_CLASS + "')]//h3[contains(@class,'emptyResultsText')]";
private static final String CANNOT_FIND_CATEGORY_EXCEPTION = "Cannot find category ";
    private EditableList(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static EditableList createById(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        Widget.waitForWidget(webDriverWait, LIST_WIDGET_CLASS);
        Widget.waitForWidgetById(webDriverWait, componentId);
        return new EditableList(driver, webDriverWait, componentId);
    }

    public Row addRow() {
        DelayUtils.waitByXPath(webDriverWait, XPATH_ADD_ROW);
        WebElement addRowButton = driver.findElement(By.xpath(XPATH_ADD_ROW));
        addRowButton.click();
        List<Row> visibleRows = getVisibleRows();
        return visibleRows.get(visibleRows.size() - 1);
    }

    public void setValue(int rowIndex, String value, String columnId, String componentId, Input.ComponentType componentType) {
        Row row = getRow(rowIndex);
        row.setValue(value, columnId, componentId, componentType);
    }

    public Row getRow(int row) {
        return getVisibleRows().get(row);
    }

    public Row getRowByValue(String columnId, String value) {
        List<Row> allRows = getVisibleRows();
        for (Row row : allRows) {
            Row.Cell cell = row.getCell(columnId);
            String getValue = cell.getText();
            if (getValue.equals(value)) {
                return row;
            }
        }
        throw new NoSuchElementException("Cannot find a row with the provided value");
    }

    public List<Row> getVisibleRows() {
        DelayUtils.waitByXPath(webDriverWait, XPATH_ROWS_OF_LIST);
        List<WebElement> listElements = webElement.findElements(By.xpath(XPATH_ROWS_OF_LIST));
        List<Row> rows = new ArrayList<>();
        for (WebElement listElement : listElements) {
            rows.add(new Row(driver, webDriverWait, listElement));
        }
        return rows;
    }

    public boolean hasNoData() {
        List<WebElement> noData = this.driver.findElements(By.xpath(EMPTY_RESULTS_XPATH));
        return !noData.isEmpty();
    }

    public void expandCategory(String categoryName) {
        CategoryList category = getCategories().stream()
                .filter(categoryList -> categoryList.getValue().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_CATEGORY_EXCEPTION + categoryName));
        category.expandCategory();
    }

    private List<CategoryList> getCategories() {
        return CategoryList.create(driver, webDriverWait, id);
    }
    public static class Row {
        private static final String ROW_CHECKBOX_XPATH = ".//div[contains(@class,'checkbox')]";
        private static final String CELL_PATTERN = ".//div[@" + CSSUtils.TEST_ID + "='%s']";
        private static final String PLACEHOLDERS_XPATH = ".//div[contains(@class,'placeholders')]";
        private static final String ARIA_LABEL_PATTERN = ".//i[@aria-label='%s']";

        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement webElement;

        private Row(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
            this.driver = driver;
            this.wait = webDriverWait;
            this.webElement = webElement;
        }

        public void click() {
            if (isCheckboxPresent()) {
                WebElement checkbox = webElement.findElement(By.xpath(ROW_CHECKBOX_XPATH));
                checkbox.click();
            } else {
                webElement.click();
            }
        }

        public Cell getCell(String columnId) {
            DelayUtils.waitByXPath(wait, String.format(CELL_PATTERN, columnId));
            WebElement cell = webElement.findElement(By.xpath(String.format(CELL_PATTERN, columnId)));
            return new Cell(driver, wait, cell);
        }

        public String getCellValue(String columnId) {
            return getCell(columnId).getText();
        }

        public void setValue(String value, String columnId, String componentId, Input.ComponentType componentType) {
            getCell(columnId).setValue(value, componentId, componentType);
        }

        public void clearValue(String columnId, String componentId, Input.ComponentType componentType) {
            getCell(columnId).clearValue(componentId, componentType);
        }

        public boolean isAttributeEditable(String columnId) {
            return getCell(columnId).isAttributeEditable();
        }

        public void callAction(String actionId) {
            InlineMenu inlineMenu = InlineMenu.create(webElement, driver, wait);
            inlineMenu.callAction(actionId);
        }

        public void callActionIcon(String ariaLabel) {
            DelayUtils.waitForNestedElements(wait, webElement, PLACEHOLDERS_XPATH);
            WebElement placeholdersAndActions = webElement.findElement(By.xpath(PLACEHOLDERS_XPATH));
            WebElement icon = placeholdersAndActions.findElement(By.xpath(String.format(ARIA_LABEL_PATTERN, ariaLabel)));
            DelayUtils.waitForClickability(wait, icon);
            WebElementUtils.clickWebElement(driver, icon);
        }

        private boolean isCheckboxPresent() {
            return !webElement.findElements(By.xpath(ROW_CHECKBOX_XPATH)).isEmpty();
        }

        public static class Cell {
            private static final String TEXT_XPATH = ".//div[@class='text-wrapper' or 'textContainer']";
            private static final String SAVE_BUTTON = "Save";
            private static final String EDIT_ICON_CSS = ".OSSIcon[aria-label='EDIT']";
            private final WebDriver driver;
            private final WebDriverWait wait;
            private final WebElement webElement;

            private Cell(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
                this.driver = driver;
                this.wait = webDriverWait;
                this.webElement = webElement;
            }

            public String getText() {
                return webElement.findElement(By.xpath(TEXT_XPATH)).getText();
            }

            public void setValue(String value, String componentId, Input.ComponentType componentType) {
                Actions action = new Actions(driver);
                if (componentType.equals(Input.ComponentType.CHECKBOX)) {
                    WebElementUtils.clickWebElement(driver, webElement);
                    Input input = ComponentFactory.createFromParent(componentId, componentType, driver, wait, webElement);
                    input.setSingleStringValue(value);
                    return;
                }
                action.moveToElement(webElement).click(webElement.findElement(By.cssSelector(EDIT_ICON_CSS))).build().perform();
                InlineForm inlineForm = InlineForm.create(driver, wait);
                Input component = inlineForm.getComponent(componentId, componentType);
                DelayUtils.sleep(500);
                component.setSingleStringValue(value);
                inlineForm.clickButtonByLabel(SAVE_BUTTON);
            }

            public void clearValue(String componentId, Input.ComponentType componentType) {
                WebElementUtils.clickWebElement(driver, webElement);
                if (componentType.equals(Input.ComponentType.CHECKBOX)) {
                    Input input = ComponentFactory.create(componentId, componentType, driver, wait);
                    input.clear();
                    return;
                }
                InlineForm inlineForm = InlineForm.create(driver, wait);
                Input component = inlineForm.getComponent(componentId, componentType);
                component.clear();
                inlineForm.clickButtonByLabel(SAVE_BUTTON);
            }

            public boolean isAttributeEditable() {
                return webElement.getAttribute("class").contains("editable");
            }
        }
    }
}
