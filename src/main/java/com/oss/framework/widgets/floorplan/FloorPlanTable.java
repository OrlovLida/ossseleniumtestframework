package com.oss.framework.widgets.floorplan;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class FloorPlanTable {
    private static final String TABLE_ROWS = ".//div[@class='grid-canvas']/div[contains(@class, 'slick-row')]";
    private static final String CELL_PATH = ".//div[contains(@class, 'slick-cell')]";
    private static final String CELL_IN_ROW_PATTERN = ".//div[contains(@class, 'slick-cell')][%d]/input";

    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final String id;

    private FloorPlanTable(WebDriver driver, WebDriverWait wait, String id) {
        this.driver = driver;
        this.webDriverWait = wait;
        this.id = id;
    }

    public static FloorPlanTable createById(WebDriver driver, WebDriverWait wait, String id) {
        return new FloorPlanTable(driver, wait, id);
    }

    public int getRowNr(String rowName) {
        List<WebElement> rows = getTableRows();
        for (WebElement row : rows) {
            WebElement cell = row.findElement(By.xpath(CELL_PATH));
            if (cell.getText().equals(rowName)) {
                return rows.indexOf(row);
            }
        }
        throw new NoSuchElementException("Cannot find a row with the provided value");
    }

    public void checkNthRowAndNthColumn(int rowNr, int columnNr) {
        String cellInRow = String.format(CELL_IN_ROW_PATTERN, columnNr);
        WebElement cell = getTableRows().get(rowNr).findElement(By.xpath(cellInRow));
        if (cell.isDisplayed() && !cell.isSelected()) {
            cell.click();
        }
    }

    private List<WebElement> getTableRows() {
        DelayUtils.waitByXPath(webDriverWait, TABLE_ROWS);
        return driver.findElements(By.xpath(TABLE_ROWS));
    }
}
