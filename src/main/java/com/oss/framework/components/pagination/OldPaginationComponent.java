package com.oss.framework.components.pagination;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class OldPaginationComponent extends PaginationComponent {

    private static final String PAGINATION_CSS = ".OSSPagination";
    private static final String DATA_COUNT_SELECTOR = ".rowsCounter > span:nth-child(4)";
    private static final String NEXT_PAGE_SELECTOR = ".pagination > li:last-child";
    private static final String PREV_PAGE_SELECTOR = ".pagination > li:first-child";
    private static final String FIRST_PAGE_SELECTOR = ".pagination > li:nth-child(2)";

    private final WebElement paginationComponentElement;

    private OldPaginationComponent(WebElement paginationComponentElement) {
        super(paginationComponentElement);
        this.paginationComponentElement = paginationComponentElement;
    }

    public static OldPaginationComponent createFromParent(WebElement parent) {
        WebElement paginationComponent = parent.findElement(By.cssSelector(PAGINATION_CSS));
        return new OldPaginationComponent(paginationComponent);
    }

    public void goOnNextPage() {
        getElement(NEXT_PAGE_SELECTOR).click();
    }

    public void goOnPrevPage() {
        getElement(PREV_PAGE_SELECTOR).click();
    }

    public void goOnFirstPage() {
        getElement(FIRST_PAGE_SELECTOR).click();
    }

    public int getTotalCount() {
        String totalCount = getElement(DATA_COUNT_SELECTOR).getText();
        return Integer.parseInt(totalCount);
    }

    public boolean isNextPageButtonPresent() {
        return isButtonPresent(getElement(NEXT_PAGE_SELECTOR));
    }

    public boolean isPreviousPageButtonPresent() {
        return isButtonPresent(getElement(PREV_PAGE_SELECTOR));
    }

    public boolean isFirstPageButtonPresent() {
        return isButtonPresent(getElement(FIRST_PAGE_SELECTOR));
    }

    private WebElement getElement(String cssSelector) {
        return this.paginationComponentElement.findElement(By.cssSelector(cssSelector));
    }
}
