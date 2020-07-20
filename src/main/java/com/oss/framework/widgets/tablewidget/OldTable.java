package com.oss.framework.widgets.tablewidget;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
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
    public static TableInterface createByComponentId(WebDriver driver, WebDriverWait wait, String componentId){
        DelayUtils.waitByXPath(wait,"//div[contains(@id,'"+componentId+"')]");
        WebElement table = driver.findElement(By.id(componentId));
        return  new OldTable(driver,wait, table);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement table;
    private  WebElement window;

    //private final Map<String, Column> columns = Maps.newHashMap();

    private OldTable(WebDriver driver, WebDriverWait wait, WebElement table, WebElement window) {
        this.driver = driver;
        this.wait = wait;
        this.table = table;
        this.window = window;
    }
    private OldTable(WebDriver driver, WebDriverWait wait, WebElement table){
        this.driver=driver;
        this.wait= wait;
        this.table=table;
    }


    @Override
    public void selectRow(int row) {
        Map<String, Column> columns = createColumnsFilters();
        Lists.newArrayList(columns.values()).get(0).selectCell(0);

    }

    @Override
    public void selectRowByAttributeValue(String attributeId, String value) {
        throw new RuntimeException("Not implemented for the old table widget");
    }

    @Override
    public void selectRowByAttributeValueWithLabel(String attributeLabel, String value) {
        Map<String, Column> columns = createColumnsFilters();

        Column column = columns.get(attributeLabel);
        column.selectCell(value);

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
        Map<String, Column> columns = createColumnsFilters();

        Column column = columns.get(attributeLabel);
        column.clear();
        column.setValue(value);
        DelayUtils.waitByXPath(wait,".//div[contains(@class, 'Cell')]");

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
    public String getValueCell(int index, String attributeLabel){
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        return column.getValueCell(index);

    }
    public int getRowNumber(String value, String attributeLabel){
        Map<String, Column> columns = createColumnsFilters();
        Column column = columns.get(attributeLabel);
        return column.indexOf(value);
    }

    private Map<String, Column> createColumnsFilters() {
        Map<String, Column> columns = Maps.newHashMap();
        DelayUtils.waitForNestedElements(wait, this.table,"//div[contains(@class, 'OSSTableComponent')]");
        WebElement tableBody = this.table.findElement(By.className("OSSTableComponent"));
        List<WebElement> columns2 = tableBody.findElements(By.className("OSSTableColumn"));
        for(WebElement element : columns2) {
            DelayUtils.waitForNestedElements(wait,element,".//span");
            String label = element.findElement(By.xpath(".//span")).getText();
            columns.put(label, new Column(label, element, wait));
        }
        return columns;
    }

    private static class Column {
        private final WebElement column;
        private final String label;
        private final WebDriverWait wait;

        private Column(String label, WebElement column, WebDriverWait wait) {
            this.label = label;
            this.column = column;
            this.wait=wait;
        }

        private String getLabel() {
            return label;
        }

        private void selectCell(String value) {
            List<WebElement> cells=column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            for (WebElement cell: cells){
                DelayUtils.waitForNestedElements(this.wait,cell,".//div[contains(@class, 'OSSRichText')]");
                WebElement richText = cell.findElement(By.xpath(".//div[contains(@class, 'OSSRichText')]"));
               if (richText.getText().equals(value)){
                   cell.click();
               }
            }
        }

        public int indexOf(String value) {
            List<WebElement> cells = column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));

            for(WebElement cell : cells) {

                DelayUtils.waitForNestedElements(this.wait,cell,".//div[contains(@class, 'OSSRichText')]");
                WebElement richText = cell.findElement(By.xpath(".//div[contains(@class, 'OSSRichText')]"));
                if (richText.getText().equals(value)){
                    return cells.indexOf(cell);
                }
            }
            throw new RuntimeException("Cannot find a row with the provided value");
        }
        public void selectCell(int index){
            List<WebElement> cells=column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            WebElement cell = cells.get(index);
            cell.click();

        }
        public String getValueCell(int index){
            List<WebElement> cells=column.findElements(By.xpath(".//div[contains(@class, 'Cell')]"));
            WebElement cell = cells.get(index);
            return cell.getText();
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
