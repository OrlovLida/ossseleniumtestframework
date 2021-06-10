package com.oss.framework.components.table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.DragAndDrop.DraggableElement;
import com.oss.framework.utils.DragAndDrop.DropElement;
import com.oss.framework.widgets.tablewidget.TableRow;

public class TableComponent {
    private static final String HEADERS_XPATH = ".//div[@class='sticky-table__header']/div/div";
    private static final String HEADERS2_XPATH = ".//div[@class='sticky-table__header']/div";

    private static final String TABLE_COMPONENT_CLASS = "table-component";

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement webElement;
    private final String widgetId;

    public static TableComponent create(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        DelayUtils.waitByXPath(webDriverWait, "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']/div[contains(@class,"+TABLE_COMPONENT_CLASS+")]");
        WebElement webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']//div[contains(@class,'"+TABLE_COMPONENT_CLASS+"')]"));
        return new TableComponent(driver, webDriverWait, webElement, widgetId);
    }

    private TableComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement component, String widgetId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = component;
        this.widgetId = widgetId;
    }

    public void selectRow(int index) {
        Row row = getRow(index);
        row.selectRow();
    }

    public void selectAll(){
       Cell cell = Cell.createCheckBoxCell(webElement, 0);
       cell.click();
    }

    public void unselectRow(int row) {
        getVisibleRows().get(row).unselectRow();
    }

    public List<TableRow> getVisibleRows() {
        List<Integer> rowIds = this.webElement.findElements(By.xpath(".//div[contains(@"+CSSUtils.TEST_ID+", 'table-content-scrollbar')]//div[contains(@class, 'table-component__cell')]"))
                .stream().filter(e -> e.getAttribute("data-row") != null).map(e -> e.getAttribute("data-row"))
                .distinct().map(Integer::parseInt).sorted().collect(Collectors.toList());

        return rowIds.stream().map( index -> new Row(this.webElement, index)).collect(Collectors.toList());
    }

    public void scrollHorizontally(int offset){
        CustomScrolls customScrolls = getCustomScrolls();
        customScrolls.scrollHorizontally(offset);
    }

    public void scrollVertically(int offset){
        CustomScrolls customScrolls = getCustomScrolls();
        customScrolls.scrollVertically(offset);
    }

    public int getColumnSizeByPosition(int columnIndex) {
        String columnId = getColumnIds().get(columnIndex);
        return getColumnSize(columnId);
    }

    public int getColumnSize(String columnId) {
        Header header = Header.createHeader(this.driver, this.webDriverWait, this.webElement, columnId);
        return header.getSize();
    }

    public void resizeColumnByPosition(int index, int size) {
        String columnId = getColumnIds().get(index);
        resizeColumn(columnId, size);
    }

    public void resizeColumn(String columnId, int size){
        Header header = Header.createHeader(this.driver, this.webDriverWait, this.webElement, columnId);
        header.resize(size);
    }

    public String getCellValue(int row, String columnId) {
        Cell cell = Cell.createFromParent(webElement, row, columnId);
        return cell.getText();
    }

    public AttributesChooser getAttributesChooser() {
        Actions action = new Actions(this.driver);
        action.click(getColumnsManagement()).perform();
        return AttributesChooser.create(this.driver, this.webDriverWait);
    }

    public void changeColumnsOrder(String columnLabel, int position) {
        List<Header> headers = getHeaders();
        Header sourceHeader = headers.stream().filter(h->h.getText().equals(columnLabel))
                .findFirst().orElseThrow( () -> new RuntimeException("Cant find column: " + columnLabel));
        Header targetHeader = headers.get(position);
        DragAndDrop.dragAndDrop(sourceHeader.getDragElement(), targetHeader.getDropElement(), driver);
    }

    private CustomScrolls getCustomScrolls() {
        return CustomScrolls.create(driver, webDriverWait, webElement);
    }

    private WebElement getColumnsManagement() {
        return webElement.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "='table-" + widgetId + "-mng-btn" +"']"));
    }

    public List<String> getColumnIds(){
        return getHeaders().stream()
                .map(Header::getColumnId).collect(Collectors.toList());
    }

    public List<String> getColumnHeaders() {
        return getHeaders().stream()
                .map(Header::getText).collect(Collectors.toList());
    }

    private PaginationComponent getPaginationComponent() {
        WebElement webElement = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        return PaginationComponent.createFromParent(this.driver, this.webDriverWait, webElement);
    }

    private Row getRow(int index) {
        return new Row(webElement, index);
    }

    private List<Header> getHeaders() {
        scrollToFirstColumn();

        List<Header> headers = Lists.newArrayList();
        List<Header> tempHeaders =  this.webElement.findElements(By.xpath(HEADERS_XPATH)).stream()
                .map(e -> Header.createFromWrapper(this.driver, this.webDriverWait, this.webElement, e))
                .filter(header -> !header.getText().equals("")).collect(Collectors.toList());

        Header lastElement = null;
        Header tempElement = tempHeaders.get(tempHeaders.size() - 1);

        while(lastElement == null || !lastElement.equals(tempElement)) {
            tempHeaders.stream().filter(h -> !headers.contains(h))
                    .forEach(headers::add);

            lastElement = tempHeaders.get(tempHeaders.size() -1);
            scrollRightToHeader(lastElement);

            tempHeaders =  this.webElement.findElements(By.xpath(HEADERS_XPATH))
                    .stream().map(e -> Header.createFromWrapper(this.driver, this.webDriverWait, this.webElement, e))
                    .filter(header -> !header.getText().equals("")).collect(Collectors.toList());
            tempElement = tempHeaders.get(tempHeaders.size() -1);
        }

        scrollToFirstColumn();
        return headers;
    }

    private BigDecimal getContentWidth() {
        WebElement headersBody = this.webElement.findElement(By.xpath(HEADERS2_XPATH));
        return BigDecimal.valueOf(CSSUtils.getDecimalWidthValue(headersBody));
    }

    private void scrollRightToHeader(Header header) {
        CustomScrolls scrolls = getCustomScrolls();

        BigDecimal contentWidth =  getContentWidth();
        BigDecimal scrollWidth = BigDecimal.valueOf(scrolls.getVerticalScrollWidth());
        BigDecimal cellLeft = BigDecimal.valueOf(header.getLeft());
        BigDecimal ratio = contentWidth.divide(scrollWidth,2, RoundingMode.FLOOR);

        scrolls.scrollVertically(cellLeft.divide(ratio, 2, RoundingMode.FLOOR).toBigInteger().intValue());
    }

    private void scrollToFirstColumn() {
        CustomScrolls scrolls = getCustomScrolls();
        BigDecimal scrollWidth = BigDecimal.valueOf(scrolls.getVerticalScrollWidth());
        scrolls.scrollVertically(scrollWidth.negate().toBigInteger().intValue());
    }

    public static class Header {
        private static String HEADER_CLASS = "table-component__header";
        private static String RESIZE_XPATH = ".//div[@" + CSSUtils.TEST_ID + "='col-%s-resizer']";

        private final WebElement tableComponent;
        private final String columnId;
        private final String label;

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;

        private static Header createHeader(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent, String columnId) {
            WebElement webElement = getHeader(tableComponent, columnId);
            String label = webElement.getText();
            return new Header(driver, webDriverWait, tableComponent, columnId, label);
        }

        private static Header createFromWrapper(WebDriver driver, WebDriverWait webDriverWait,  WebElement tableComponent, WebElement wrapper) {
            WebElement header = wrapper.findElement(By.xpath("./div"));
            String columnId = CSSUtils.getAttributeValue("data-col", header);
            String label = header.getText();
            return new Header(driver, webDriverWait, tableComponent, columnId, label);
        }

        private static WebElement getHeader(WebElement parent, String columnId) {
            return parent.findElement(By.xpath(".//div[contains(@class, '"+HEADER_CLASS+"') and @data-col='"+columnId+"']"));
        }

        private Header(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent, String columnId, String label) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.tableComponent = tableComponent;
            this.columnId = columnId;
            this.label = label;
        }

        private String getResizeXpath() {
            return String.format(RESIZE_XPATH, columnId);
        }

        public void resize(int offset) {
            WebElement resize = getHeader(tableComponent, columnId).findElement(By.xpath(getResizeXpath()));
            Actions action = new Actions(this.driver);
            action.dragAndDropBy(resize, offset,0).perform();
        }

        public int getSize() {
            return (int) Math.round(getWidth());
        }

        public String getColumnId() {
            return columnId;
        }

        public String getText() {
            return label;
        }

        public double getWidth() {
            return CSSUtils.getDecimalWidthValue(getHeader(tableComponent, columnId));
        }

        public int getLeft() {
            return CSSUtils.getLeftValue(getHeader(tableComponent, columnId).findElement(By.xpath("./..")));
        }

        public DropElement getDropElement() {
            return new DropElement(getHeader(tableComponent, columnId));
        }

        public DraggableElement getDragElement() {
            WebElement header = getHeader(tableComponent, columnId);
            WebElement dragButton = header.findElement(By.xpath(".//div[@ "+ CSSUtils.TEST_ID + "='col-"+columnId+"-drag']"));

            return new DraggableElement(dragButton);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Header header = (Header) o;
            return Objects.equal(columnId, header.columnId);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(columnId);
        }
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

        public int getSize() {
            return (int) Math.round(getWidth());
        }

        public double getWidth() {
            return CSSUtils.getDecimalWidthValue(cell);
        }

        public int getLeft() {
            return CSSUtils.getLeftValue(cell);
        }

        public String getText() {
            if(!isCheckBox()) {
                if(isSelected()) {
                    return "true";
                }
                return "false";
            }
            return cell.getText();
        }

        public void click() {
            cell.click();
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
            return Objects.equal(index, cell1.index) && Objects.equal(columnId, cell1.columnId);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(index, columnId);
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
                    this.tableComponent.findElements(By.xpath(".//div[@data-row='"+this.index+"']"))
                            .stream().findAny().orElseThrow(() -> new RuntimeException("Cant find row "+ this.index));
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
    }
}
