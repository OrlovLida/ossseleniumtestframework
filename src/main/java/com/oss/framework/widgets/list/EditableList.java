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
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    private static final String HEADERS_SELECTOR_CSS = ".list_row--headers";
    private static final String LIST_HEADERS_SELECTOR_CSS = ".list_row--headers > .header";

    private EditableList(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static EditableList createById(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        Widget.waitForWidget(webDriverWait, LIST_WIDGET_CLASS);
        Widget.waitForWidgetById(webDriverWait, componentId);
        return new EditableList(driver, webDriverWait, componentId);
    }

    public List<String> getColumnHeadersLabels() {
        DelayUtils.waitBy(webDriverWait, By.cssSelector(HEADERS_SELECTOR_CSS));
        List<WebElement> listElements = webElement.findElements(By.cssSelector(LIST_HEADERS_SELECTOR_CSS));
        return listElements.stream().map(WebElement::getText).collect(Collectors.toList());
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

    public void setValue(int rowIndex, String value, String columnId, String componentId) {
        Row row = getRow(rowIndex);
        row.setValue(value, columnId, componentId);
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
            DelayUtils.waitForPresence(wait, By.xpath(String.format(CELL_PATTERN, columnId)));
            WebElement cell = webElement.findElement(By.xpath(String.format(CELL_PATTERN, columnId)));
            return new Cell(driver, wait, cell);
        }

        public String getCellValue(String columnId) {
            return getCell(columnId).getText();
        }

        public void setValue(String value, String columnId, String componentId, Input.ComponentType componentType) {
            getCell(columnId).setValue(value, componentId, componentType);
        }

        public void setValue(String value, String columnId, String componentId) {
            getCell(columnId).setValue(value, componentId);
        }

        public void clearValue(String columnId, String componentId, Input.ComponentType componentType) {
            getCell(columnId).clearValue(componentId, componentType);
        }

        public void clearValue(String columnId, String componentId) {
            getCell(columnId).clearValue(componentId);
        }

        public boolean isAttributeEditable(String columnId) {
            return getCell(columnId).isAttributeEditable();
        }

        public void callAction(String actionId) {
            InlineMenu inlineMenu = InlineMenu.create(webElement, driver, wait);
            inlineMenu.callAction(actionId);
        }

        public void callAction(String groupId, String actionId) {
            InlineMenu inlineMenu = InlineMenu.create(webElement, driver, wait);
            inlineMenu.callAction(groupId, actionId);
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
            private static final String PARENT_XPATH = ".//parent::div";
            private static final String CLASS_TAG_VALUE = "class";
            private static final String EDITABLE_TAG_VALUE = "editable";
            private static final String EDIT_XPATH = ".//ancestor::div[contains(@class, 'list__cell--editable')]//i[@aria-label='EDIT']";
            private static final String CHECKBOX_INPUT_XPATH = ".//input[@type='checkbox']";
            private static final String CHECKBOX_INPUT_ATTRIBUTE_NAME = "value";
            private final WebDriver driver;
            private final WebDriverWait wait;
            private final WebElement webElement;

            private Cell(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
                this.driver = driver;
                this.wait = webDriverWait;
                this.webElement = webElement;
            }

            public String getText() {
                if (!webElement.findElements(By.xpath(CHECKBOX_INPUT_XPATH)).isEmpty()) {
                    return CSSUtils.getAttributeValue(CHECKBOX_INPUT_ATTRIBUTE_NAME, webElement.findElement(By.xpath(CHECKBOX_INPUT_XPATH)));
                } else {
                    return webElement.findElement(By.xpath(TEXT_XPATH)).getText();
                }
            }

            public void setValue(String value, String componentId, Input.ComponentType componentType) {
                if (componentType.equals(Input.ComponentType.CHECKBOX)) {
                    getCheckbox(componentId).setSingleStringValue(value);
                    return;
                }
                WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(EDIT_XPATH)));
                InlineForm inlineForm = InlineForm.create(driver, wait);
                Input component = inlineForm.getComponent(componentId, componentType);
                DelayUtils.sleep(500);
                component.setSingleStringValue(value);
                inlineForm.clickButtonByLabel(SAVE_BUTTON);
            }

            public void setValue(String value, String componentId) {
                if (WebElementUtils.isElementPresent(webElement, By.xpath(EDIT_XPATH))) {
                    WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(EDIT_XPATH)));
                    InlineForm inlineForm = InlineForm.create(driver, wait);
                    Input component = inlineForm.getComponent(componentId);
                    DelayUtils.sleep(500);
                    component.setSingleStringValue(value);
                    inlineForm.clickButtonByLabel(SAVE_BUTTON);
                    return;
                }
                getCheckbox(componentId).setSingleStringValue(value);
            }

            public void clearValue(String componentId, Input.ComponentType componentType) {
                if (componentType.equals(Input.ComponentType.CHECKBOX)) {
                    Input input = ComponentFactory.createFromParent(componentId, componentType, driver, wait, webElement);
                    input.clear();
                    return;
                }
                WebElementUtils.moveToElement(driver, webElement);
                WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(EDIT_XPATH)));
                InlineForm inlineForm = InlineForm.create(driver, wait);
                Input component = inlineForm.getComponent(componentId, componentType);
                component.clear();
                inlineForm.clickButtonByLabel(SAVE_BUTTON);
            }

            public void clearValue(String componentId) {
                if (WebElementUtils.isElementPresent(webElement, By.xpath(EDIT_XPATH))) {
                    WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(EDIT_XPATH)));
                    InlineForm inlineForm = InlineForm.create(driver, wait);
                    Input component = inlineForm.getComponent(componentId);
                    DelayUtils.sleep(500);
                    component.clear();
                    inlineForm.clickButtonByLabel(SAVE_BUTTON);
                    return;
                }
                getCheckbox(componentId).clear();
            }

            public boolean isAttributeEditable() {
                return webElement.findElement(By.xpath(PARENT_XPATH)).getAttribute(CLASS_TAG_VALUE).contains(EDITABLE_TAG_VALUE);
            }

            private Input getCheckbox(String componentId) {
                WebElementUtils.clickWebElement(driver, webElement);
                return ComponentFactory.createFromParent(componentId, Input.ComponentType.CHECKBOX, driver, wait, webElement);
            }
        }
    }
}
