package com.oss.framework.components.table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.oss.framework.components.alerts.ComponentMessage;
import com.oss.framework.components.alerts.ElementMessage;
import com.oss.framework.components.alerts.Message;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.attributechooser.ListAttributesChooser;
import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.components.icons.interactiveicons.InteractiveIcon;
import com.oss.framework.components.icons.interactiveicons.InteractiveIconFactory;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.scrolls.CustomScrolls;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.DragAndDrop.DraggableElement;
import com.oss.framework.utils.DragAndDrop.DropElement;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.table.TableRow;

import static com.oss.framework.utils.WebElementUtils.clickWebElement;
import static com.oss.framework.utils.WebElementUtils.isElementPresent;
import static com.oss.framework.utils.WebElementUtils.moveToElement;

public class TableComponent {
    private static final String HEADERS_XPATH = ".//div[@class='sticky-table__header']/div";
    private static final String EMPTY_DATA_ROW_XPATH = ".//div[contains(@class, 'empty_data_row')]";
    private static final String HEADER_CLASS = "table-component__header";
    private static final String TABLE_COMPONENT_CLASS = "table-component";
    private static final String DATA_ROW = "data-row";
    private static final String DATA_COL = "data-col";
    private static final String NOT_CELL_CHECKBOX_CSS = ":not(.table-component__cell__checkbox)";
    private static final String CELL_ROW_PATTERN = "[" + DATA_ROW + "='%s']";
    private static final String CONTEXT_ACTIONS_CELL = "[" + CSSUtils.TEST_ID + "= 'cell-row-%s-col-contextActions']";
    private static final String TABLE_CONTENT_CSS = ".sticky-table__content";
    private static final String TABLE_COMPONENT_PATTERN = "[" + CSSUtils.TEST_ID + "= '%s'] ." + TABLE_COMPONENT_CLASS;
    private static final String TABLE_COMPONENT_ID_PATTERN = "[" + CSSUtils.TEST_ID + "= '%s']." + TABLE_COMPONENT_CLASS;
    private static final String CELLS_IN_COLUMN_PATTERN = ".table-component__cell[data-col='%s']";
    private static final String COLUMN_MANAGER_BUTTON = ".table-component__management-btn button";
    private static final By ATTRIBUTES_CHOOSER_OPENED = By.xpath("//div[@id='attributes-management']");
    private static final String CUSTOM_SCROLLBARS_CSS = ".custom-scrollbars";

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement webElement;
    private final WebElement parent;

    private PaginationComponent paginationComponent;

