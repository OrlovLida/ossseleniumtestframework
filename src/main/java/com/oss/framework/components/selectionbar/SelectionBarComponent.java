package com.oss.framework.components.selectionbar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectionBarComponent {

    private static final String SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH = "//span[@data-testid='selected-objects-count-label']";
    private static final String SHOW_ONLY_SELECTED_BUTTON_XPATH = "//*[@data-testid='show-selected-only-button']";
    private static final String UNSELECT_ALL_BUTTON_XPATH = "//*[@data-testid='unselect-all-button']";
    private static final String VISIBLE_SELECTION_BAR_XPATH = "//*[@class='selection-bar']";
    private static final String TOGGLER_BUTTON_XPATH = "//button[@data-testid='selection-bar-toggler-button']";
    private static final String SHOW_SELECTED_ONLY_BUTTON_ACTIVE_XPATH = "//*[@data-testid='show-selected-only-button' and text()='Show Selected']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    private SelectionBarComponent(WebDriver driver, WebDriverWait wait, String widgetId) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
    }

    public static SelectionBarComponent create(WebDriver driver, WebDriverWait wait, String widgetId) {
        return new SelectionBarComponent(driver, wait, widgetId);
    }

    public void openSelectionBar() {
        if (!isActive()) {
            driver.findElement(By.xpath("//div[@data-testid='" + widgetId + "']" + TOGGLER_BUTTON_XPATH)).click();
        }
    }

    public void hideSelectionBar() {
        if (isActive()) {
            driver.findElement(By.xpath("//div[@data-testid='" + widgetId + "']" + TOGGLER_BUTTON_XPATH)).click();
        }
    }

    public void clickUnselectAllButton() {
        if (!isActive()) {
            openSelectionBar();
        }
        driver.findElement(By.xpath("//*[@data-testid='" + widgetId + "']" + UNSELECT_ALL_BUTTON_XPATH)).click();
    }

    public void clickShowOnlySelectedButton() {
        if (isActive() && areAllObjectShown()) {
            driver.findElement(By.xpath("//*[@data-testid='" + widgetId + "']" + SHOW_ONLY_SELECTED_BUTTON_XPATH)).click();
        }
    }

    public void clickShowAllButton() {
        if (isActive() && !areAllObjectShown()) {
            driver.findElement(By.xpath("//*[@data-testid='" + widgetId + "']" + SHOW_ONLY_SELECTED_BUTTON_XPATH)).click();
        }
    }

    public String getSelectedObjectsCount() {
        if (!isActive()) {
            openSelectionBar();
        }
        return driver.findElement(By.xpath("//*[@data-testid='" + widgetId + "']" + SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH)).getText();
    }

    private boolean isActive() {
        return !driver.findElements(By.xpath("//div[@data-testid='" + widgetId + "']" + VISIBLE_SELECTION_BAR_XPATH)).isEmpty();
    }

    private boolean areAllObjectShown() {
        return !driver.findElements(By.xpath("//*[@data-testid='" + widgetId + "']" + SHOW_SELECTED_ONLY_BUTTON_ACTIVE_XPATH)).isEmpty();
    }
}
