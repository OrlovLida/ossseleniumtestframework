package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaginationComponent {
    private static final String PAGINATION_XPATH = ".//div[contains(@class, 'OSSPagination')]";
    private static final String PAGE_SIZE_OPTIONS_VALUE_XPATH = ".//div[contains(@class, 'pageSizeOptions')]//span[contains(@class, 'pageSize')]";

    private static final String DATA_COUNT_SELECTOR = ".rowsCounter > span:last-child";
    private static final String RAGE_OF_ROWS_SELECTOR = ".rowsCounter > span:nth-child(2)";

    private static final String NEXT_PAGE_SELECTOR = ".pagination > span:last-child";
    private static final String PREV_PAGE_SELECTOR = ".pagination > span:nth-child(2)";
    private static final String FIRST_PAGE_SELECTOR = ".pagination > span:first-child";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement paginationComponent;

    public static PaginationComponent createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        WebElement paginationComponent = parent.findElement(By.xpath(PAGINATION_XPATH));
        return new PaginationComponent(driver, wait, paginationComponent);
    }

    private PaginationComponent(WebDriver driver, WebDriverWait wait, WebElement paginationComponent) {
        this.driver = driver;
        this.wait = wait;
        this.paginationComponent = paginationComponent;
    }

    private String getRageOfRows() {
        return this.paginationComponent.findElement(By.cssSelector(RAGE_OF_ROWS_SELECTOR)).getText();
    }

    public int getStep() {
        String step = this.paginationComponent.findElement(By.xpath(PAGE_SIZE_OPTIONS_VALUE_XPATH)).getText();
        return Integer.parseInt(step);
    }

    private boolean isBtnEnabled(WebElement element) {
        String cssAttribute = "class";
        String classDisabled = "disabled";

        return !element.getAttribute(cssAttribute).contains(classDisabled);
    }

    private WebElement getNextPageBtn() {
        return this.paginationComponent.findElement(By.cssSelector(NEXT_PAGE_SELECTOR));
    }

    private WebElement getPrevPageBtn() {
        return this.paginationComponent.findElement(By.cssSelector(PREV_PAGE_SELECTOR));
    }

    private WebElement getFirstPageBtn() {
        return this.paginationComponent.findElement(By.cssSelector(FIRST_PAGE_SELECTOR));
    }

    public boolean isNextPageBtnEnabled() {
        return isBtnEnabled(getNextPageBtn());
    }

    public boolean isPrevPageBtnEnabled() {
        return isBtnEnabled(getPrevPageBtn());
    }

    public boolean isFirstPageBtnEnabled() {
        return isBtnEnabled(getFirstPageBtn());
    }

    public void goOnNextPage() {
        getNextPageBtn().click();
    }

    public void goOnPrevPage() {
        getPrevPageBtn().click();
    }

    public void goOnFirstPage() {
        getFirstPageBtn().click();
    }

    public int getTotalCount() {
        String totalCount = this.paginationComponent.findElement(By.cssSelector(DATA_COUNT_SELECTOR)).getText();
        return Integer.parseInt(totalCount);
    }

    public int getBottomRageOfRows() {
        String range = getRageOfRows();
        String topRage = range.split("-")[0].trim();
        return Integer.parseInt(topRage);
    }

    public int getTopRageOfRows() {
        String range = getRageOfRows();
        String topRage = range.split("-")[1].trim();
        return Integer.parseInt(topRage);
    }

    public int getRowsCount() {
        return 50;
    }
}
