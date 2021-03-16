/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.listwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Checkbox;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.InlineForm;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

import static com.oss.framework.listwidget.EditableList.Row.TEXT_WRAPPER;
import static com.oss.framework.listwidget.EditableList.Row.TEXT_CONTAINER;

/**
 * @author Gabriela Kasza
 */
public class EditableList extends Widget {

    private static final String LIST_WIDGET_CLASS = "ExtendedList";
    private static final String XPATH_ADD_ROW = "//button[contains(@class, 'add-row-button')]";
    private static final String XPATH_ROWS_OF_LIST = "//li[contains(@class,'editableListElement')]";

    public static EditableList create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.xpath("//div[contains(@class, '" + LIST_WIDGET_CLASS + "')]"));
        return new EditableList(driver, LIST_WIDGET_CLASS, webDriverWait);
    }

    public static EditableList createById(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        DelayUtils.waitBy(webDriverWait, By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + componentId + "')]"));
        WebElement webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        return new EditableList(driver, webElement, webDriverWait);
    }

    private EditableList(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    private EditableList(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public void addRow() {
        DelayUtils.waitByXPath(webDriverWait, XPATH_ADD_ROW);
        WebElement row = driver.findElement(By.xpath(XPATH_ADD_ROW));
        row.click();
    }

    @Deprecated
    public void setValue(String value, String columnId, int rowIndex, String componentId, Input.ComponentType componentType) {
        Row row = selectRow(rowIndex - 1);
        Row.Cell cell = row.selectCell(columnId);
        cell.setValue(value, componentId, componentType);

    }

    @Deprecated
    public void clearValue (String columnId, int rowIndex, String componentId, Input.ComponentType componentType){
        Row row = selectRow(rowIndex - 1);
        Row.Cell cell = row.selectCell(columnId);
        cell.clearValue(componentId, componentType);
    }

    public void callActionByLabel(String actionLabel, int row) {
        selectRow(row - 1).click();
        ActionsContainer action = ActionsContainer.createFromParent(webElement, driver, webDriverWait);
        action.callActionByLabel("frameworkObjectButtonsGroup", actionLabel);

    }

    public void callActionByLabel(String actionLabel, String columnId, String value) {
        selectRowByEditableAttributeValue(columnId, value).click();
        ActionsContainer action = ActionsContainer.createFromParent(webElement, driver, webDriverWait);
        action.callActionByLabel("frameworkObjectButtonsGroup", actionLabel);

    }

    //TODO update xpath
    public List<String> getValues() {
        List<String> values = new ArrayList<String>();
        DelayUtils.waitForNestedElements(webDriverWait, webElement, "//div[contains(@class,'rowData')]");
        List<WebElement> allRows = webElement.findElements(By.xpath(".//div[contains(@class,'rowData')]"));
        for (WebElement value: allRows) {
            values.add(value.getText());
        }
        return values;
    }

    private Row selectRow(int row) {
        return getVisibleRows().get(row);
    }

    public Row selectRowByStaticAttributeValue(String columnId, String value){
        return selectRowByAttributeValue(columnId, value, TEXT_WRAPPER);
    }

    private Row selectRowByEditableAttributeValue(String columnId, String value){
        return selectRowByAttributeValue(columnId, value, TEXT_CONTAINER);
    }

    private Row selectRowByAttributeValue(String columnId, String value, String columnClassName) {
        List<Row> allRows = getVisibleRows();
        for (Row row : allRows) {
            Row.Cell cell = row.selectCell(columnId);
            String getValue = cell.getText(columnClassName);
            if (getValue.equals(value)) {
                return row;
            }
        }
        throw new RuntimeException("Cannot find a row with the provided value");
    }

    public List<Row> getVisibleRows(){
        return driver.findElements(By.xpath(XPATH_ROWS_OF_LIST))
                .stream()
                .map(element -> new Row(driver, webDriverWait, element))
                .collect(Collectors.toList());
    }

    public static class Row {

        public static final String TEXT_CONTAINER = "textContainer";
        public static final String TEXT_WRAPPER = "text-wrapper";

        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement webElement;

        public Row(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
            this.driver = driver;
            this.wait = webDriverWait;
            this.webElement = webElement;
        }

        public void click(){
            webElement.click();
        }

        public Cell selectCell(String columnId) {
            DelayUtils.waitByXPath(wait, "//div[contains(@class,'" + columnId + "')]");
            WebElement cell = webElement.findElement(By.xpath(".//div[contains(@class,'" + columnId + "')]"));
            return new Cell(driver, wait, cell);
        }

        public String getStaticAttributeValue(String columnId){
            return selectCell(columnId).getText(TEXT_WRAPPER);
        }

        public String getEditableAttributeValue(String columnId){
            return selectCell(columnId).getText(TEXT_CONTAINER);
        }

        public void setEditableAttributeValue(String value, String columnId, String componentId, Input.ComponentType componentType){
            selectCell(columnId).setValue(value, componentId, componentType);
        }

        public static class Cell {

            private static final String SAVE_BUTTON = "Save";
            private final WebDriver driver;
            private final WebDriverWait wait;
            private final WebElement webElement;

            public Cell(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
                this.driver = driver;
                this.wait = webDriverWait;
                this.webElement = webElement;
            }

            public String getText(String columnClassName){
                return webElement.findElement(By.className(columnClassName)).getText();
            }

            public void setValue(String value, String componentId, Input.ComponentType componentType) {
                Actions action = new Actions(driver);
                action.moveToElement(webElement.findElement(By.className(TEXT_CONTAINER))).click().build().perform();
                if (componentType.equals(Input.ComponentType.CHECKBOX)) {
                    Input input = ComponentFactory.create(componentId, componentType, driver, wait);
                    input.setSingleStringValue(value);
                    return;
                }
                InlineForm inlineForm = InlineForm.create(driver, wait);
                Input component = inlineForm.getComponent(componentId, componentType);
                DelayUtils.sleep(500);
                component.setSingleStringValue(value);
                inlineForm.clickButtonByLabel(SAVE_BUTTON);
            }

            public void clearValue(String componentId, Input.ComponentType componentType){
                Actions action = new Actions(driver);
                action.moveToElement(webElement).click().build().perform();
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

        }

    }


}
