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
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Gabriela Zaranek
 */
public class Subcategory {
    private static final String SUBCATEGORIES_NAME_CSS = "div.subcategories__name";
    private static final String SUBCATEGORIES_CONTENT_SEE_ALL_CSS = "div.subcategories__content__see-all--text";
    private static final String DRAG_BUTTON_CSS = ".btn-drag";
    private static final String APPLICATIONS_IN_SUBCATEGORY_CSS = "div.category-box";
    private static final String STAR_CSS = ".subcategories__name__favourite .OSSIcon";
    private static final String FAVOURITE = "FAVOURITE";
    private static final String ARIA_LABEL = "aria-label";
    private static final String BADGE_CSS = ".subcategories__name__counter";
    private static final String SUBCATEGORIES_CONTENT_CSS = ".subcategories__content,.subcategories--empty";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
    private static final String LESS = "Less";
    private static final String SUBCATEGORIES_LOADER = ".subcategories__name .skeleton-preloader";
    
    private WebElement subcategoryElement;
    private WebDriver driver;
    private WebDriverWait wait;
    
    private Subcategory(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        this.subcategoryElement = webElement;
        this.driver = driver;
        this.wait = wait;
    }
    
    static Subcategory createSubcategory(WebDriver driver, WebDriverWait wait, WebElement subcategory) {
        DelayUtils.waitForElementDisappear(wait, By.cssSelector(SUBCATEGORIES_LOADER));
        WebElementUtils.moveToElement(driver, subcategory.findElement(By.cssSelector(SUBCATEGORIES_CONTENT_CSS)));
        return new Subcategory(subcategory, driver, wait);
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
        if (isSeeAllPresent()) {
            showAll();
        }
    }
    
    public void clickShowLess() {
        if (isSeeAllPresent()) {
            showLess();
        }
    }
    
    public void markFavorite() {
        if (!isFavorite()) {
            WebElementUtils.clickWebElement(driver, getStar());
        }
    }
    
    public boolean isFavorite() {
        return getStar().getAttribute(ARIA_LABEL).equals(FAVOURITE);
    }
    
    public String getBadge() {
        return subcategoryElement.findElement(By.cssSelector(BADGE_CSS)).getText();
    }
    
    String getSubcategoryName() {
        String subcategoryFullText =
                subcategoryElement.findElement(By.cssSelector(SUBCATEGORIES_NAME_CSS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        String badge = getBadge();
        return subcategoryFullText.split(badge)[0];
    }
    
    DragAndDrop.DropElement getDropElement() {
        return new DragAndDrop.DropElement(subcategoryElement.findElement(By.cssSelector(SUBCATEGORIES_CONTENT_CSS)));
    }
    
    DragAndDrop.DraggableElement getDragElement() {
        WebElement dragButton = subcategoryElement.findElement(By.cssSelector(DRAG_BUTTON_CSS));
        return new DragAndDrop.DraggableElement(dragButton);
    }
    
    private void showAll() {
        if (!isExpanded()) {
            toggleSeeAll();
            DelayUtils.waitForElementToLoad(wait, subcategoryElement);
        }
    }
    
    private void showLess() {
        if (isExpanded()) {
            toggleSeeAll();
        }
    }
    
    private WebElement getStar() {
        return subcategoryElement.findElement(By.cssSelector(STAR_CSS));
    }
    
    private void toggleSeeAll() {
        subcategoryElement.findElement(By.cssSelector(SUBCATEGORIES_CONTENT_SEE_ALL_CSS)).click();
    }
    
    private boolean isExpanded() {
        String expandedText =
                subcategoryElement.findElement(By.cssSelector(SUBCATEGORIES_CONTENT_SEE_ALL_CSS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        return expandedText.equals(LESS);
    }
    
    private boolean isSeeAllPresent() {
        return WebElementUtils.isElementPresent(subcategoryElement, By.cssSelector(SUBCATEGORIES_CONTENT_SEE_ALL_CSS));
    }
    
}
