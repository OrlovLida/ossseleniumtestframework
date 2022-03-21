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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

/**
 * @author Gabriela Zaranek
 */
public class Category {
    private static final String CATEGORIES_NAME_CLASS = "categories__bar__content__name";
    private static final String CANNOT_FIND_CATEGORY_WITH_PROVIDED_NAME_EXCEPTION = "Cannot find category with provided name";
    private static final String CATEGORIES_CONTENT_NAME_DESCRIPTION_CSS = "div.categories__bar__content__name";
    private static final String CATEGORIES_DESCRIPTION_CSS = "div.categories__bar__content__description";
    private static final String CATEGORIES_BUTTON_EXPANDER_CSS = ".categories__buttons__rollout i";
    private static final String CHEVRON_UP_ICON_CLASS = "fa-chevron-up";
    private static final String SUBCATEGORIES_CSS = "//div[@class='subcategories__name']//ancestor::div[@class='subcategories']";
    private static final String DRAG_BUTTON_XPATH = ".//ancestor::div[contains(@class,'draggableBox')]//div[@class='btn-drag']";
    private static final String CATEGORIES_CLASS = "categories";
    private static final String APPLICATION_BOX_CSS = "div.category-box__content";
    
    private WebDriver driver;
    private WebElement categoryElement;
    private WebDriverWait wait;
    
    private Category(WebDriver driver, WebDriverWait wait, WebElement category) {
        this.driver = driver;
        this.categoryElement = category;
        this.wait = wait;
    }
    
    static Category createCategoryByName(WebDriver driver, WebDriverWait wait, WebElement toolsManagerWindow, String categoryName) {
        DelayUtils.waitBy(wait, By.className(CATEGORIES_CLASS));
        WebElement category = toolsManagerWindow.findElements(By.className(CATEGORIES_CLASS)).stream().filter(
                categoryElement -> categoryElement.findElement(By.className(CATEGORIES_NAME_CLASS)).getText().contains(categoryName))
                .findFirst().orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_CATEGORY_WITH_PROVIDED_NAME_EXCEPTION));
        
        return new Category(driver, wait, category);
    }
    
    static Category createCategory(WebDriver driver, WebDriverWait wait, WebElement categoryElement) {
        return new Category(driver, wait, categoryElement);
    }
    
    public void callAction(String actionId) {
        InlineMenu inlineMenu = InlineMenu.create(categoryElement, driver, wait);
        inlineMenu.callAction(actionId);
    }
    
    List<Application> getApplications() {
        if (!isCategoryExpanded()) {
            expandCategory();
        }
        List<WebElement> applicationsWebElements = this.categoryElement.findElements(By
                .cssSelector(APPLICATION_BOX_CSS));
        return applicationsWebElements.stream()
                .map(webElement -> Application.createApplication(driver, wait, webElement))
                .collect(Collectors.toList());
    }
    
    public void collapseCategory() {
        if (isCategoryExpanded()) {
            toggleCategory();
        }
    }
    
    public void expandCategory() {
        if (!isCategoryExpanded()) {
            toggleCategory();
        }
        DelayUtils.waitBy(this.wait, By.className(CHEVRON_UP_ICON_CLASS));
    }
    
    boolean isCategoryExpanded() {
        return !this.categoryElement
                .findElements(By.className(CHEVRON_UP_ICON_CLASS))
                .isEmpty();
    }
    
    List<Subcategory> getSubcategories() {
        if (!isCategoryExpanded()) {
            expandCategory();
        }
        List<WebElement> subcategories = categoryElement.findElements(By.xpath(SUBCATEGORIES_CSS));
        return subcategories.stream()
                .map(subcategory -> Subcategory.createSubcategory(driver, wait, subcategory))
                .collect(Collectors.toList());
    }
    
    DragAndDrop.DraggableElement getDragElement() {
        WebElement dragButton = categoryElement.findElement(By.xpath(DRAG_BUTTON_XPATH));
        return new DragAndDrop.DraggableElement(dragButton);
    }
    
    DragAndDrop.DropElement getDropElement() {
        return new DragAndDrop.DropElement(categoryElement);
    }
    
    String getName() {
        String categoryText = getCategoryText();
        return categoryText.substring(0, (categoryText.length() - getDescription().length()));
    }
    
    String getDescription() {
        return categoryElement.findElement(By.cssSelector(CATEGORIES_DESCRIPTION_CSS)).getText();
    }
    
    private String getCategoryText() {
        return categoryElement.findElement(By.cssSelector(CATEGORIES_CONTENT_NAME_DESCRIPTION_CSS)).getAttribute("textContent");
    }
    
    private void toggleCategory() {
        WebElement expanderIcon = categoryElement.findElement(By.cssSelector(CATEGORIES_BUTTON_EXPANDER_CSS));
        Actions actions = new Actions(driver);
        actions.moveToElement(categoryElement).click(expanderIcon).build().perform();
    }
}
