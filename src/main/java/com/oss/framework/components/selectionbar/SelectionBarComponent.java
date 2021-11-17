package com.oss.framework.components.selectionbar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectionBarComponent {

    private static final String SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH = "//span[@data-testid='selected-objects-count-label']";
    private static final String SHOW_ONLY_SELECTED_BUTTON_XPATH = ".//*[@data-testid='show-selected-only-button']";
    private static final String UNSELECT_ALL_BUTTON_XPATH = ".//*[@data-testid='unselect-all-button']";
    private static final String DATA_TESTID_SELECTION_BAR_XPATH = "//*[@data-testid='selection-bar']";
    private static final String SELECTION_BAR_ACTIVE_XPATH = "//*[@class='selection-bar-toggler active']";
    private static final String TOGGLER_BUTTON_XPATH = "//button[@data-testid='selection-bar-toggler-button']";
    private static final String SHOW_SELECTED_ONLY_BUTTON_ACTIVE_XPATH = "*//*[@data-testid='show-selected-only-button' and text()='Show Selected']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String widgetId;

    public static SelectionBarComponent create(WebDriver driver, WebDriverWait wait, String widgetId) {
        WebElement selectionBar = getSelectionBarComponent(driver, widgetId);
        return new SelectionBarComponent(driver, wait, selectionBar, widgetId);
    }

    private static WebElement getSelectionBarComponent(WebDriver driver, String widgetId) {
        String selectionBarXpath = getSelectionBarInWidgetXpath(widgetId);
        try {
            return driver.findElement(By.xpath(selectionBarXpath));
        } catch (Exception ignored) {
            throw new RuntimeException("Cannot get Selection Bar in widget: " + widgetId);
        }
    }

    private SelectionBarComponent(WebDriver driver, WebDriverWait wait, WebElement webElement, String widgetId) {
        this.driver = driver;
        this.wait = wait;
        this.widgetId = widgetId;
    }

    private static String getSelectionBarInWidgetXpath(String widgetId) {
        return "//*[@data-testid='" + widgetId + "']" + DATA_TESTID_SELECTION_BAR_XPATH;
    }

    public static void openSelectionBar(WebDriver driver, String widgetId) {
        try {
            if (!isActive(driver, widgetId)) {
                driver.findElement(By.xpath("//div[@data-testid='" + widgetId + "']" + TOGGLER_BUTTON_XPATH)).click();
            }
        } catch (Exception ignored) {
            throw new RuntimeException("Cannot get Selection Bar in widget: " + widgetId);
        }
    }

    public static void hideSelectionBar(WebDriver driver, String widgetId) {
        if (isActive(driver, widgetId)) {
            getSelectionBarComponent(driver, widgetId).findElement(By.xpath("//div[@data-testid='" + widgetId + "']" + TOGGLER_BUTTON_XPATH)).click();
        }
    }

    private static boolean isActive(WebDriver driver, String widgetId) {
        return driver.findElements(By.xpath("//div[@data-testid='" + widgetId + "']" + SELECTION_BAR_ACTIVE_XPATH)).size() > 0;
    }

    private boolean areAllObjectShown() {
        return getSelectionBarComponent(driver, widgetId).findElements(By.xpath(SHOW_SELECTED_ONLY_BUTTON_ACTIVE_XPATH)).size() > 0;
    }

    public void clickUnselectAllButton() {
        getSelectionBarComponent(driver, widgetId).findElement(By.xpath(UNSELECT_ALL_BUTTON_XPATH)).click();
    }

    public void clickShowOnlySelectedButton() {
        if (isActive(driver, widgetId) && areAllObjectShown()) {
            getSelectionBarComponent(driver, widgetId).findElement(By.xpath(SHOW_ONLY_SELECTED_BUTTON_XPATH)).click();
        }
    }

    public void clickShowAllButton() {
        if (isActive(driver, widgetId) && !areAllObjectShown()) {
            getSelectionBarComponent(driver, widgetId).findElement(By.xpath(SHOW_ONLY_SELECTED_BUTTON_XPATH)).click();
        }
    }

    public String getSelectedObjectsCount() {
        return getSelectionBarComponent(driver, widgetId).findElement(By.xpath(SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH)).getText();
    }
}
