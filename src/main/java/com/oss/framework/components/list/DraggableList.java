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

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

/**
 * @author Gabriela Kasza
 */
public class DraggableList {
    private static final String DRAG_BUTTON_XPATH = ".//div[contains(@class,'dragButton')]//div";
    private static final String DRAGGABLE_LIST_ROW_XPATH = ".//ul[contains(@class,'DraggableListRows')]";
    private static final String DRAGGABLE_ELEMENT_XPATH = ".//li[@class='listElement']";
    private static final String DROPDOWN_LIST_XPATH = "//div[@class = 'DropdownList']";
    private static final String DROPDOWN_LIST_LABEL_XPATH = ".//div[@class='categoryLabel']";
    private static final String DRAGGABLE_LIST_PATTERN = "//div[@class = 'DropdownList']//div[contains(text(),'%s')]";
    private static final String DROPDOWN_LIST_NOT_EXIST_EXCEPTION = "The Dropdown List doesn't exist";
    private static final String OBJECT_NOT_AVAILABLE_EXCEPTION = "Object not available on the list";
    private final WebDriver driver;
    private final WebElement dropdownListElement;

    private DraggableList(WebDriver driver, WebElement dropdownListElement) {
        this.driver = driver;
        this.dropdownListElement = dropdownListElement;
    }

    public static DraggableList create(WebDriver driver, WebDriverWait wait, String componentName) {
        DelayUtils.waitByXPath(wait, String.format(DRAGGABLE_LIST_PATTERN, componentName));
        // TODO: get rid of stream after fix OSSWEB-10056
        List<WebElement> allLists = driver.findElements(By.xpath(DROPDOWN_LIST_XPATH));
        WebElement dropdownList = allLists.stream()
                .filter(list -> isContainsName(componentName, list))
                .findFirst().orElseThrow(() -> new NoSuchElementException(DROPDOWN_LIST_NOT_EXIST_EXCEPTION));
        return new DraggableList(driver, dropdownList);
    }

    private static boolean isContainsName(String componentName, WebElement dropdownList) {
        WebElement label = dropdownList.findElement(By.xpath(DROPDOWN_LIST_LABEL_XPATH));
        return label.getText().contains(componentName);
    }

    public DragAndDrop.DraggableElement getDraggableElement(String value) {
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

}