    private TableComponent(WebDriver driver, WebDriverWait webDriverWait, WebElement component, WebElement parent) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = component;
        this.parent = parent;
    }

    public static TableComponent create(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        DelayUtils.waitBy(webDriverWait,
                By.cssSelector(String.format(TABLE_COMPONENT_PATTERN, widgetId) + " " + TABLE_CONTENT_CSS));
        WebElement webElement = driver.findElement(By.cssSelector(String.format(TABLE_COMPONENT_PATTERN, widgetId)));
        WebElement parent = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(widgetId)));
        return new TableComponent(driver, webDriverWait, webElement, parent);
    }

    public static TableComponent createById(WebDriver driver, WebDriverWait webDriverWait, String tableComponentId) {
        DelayUtils.waitBy(webDriverWait,
                By.cssSelector(String.format(TABLE_COMPONENT_ID_PATTERN, tableComponentId) + " " + TABLE_CONTENT_CSS));
        WebElement webElement = driver.findElement(By.cssSelector(String.format(TABLE_COMPONENT_ID_PATTERN, tableComponentId)));
        WebElement parent = webElement.findElement(By.xpath(".."));
        return new TableComponent(driver, webDriverWait, webElement, parent);
    }

    public void selectRow(int index) {
        Row row = getRow(index);
        row.selectRow();
    }

    public void clickRow(int index) {
        Row row = getRow(index);
        row.clickRow();
    }

    public void clickLink(int index, String columnId) {
        Cell cell = Cell.createFromParent(driver, webElement, index, columnId);
        cell.clickLink();
    }

    public void selectAll() {
        Header.getHeader(webElement, Cell.CHECKBOX_COLUMN_ID).click();
    }

    public Boolean isHeaderCheckboxSelected() {
        return Header.getHeader(webElement, Cell.CHECKBOX_COLUMN_ID).findElement(By.xpath(".//input")).isSelected();
    }

    public void unselectRow(int index) {
        Row row = getRow(index);
        row.unselectRow();
    }

    public boolean hasNoData() {
        return WebElementUtils.isElementPresent(webElement, By.xpath(EMPTY_DATA_ROW_XPATH));
    }

    public Cell getCell(int index, String columnId) {
        return Cell.createFromParent(driver, webElement, index, columnId);
    }

    private Optional<Integer> getRowIndex(String value, String columnId) {
        Optional<Cell> cell = getCells(columnId).stream().filter(c -> c.getText().equals(value)).findFirst();
        return cell.map(Cell::getIndex);
    }

    private Optional<Integer> getRowIndexContains(String value, String columnId) {
        Optional<Cell> cell = getCells(columnId).stream().filter(c -> c.getText().contains(value)).findFirst();
        return cell.map(Cell::getIndex);
    }

    private List<Cell> getCells(String columnId) {
        return webElement.findElements(By.cssSelector(String.format(CELLS_IN_COLUMN_PATTERN, columnId))).stream()
                .map(cell -> Cell.createCell(driver, cell)).collect(Collectors.toList());
    }

    public Row getRow(String value, String columnId) {
        Optional<Integer> rowIndex = getRowIndex(value, columnId);
        if (rowIndex.isPresent()) {
            return getRow(rowIndex.get());
        }
        throw new IllegalStateException("Cannot find Row with provided value and columnId");
    }

    public List<TableRow> getVisibleRows() {
        String firstColumn = getColumnIds().stream().findFirst().orElse("");
        String xpath = ".//div[@data-col='" + firstColumn + "']";
        List<Integer> rowIds = this.webElement
                .findElements(By.xpath(xpath))
                .stream().filter(e -> e.getAttribute(DATA_COL).equals(firstColumn)).filter(e -> e.getAttribute(DATA_ROW) != null)
                .map(e -> e.getAttribute(DATA_ROW))
                .map(Integer::parseInt).sorted().collect(Collectors.toList());

        return rowIds.stream().map(index -> new Row(this.driver, this.webDriverWait, this.webElement, index)).collect(Collectors.toList());
    }

    public boolean isValuePresent(String value, String columnId) {
        return getRowIndex(value, columnId).isPresent();
    }

    public boolean isValuePresentContains(String value, String columnId) {
        return getRowIndexContains(value, columnId).isPresent();
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

    public void setLinkPattern(String columnId, String linkPattern) {
        getHeaderByIndex(columnId).openSettings().setLinkPattern(linkPattern);
    }

    public String getDefaultColumnWidth(String columnId) {
        getHeaderByIndex(columnId).openSettings();
        return getHeaderByIndex(columnId).getHeaderSettings().getDefaultColumnWidth();
    }

    public void resizeColumn(String columnId, int size) {
        Header header = Header.createHeader(this.driver, this.webDriverWait, this.webElement, columnId);
        header.resize(size);
    }

    public String getCellValue(int row, String columnId) {
        Cell cell = Cell.createFromParent(driver, webElement, row, columnId);
        return cell.getText();
    }

    public void setCellValue(int row, String columnId, String value) {
        Cell cell = Cell.createFromParent(driver, webElement, row, columnId);
        cell.setValue(value);
    }

    public boolean isCellValueBold(int row, String columnId) {
        Cell cell = Cell.createFromParent(driver, webElement, row, columnId);
        return cell.isBold();
    }

    public Optional<Message> getCellMessage(int row, String columnId) {
        Cell cell = Cell.createFromParent(driver, webElement, row, columnId);
        return cell.getMessage();
    }

    public AttributesChooser getAttributesChooser() {
        if (!isElementPresent(driver, ATTRIBUTES_CHOOSER_OPENED)) {
            clickWebElement(driver, getColumnsManagement());
        }
        return AttributesChooser.create(this.driver, this.webDriverWait);
    }

    public ListAttributesChooser getListAttributesChooser() {
        if (!isElementPresent(driver, ATTRIBUTES_CHOOSER_OPENED)) {
            clickWebElement(driver, getColumnsManagement());
        }
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
        DropElement targetHeader = headers.get(position).getDropElement();
        DragAndDrop.dragAndDrop(sourceHeader.getDragElement(), targetHeader, driver);
        targetHeader.waitUntilElementRecalculate(webDriverWait);
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
            paginationComponent = PaginationComponent.createFromParent(parent);
        }
        return paginationComponent;
    }

    public void scrollToFirstRow() {
        if (!isScrollPresent()) {
            return;
        }
        CustomScrolls scrolls = getCustomScrolls();
        if (scrolls.getVerticalBarHeight() == 0) {
            return;
        }
        int translateY = scrolls.getTranslateYValue();
        if (translateY == 0) {
            return;
        }
        scrolls.scrollVertically(-translateY);
    }

    public List<Message> getMessages() {
        return ComponentMessage.create(driver, webElement.getAttribute(CSSUtils.TEST_ID)).getMessages();
    }

    private CustomScrolls getCustomScrolls() {
        return CustomScrolls.create(driver, webDriverWait, webElement);
    }

    private boolean isScrollPresent() {
        return WebElementUtils.isElementPresent(webElement, By.cssSelector(CUSTOM_SCROLLBARS_CSS));
    }

    private WebElement getColumnsManagement() {
        return webElement.findElement(By.cssSelector(COLUMN_MANAGER_BUTTON));
    }

    public Row getRow(int index) {
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
        int translateXValue = scrolls.getTranslateXValue();
        int diff = (scrollWidth.toBigInteger().intValue() - contentVisibleWidth) - translateXValue;

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
        private static final String RESIZE_XPATH = "[" + CSSUtils.TEST_ID + "='col-%s-resizer']";
        private static final String SETTINGS_XPATH = "[" + CSSUtils.TEST_ID + "='col-%s-settings']";

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
            String columnId = CSSUtils.getAttributeValue(DATA_COL, wrapper);
            String label = wrapper.getText();
            return new Header(driver, webDriverWait, tableComponent, columnId, label);
        }

        private static WebElement getHeader(WebElement parent, String columnId) {
            return parent.findElement(By.xpath(".//div[contains(@class, '" + HEADER_CLASS + "') and @data-col='" + columnId + "']"));
        }

        public void resize(int offset) {
            WebElement resize = getHeader(tableComponent, columnId).findElement(By.cssSelector(getResizeCss()));
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
            getHeader(tableComponent, columnId).findElement(By.cssSelector(getSettingsCss())).click();
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

        private String getResizeCss() {
            return String.format(RESIZE_XPATH, columnId);
        }

        private String getSettingsCss() {
            return String.format(SETTINGS_XPATH, columnId);
        }
    }

    private static class HeaderSettings {
        private static final String COLUMN_PANEL_SETTINGS_XPATH = "//*[contains(@class, 'column-panel-settings')]";
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

        public String getDefaultColumnWidth() {
            selectAdministrationTab();
            Input input = ComponentFactory.create(SIZE_DEFAULT_INPUT_ID, Input.ComponentType.TEXT_FIELD, driver, webDriverWait);
            return input.getValue().getStringValue();
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

        private void setLinkPattern(String linkPattern) {
            selectAdministrationTab();
            ComponentFactory.create("link_pattern", Input.ComponentType.TEXT_FIELD, driver, webDriverWait).setSingleStringValue(linkPattern);
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
        private static final String TREE_NODE_EXPAND_CSS = ".tree-node-expand";
        private static final String CELL_DOESN_T_HAVE_EXPAND_ICON_EXCEPTION = "Cell doesn't have expand icon";
        private static final String CELL_PATTERN = "[" + DATA_ROW + "='%s'][" + DATA_COL + "='%s']";
        private static final String PLUS_ICON_CSS = ".OSSIcon";
        private static final String ARIA_LABEL_ATTRIBUTE = "aria-label";
        private static final String MINUS = "MINUS";
        private static final String ADD = "ADD";
        private static final String TREE_NODE_EXPAND_DISABLED_CSS = TREE_NODE_EXPAND_CSS + "--disabled";
        private static final String LONG_TEXT_WRAPPER_CSS = ".long-text__wrapper";
        private static final String TEXT_CONTENT = "textContent";
        private static final String LINK_IS_NOT_AVAILABLE_EXCEPTION = "Link is not available";
        private static final String A_HREF_CSS = "span a[href]";
        private static final String CELL_EDIT_BUTTON_PATTERN = "[data-testid='%s-btn-cells-edit']";
        private static final String INLINE_EDITOR_INPUT_ID = "-inline-editor-input";
        private static final String SAVE_BUTTON_INLINE_EDITOR_CSS = "[data-testid='save-inline-editor']";
        private static final String CELL_IS_NOT_EDITABLE_EXCEPTION = "Cell is not editable";
        private static final String CELL_DOESN_T_HAVE_MESSAGE_TEXT_EXCEPTION = "Cell doesn't have Message Text";

        private final WebDriver driver;
        private final WebElement cellElement;
        private final int index;
        private final String columnId;

        private Cell(WebDriver driver, WebElement cellElement, int index, String columnId) {
            this.driver = driver;
            this.cellElement = cellElement;
            this.index = index;
            this.columnId = columnId;
        }

        private static boolean hasCheckboxCell(WebElement tableComponent, int index) {
            return tableComponent
                    .findElements(By.cssSelector(String.format(CELL_PATTERN, index, CHECKBOX_COLUMN_ID)))
                    .stream().findAny().isPresent();
        }

        private static Cell createCell(WebDriver driver, WebElement cell) {
            int index = Integer.parseInt(cell.getAttribute(DATA_ROW));
            String columnId = cell.getCssValue(DATA_COL);
            return new Cell(driver, cell, index, columnId);
        }

        private static Cell createCheckboxCell(WebDriver driver, WebElement tableComponent, int index) {
            return createFromParent(driver, tableComponent, index, CHECKBOX_COLUMN_ID);
        }

        private static Cell createFromParent(WebDriver driver, WebElement tableComponent, int index, String columnId) {
            WebElement cell = tableComponent.findElements(By.cssSelector(String.format(CELL_PATTERN, index, columnId)))
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Cant find cell: rowId " + index + " columnId: " + columnId));
            return new Cell(driver, cell, index, columnId);
        }

        private static Cell createRandomCell(WebDriver driver, WebElement tableComponent, int index) {
            WebElement randomCell =
                    tableComponent.findElements(By.cssSelector(String.format(CELL_ROW_PATTERN, index)))
                            .stream().findAny().orElseThrow(() -> new RuntimeException("Cant find row " + index));
            String columnId = CSSUtils.getAttributeValue(DATA_COL, randomCell);
            return new Cell(driver, randomCell, index, columnId);
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

        public boolean isBold() {
            return CSSUtils.getStyleAttribute(cellElement.findElement(By.xpath("./div"))).containsValue("bold");
        }

        public String getText() {
            if (isCheckBox()) {
                if (isSelected()) {
                    return "true";
                }
                return "false";
            }
            return cellElement.findElement(By.cssSelector(LONG_TEXT_WRAPPER_CSS)).getAttribute(TEXT_CONTENT);
        }

        public InteractiveIcon<?> getInteractiveIcon() {
            return InteractiveIconFactory.create(driver, cellElement);
        }

        private int getIndex() {
            return this.index;
        }

        public void click() {
            WebElementUtils.clickWebElement(driver, cellElement);
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

        private void expandCell() {
            if (!isCellExpanded()) {
                toggleCell(MINUS);
            } else
                throw new IllegalStateException(CELL_DOESN_T_HAVE_EXPAND_ICON_EXCEPTION);
        }

        private void collapseCell() {
            if (isCellExpanded()) {
                toggleCell(ADD);
            } else
                throw new IllegalStateException(CELL_DOESN_T_HAVE_EXPAND_ICON_EXCEPTION);
        }

        private void toggleCell(String character) {
            WebElement expandIcon = cellElement.findElement(By.cssSelector(TREE_NODE_EXPAND_CSS));
            WebElementUtils.clickWebElement(driver, expandIcon);
            DelayUtils.waitForNestedElements(new WebDriverWait(driver, Duration.ofSeconds(20)), expandIcon,
                    By.cssSelector("[" + ARIA_LABEL_ATTRIBUTE + "='" + character + "']"));
        }

        private boolean isExpandPresent() {
            if (cellElement.findElements(By.cssSelector(TREE_NODE_EXPAND_DISABLED_CSS)).isEmpty()) {
                return (!cellElement.findElements(By.cssSelector(TREE_NODE_EXPAND_CSS)).isEmpty());
            }
            return false;
        }

        private boolean isCellExpanded() {
            if (isExpandPresent()) {
                return cellElement.findElement(By.cssSelector(PLUS_ICON_CSS)).getAttribute(ARIA_LABEL_ATTRIBUTE).equals(MINUS);
            }
            throw new IllegalStateException(CELL_DOESN_T_HAVE_EXPAND_ICON_EXCEPTION);
        }

        private boolean isSelected() {
            List<String> classes = CSSUtils.getAllClasses(cellElement);
            return classes.contains(SELECTED_CLASS);
        }

        private boolean isCheckBox() {
            return CHECKBOX_COLUMN_ID.equals(columnId);
        }

        private void clickLink() {
            Optional<WebElement> cellLink = cellElement.findElements(By.cssSelector(A_HREF_CSS)).stream()
                    .findFirst();
            if (cellLink.isPresent()) {
                cellLink.get().click();
            } else
                throw new NoSuchElementException(LINK_IS_NOT_AVAILABLE_EXCEPTION);
        }

        private void setValue(String value) {
            openInlineEditor();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            ComponentFactory.create(columnId + INLINE_EDITOR_INPUT_ID, driver, wait).setSingleStringValue(value);
            driver.findElement(By.cssSelector(SAVE_BUTTON_INLINE_EDITOR_CSS)).click();
        }

        private void openInlineEditor() {
            moveToElement(driver, cellElement);
            cellElement.findElements(By.cssSelector(String.format(CELL_EDIT_BUTTON_PATTERN, columnId))).stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(CELL_IS_NOT_EDITABLE_EXCEPTION))
                    .click();
        }

        private Optional<Message> getMessage() {
            if (driver.findElements(By.cssSelector("[" + CSSUtils.DATA_PARENT_TEST_ID + "='" + cellElement.getAttribute(CSSUtils.TEST_ID) + "']")).isEmpty()) {
                return Optional.empty();
            }
            List<String> messageTypeClasses = CSSUtils.getAllClasses(cellElement);
            return Optional.of(Message.create(getMessageText(), messageTypeClasses));
        }

        private String getMessageText() {
            return ElementMessage.create(driver, cellElement.getAttribute(CSSUtils.TEST_ID)).getMessagesText().stream()
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(CELL_DOESN_T_HAVE_MESSAGE_TEXT_EXCEPTION));
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
            Cell cell = Cell.createFromParent(driver, this.tableComponent, index, columnId);
            return cell.getText();
        }

        @Override
        public boolean isSelected() {
            Cell cell = Cell.createRandomCell(driver, this.tableComponent, index);
            return cell.isSelected();
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public void clickRow() {
            Actions actions = new Actions(driver);
            WebElement randomCell = this.tableComponent
                    .findElements(By.cssSelector(String.format(CELL_ROW_PATTERN, index) + NOT_CELL_CHECKBOX_CSS))
                    .stream().findAny()
                    .orElseThrow(() -> new RuntimeException("Cant find row " + this.index));
            actions.moveToElement(randomCell).click(randomCell).build().perform();
        }

        public void selectRow() {
            if (!isSelected()) {
                Cell cell = getCell();
                cell.click();
            }
        }

        public void expandRow() {
            getFirstCell().expandCell();
        }

        public void collapseRow() {
            getFirstCell().collapseCell();
        }

        public boolean isRowExpanded() {
            return getFirstCell().isCellExpanded();
        }

        public boolean isExpandPresent() {
            return getFirstCell().isExpandPresent();
        }

        private Cell getFirstCell() {
            List<Cell> cells =
                    tableComponent.findElements(By.cssSelector(String.format(CELL_ROW_PATTERN, index) + NOT_CELL_CHECKBOX_CSS)).stream()
                            .map(cell -> Cell.createCell(driver, cell))
                            .collect(Collectors.toList());
            return cells.get(0);
        }

        private Cell getCell() {
            Cell cell;
            if (Cell.hasCheckboxCell(this.tableComponent, index)) {
                cell = Cell.createCheckboxCell(driver, this.tableComponent, index);
            } else {
                cell = Cell.createRandomCell(driver, this.tableComponent, index);
            }
            return cell;
        }

        public void unselectRow() {
            if (isSelected()) {
                Cell cell = getCell();
                cell.click();
            }
        }

        public void callAction(String groupId, String actionId) {
            WebElement action = getContextActionsCellElement();
            moveToElement(driver, action);
            InlineMenu.create(action, driver, webDriverWait).callAction(groupId, actionId);
        }

        public void callAction(String actionId) {
            WebElement action = getContextActionsCellElement();
            moveToElement(driver, action);
            InlineMenu.create(action, driver, webDriverWait).callAction(actionId);
        }

        private WebElement getContextActionsCellElement() {
            return tableComponent.findElement(By.cssSelector(String.format(CONTEXT_ACTIONS_CELL, index)));
        }
    }
}


