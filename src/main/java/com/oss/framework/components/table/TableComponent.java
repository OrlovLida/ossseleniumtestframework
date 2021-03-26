package com.oss.framework.components.table;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableRow;

public class TableComponent {

    private static final String tableRows = ".//div[@class='TableBody']//div[@class='custom-scrollbars']//div[contains(@class, 'Row')]";
    private static final String columnResizeGrips = ".//div[@class='resizeGrip']";
    private static final String headers = ".//div[@class='headerItem']";
    private static final String gearIcon = ".//i[contains(@class,'fa-cog')]";
    private static final String horizontalTableScroller = ".//div[contains(@style,'position: relative; display: block; height: 100%; cursor: pointer;')]";

    private static final String verticalTableScroller = ".//div[contains(@style,'position: relative; display: block; width: 100%; cursor: pointer;')]";
    private static final String cells = ".//div[@class='Cell']//*//div[contains(@class,'OSSRichText')]";

    private static final String TABLE_COMPONENT_CLASS = "TableContainer";
    private static final String GRID_CONTAINER = "grid-container";

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement webElement;
    private final String widgetID;

    public static TableComponent create(WebDriver driver, WebDriverWait webDriverWait, String widgetID) {
        DelayUtils.waitByXPath(webDriverWait, "//div[@" + CSSUtils.TEST_ID + "='" + widgetID + "']/div[contains(@class,"+TABLE_COMPONENT_CLASS+")]");
        WebElement webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetID + "']//div[contains(@class,'"+TABLE_COMPONENT_CLASS+"')]"));
        return new TableComponent(driver, webDriverWait, webElement, widgetID);
    }

    private TableComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement component, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = component;
        this.widgetID = componentId;
    }

    public void selectRow(int row) {
        getVisibleRows().get(row).selectRow();
    }

    public void unselectRow(int row) {
        getVisibleRows().get(row).unselectRow();
    }

    public List<TableRow> getVisibleRows() {
        WebElement gridContainer = this.webElement.findElement(By.xpath(".//div[@class='grid-container']/div"));
        List<WebElement> elements = gridContainer.findElements(By.xpath("./div"));
        List<TableRow> rows = Lists.newArrayList();
        for(int i = 0; i < elements.size(); i++) {
            rows.add(new Row(elements.get(i), i));
        }
        return rows;
    }

    public void scrollHorizontally(int offset){
        getHorizontalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getHorizontalTableScroller()).clickAndHold();
        action.moveByOffset(offset,0);
        action.release();
        action.perform();
    }

    public void scrollVertically(int offset){
        getVerticalTableScroller().click();
        Actions action = new Actions(this.driver);
        action.moveToElement(getVerticalTableScroller()).clickAndHold();
        action.moveByOffset(0,offset);
        action.release();
        action.perform();
    }

    public List<String> getActiveColumns() {
        return this.webElement.findElements(By.xpath(headers)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public int getColumnSize(String columnId) {
        int index = getColumnHeaders().indexOf(columnId);
        return 0;
//        return CSSUtils.getWidthValue(getColumnHeaders().get(index));
    }

    public void resizeColumn(String columnId, int size){
        Actions action = new Actions(this.driver);
        action.dragAndDropBy(getColumnResizeGrips().get(0), size,0).perform();
    }

    public String getCellValue(int row, String columnId) {
        int index = getActiveColumns().indexOf(columnId);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath(".//div[@id='table-wrapper']/div[@class='TableBody']//div[@id='"+row+"' and contains(@class,'Row')]/div[@class='Cell']//div[contains(@class,'text-wrapper')]"));
        return valueCells.get(index).getText();
    }

    public AttributesChooser getAttributesChooser() {
        //Add click on the chooser
        return AttributesChooser.create(this.driver, this.webDriverWait);
    }

    private PaginationComponent getPaginationComponent() {
        WebElement webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetID + "']"));
        return PaginationComponent.createFromParent(this.driver, this.webDriverWait, webElement);
    }

    private WebElement getHorizontalTableScroller(){
        return this.webElement.findElement(By.xpath(horizontalTableScroller));
    }
    private WebElement getVerticalTableScroller(){
        return this.webElement.findElement(By.xpath(verticalTableScroller));
    }

    private List<String> getColumnHeaders(){
        return this.webElement.findElements(By.xpath(headers)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    private List<WebElement> getColumnResizeGrips(){
        return this.webElement.findElements(By.xpath(columnResizeGrips));
    }

    public static class Column {

    }

    public static class Row implements TableRow {
        private final WebElement webElement;
        private final int index;

        private Row(WebElement webElement, int index) {
            this.webElement = webElement;
            this.index = index;
        }

        public void clickRow() {
            this.webElement.click();
        }

        public void selectRow() {
            if(!isSelected()) {
                clickRow();
            }
        }

        public void unselectRow() {
            if(isSelected()) {
                clickRow();
            }
        }

        @Override
        public boolean isSelected() {
            return this.webElement.findElement(By.xpath("./div"))
                    .getAttribute("class").contains("selected");
        }

        @Override
        public int getIndex() {
            return this.index;
        }
    }
}
