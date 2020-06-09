package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WidgetUtils;

public class OldTable implements TableInterface {

    public static TableInterface createByWindowTitle(WebDriver driver, WebDriverWait wait, String windowTitle) {

        DelayUtils.waitByXPath(wait,"//div[contains(text(), '"+ windowTitle +"')]");
        WebElement window = driver.findElement(By.xpath("//div[contains(text(), '"+ windowTitle +"')]"));
        WebElement table =  WidgetUtils.findOldWidget(driver, wait, windowTitle, "OSSTableContainer");
        return new OldTable(driver, wait, table, window);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement table;
    private final WebElement window;

    private final Map<String, Column> columnFilters = Maps.newHashMap();

    private OldTable(WebDriver driver, WebDriverWait wait, WebElement table, WebElement window) {
        this.driver = driver;
        this.wait = wait;
        this.table = table;
        this.window = window;
    }

    @Override
    public void selectRow(int row) {
        //TODO; implement
    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {

    }

    @Override
    public void searchByAttribute(String attributeId, ComponentType componentType, String value) {
        throw new RuntimeException("Not implemented for the old table widget");

    }

    @Override
    public void searchByAttributeWithLabel(String attributeLabel, ComponentType componentType, String value) {
        if(componentType != ComponentType.TEXT_FIELD) {
            throw new RuntimeException("Old table widget supports" + ComponentType.TEXT_FIELD + "only");
        }

        if(!columnFilters.containsKey(attributeLabel)) {
            createColumnsFilters();
        }

        Column column = columnFilters.get(attributeLabel);
        column.clear();
        column.setValue(value);

    }

    @Override
    public void callAction(String actionId) {

    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actions = OldActionsContainer.createFromWidget(driver, wait, window);
        actions.callActionByLabel(actionLabel);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    private void createColumnsFilters() {
        this.columnFilters.clear();

        DelayUtils.waitForNestedElements(wait, this.table,"//div[contains(@class, 'OSSTableComponent')]");
        WebElement tableBody = this.table.findElement(By.className("OSSTableComponent"));
        List<WebElement> columns = tableBody.findElements(By.className("OSSTableColumn"));
        for(WebElement element : columns) {
            DelayUtils.waitForNestedElements(wait,element,".//span");
            String label = element.findElement(By.xpath(".//span")).getText();
            this.columnFilters.put(label, new Column(label, element));
        }
    }

    private static class Column {
        private final WebElement column;
        private final String label;

        private Column(String label, WebElement column) {
            this.label = label;
            this.column = column;
        }

        private String getLabel() {
            return label;
        }

        private void selectCell(String value) {

        }

        private void setValue(String value) {
            WebElement input = column.findElement(By.xpath(".//input"));
            input.sendKeys(value);
        }

        private void clear() {
            WebElement input = column.findElement(By.xpath(".//input"));
            input.clear();
        }
    }
}
