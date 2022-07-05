/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.navigation.toolsmanager;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;

/**
 * @author Gabriela Zaranek
 */
public class Application {
    private static final String APPLICATION_LINK_CSS = ".category-box__content__link";
    private static final String APPLICATIONS_IN_SUBCATEGORY_CSS = "div.category-box";
    private static final String APPLICATION_LINK_CLASS = "category-box__content__link";
    private static final String CANNOT_FIND_APPLICATION_BOX_WITH_PROVIDED_NAME_EXCEPTION =
            "Cannot find Application Box with provided name";
    private static final String DRAG_BUTTON_XPATH = "../../../div[contains(@class,'draggableBox')]//div[@class='btn-drag tile-drag']";
    private static final String APPLICATION_BOX_CSS = "div.category-box__content";
    private static final String FAVOURITE = "FAVOURITE";
    private static final String OSSICON_CSS = ".OSSIcon";
    private static final String ARIA_LABEL_ATTRIBUTE = "aria-label";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
    private static final String HREF_ATTRIBUTE = "href";

    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement applicationBox;
    private String applicationName;

    private Application(WebDriver driver, WebDriverWait wait, WebElement applicationBox, String applicationName) {
        this.driver = driver;
        this.wait = wait;
        this.applicationBox = applicationBox;
        this.applicationName = applicationName;
    }

    static Application createApplicationByName(WebDriver driver, WebDriverWait wait, WebElement parent, String applicationName) {
        WebElement applicationBox = parent.findElements(By.cssSelector(APPLICATIONS_IN_SUBCATEGORY_CSS)).stream()
                .filter(application -> application.findElement(By.cssSelector(APPLICATION_BOX_CSS)).getText().equals(applicationName))
                .findFirst().orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_APPLICATION_BOX_WITH_PROVIDED_NAME_EXCEPTION));

        return new Application(driver, wait, applicationBox, applicationName);
    }

    static Application createApplication(WebDriver driver, WebDriverWait wait, WebElement applicationBox) {
        String applicationName = applicationBox.getAttribute(TEXT_CONTENT_ATTRIBUTE);
        return new Application(driver, wait, applicationBox, applicationName);
    }

    public Optional<String> getApplicationsURL() {
        List<WebElement> links = applicationBox.findElements(By.cssSelector(APPLICATION_LINK_CSS));
        if (!links.isEmpty()) {
            return Optional.of(links.get(0).getAttribute(HREF_ATTRIBUTE));
        } else
            return Optional.empty();
    }

    public String getApplicationName() {
        return applicationName;
    }

    public boolean isFavorite() {
        return getStar().getAttribute(ARIA_LABEL_ATTRIBUTE).equals(FAVOURITE);
    }

    public void markFavorite() {
        if (!isFavorite()) {
            WebElementUtils.clickWebElement(driver, getStar());
        }
    }

    void openApplication() {
        applicationBox.findElement(By.className(APPLICATION_LINK_CLASS)).click();
    }

    void callAction(String actionId) {
        InlineMenu.create(applicationBox, driver, wait).callAction(actionId);
    }

    DragAndDrop.DropElement getDropElement() {
        return new DragAndDrop.DropElement(applicationBox);
    }

    DragAndDrop.DraggableElement getDragElement() {
        WebElement dragButton = applicationBox.findElement(By.xpath(DRAG_BUTTON_XPATH));
        return new DragAndDrop.DraggableElement(dragButton);
    }

    private WebElement getStar() {
        return applicationBox.findElement(By.cssSelector(OSSICON_CSS));
    }

}
