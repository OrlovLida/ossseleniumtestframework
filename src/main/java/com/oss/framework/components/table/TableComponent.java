package com.oss.framework.components.table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.common.ListAttributesChooser;
import com.oss.framework.components.common.PaginationComponent;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.scrolls.CustomScrolls;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.DragAndDrop.DraggableElement;
import com.oss.framework.utils.DragAndDrop.DropElement;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.framework.widgets.treewidget.InlineMenu;

public class TableComponent {
    private static final String HEADERS_XPATH = ".//div[@class='sticky-table__header']/div";
    private static final String EMPTY_DATA_ROW_XPATH = ".//div[contains(@class, 'empty_data_row')]";
    private static final String HEADER_CLASS = "table-component__header";

    private static final String TABLE_COMPONENT_CLASS = "table-component";

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement webElement;
    private final String widgetId;

    private PaginationComponent paginationComponent;

    private TableComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement component, String widgetId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = component;
        this.widgetId = widgetId;
    }

    public static TableComponent create(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        DelayUtils.waitByXPath(webDriverWait, getTableComponentPath(widgetId));
        WebElement webElement = driver.findElement(By.xpath(getTableComponentPath(widgetId)));
        return new TableComponent(driver, webDriverWait, webElement, widgetId);
    }

    private static String getTableComponentPath(String widgetId) {
        return "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']//div[contains(@class,'" + TABLE_COMPONENT_CLASS + "')]";
    }

    public void selectRow(int index) {
        Row row = getRow(index);
        row.selectRow();
    }

    public void selectAll() {
        Header.getHeader(webElement, Cell.CHECKBOX_COLUMN_ID).click();
    }

    public void unselectRow(int index) {
        Row row = getRow(index);
        row.unselectRow();
    }

    public boolean hasNoData() {
        return !webElement.findElements(By.xpath(EMPTY_DATA_ROW_XPATH)).isEmpty();
    }

    public List<TableRow> getVisibleRows() {
        String firstColumn = getColumnIds().stream().findFirst().orElse("");
        String xpath = ".//div[@data-col='" + firstColumn + "']";

        List<Integer> rowIds = this.webElement
                .findElements(By.xpath(xpath))
                .stream().filter(e -> e.getAttribute("data-col").equals(firstColumn)).filter(e -> e.getAttribute("data-row") != null)
                .map(e -> e.getAttribute("data-row"))
                .map(Integer::parseInt).sorted().collect(Collectors.toList());

        return rowIds.stream().map(index -> new Row(this.driver, this.webDriverWait, this.webElement, index)).collect(Collectors.toList());
    }

    public void scrollHorizontally(int offset) {
        CustomScrolls customScrolls = getCustomScrolls();
        customScrolls.scrollHorizontally(offset);
    }

    public void scrollVertically(int offset) {
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

    public Header getHeaderByIndex(String columnId) {
        return Header.createHeader(this.driver, this.webDriverWait, this.webElement, columnId);
    }

    public void sortColumnByASC(String columnId) {
        getHeaderByIndex(columnId).openSettings().sortByASC();
    }

    public void sortColumnByDESC(String columnId) {
        getHeaderByIndex(columnId).openSettings().sortByDESC();
    }

    public void turnOffSorting(String columnId) {
        getHeaderByIndex(columnId).openSettings().turnOffSorting();
    }

    public void setColumnWidth(String columnId, String columnWidth) {
        getHeaderByIndex(columnId).openSettings().setDefaultColumnWidth(columnWidth);
    }

    public void resizeColumn(String columnId, int size) {
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

    public ListAttributesChooser getListAttributesChooser() {
        Actions action = new Actions(this.driver);
        action.click(getColumnsManagement()).perform();
        return ListAttributesChooser.create(this.driver, this.webDriverWait);
    }

    public void changeColumnsOrder(String columnLabel, int position) {
        List<Header> headers = getHeaders();
        Header sourceHeader = headers.stream().filter(h -> h.getText().equals(columnLabel))
                .findFirst().orElseThrow(() -> new RuntimeException("Cant find column: " + columnLabel));
        Header targetHeader = headers.get(position);
        DragAndDrop.dragAndDrop(sourceHeader.getDragElement(), targetHeader.getDropElement(), driver);
    }

    public void changeColumnsOrderById(String columnId, int position) {
        List<Header> headers = getHeaders();
        Header sourceHeader = headers.stream().filter(h -> h.getColumnId().equals(columnId))
                .findFirst().orElseThrow(() -> new RuntimeException("Cant find column: " + columnId));
        Header targetHeader = headers.get(position);
        DragAndDrop.dragAndDrop(sourceHeader.getDragElement(), targetHeader.getDropElement(), driver);
    }

    public List<String> getColumnIds() {
        return getHeaders().stream()
                .map(Header::getColumnId).collect(Collectors.toList());
    }

    public List<String> getColumnHeaders() {
        return getHeaders().stream()
                .map(Header::getText).collect(Collectors.toList());
    }

    public PaginationComponent getPaginationComponent() {
        if (paginationComponent == null) {
            WebElement parent = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
            paginationComponent = PaginationComponent.createFromParent(this.driver, this.webDriverWait, parent);
        }
        return paginationComponent;
    }

    private CustomScrolls getCustomScrolls() {
        return CustomScrolls.create(driver, webDriverWait, webElement);
    }

    private WebElement getColumnsManagement() {
        return webElement.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "='table-" + widgetId + "-mng-btn" + "']"));
    }

    private Row getRow(int index) {
        return new Row(driver, webDriverWait, webElement, index);
    }

    private List<Header> getHeaders() {
        scrollToFirstColumn();

        List<Header> headers = Lists.newArrayList();
        List<Header> tempHeaders = this.webElement.findElements(By.className(HEADER_CLASS)).stream()
                .map(e -> Header.createFromWrapper(this.driver, this.webDriverWait, this.webElement, e))
                .filter(header -> !header.getText().equals("")).collect(Collectors.toList());

        Header lastElement = null;
        Header tempElement = tempHeaders.get(tempHeaders.size() - 1);

        while (lastElement == null || !lastElement.equals(tempElement)) {
            tempHeaders.stream().filter(h -> !headers.contains(h))
                    .forEach(headers::add);

            lastElement = tempHeaders.get(tempHeaders.size() - 1);
            scrollRightToHeader(lastElement);

            tempHeaders = this.webElement.findElements(By.className(HEADER_CLASS))
                    .stream().map(e -> Header.createFromWrapper(this.driver, this.webDriverWait, this.webElement, e))
                    .filter(header -> !header.getText().equals("")).collect(Collectors.toList());
            tempElement = tempHeaders.get(tempHeaders.size() - 1);
        }

        scrollToFirstColumn();
        return headers;
    }

    private BigDecimal getContentWidth() {
        WebElement headersBody = this.webElement.findElement(By.xpath(HEADERS_XPATH));
        return BigDecimal.valueOf(CSSUtils.getDecimalWidthValue(headersBody));
    }

    private void scrollRightToHeader(Header header) {
        CustomScrolls scrolls = getCustomScrolls();
        BigDecimal scrollWidth = BigDecimal.valueOf(scrolls.getHorizontalScrollWidth());
        int contentVisibleWidth = scrolls.getHorizontalBarWidth();
        int diff = scrollWidth.toBigInteger().intValue() - contentVisibleWidth;

        BigDecimal contentWidth = getContentWidth();
        BigDecimal cellLeft = BigDecimal.valueOf(header.getLeft());
        BigDecimal ratio = contentWidth.divide(scrollWidth, 2, RoundingMode.FLOOR);

        int offset = cellLeft.divide(ratio, 2, RoundingMode.FLOOR).toBigInteger().intValue();

        scrolls.scrollHorizontally(Math.min(offset, diff));
    }

    private void scrollToFirstColumn() {
        CustomScrolls scrolls = getCustomScrolls();
        if (scrolls.getHorizontalBarWidth() == 0)
            return;

        int translateX = scrolls.getTranslateXValue();
        if (translateX == 0)
            return;

        scrolls.scrollHorizontally(-translateX);
    }

    public static class Header {
        private static final String RESIZE_XPATH = ".//div[@" + CSSUtils.TEST_ID + "='col-%s-resizer']";
        private static final String SETTINGS_XPATH = ".//div[@" + CSSUtils.TEST_ID + "='col-%s-settings']";

        private final WebElement tableComponent;
        private final String columnId;
        private final String label;

        private final WebDriver driver;
        private final WebDriverWait webDriverWait;

        private Header(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent, String columnId, String label) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.tableComponent = tableComponent;
            this.columnId = columnId;
            this.label = label;
        }

        private static Header createHeader(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent, String columnId) {
            WebElement webElement = getHeader(tableComponent, columnId);
            String label = webElement.getText();
            return new Header(driver, webDriverWait, tableComponent, columnId, label);
        }

        private static Header createFromWrapper(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent,
                                                WebElement wrapper) {
            String columnId = CSSUtils.getAttributeValue("data-col", wrapper);
            String label = wrapper.getText();
            return new Header(driver, webDriverWait, tableComponent, columnId, label);
        }

        private static WebElement getHeader(WebElement parent, String columnId) {
            return parent.findElement(By.xpath(".//div[contains(@class, '" + HEADER_CLASS + "') and @data-col='" + columnId + "']"));
        }

        public void resize(int offset) {
            WebElement resize = getHeader(tableComponent, columnId).findElement(By.xpath(getResizeXpath()));
            Actions action = new Actions(this.driver);
            action.dragAndDropBy(resize, offset, 0).perform();
        }

        public HeaderSettings getHeaderSettings() {
            return HeaderSettings.createHeaderSettings(this.driver, this.webDriverWait);
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

        public HeaderSettings openSettings() {
            getHeader(tableComponent, columnId).findElement(By.xpath(getSettingsXpath())).click();
            return getHeaderSettings();
        }

        public DropElement getDropElement() {
            return new DropElement(getHeader(tableComponent, columnId));
        }

        public DraggableElement getDragElement() {
            WebElement header = getHeader(tableComponent, columnId);
            WebElement dragButton = header.findElement(By.xpath(".//div[@ " + CSSUtils.TEST_ID + "='col-" + columnId + "-drag']"));

            return new DraggableElement(dragButton);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(columnId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Header header = (Header) o;
            return Objects.equal(columnId, header.columnId);
        }

        private String getResizeXpath() {
            return String.format(RESIZE_XPATH, columnId);
        }

        private String getSettingsXpath() {
            return String.format(SETTINGS_XPATH, columnId);
        }
    }

    public static class HeaderSettings {
        private static final String COLUMN_PANEL_SETTINGS_XPATH = "//div[@class='column-panel-settings']";
        private static final String TABS_BUTTON_CLASS = "tabs-button";
        private static final String ADMINISTRATION = "Administration";
        private static final String ADMINISTRATION_TAB_IS_NOT_AVAILABLE_EXCEPTION = "Administration tab is not available";
        private static final String APPLY_BUTTON_IS_NOT_AVAILABLE_EXCEPTION = "Apply button is not available";
        private static final String BUTTON_CLASS = "CommonButton";
        private static final String APPLY_BUTTON_LABEL = "Apply";
        private static final String SIZE_DEFAULT_INPUT_ID = "size_default";
        private static final String RADIO_BUTTON_CSS = "div.radio";
        private final WebElement webElement;
        private final WebDriver driver;
        private final WebDriverWait webDriverWait;

        private HeaderSettings(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.webElement = webElement;
        }

        private static HeaderSettings createHeaderSettings(WebDriver driver, WebDriverWait webDriverWait) {
            WebElement webElement = driver.findElement(By.xpath(COLUMN_PANEL_SETTINGS_XPATH));
            return new HeaderSettings(driver, webDriverWait, webElement);
        }

        private List<WebElement> sortButtons() {
            WebElement buttonsWrapper = this.webElement.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='sort_btn']"));
            return buttonsWrapper.findElements(By.cssSelector(RADIO_BUTTON_CSS));
        }

        private void sortByASC() {
            sortButtons().get(1).click();
        }

        private void sortByDESC() {
            sortButtons().get(2).click();
        }

        private void turnOffSorting() {
            sortButtons().get(0).click();
        }

        private void selectAdministrationTab() {
            WebElement administrationTab = this.webElement.findElements(By.className(TABS_BUTTON_CLASS)).stream()
                    .filter(tab -> tab.getText().equals(ADMINISTRATION)).findFirst()
                    .orElseThrow(() -> new RuntimeException(ADMINISTRATION_TAB_IS_NOT_AVAILABLE_EXCEPTION));
            administrationTab.click();
        }

        private void setDefaultColumnWidth(String columnWidth) {
            selectAdministrationTab();
            Input input = ComponentFactory.create(SIZE_DEFAULT_INPUT_ID, Input.ComponentType.TEXT_FIELD, driver, webDriverWait);
            input.setSingleStringValue(columnWidth);
            apply();
        }

        private void apply() {
            WebElement applyButton = this.webElement.findElements(By.className(BUTTON_CLASS)).stream()
                    .filter(button -> button.getText().equals(APPLY_BUTTON_LABEL)).findFirst()
                    .orElseThrow(() -> new RuntimeException(APPLY_BUTTON_IS_NOT_AVAILABLE_EXCEPTION));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(applyButton)).click();
        }
    }

    public static class Cell {
        private static final String SELECTED_CLASS = "table-component__cell--selected";
        private static final String CHECKBOX_COLUMN_ID = "checkbox";

        private final WebElement cellElement;
        private final int index;
        private final String columnId;

        private Cell(WebElement cellElement, int index, String columnId) {
            this.cellElement = cellElement;
            this.index = index;
            this.columnId = columnId;
        }

        private static boolean hasCheckboxCell(WebElement tableComponent, int index) {
            return tableComponent.findElements(By.xpath(".//div[@data-row='" + index + "' and @data-col='" + CHECKBOX_COLUMN_ID + "']"))
                    .stream().findAny().isPresent();
        }

        private static Cell createFromWrapper(WebElement wrapper) {
            WebElement cell = wrapper.findElement(By.xpath("./div"));
            int index = CSSUtils.getIntegerValue("data-row", cell);
            String columnId = cell.getCssValue("data-col");
            return new Cell(cell, index, columnId);
        }

        private static Cell createCheckboxCell(WebElement tableComponent, int index) {
            return createFromParent(tableComponent, index, CHECKBOX_COLUMN_ID);
        }

        private static Cell createFromParent(WebElement tableComponent, int index, String columnId) {
            WebElement cell = tableComponent.findElements(By.xpath(".//div[@data-row='" + index + "' and @data-col='" + columnId + "']"))
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Cant find cell: rowId " + index + " columnId: " + columnId));
            return new Cell(cell, index, columnId);
        }

        private static Cell createRandomCell(WebElement tableComponent, int index) {
            WebElement randomCell =
                    tableComponent.findElements(By.xpath(".//div[@data-row='" + index + "']"))
                            .stream().findAny().orElseThrow(() -> new RuntimeException("Cant find row " + index));
            String columnId = CSSUtils.getAttributeValue("data-col", randomCell);
            return new Cell(randomCell, index, columnId);
        }

        public int getSize() {
            return (int) Math.round(getWidth());
        }

        public double getWidth() {
            return CSSUtils.getDecimalWidthValue(cellElement);
        }

        public int getLeft() {
            return CSSUtils.getLeftValue(cellElement);
        }

        public String getText() {
            if (isCheckBox()) {
                if (isSelected()) {
                    return "true";
                }
                return "false";
            }
            return cellElement.getAttribute("textContent");
        }

        public void click() {
            cellElement.click();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(index, columnId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Cell cell1 = (Cell) o;
            return Objects.equal(index, cell1.index) && Objects.equal(columnId, cell1.columnId);
        }

        private boolean isSelected() {
            List<String> classes = CSSUtils.getAllClasses(cellElement);
            return classes.contains(SELECTED_CLASS);
        }

        private boolean isCheckBox() {
            return CHECKBOX_COLUMN_ID.equals(columnId);
        }
    }

    public static class Row implements TableRow {

        private final WebElement tableComponent;
        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final int index;

        private Row(WebDriver driver, WebDriverWait webDriverWait, WebElement tableComponent, int index) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.tableComponent = tableComponent;
            this.index = index;
        }

        public String getColumnValue(String columnId) {
            Cell cell = Cell.createFromParent(this.tableComponent, index, columnId);
            return cell.getText();
        }

        @Override
        public boolean isSelected() {
            Cell cell = Cell.createRandomCell(this.tableComponent, index);
            return cell.isSelected();
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public void clickRow() {
            WebElement randomCell =
                    this.tableComponent.findElements(By.xpath(".//div[@data-row='" + this.index + "']"))
                            .stream().findAny().orElseThrow(() -> new RuntimeException("Cant find row " + this.index));
            randomCell.click();
        }

        public void selectRow() {
            if (!isSelected()) {
                Cell cell;
                if (Cell.hasCheckboxCell(this.tableComponent, index)) {
                    cell = Cell.createCheckboxCell(this.tableComponent, index);
                } else {
                    cell = Cell.createRandomCell(this.tableComponent, index);
                }
                cell.click();
            }
        }

        public void unselectRow() {
            if (isSelected()) {
                clickRow();
            }
        }

        public void callAction(String groupId, String actionId) {
            Actions actions = new Actions(driver);
            actions.moveToElement(tableComponent).build().perform();

            InlineMenu menu = InlineMenu.create(tableComponent, driver, webDriverWait);
            menu.callAction(groupId, actionId);
        }
    }

}
