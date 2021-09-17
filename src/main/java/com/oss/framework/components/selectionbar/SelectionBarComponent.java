package com.oss.framework.components.selectionbar;

import com.oss.framework.components.inputs.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectionBarComponent {

    private static final String SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH = "//span[@data-testid='selected-objects-count-label']";
    private static final String SHOW_ONLY_SELECTED_BUTTON_XPATH = ".//*[@data-testid='show-selected-only-button']";
    private static final String UNSELECT_ALL_BUTTON_XPATH = ".//*[@data-testid='unselect-all-button']";
    private static final String DATA_TESTID_SELECTION_BAR_XPATH = "//*[@data-testid='selection-bar']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static SelectionBarComponent create(WebDriver driver, WebDriverWait wait, String widgetId) {
        WebElement selectionBar;
        String selectionBarXpath = getSelectionBarInWidgetXpath(widgetId);
        boolean isSelectionBarVisible = driver.findElements(By.xpath(selectionBarXpath)).size() > 0;
        if (isSelectionBarVisible) {
            selectionBar = driver.findElement(By.xpath(DATA_TESTID_SELECTION_BAR_XPATH));
            return new SelectionBarComponent(driver, wait, selectionBar);
        }
        Button.createById(driver, "selection-bar-toggler-button").click();
        selectionBar = driver.findElement(By.xpath(DATA_TESTID_SELECTION_BAR_XPATH));
        return new SelectionBarComponent(driver, wait, selectionBar);
    }

    private SelectionBarComponent(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    private static String getSelectionBarInWidgetXpath(String widgetId) {
        return "//*[@data-testid='" + widgetId + "']" + DATA_TESTID_SELECTION_BAR_XPATH;
    }

    public void toggleSelectionBar() {
        Button.createById(driver, "selection-bar-toggler-button").click();
    }

    public void clickUnselectAllButton() {
        this.webElement.findElement(By.xpath(UNSELECT_ALL_BUTTON_XPATH)).click();
    }

    public void clickShowOnlySelectedOrShowAllButton() {
        this.webElement.findElement(By.xpath(SHOW_ONLY_SELECTED_BUTTON_XPATH)).click();
    }

    public String getSelectionObjectCountLabelFromSelectionBar() {
        return this.webElement.findElement(By.xpath(SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_XPATH)).getText();
    }
}
