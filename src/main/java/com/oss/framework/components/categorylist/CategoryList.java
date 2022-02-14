/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.categorylist;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Zaranek
 */
public class CategoryList {
    private static final String COLLAPSE_ICON_XPATH = ".//i[contains(@class,'chevron-up')]";
    private static final String CATEGORY_NAME_XPATH = "categoryLabel-text";
    private static final String EXPAND_ICON_XPATH = ".//i[contains(@class,'chevron-down')]";

    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement categoryElement;

    private CategoryList(WebDriver driver, WebDriverWait wait, WebElement categoryElement){
        this.driver = driver;
        this.wait = wait;
        this.categoryElement = categoryElement;
    }

    public static List<CategoryList> create (WebDriver driver, WebDriverWait wait, String widgetId){
        DelayUtils.waitBy(wait, By.cssSelector("[" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        WebElement widget = driver.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        return widget.findElements(By.cssSelector(".categoryListElement")).stream().map(category -> new CategoryList(driver, wait,category)).collect(Collectors.toList());
    }

    public String getValue() {
        return categoryElement.findElement(By.className(CATEGORY_NAME_XPATH)).getText();
    }

    public void callAction(String groupId, String actionId) {
        InlineMenu.create(categoryElement, driver, wait).callAction(groupId, actionId);
    }

    public void callAction(String actionId) {
        InlineMenu.create(categoryElement, driver, wait).callAction(actionId);
    }

    public void expandCategory() {
        if (!isExpanded()) {
            Actions actions = new Actions(driver);
            actions.moveToElement(categoryElement.findElement(By.xpath(EXPAND_ICON_XPATH))).click().build().perform();
        }
        DelayUtils.waitForElementToLoad(wait, categoryElement);
    }

    public void collapseCategory() {
        if (isExpanded()) {
            Actions actions = new Actions(driver);
            actions.moveToElement(categoryElement.findElement(By.xpath(COLLAPSE_ICON_XPATH))).click().build().perform();
        }
        DelayUtils.waitForElementToLoad(wait, categoryElement);
    }

    private boolean isExpanded() {
        return !categoryElement.findElements(By.xpath(COLLAPSE_ICON_XPATH)).isEmpty();
    }
}
