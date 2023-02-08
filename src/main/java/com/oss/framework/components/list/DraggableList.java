/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.list;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Preconditions;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Gabriela Kasza
 */
public class DraggableList {
    public static final String ROW_DATA_CSS_SELECTOR = ".rowData";
    private static final String DRAG_BUTTON_XPATH = ".//div[contains(@class,'dragButton')]//div";
    private static final String DRAGGABLE_LIST_ROW_XPATH = ".//ul[contains(@class,'DraggableListRows')]";
    private static final String DRAGGABLE_ELEMENT_XPATH = ".//li[@class='listElement']";
    private static final String DROPDOWN_LIST_LABEL_XPATH = ".//div[@class='categoryLabel']";
    private static final String DRAGGABLE_LIST_PATTERN = "div[" + CSSUtils.TEST_ID + "='%s']";
    private static final String OBJECT_NOT_AVAILABLE_EXCEPTION = "Object not available on the list";
    private static final String POSITION_OUT_OF_RANGE_EXCEPTION = "Position is out of Rows size range. Provided %1$d, but list size is: %2$d.";
    private static final String CATEGORY_LIST_CSS = ".category";
    private static final String LABEL_CSS = "span p";
    private final WebDriver driver;
    private final WebElement dropdownListElement;
    private final WebDriverWait wait;

    private DraggableList(WebDriver driver, WebDriverWait wait, WebElement dropdownListElement) {
        this.driver = driver;
        this.dropdownListElement = dropdownListElement;
        this.wait = wait;
    }

    public static DraggableList create(WebDriver driver, WebDriverWait wait, String componentId) {
        String selector = String.format(DRAGGABLE_LIST_PATTERN, componentId);
        DelayUtils.waitBy(wait, By.cssSelector(selector));
        WebElement dropdownList = driver.findElement(By.cssSelector(selector));
        return new DraggableList(driver, wait, dropdownList);
    }

    private static boolean isContainsName(String componentName, WebElement dropdownList) {
        WebElement label = dropdownList.findElement(By.xpath(DROPDOWN_LIST_LABEL_XPATH));
        return label.getText().contains(componentName);
    }

    public DragAndDrop.DraggableElement getDraggableElement(String value) {
        DelayUtils.waitForNestedElements(wait, dropdownListElement, By.cssSelector(LABEL_CSS));
        List<WebElement> allSource = dropdownListElement.findElements(By.xpath(DRAGGABLE_ELEMENT_XPATH));
        WebElement row = allSource.stream().filter(object -> object.getText().contains(value)).findFirst()
                .orElseThrow(() -> new NoSuchElementException(OBJECT_NOT_AVAILABLE_EXCEPTION));
        WebElement source = row.findElement(By.xpath(DRAG_BUTTON_XPATH));
        return new DragAndDrop.DraggableElement(source);
    }

    public void drop(DragAndDrop.DraggableElement draggableElement) {
        WebElement target = dropdownListElement.findElement(By.xpath(DRAGGABLE_LIST_ROW_XPATH));
        DragAndDrop.dragAndDrop(draggableElement, new DragAndDrop.DropElement(target), driver);
    }

    public void drop(DragAndDrop.DraggableElement draggableElement, int position) {
        WebElement targetDraggableList = dropdownListElement.findElement(By.xpath(DRAGGABLE_LIST_ROW_XPATH));
        List<WebElement> rows = targetDraggableList.findElements(By.cssSelector(ROW_DATA_CSS_SELECTOR));
        if (rows.isEmpty()) {
            drop(draggableElement);
        } else {
            Preconditions.checkArgument(position < rows.size() && position >= 0, POSITION_OUT_OF_RANGE_EXCEPTION, position, rows.size());
            DragAndDrop.dragAndDrop(draggableElement, new DragAndDrop.DropElement(rows.get(position)), driver);
        }
    }

    private CategoryList getCategory() {
        return new CategoryList(driver, wait, dropdownListElement.findElement(By.cssSelector(CATEGORY_LIST_CSS)));
    }

    public void expandCategory() {
        getCategory().expand();
    }

    public void collapseCategory() {
        getCategory().collapse();
    }

    private static class CategoryList {
        private static final String COLLAPSE_ICON_CSS = ".fa-chevron-up";
        private static final String EXPAND_ICON_CSS = ".fa-chevron-down";
        private final WebDriver driver;
        private final WebElement categoryListElement;
        private final WebDriverWait wait;

        private CategoryList(WebDriver driver, WebDriverWait wait, WebElement categoryListElement) {
            this.driver = driver;
            this.categoryListElement = categoryListElement;
            this.wait = wait;
        }

        private void expand() {
            if (!isExpanded()) {
                WebElementUtils.clickWebElement(driver, categoryListElement.findElement(By.cssSelector(EXPAND_ICON_CSS)));
            }
            DelayUtils.waitForElementToLoad(wait, categoryListElement);
        }

        private void collapse() {
            if (isExpanded()) {
                WebElementUtils.clickWebElement(driver, categoryListElement.findElement(By.cssSelector(COLLAPSE_ICON_CSS)));
            }
            DelayUtils.waitForElementToLoad(wait, categoryListElement);
        }

        private boolean isExpanded() {
            return !categoryListElement.findElements(By.cssSelector(COLLAPSE_ICON_CSS)).isEmpty();
        }
    }
}