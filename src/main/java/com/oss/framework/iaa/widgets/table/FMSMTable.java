package com.oss.framework.iaa.widgets.table;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

/**
 * @author Bartosz Nowak
 */
public class FMSMTable extends Widget {
    private static final String TABLE_ROW_XPATH = "//div[@role='row']['table-row']";
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

    private static class Cell {
        private static final String OSS_ICON_CLASS = "OSSIcon";
        private static final String OSS_ICON_CLASS_XPATH = "//i[contains(@class, '" + OSS_ICON_CLASS + "')]";
        private static final String OSS_ICON_VALUE = "title";
        private static final String OSS_ICON_VALUE_XPATH = ".//span[@" + OSS_ICON_VALUE + "]";
        private static final String CELL_PATTERN = "//div[@" + CSSUtils.TEST_ID + "='%s']";

        private final WebElement cellElement;

        private Cell(WebElement cellElement) {
            this.cellElement = cellElement;
        }

        private static Cell create(WebElement tableWidget, int index, String columnNameId) {
            List<WebElement> cells = tableWidget.findElements(By.xpath(String.format(CELL_PATTERN, columnNameId)));
            return new Cell(cells.get(index));
        }

        private String getValue() {
            if (isIconPresent()) {
                return getAttributeValue();
            } else {
                return cellElement.getText();
            }
        }

        private boolean isIconPresent() {
            return !cellElement.findElements(By.xpath(OSS_ICON_VALUE_XPATH + OSS_ICON_CLASS_XPATH)).isEmpty();
        }

        private String getAttributeValue() {
            return cellElement.findElement(By.xpath(OSS_ICON_VALUE_XPATH)).getAttribute(OSS_ICON_VALUE);
        }
    }
}
