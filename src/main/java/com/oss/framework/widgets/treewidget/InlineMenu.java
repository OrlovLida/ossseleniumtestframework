package com.oss.framework.widgets.treewidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;

public class InlineMenu {

    private static final String GROUP_BUTTON_ID = "frameworkObjectButtonsGroup";

    private final WebDriver driver;
    private final WebElement webElement;
    private final WebDriverWait wait;

    private InlineMenu(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static InlineMenu create(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        return new InlineMenu(webElement, driver, wait);
    }

    public void callAction(String actionId) {
        expandKebabMenu();
        ActionsContainer.createFromParent(webElement, driver, wait).callActionForInline(actionId);
    }

    public void callAction(String groupId, String actionId) {
        expandKebabMenu();
        ActionsContainer.createFromParent(webElement, driver, wait).callActionForInline(groupId, actionId);
    }

    private void expandKebabMenu() {
        WebElement inlineKebab = webElement.findElement(By.id(GROUP_BUTTON_ID));
        Actions actions = new Actions(driver);
        actions.moveToElement(inlineKebab).click(inlineKebab).build().perform();
    }

}
