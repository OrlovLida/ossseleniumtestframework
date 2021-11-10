package com.oss.framework.components.contextactions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    public static final String ASSIGN_GROUP_ID = "ASSIGN";
    public static final String OTHER_GROUP_ID = "OTHER";
    public static final String SHOW_ON_GROUP_ID = "NAVIGATION";
    public static final String MORE_GROUP_ID = "moreActions";
    public static final String ACTIONS_LIST = "actionsList";

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

    @Override
    public void callActionByLabel(String label) {
        throw new UnsupportedOperationException("Method not implemented for Actions Container");
    }

    @Override
    public void callActionByLabel(String groupId, String actionLabel) {
        clickOnGroup(groupId);
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callActionByLabel(actionLabel);
    }

    @Override
    public void callActionById(String id) {
        if (isElementPresent(webElement, By.id(id))) {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(id)));
        } else {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(MORE_GROUP_ID)));
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(id);
        }
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        if (ActionsContainer.KEBAB_GROUP_ID.equals(groupId)) {
            callActionFromKebab(actionId);
            return;
        }
        clickOnGroup(groupId);
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callAction(actionId);
    }

    public void callActionForInline(String actionId) {
            Dropdown.create(webDriver, webDriverWait).callAction(actionId);
    }

    public void callActionForInline(String groupId, String actionId){
        Dropdown.create(webDriver,webDriverWait).callAction(groupId);
        Dropdown.create(webDriver,webDriverWait).callAction(actionId);
    }

    private void clickOnGroup(String groupId) {
        DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement, "//div[@class='actionsGroup-default']");
        if (isElementPresent(webElement, By.id(groupId))) {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(groupId)));
        } else {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(MORE_GROUP_ID)));
            Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
            dropdown.callAction(groupId);
        }
    }

    private void callActionFromKebab(String actionId) {
        clickOnWebElement(webDriver, webDriverWait, getKebabMenuBtn());
        Dropdown dropdown = Dropdown.create(this.webDriver, this.webDriverWait);
        dropdown.callAction(actionId);
    }

    private WebElement getKebabMenuBtn() {
        return this.webElement.findElement(By.xpath(KEBAB_BUTTON_XPATH));
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        return !webElement.findElements(by).isEmpty();
    }

    private static void clickOnWebElement(WebDriver webDriver, WebDriverWait webDriverWait, WebElement webElement) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).click(webElement).build().perform();
    }

    private static class Dropdown {
        private final WebElement webElement;
        private final WebDriverWait webDriverWait;
        private final WebDriver driver;

        private static Dropdown create(WebDriver webDriver, WebDriverWait webDriverWait) {
            DelayUtils.waitBy(webDriverWait, By.className(ACTIONS_LIST));
            return new Dropdown(webDriver, webDriverWait);
        }

        private Dropdown(WebDriver driver, WebDriverWait webDriverWait) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            List<WebElement> actionsLists = driver.findElements(By.className(ACTIONS_LIST));
            this.webElement = actionsLists.get(actionsLists.size() - 1);
        }

        private void callAction(String actionId) {
            DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement, "//*[@id='" + actionId + "']");
            clickOnWebElement(driver, webDriverWait, this.webElement.findElement(By.id(actionId)));
        }

        private void callActionByLabel(String actionLabel) {
            DelayUtils.waitForNestedElements(webDriverWait, this.webElement, "//a[contains(text(),'" + actionLabel + "')]");
            clickOnWebElement(driver, webDriverWait, this.webElement.findElement(By.xpath("//a[contains(text(),'" + actionLabel + "')]")));
        }
    }
}
