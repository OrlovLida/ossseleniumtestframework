package com.oss.framework.components.table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableRow;

public class TableComponent {

    private static final String tableRows = ".//div[@class='TableBody']//div[@class='custom-scrollbars']//div[contains(@class, 'Row')]";
    private static final String columnResizeGrips = ".//div[@class='resizeGrip']";
    private static final String HEADERS_XPATH = ".//div[@class='sticky-table__header']/div/div";
    private static final String HEADERS2_XPATH = ".//div[@class='sticky-table__header']/div";
    private static final String gearIcon = ".//i[contains(@class,'fa-cog')]";
    private static final String horizontalTableScroller = ".//div[contains(@style,'position: relative; display: block; height: 100%; cursor: pointer;')]";

    private static final String verticalTableScroller = ".//div[contains(@style,'position: relative; display: block; width: 100%; cursor: pointer;')]";
    private static final String cells = ".//div[@class='Cell']//*//div[contains(@class,'OSSRichText')]";

    private static final String TABLE_COMPONENT_CLASS = "table-component";
    private static final String GRID_CONTAINER = "grid-container";
    private static final String STICKY_COLUMNS_XPATH = ".//div[contains(@class, 'sticky-table__sticky-column__content')]/div/div";
    private static final String CONTENT_CELLS_XPATH = ".//div[contains(@class, 'sticky-table__content')]/div/div/div";

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

    public void selectRow(int index) {
        Row row = getRow(index);
        row.selectRow();
    }

    private Row getRow(int index) {
        return new Row(webElement, index);
    }

    public void unselectRow(int row) {
        getVisibleRows().get(row).unselectRow();
    }

    public List<TableRow> getVisibleRows() {
        List<Integer> rowIds = this.webElement.findElements(By.xpath(".//div[contains(@data-testId, 'table-content-scrollbar')]//div[contains(@class, 'table-component__cell')]"))
                .stream().filter(e -> e.getAttribute("data-row") != null).map(e -> e.getAttribute("data-row"))
                .distinct().map(Integer::parseInt).sorted().collect(Collectors.toList());

        return rowIds.stream().map( index -> new Row(this.webElement, index)).collect(Collectors.toList());
    }

    private CustomScrolls getCustomScrolls() {
        return CustomScrolls.create(driver, webDriverWait, webElement);
    }

    public void scrollHorizontally(int offset){
        CustomScrolls customScrolls = getCustomScrolls();
        customScrolls.scrollHorizontally(offset);
    }

    public void scrollVertically(int offset){
        CustomScrolls customScrolls = getCustomScrolls();
        customScrolls.scrollVertically(offset);
    }

