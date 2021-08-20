package com.oss.framework.widgets;

import java.util.List;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.widgets.treewidget.InlineMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class Wizard {

    private static final String POPUP_ID = "Popup";
    private static final String OSS_WINDOW = "//div[contains(@class,'OssWindow')]";
    private static final String NEXT_BUTTON = ".//button[text()='Next']";
    private static final String ACCEPT_BUTTON = ".//button[text()='Accept']";
    private static final String SUBMIT_BUTTON = ".//button[text()='Submit']";
    private static final String CANCEL_BUTTON = ".//button[text()='Cancel']";
    private static final String DELETE_BUTTON = ".//a[text()='Delete']";
    private static final String OK_BUTTON = ".//a[text()='OK']";
    private static final String UPDATE_BUTTON = ".//a[text()='Update']";
    private static final String SAVE_BUTTON = ".//a[text()='Save']";
    private static final String PROCEED_BUTTON = ".//a[text()='Proceed']";
    private static final String BY_TEXT_XPATH = "//*[text()='%s']";
    private static final String DATA_TEST_ID_XPATH = "//*[@" + CSSUtils.TEST_ID + "='%s']";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static Wizard createWizard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, OSS_WINDOW);
        WebElement webElement = driver.findElement(By.xpath(OSS_WINDOW));
        return new Wizard(driver, wait, webElement);
    }

    public static Wizard createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        Widget.waitForWidgetById(wait, componentId);
        WebElement webElement = driver.findElement(By.xpath(String.format(DATA_TEST_ID_XPATH, componentId)));
        return new Wizard(driver, wait, webElement);
    }

    public static Wizard createPopupWizard(WebDriver driver, WebDriverWait wait) {
        return createByComponentId(driver, wait, POPUP_ID);
    }

    //TODO: temporary method due to OSSWEB-9886 and OSSWEB-9896
    public static Wizard createWizardByHeaderText(WebDriver driver, WebDriverWait wait, String headerText) {
        DelayUtils.waitByXPath(wait, OSS_WINDOW);
        WebElement webElement = driver.findElement(By.xpath(".//div[text()='" + headerText + "']/../../../../.."));
        return new Wizard(driver, wait, webElement);
    }

    //TODO: temporary method due to OSSWEB-9886 and OSSWEB-9896
    public static Wizard createWizardByClassArrayIndex(WebDriver driver, WebDriverWait wait, String index) {
        DelayUtils.waitByXPath(wait, "//div[@class='OssWindow'][" + index + "]");
        WebElement webElement = driver.findElement(By.xpath("//div[@class='OssWindow'][" + index + "]"));
        return new Wizard(driver, wait, webElement);
    }

    private Wizard(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

    public Input setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(DATA_TEST_ID_XPATH, componentId));
        Input input = getComponent(componentId, componentType);
        input.setSingleStringValue(value);
        return input;
    }

    public void clickNext() {
        DelayUtils.waitForNestedElements(wait, webElement, NEXT_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(NEXT_BUTTON))))).click()
                .perform();
    }

    public void clickNextStep() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath("//button[text()='Next Step']"));
        wait.until(ExpectedConditions.elementToBeClickable(foundedElement));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickAccept() {
        DelayUtils.waitForNestedElements(wait, webElement, ACCEPT_BUTTON);
        WebElement accept = wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(ACCEPT_BUTTON))));
        accept.click();
        waitForButtonDisappear(ACCEPT_BUTTON);
    }

    public void clickAcceptOldWizard() {
        DelayUtils.waitByXPath(wait, OSS_WINDOW);
        WebElement wizardElement = driver.findElement(By.xpath(OSS_WINDOW));
        DelayUtils.waitForNestedElements(wait, wizardElement, ACCEPT_BUTTON);
        wait.until(ExpectedConditions.elementToBeClickable(wizardElement.findElement(By.xpath(ACCEPT_BUTTON)))).click();
        waitForButtonDisappear(ACCEPT_BUTTON);
    }

    public void waitToClose() {
        wait.until(ExpectedConditions.invisibilityOf(this.webElement));
    }

    public void submit() {
        DelayUtils.waitForNestedElements(wait, webElement, SUBMIT_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(webElement.findElement(By.xpath(SUBMIT_BUTTON))).click().perform();
        waitForButtonDisappear(SUBMIT_BUTTON);
    }

    public void cancel() {
        DelayUtils.waitForNestedElements(wait, webElement, CANCEL_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(webElement.findElement(By.xpath(CANCEL_BUTTON))).click().perform();
        waitForButtonDisappear(CANCEL_BUTTON);
    }

    public void proceed() {
        DelayUtils.waitForNestedElements(wait, webElement, PROCEED_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(PROCEED_BUTTON))))).click()
                .perform();
        waitForButtonDisappear(PROCEED_BUTTON);
    }

    public void clickSave() {
        DelayUtils.waitForNestedElements(wait, webElement, SAVE_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(SAVE_BUTTON)))))
                .click().perform();
        waitForButtonDisappear(SAVE_BUTTON);
    }

    public void clickUpdate() {
        DelayUtils.waitForNestedElements(wait, webElement, UPDATE_BUTTON);
        Actions action = new Actions(driver);
        WebElement foundedElement =
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(UPDATE_BUTTON))));
        action.moveToElement(foundedElement).click().perform();
        waitForButtonDisappear(UPDATE_BUTTON);
    }

    public void clickOK() {
        DelayUtils.waitForNestedElements(wait, webElement, OK_BUTTON);
        Actions action = new Actions(driver);
        WebElement foundedElement =
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(OK_BUTTON))));
        action.moveToElement(foundedElement).click().perform();
        waitForButtonDisappear(OK_BUTTON);
    }

    public void clickDelete() {
        DelayUtils.waitForNestedElements(wait, webElement, DELETE_BUTTON);
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(DELETE_BUTTON)))))
                .click().perform();
        waitForButtonDisappear(DELETE_BUTTON);
    }

    public void clickActionById(String actionId) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(DATA_TEST_ID_XPATH, actionId));
        Actions action = new Actions(driver);
        WebElement foundedElement = wait.until(
                ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(String.format(DATA_TEST_ID_XPATH, actionId)))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundedElement);
        action.moveToElement(foundedElement).click().perform();
        waitForButtonDisappear(String.format(DATA_TEST_ID_XPATH, actionId));
    }

    public void clickButtonByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(BY_TEXT_XPATH, label));
        wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(String.format(BY_TEXT_XPATH, label)))));
        driver.findElement(By.xpath(String.format(BY_TEXT_XPATH, label))).click();
    }

    public void callButtonByLabel(String label) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(webElement, driver, wait);
        buttonContainer.callActionByLabel(label);
    }

    public void callButtonById(String id) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(webElement, driver, wait);
        buttonContainer.callActionById(id);
    }

    public void rolloutById(String id) {
        if (isElementPresent(webElement, By.xpath("//div[contains(@class, 'collapsedRollout')]/div[@" + CSSUtils.TEST_ID + "='" + id + "']"))) {
            webElement.findElement(By.xpath(String.format(DATA_TEST_ID_XPATH, id) + "/span")).click();
        }
    }

    public void skipAndAccept(String acceptButtonId) {
        int stepsNumber = numberOfSteps();
        if (stepsNumber > 1) {
            for (int i = 0; i < stepsNumber - 1; i++) {
                clickNext();
            }
        }
        clickActionById(acceptButtonId);
    }

    public void clickButtonByLabel(String groupLabel, String actionId) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(webElement,driver,wait);
        buttonContainer.callActionById(groupLabel, actionId);
    }

    public int numberOfSteps() {
        if (isStepsPresent()) {
            DelayUtils.waitForNestedElements(wait, webElement, ".//div[@class='simple-progress-bar-item-label']");
            List<WebElement> steps = webElement.findElements(By.xpath(".//div[contains(@class,'simple-progress-bar-item-label')]"));
            return steps.size();
        }
        return 1;
    }


    private boolean isStepsPresent() {
        List<WebElement> steps = this.webElement.findElements(By.xpath("//div[@class='simple-progress-bar']"));
        return !steps.isEmpty();
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void waitForButtonDisappear(String buttonXpath) {
        DelayUtils.waitForButtonDisappear(driver, wait, buttonXpath);
    }
}
