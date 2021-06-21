package com.oss.framework.components.contextactions;

import java.util.List;

import javax.swing.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class ActionsContainer implements ActionsInterface {
    public static final String KEBAB_GROUP_ID = "KEBAB";
    public static final String CREATE_GROUP_ID = "CREATE";
    public static final String EDIT_GROUP_ID = "EDIT";
    public static final String OTHER_GROUP_ID = "OTHER";
    public static final String SHOW_ON_GROUP_ID = "NAVIGATION";
    public static final String MORE_GROUP_ID = "moreActions";

    private static final String CONTEXT_ACTIONS_CLASS = "actionsContainer";
    private static final String KEBAB_BUTTON_XPATH = ".//div[@id='frameworkCustomButtonsGroup'] | .//div[@id='frameworkObjectButtonsGroup']";

    private final WebElement webElement;
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    public static ActionsContainer createFromParent(WebElement parentElement, WebDriver webDriver, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(CONTEXT_ACTIONS_CLASS));
        List<WebElement> allContextAction = parentElement.findElements(By.className(CONTEXT_ACTIONS_CLASS));
        WebElement activeContextActions = allContextAction.stream().filter(WebElement::isDisplayed).findFirst().orElseThrow(() -> new RuntimeException("No active Context Action"));
        return new ActionsContainer(activeContextActions, webDriver, webDriverWait);
    }

    private ActionsContainer(WebElement activeContextActions, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webElement = activeContextActions;
        this.webDriverWait = webDriverWait;
    }

    public void callAction(String actionId) {
        callAction(null, actionId);
    }

    @Override
    public void callActionByLabel(String label) {

    }

    public void callAction(String groupId, String actionId) {
        if (groupId != null) {
            if (ActionsContainer.KEBAB_GROUP_ID.equals(groupId)) {
                callActionFromKebab(actionId);
                return;
            }
            clickOnGroup(groupId);
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(actionId);
        } else {
            clickOnAction(actionId);
        }
    }

    @Override
    public void callActionByLabel(String groupId, String actionLabel) {
        clickOnGroup(groupId);
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callActionByLabel(actionLabel);
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        clickOnGroup(groupId);
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callAction(actionId);
    }

    private void callActionFromKebab(String actionId) {
        getKebabMenuBtn().click();
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callAction(actionId);
    }

    private WebElement getKebabMenuBtn() {
        return this.webElement.findElement(By.xpath(KEBAB_BUTTON_XPATH));
    }

    private void clickOnGroup(String groupId) {
        DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement, "//div[@class='actionsGroup-default']");
        // Info: Action for inline
        if (!webDriver.findElements(By.className("actionsList")).isEmpty()){
            Dropdown.create(webDriver, webDriverWait).callAction(groupId);
            return;
        }
        if (isElementPresent(webElement, By.id(groupId))) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement.findElement(By.id(groupId)));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(groupId)))).click();
        } else {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement.findElement(By.id(MORE_GROUP_ID)));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(MORE_GROUP_ID)))).click();
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(groupId);
        }
    }

    public void callActionById(String id) {
        if (isElementPresent(webElement, By.id(id))) {
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(id)))).click();
        } else {
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(MORE_GROUP_ID)))).click();
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(id);
        }
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        return !webElement.findElements(by).isEmpty();
    }

    private void clickOnAction(String actionId) {
        // Info: Action for inline
        if (!webDriver.findElements(By.className("actionsList")).isEmpty()){
            Dropdown.create(webDriver, webDriverWait).callAction(actionId);
            return;
        }
        DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement, "//*[@id='" + actionId + "']");
        webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(actionId)))).click();
    }

    private static class Dropdown {
        private final WebElement webElement;
        private final WebDriverWait webDriverWait;

        private static Dropdown create(WebDriver webDriver, WebDriverWait webDriverWait) {
            DelayUtils.waitBy(webDriverWait, By.className("actionsList"));
            return new Dropdown(webDriver, webDriverWait);
        }

        private Dropdown(WebDriver driver, WebDriverWait webDriverWait) {
            this.webDriverWait = webDriverWait;
            List<WebElement> actionsLists = driver.findElements(By.className("actionsList"));
            this.webElement = actionsLists.get(actionsLists.size() - 1);
        }

        private void callAction(String actionId) {
            DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement, "//*[@id='" + actionId + "']");
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.id(actionId)))).click();
        }

        private void callActionByLabel(String actionLabel) {
            DelayUtils.waitForNestedElements(webDriverWait, this.webElement, "//a[contains(text(),'" + actionLabel + "')]");
            webDriverWait.until(ExpectedConditions.elementToBeClickable(this.webElement.findElement(By.xpath("//a[contains(text(),'" + actionLabel + "')]")))).click();
        }

    }
}
