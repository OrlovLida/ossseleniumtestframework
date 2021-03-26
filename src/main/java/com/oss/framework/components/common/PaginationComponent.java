package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaginationComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement paginationComponent;

    public static PaginationComponent createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        WebElement paginationComponent = null;

        return new PaginationComponent(driver, wait, paginationComponent);
    }

    private PaginationComponent(WebDriver driver, WebDriverWait wait, WebElement paginationComponent) {
        this.driver = driver;
        this.wait = wait;
        this.paginationComponent = paginationComponent;
    }

    public Integer getStep() {
        String step = this.paginationComponent.findElement(By.className("pageSize")).getText();
        return Integer.valueOf(step);
    }

    public Integer getCurrentPage() {
        WebElement pages = this.paginationComponent.findElement(By.className("pagination"));
        String currentPage = pages.findElement(By.className("active")).getText();
        return Integer.valueOf(currentPage);
    }


}
