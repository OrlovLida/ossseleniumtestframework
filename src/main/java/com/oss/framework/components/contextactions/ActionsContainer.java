package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class ActionsContainer implements ActionsInterface {
    private static final String CONTEXT_ACTIONS_CLASS = "actionsContainer";

    private final WebElement webElement;
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    public static ActionsContainer createFromParent(WebElement parentElement, WebDriver webDriver, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(CONTEXT_ACTIONS_CLASS));
        return new ActionsContainer(parentElement, webDriver, webDriverWait);
    }

    private ActionsContainer(WebElement parentElement, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webElement = parentElement.findElement(By.className(CONTEXT_ACTIONS_CLASS));
        this.webDriverWait = webDriverWait;
    }

    public void callAction(String actionId) {
        callAction(null, actionId);
    }

    @Override
    public void callActionByLabel(String label) {

    }

    public void callAction(String groupId, String actionId) {
        if(groupId != null) {
            clickOnGroup(groupId);
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(actionId);
        } else {
            clickOnAction(actionId);
        }
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {

    }

    private void clickOnGroup(String groupId) {
        DelayUtils.waitBy(this.webDriverWait, By.id(groupId));
        this.webElement.findElement(By.id(groupId)).click();
    }

    private void clickOnAction(String actionId) {
        DelayUtils.waitBy(this.webDriverWait, By.id(actionId));
        this.webElement.findElement(By.id(actionId)).click();
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
            this.webElement = driver.findElement(By.className("actionsList"));
        }

        private void callAction(String actionId) {
            DelayUtils.waitBy(this.webDriverWait, By.id(actionId));
            this.webElement.findElement(By.id(actionId)).click();
        }
    }
}
