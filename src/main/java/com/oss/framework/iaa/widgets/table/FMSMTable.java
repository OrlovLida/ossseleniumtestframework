package com.oss.framework.iaa.widgets.table;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

/**
 * @author Bartosz Nowak
 */
public class FMSMTable extends Widget {
    private static final String TABLE_ROW_XPATH = ".//div[@role='row']['table-row']";
    private static final String CELL_CONTENT = "cell__content";
    
    private FMSMTable(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        super(driver, wait, tableWidgetId);
    }
    
    public static FMSMTable createById(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        Widget.waitForWidgetById(wait, tableWidgetId);
        return new FMSMTable(driver, wait, tableWidgetId);
    }
    
    public void selectRow(int row) {
        DelayUtils.waitForPresence(webDriverWait, By.className(CELL_CONTENT));
        List<WebElement> rowElement = webElement.findElements(By.xpath(TABLE_ROW_XPATH));
        if (row >= rowElement.size()) {
            rowElement.get(rowElement.size() - 1).click();
        } else {
            rowElement.get(row).click();
        }
    }
    
    public String getCellValue(int row, String columnId) {
        Cell cell = Cell.create(webElement, row, columnId);
        return cell.getValue();
    }
    
    public void sortColumnByASC(String headerId) {
        Header.createHeader(driver, webDriverWait, webElement, headerId).openSettings().sortByASC();
    }
    
    public void sortColumnByDESC(String headerId) {
        Header.createHeader(driver, webDriverWait, webElement, headerId).openSettings().sortByDESC();
    }
    
    public void turnOffSortingForColumn(String headerId) {
        Header.createHeader(driver, webDriverWait, webElement, headerId).openSettings().turnOffSorting();
    }
    
    public Cell getCell(int row, String columnId) {
        return Cell.create(webElement, row, columnId);
    }
    
    public static class Cell {
        private static final String OSS_ICON_CLASS = ".OSSIcon";
        private static final String OSS_ICON_VALUE = "title";
        private static final String CELL_PATTERN = "//div[@" + CSSUtils.TEST_ID + "='%s']";
        
        private final WebElement cellElement;
        
        private Cell(WebElement cellElement) {
            this.cellElement = cellElement;
        }
        
        private static Cell create(WebElement tableWidget, int index, String columnNameId) {
            List<WebElement> cells = tableWidget.findElements(By.xpath(String.format(CELL_PATTERN, columnNameId)));
            return new Cell(cells.get(index));
        }
        
        public String getValue() {
            if (isIconPresent()) {
                return getAttributeValue();
            } else {
                return cellElement.getText();
            }
        }
        
        private boolean isIconPresent() {
            return !cellElement.findElements(By.cssSelector(OSS_ICON_CLASS)).isEmpty();
        }
        
        private String getAttributeValue() {
            return cellElement.findElement(By.cssSelector(OSS_ICON_CLASS)).getAttribute(OSS_ICON_VALUE);
        }
        
        public void waitForExpectedValue(WebDriverWait wait, String value) {
            if (isIconPresent()) {
                DelayUtils.sleep(2000);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@title='" + value + "']")));
            } else {
                DelayUtils.sleep(2000);
                wait.until(ExpectedConditions.textToBePresentInElement(cellElement, value));
            }
        }
    }
    
    private static class Header {
        private static final String HEADER_OPTIONS_BUTTON = "header-options-button";
        private static final String HEADER_PATTERN = ".header-container [" + CSSUtils.TEST_ID + "='%s']";
        private final WebDriver driver;
        private final WebDriverWait webDriverWait;
        private final WebElement tableWidget;
        private final String headerId;
        
        private Header(WebDriver driver, WebDriverWait webDriverWait, WebElement tableWidget, String headerId) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.tableWidget = tableWidget;
            this.headerId = headerId;
        }
        
        private static Header createHeader(WebDriver driver, WebDriverWait webDriverWait, WebElement tableWidget, String headerId) {
            return new Header(driver, webDriverWait, tableWidget, headerId);
        }
        
        private static WebElement getHeader(WebElement parent, String headerId) {
            return parent.findElement(By.cssSelector(String.format(HEADER_PATTERN, headerId)));
        }
        
        private HeaderSettings getHeaderSettings() {
            return HeaderSettings.createHeaderSettings(driver, webDriverWait);
        }
        
        private HeaderSettings openSettings() {
            WebElement header = getHeader(tableWidget, headerId);
            WebElementUtils.moveToElement(driver, header);
            WebElement settingsButton =
                    header.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, HEADER_OPTIONS_BUTTON)));
            WebElementUtils.clickWebElement(driver, settingsButton);
            return getHeaderSettings();
        }
    }
    
    private static class HeaderSettings {
        private static final String COLUMN_PANEL_SETTINGS_CSS = ".options-menu-content";
        private static final String ASCENDING = "ASCENDING";
        private static final String DESCENDING = "DESCENDING";
        private static final String NONE = "NONE";
        private static final String VALUE_CSS_ATTRIBUTE = "value";
        private static final String INPUT_TAG_CSS = "input";
        private static final String RADIO_BUTTON_CSS = ".radio";
        private static final String LABEL_TAG_CSS = "label";
        private final WebElement headerSettingsElement;
        
        private HeaderSettings(WebElement webElement) {
            this.headerSettingsElement = webElement;
        }
        
        private static HeaderSettings createHeaderSettings(WebDriver driver, WebDriverWait webDriverWait) {
            DelayUtils.waitBy(webDriverWait, By.cssSelector(COLUMN_PANEL_SETTINGS_CSS));
            WebElement webElement = driver.findElement(By.cssSelector(COLUMN_PANEL_SETTINGS_CSS));
            return new HeaderSettings(webElement);
        }
        
        private void sortByASC() {
            clickRadioButton(ASCENDING);
        }
        
        private void sortByDESC() {
            clickRadioButton(DESCENDING);
        }
        
        private void turnOffSorting() {
            clickRadioButton(NONE);
        }
        
        private void clickRadioButton(String sortType) {
            Optional<WebElement> radioButton = headerSettingsElement.findElements(By.cssSelector(RADIO_BUTTON_CSS)).stream()
                    .filter(radio -> radio.findElement(By.cssSelector(INPUT_TAG_CSS)).getAttribute(VALUE_CSS_ATTRIBUTE).equals(sortType))
                    .findFirst();
            radioButton.ifPresent(element -> element.findElement(By.cssSelector(LABEL_TAG_CSS)).click());
        }
        
    }
}