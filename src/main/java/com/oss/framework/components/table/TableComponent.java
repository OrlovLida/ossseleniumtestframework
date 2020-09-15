package com.oss.framework.components.table;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.ColumnsManagement;
import com.oss.framework.widgets.tablewidget.TableWidget.PaginationComponent;

public class TableComponent {

    private static final String tableRows = ".//div[@class='TableBody']//div[@class='custom-scrollbars']//div[contains(@class, 'Row')]";
    private static final String columnResizeGrips = ".//div[@class='resizeGrip']";
    private static final String headers = ".//div[@class='headerItem']";
    private static final String gearIcon = ".//i[contains(@class,'fa-cog')]";
    private static final String horizontalTableScroller = ".//div[contains(@style,'position: relative; display: block; height: 100%; cursor: pointer;')]";

    private static final String verticalTableScroller = ".//div[contains(@style,'position: relative; display: block; width: 100%; cursor: pointer;')]";
    private static final String cells = ".//div[@class='Cell']//*//div[contains(@class,'OSSRichText')]";

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    private AdvancedSearch advancedSearch;
    private ActionsContainer contextActions;
    private PaginationComponent paginationComponent;
    private WebElement webElement;

    public static TableComponent create(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(widgetClass)); //TODO: change to id
        return new TableComponent(driver, widgetClass, webDriverWait);
    }

    private TableComponent(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
    }

    public int howManyRowsOnFirstPage(){
        return driver.findElements(By.xpath(tableRows)).size();
    }

    public void selectFirstRow(){
        selectTableRow(0);
    }

    public void selectTableRow(int row) {
        this.contextActions = null;
        getTableRows().get(row).click();
    }

    private List<WebElement> getTableRows(){
        return this.webElement.findElements(By.xpath(tableRows));
    }

    public List<WebElement> getTableCells(){
        return this.webElement.findElements(By.xpath(cells));
    }

    //TODO: Due to virtual scrolls returns only selected visible rows.
    public List<TableComponent.Row> getSelectedVisibleRows() {
        return getVisibleRows().stream().filter(TableComponent.Row::isSelected).collect(Collectors.toList());
    }

    public List<TableComponent.Row> getVisibleRows() {
        int currentPage = this.paginationComponent.getCurrentPage();
        int step = this.paginationComponent.getStep();
        WebElement gridContainer = this.webElement.findElement(By.className("TableBody"))
                .findElement(By.xpath(".//div[@class='grid-container']/div"));
        List<TableComponent.Row> rows = gridContainer.findElements(By.xpath("./div")).stream().map(we -> new TableComponent.Row(we, currentPage, step))
                .collect(Collectors.toList());
        return rows;
    }

    public ColumnsManagement getColumnsManagement(){
        DelayUtils.waitByXPath(this.webDriverWait, gearIcon);
        this.webElement.findElement(By.xpath(gearIcon)).click();
        return ColumnsManagement.create(this.driver);
    }

    private WebElement getHorizontalTableScroller(){
        return this.webElement.findElement(By.xpath(horizontalTableScroller));
    }

    private WebElement getVerticalTableScroller(){
        return this.webElement.findElement(By.xpath(verticalTableScroller));
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

    private List<WebElement> getColumnHeaders(){
        return this.webElement.findElements(By.xpath(headers));
    }

    public List<String> getActiveColumns() {
        return this.webElement.findElements(By.xpath(headers)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public int getFirstColumnSize() {
        return CSSUtils.getWidthValue(getColumnHeaders().get(0));
    }


    public void resizeFirstColumn(int offset){
        Actions action = new Actions(this.driver);
        action.dragAndDropBy(getColumnResizeGrips().get(0), offset,0).perform();
    }

    private List<WebElement> getColumnResizeGrips(){
        return this.webElement.findElements(By.xpath(columnResizeGrips));
    }

    public String getValueFromRowWithID(String columnLabel, String id) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath(".//div[@id='table-wrapper']/div[@class='TableBody']//div[@id='"+id+"' and contains(@class,'Row')]/div[@class='Cell']//div[contains(@class,'text-wrapper')]"));
        return valueCells.get(index).getText();
    }

    public String getValueFromNthRow(String columnLabel, String rowNumber) {
        int index = getActiveColumns().indexOf(columnLabel);
        List<WebElement> valueCells = this.webElement.findElements(By.xpath("(.//div[@id='table-wrapper']/div[@class='TableBody']//div[@class='Row' or @class='Row selected'])["+rowNumber+"]/div[@class='Cell']/div/div"));
        return valueCells.get(index).getText();
    }


    public static class Row {
        private final WebElement webElement;
        private final int pageNumber;
        private final int paginationStep;

        private Row(WebElement webElement, int pageNumber, int paginationStep) {
            this.webElement = webElement;
            this.pageNumber = pageNumber;
            this.paginationStep = paginationStep;
        }

        public boolean isSelected() {
            return this.webElement.findElement(By.xpath("./div"))
                    .getAttribute("class").contains("selected");
        }

        public int getIndex() {
            int topValue = CSSUtils.getTopValue(this.webElement);
            int heightValue = CSSUtils.getHeightValue(this.webElement);
            return ((topValue + heightValue) / heightValue) +
                    ((pageNumber * paginationStep) - paginationStep);
        }
    }
}
