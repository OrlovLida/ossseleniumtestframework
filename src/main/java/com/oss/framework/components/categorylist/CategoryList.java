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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Gabriela Zaranek
 */
public class CategoryList {
    private static final String COLLAPSE_ICON_XPATH = ".//i[contains(@class,'chevron-up')]";
    private static final String CATEGORY_LABEL_TEXT_CLASS = "categoryLabel-text";
    private static final String CATEGORY_LABEL_CLASS = "categoryLabel";
    private static final String EXPAND_ICON_XPATH = ".//i[contains(@class,'chevron-down')]";
    private static final String CATEGORY_LIST_ELEMENT_CSS = ".categoryListElement";
    private static final String ICON_BY_ID_PATTERN = ".//button[@" + CSSUtils.TEST_ID + "= '%s']";
    private static final String SKELETON_PRELOADER_CSS = " .skeleton-preloader";
    private static final String DROPDOWN_LIST = "DropdownList";
    private static final String CHEVRON_XPATH = ".//i[contains(@class,'chevron fa')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement categoryElement;

    private CategoryList(WebDriver driver, WebDriverWait wait, WebElement categoryElement) {
        this.driver = driver;
        this.wait = wait;
        this.categoryElement = categoryElement;
    }

    public static List<CategoryList> create(WebDriver driver, WebDriverWait wait, String widgetId) {
        String listWidgetCss = String.format(CSSUtils.WEB_ELEMENT_PATTERN, widgetId);
        DelayUtils.waitBy(wait, By.cssSelector(listWidgetCss));
        WebElement widget = driver.findElement(By.cssSelector(listWidgetCss));
        DelayUtils.waitForElementDisappear(wait, By.cssSelector(listWidgetCss + SKELETON_PRELOADER_CSS));
        return widget.findElements(By.cssSelector(CATEGORY_LIST_ELEMENT_CSS)).stream()
                .map(category -> new CategoryList(driver, wait, category)).collect(Collectors.toList());
    }

    public String getValue() {
        WebElement categoryLabel;
        if (!categoryElement.findElements(By.className(CATEGORY_LABEL_TEXT_CLASS)).isEmpty()) {
            categoryLabel = categoryElement.findElement(By.className(CATEGORY_LABEL_TEXT_CLASS));
        } else {
            categoryLabel = categoryElement.findElement(By.className(CATEGORY_LABEL_CLASS));
        }
        return categoryLabel.getAttribute("title");
    }

    public void callAction(String groupId, String actionId) {
        InlineMenu.create(categoryElement, driver, wait).callAction(groupId, actionId);
    }

    public void callAction(String actionId) {
        if (!categoryElement.findElements(By.xpath(String.format(ICON_BY_ID_PATTERN, actionId))).isEmpty()) {
            WebElement button = categoryElement.findElement(By.xpath(String.format(ICON_BY_ID_PATTERN, actionId)));
            WebElementUtils.clickWebElement(driver, button);
        } else {
            InlineMenu.create(categoryElement, driver, wait).callAction(actionId);
        }
    }

    public void expandCategory() {
        if (!isExpanded()) {
            WebElementUtils.clickWebElement(driver, categoryElement.findElement(By.xpath(EXPAND_ICON_XPATH)));
        }
        DelayUtils.waitForElementToLoad(wait, categoryElement);
    }

    public void collapseCategory() {
        if (isExpanded()) {
            WebElementUtils.clickWebElement(driver, categoryElement.findElement(By.xpath(COLLAPSE_ICON_XPATH)));
        }
        DelayUtils.waitForElementToLoad(wait, categoryElement);
    }

    private boolean isExpanded() {
        return !categoryElement.findElements(By.xpath(COLLAPSE_ICON_XPATH)).isEmpty();
    }

    public String getCategoryId() {
        return categoryElement.findElement(By.className(DROPDOWN_LIST)).getAttribute(CSSUtils.TEST_ID);
    }

    public boolean isCategoryLabelVisible() {
        return !categoryElement.findElements(By.className(CATEGORY_LABEL_CLASS)).isEmpty();
    }

    public boolean isCategoryChevronVisible() {
        return !categoryElement.findElements(By.xpath(CHEVRON_XPATH)).isEmpty();
    }
}
