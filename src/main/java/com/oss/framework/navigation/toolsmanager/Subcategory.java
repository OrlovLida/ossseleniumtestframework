/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.navigation.toolsmanager;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

/**
 * @author Gabriela Zaranek
 */
 class Subcategory {
    private static final String SUBCATEGORIES_NAME_CSS = "div.subcategories__name";
    private static final String SUBCATEGORIES_CONTENT_SEE_ALL_CSS = "div.subcategories__content__see-all--text";
    private static final String DRAG_BUTTON_CSS = ".btn-drag";
    private static final String ANCESTOR_SUBCATEGORIES_XPATH = ".//ancestor::div[@class='subcategories']";
    private static final String APPLICATIONS_IN_SUBCATEGORY_CSS = "div.category-box";
    private WebElement subcategoryElement;
    private WebDriver driver;
    private WebDriverWait wait;
    private String subcategoryName;

    private Subcategory(WebElement webElement, WebDriver driver, WebDriverWait wait, String subcategoryName) {
        this.subcategoryElement = webElement;
        this.driver = driver;
        this.wait = wait;
        this.subcategoryName = subcategoryName;
    }

    static Subcategory createSubcategoryByName(WebDriver driver, WebDriverWait wait, WebElement category, String subcategoryName) {
        WebElement subcategory =
                category.findElement(By.xpath(".//*[@class='subcategories__name' and contains(text(),'" + subcategoryName + "')]"));
        return new Subcategory(subcategory, driver, wait, subcategoryName);
    }

    static Subcategory createSubcategory(WebDriver driver, WebDriverWait wait, WebElement subcategory) {
        DelayUtils.waitBy(wait, By.cssSelector(SUBCATEGORIES_NAME_CSS));
        String subcategoryFullText = subcategory.findElement(By.cssSelector(SUBCATEGORIES_NAME_CSS)).getText();
        String categoryName = subcategoryFullText.split("\n")[0];
        return new Subcategory(subcategory, driver, wait, categoryName);
    }

    public String getSubcategoryName() {
        return this.subcategoryName;
    }

    public void callAction(String actionId) {
        InlineMenu.create(subcategoryElement, driver, wait).callAction(actionId);
    }

    public List<Application> getApplications() {
        List<WebElement> applications = subcategoryElement.findElements(By.cssSelector(APPLICATIONS_IN_SUBCATEGORY_CSS));
        return applications.stream().map(webElement -> Application.createApplication(driver, wait, webElement))
                .collect(Collectors.toList());
    }

    public void clickShowAll() {
        if (!isExpanded()) {
            toggleSeeAll();
        }
    }

    public void clickShowLess() {
        if (isExpanded()) {
            toggleSeeAll();
        }
    }

    private void toggleSeeAll() {
        subcategoryElement.findElement(By.xpath(SUBCATEGORIES_CONTENT_SEE_ALL_CSS)).click();
    }

    private boolean isExpanded() {
        String expandedText = subcategoryElement.findElement(By.xpath(SUBCATEGORIES_CONTENT_SEE_ALL_CSS)).getText();
        return expandedText.equals("Less");
    }

     DragAndDrop.DropElement getDropElement() {
        return new DragAndDrop.DropElement(subcategoryElement);
    }

     DragAndDrop.DraggableElement getDragElement() {
        WebElement subcategory = subcategoryElement.findElement(By.xpath(ANCESTOR_SUBCATEGORIES_XPATH));
        WebElement dragButton = subcategory.findElement(By.cssSelector(DRAG_BUTTON_CSS));
        return new DragAndDrop.DraggableElement(dragButton);
    }
}
