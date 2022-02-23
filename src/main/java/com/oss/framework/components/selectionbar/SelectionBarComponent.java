package com.oss.framework.components.selectionbar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class SelectionBarComponent {

    private static final String SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_CSS = "[" + CSSUtils.TEST_ID + "=selected-objects-count-label]";
    private static final String SHOW_ONLY_SELECTED_BUTTON_CSS = "[" + CSSUtils.TEST_ID + "=show-selected-only-button]";
    private static final String UNSELECT_ALL_BUTTON_CSS = "[" + CSSUtils.TEST_ID + "=unselect-all-button]";
    private static final String VISIBLE_SELECTION_BAR_CSS = ".selection-bar";
    private static final String TOGGLE_BUTTON_CSS = "[" + CSSUtils.TEST_ID + "=selection-bar-toggler-button]";
    private static final String SHOW_ONLY_SELECTED_BUTTON_ACTIVE_XPATH = "//*[@ " + CSSUtils.TEST_ID + "='show-selected-only-button' and text() = 'Show Selected']";

    private final WebDriverWait wait;
    private final WebElement widget;

    private SelectionBarComponent(WebDriverWait wait, WebElement widget) {
        this.wait = wait;
        this.widget = widget;
    }

    public static SelectionBarComponent create(WebDriver driver, WebDriverWait wait, String widgetId) {
        WebElement widget = driver.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        return new SelectionBarComponent(wait, widget);
    }

    public void openSelectionBar() {
        if (!isActive()) {
            widget.findElement(By.cssSelector(TOGGLE_BUTTON_CSS)).click();
            DelayUtils.waitForPresence(wait, By.cssSelector(VISIBLE_SELECTION_BAR_CSS));
        }
    }

    public void hideSelectionBar() {
        if (isActive()) {
            widget.findElement(By.cssSelector(TOGGLE_BUTTON_CSS)).click();
        }
    }

    public void unselectAll() {
        if (!isActive()) {
            openSelectionBar();
        }
        widget.findElement(By.cssSelector(UNSELECT_ALL_BUTTON_CSS)).click();
    }

    public void showSelected() {
        if (isActive() && isShowSelectedPresent()) {
            widget.findElement(By.cssSelector(SHOW_ONLY_SELECTED_BUTTON_CSS)).click();
        }
    }

    public void showAll() {
        if (isActive() && !isShowSelectedPresent()) {
            widget.findElement(By.cssSelector(SHOW_ONLY_SELECTED_BUTTON_CSS)).click();
        }
    }

    public String getSelectedObjectsCount() {
        if (!isActive()) {
            openSelectionBar();
        }
        return widget.findElement(By.cssSelector(SELECTION_BAR_SELECTED_OBJECTS_COUNT_LABEL_CSS))
                .getText();
    }

    private boolean isActive() {
        return !widget.findElements(By.cssSelector(VISIBLE_SELECTION_BAR_CSS)).isEmpty();
    }

    private boolean isShowSelectedPresent() {
        return !widget.findElements(By.xpath(SHOW_ONLY_SELECTED_BUTTON_ACTIVE_XPATH)).isEmpty();
    }
}
