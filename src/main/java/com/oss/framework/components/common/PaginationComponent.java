package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaginationComponent {
    private static final String PAGINATION_XPATH = ".//div[contains(@class, 'OSSPagination')]";
    private static final String PAGE_SIZE_OPTIONS_VALUE_XPATH =
            ".//div[contains(@class, 'pageSizeOptions')]//span[contains(@class, 'pageSize')]";

    private static final String DATA_COUNT_SELECTOR = ".rowsCounter > span:last-child";
    private static final String RAGE_OF_ROWS_SELECTOR = ".rowsCounter > span:nth-child(2)";

    private static final String NEXT_PAGE_SELECTOR = ".pagination > span:last-child";
    private static final String PREV_PAGE_SELECTOR = ".pagination > span:nth-child(2)";
    private static final String FIRST_PAGE_SELECTOR = ".pagination > span:first-child";
    private static final String PAGE_SIZE_CLASS = "pageSize";
    private static final String DROPDOWN_PAGINATION_OPTION_CLASS = "dropdown-toggle";
    private static final String PAGE_OPTION_NUMBER_CLASS = "pageOption";
    private static final String CANNOT_FIND_PAGINATION_OPTION_EXCEPTION = "Cannot find pagination option";
    private static final String PAGINATION_DROPDOWN_XPATH = ".//ul[@class= 'dropdown-menu show']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement paginationComponentElement;

    private PaginationComponent(WebDriver driver, WebDriverWait wait, WebElement paginationComponentElement) {
        this.driver = driver;
        this.wait = wait;
        this.paginationComponentElement = paginationComponentElement;
    }

    public static PaginationComponent createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        WebElement paginationComponent = parent.findElement(By.xpath(PAGINATION_XPATH));
        return new PaginationComponent(driver, wait, paginationComponent);
    }

    public int getStep() {
        String step = this.paginationComponentElement.findElement(By.xpath(PAGE_SIZE_OPTIONS_VALUE_XPATH)).getText();
        return Integer.parseInt(step);
    }

    public boolean isNextPageButtonPresent() {
        return isButtonPresent(getNextPageBtn());
    }

    public boolean isPreviousPageButtonPresent() {
        return isButtonPresent(getPrevPageBtn());
    }

    public boolean isFirstPageButtonPresent() {
        return isButtonPresent(getFirstPageBtn());
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
        String totalCount = this.paginationComponentElement.findElement(By.cssSelector(DATA_COUNT_SELECTOR)).getText();
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
        String pageSize = paginationComponentElement.findElement(By.className(PAGE_SIZE_CLASS)).getText();
        return Integer.parseInt(pageSize);
    }

    public void changeRowsCount(int pageSizeOption) {
        openSizeOption();
        getPageOption(pageSizeOption).click();
    }

    private String getRageOfRows() {
        return this.paginationComponentElement.findElement(By.cssSelector(RAGE_OF_ROWS_SELECTOR)).getText();
    }

    private boolean isButtonPresent(WebElement element) {
        String cssAttribute = "class";
        String classDisabled = "disabled";
        return !element.getAttribute(cssAttribute).contains(classDisabled);
    }

    private WebElement getNextPageBtn() {
        return this.paginationComponentElement.findElement(By.cssSelector(NEXT_PAGE_SELECTOR));
    }

    private WebElement getPrevPageBtn() {
        return this.paginationComponentElement.findElement(By.cssSelector(PREV_PAGE_SELECTOR));
    }

    private WebElement getFirstPageBtn() {
        return this.paginationComponentElement.findElement(By.cssSelector(FIRST_PAGE_SELECTOR));
    }

    private void openSizeOption() {
        if (!isSizeOptionOpen()) {
            paginationComponentElement.findElement(By.className(DROPDOWN_PAGINATION_OPTION_CLASS)).click();
        }
    }

    private boolean isSizeOptionOpen() {
        return !paginationComponentElement.findElements(By.xpath(PAGINATION_DROPDOWN_XPATH)).isEmpty();

    }

    private WebElement getPageOption(int pageSizeOption) {
        return paginationComponentElement.findElements(By.className(PAGE_OPTION_NUMBER_CLASS)).stream()
                .filter(pageOption -> pageOption.getText().equals(String.valueOf(pageSizeOption))).findFirst()
                .orElseThrow(() -> new RuntimeException(CANNOT_FIND_PAGINATION_OPTION_EXCEPTION));
    }
}
