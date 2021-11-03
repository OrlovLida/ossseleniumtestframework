package com.oss.framework.widgets.treewidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;

public class InlineMenu {

    private static final String GROUP_BUTTON_ID = "frameworkObjectButtonsGroup";

    private final WebDriver driver;
    private final WebElement webElement;
    private final WebDriverWait wait;

    public static InlineMenu create(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        return new InlineMenu(webElement, driver, wait);
    }

    private InlineMenu(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public void callAction(String actionId) {
        clickActionGroup();
        ActionsContainer.createFromParent(webElement, driver, wait).callActionForInline(actionId);
    }

    public void callAction(String groupId, String actionId) {
        clickActionGroup();
        ActionsContainer.createFromParent(webElement, driver, wait).callActionById(groupId, actionId);
    }

    private void clickActionGroup() {
        WebElement inline = webElement.findElement(By.id(GROUP_BUTTON_ID));
        Actions actions = new Actions(driver);
        actions.moveToElement(inline).click(inline).build().perform();
    }

    public Boolean isActionListDisplayed() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !webElement.findElements(By.id(GROUP_BUTTON_ID)).isEmpty();
    }
}