    public List<String> getActiveColumns() {
        return this.webElement.findElements(By.xpath(HEADERS_XPATH)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public int getColumnSize(String columnId) {
        int index = getColumnHeaders().indexOf(columnId);
        return 0;
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
        List<String> headers = Lists.newArrayList();
        List<Cell> tempHeaders =  this.webElement.findElements(By.xpath(HEADERS_XPATH)).stream().map(Cell::createFromWrapper)
                .filter(cell -> !cell.getText().equals("")).collect(Collectors.toList());

        Cell lastElement = null;
        Cell tempElement = tempHeaders.get(tempHeaders.size() - 1);

        while(lastElement == null || !lastElement.getText().equals(tempElement.getText())) {
            headers.addAll(tempHeaders.stream().map(Cell::getText).filter(text -> !headers.contains(text)).collect(Collectors.toList()));

            lastElement = tempHeaders.get(tempHeaders.size() -1);
            scrollToHeader(lastElement);

            tempHeaders =  this.webElement.findElements(By.xpath(HEADERS_XPATH)).stream().map(Cell::createFromWrapper)
                    .filter(cell -> !cell.getText().equals("")).collect(Collectors.toList());
            tempElement = tempHeaders.get(tempHeaders.size() -1);
        }

        return headers;
    }

    private BigDecimal getContentWidth() {
        WebElement headersBody = this.webElement.findElement(By.xpath(HEADERS2_XPATH));
        return BigDecimal.valueOf(CSSUtils.getDecimalWidthValue(headersBody));
    }

    private void scrollToHeader(Cell cell) {
        CustomScrolls scrolls = getCustomScrolls();

        BigDecimal contentWidth =  getContentWidth();
        BigDecimal scrollWidth = BigDecimal.valueOf(scrolls.getVerticalScrollWidth());
        BigDecimal cellLeft = BigDecimal.valueOf(cell.getLeft());
        BigDecimal ratio = contentWidth.divide(scrollWidth,2, RoundingMode.FLOOR);

        scrolls.scrollVertically(cellLeft.divide(ratio, 2, RoundingMode.FLOOR).toBigInteger().intValue());
    }

    private List<WebElement> getColumnResizeGrips(){
        return this.webElement.findElements(By.xpath(columnResizeGrips));
    }

    public static class Cell {
        private static final String SELECTED_CLASS = "table-component__cell--selected";
        private static final String CHECKBOX_COLUMN_ID = "checkbox";

        private final WebElement cell;
        private final int index;
        private final String columnId;

        private static Cell createFromWrapper(WebElement wrapper) {
            WebElement cell = wrapper.findElement(By.xpath("./div"));
            int index = CSSUtils.getIntegerValue("data-row", cell);
            String columnId = cell.getCssValue("data-col");
            return new Cell(cell, index, columnId);
        }

        private static Cell createCheckBoxCell(WebElement tableComponent, int index) {
            return createFromParent(tableComponent, index, CHECKBOX_COLUMN_ID);
        }

        private static Cell createFromParent(WebElement tableComponent, int index, String columnId) {
            WebElement cell = tableComponent.findElements(By.xpath(".//div[@data-row='"+index+"' and @data-col='"+columnId+"']"))
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("Cant find cell: rowId " + index + " columnId: " + columnId));
            return new Cell(cell, index, CHECKBOX_COLUMN_ID);
        }

        private Cell(WebElement cell, int index, String columnId) {
            this.cell = cell;
            this.index = index;
            this.columnId = columnId;
        }

        public double getWidth() {
            return CSSUtils.getDecimalWidthValue(cell);
        }

        public int getLeft() {
            return CSSUtils.getLeftValue(cell);
        }

        public String getText() {
            if(isCheckBox()) {
                if(isSelected()) {
                    return "true";
                }
                return "false";
            }
            return cell.getText();
        }

        private boolean isSelected() {
            List<String> classes = CSSUtils.getAllClasses(cell);
            return classes.contains(SELECTED_CLASS);
        }

        private boolean isCheckBox() {
            return CHECKBOX_COLUMN_ID.equals(columnId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell1 = (Cell) o;
            return Objects.equal(cell.getText(), cell1.cell.getText());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(cell.getText());
        }
    }

    public static class Row implements TableRow {

        private final WebElement tableComponent;
        private final int index;

        private Row(WebElement tableComponent, int index) {
            this.tableComponent = tableComponent;
            this.index = index;
        }

        public void clickRow() {
            WebElement randomCell =
                    this.tableComponent.findElements(By.xpath(".//div[@data-row='"+this.index+"']")).stream().findAny().orElseThrow(() -> new RuntimeException("Cant find row "+ this.index));
            randomCell.click();
        }

        public String getColumnValue(String columnId) {
            WebElement cell =
                    this.tableComponent.findElements(By.xpath(".//div[@data-row='"+this.index+"' and @data-col='"+columnId+"']")).stream()
                            .findFirst().orElseThrow(() -> new RuntimeException("Cant find cell: rowId "+ this.index + " columnId: " + columnId));
            return cell.getText();
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
            Cell checkBox = Cell.createCheckBoxCell(tableComponent, index);
            return checkBox.isSelected();
        }

        @Override
        public int getIndex() {
            return this.index;
        }
    }




    public static class CustomScrolls {
        private static final String SCROLLS_XPATH = ".//div[contains(@class, 'custom-scrollbars')]";

        private final WebElement scrolls;
        private final WebDriverWait wait;
        private final WebDriver driver;

        public static CustomScrolls create(WebDriver driver, WebDriverWait wait, WebElement parent) {
            DelayUtils.waitForNestedElements(wait, parent, SCROLLS_XPATH);
            WebElement scrolls = parent.findElement(By.xpath(SCROLLS_XPATH));
            return new CustomScrolls(driver, wait, scrolls);
        }

        private CustomScrolls(WebDriver driver, WebDriverWait wait, WebElement scrolls) {
            this.driver = driver;
            this.wait = wait;
            this.scrolls = scrolls;
        }

        public void scrollHorizontally(int offset) {
        }

        public void scrollVertically(int offset) {
            List<WebElement> divs = scrolls.findElements(By.xpath("./div"));
            WebElement horizontal = divs.get(1);
            WebElement bar = horizontal.findElement(By.xpath("./div"));
            Actions action = new Actions(this.driver);
            action.moveToElement(bar).clickAndHold();
            action.moveByOffset(offset,0);
            action.release();
            action.perform();
        }

        public int getVerticalScrollWidth() {
            return 1610;
        }

        public void getHorizontalScrollSize() {

        }

        public void getVerticalScrollSize() {

        }

        public int getHorizontalScrollPosition() {
            return 0;
        }


    }
}
