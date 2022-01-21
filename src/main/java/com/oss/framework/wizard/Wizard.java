package com.oss.framework.wizard;

import java.util.List;

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
import com.oss.framework.widgets.Widget;

public class Wizard {

    private static final String OSS_WINDOW_XPATH = "//div[contains(@class,'OssWindow')]";
    private static final String NEXT_BUTTON_XPATH = ".//button[text()='Next']";
    private static final String NEXT_STEP_XPATH = ".//button[text()='Next Step']";
    private static final String ACCEPT_BUTTON_XPATH = ".//button[text()='Accept']";
    private static final String CANCEL_BUTTON_XPATH = ".//button[text()='Cancel']";
    private static final String DELETE_BUTTON_XPATH = ".//a[text()='Delete']";
    private static final String OK_BUTTON_XPATH = ".//a[text()='OK']";
    private static final String UPDATE_BUTTON_XPATH = ".//a[text()='Update']";
    private static final String SAVE_BUTTON_XPATH = ".//a[text()='Save']";
    private static final String PROCEED_BUTTON_XPATH = ".//a[text()='Proceed']";
    private static final String BY_TEXT_PATTERN = "//*[text()='%s']";
    private static final String BY_DATA_TEST_ID_PATTERN = "//*[@" + CSSUtils.TEST_ID + "='%s']";
    private static final String WIZARD_STEPS_XPATH = ".//div[@class='simple-progress-bar-step-label']";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private Wizard(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    @Deprecated
    public static Wizard createWizard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, OSS_WINDOW_XPATH);
        WebElement webElement = driver.findElement(By.xpath(OSS_WINDOW_XPATH));
        return new Wizard(driver, wait, webElement);
    }

    public static Wizard createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        Widget.waitForWidgetById(wait, componentId);
        WebElement webElement = driver.findElement(By.xpath(String.format(BY_DATA_TEST_ID_PATTERN, componentId)));
        return new Wizard(driver, wait, webElement);
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

    public Input setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(BY_DATA_TEST_ID_PATTERN, componentId));
        Input input = getComponent(componentId, componentType);
        input.setSingleStringValue(value);
        return input;
    }

    public void clickNext() {
        clickButton(NEXT_BUTTON_XPATH);
    }

    public void clickNextStep() {
        clickButton(NEXT_STEP_XPATH);
    }

    public void clickAccept() {
        clickButton(ACCEPT_BUTTON_XPATH);
    }

    public void clickCancel() {
        clickButton(CANCEL_BUTTON_XPATH);
    }

    public void clickProceed() {
        clickButton(PROCEED_BUTTON_XPATH);
    }

    public void clickSave() {
        clickButton(SAVE_BUTTON_XPATH);
    }

    public void clickUpdate() {
        clickButton(UPDATE_BUTTON_XPATH);
    }

    public void clickOK() {
        clickButton(OK_BUTTON_XPATH);
    }

    public void clickDelete() {
        clickButton(DELETE_BUTTON_XPATH);
    }

    public void clickButtonById(String actionId) {
        clickButton(String.format(BY_DATA_TEST_ID_PATTERN, actionId));
    }

    public void clickButtonByLabel(String label) {
        clickButton(String.format(BY_TEXT_PATTERN, label));
    }

    public void clickButtonById(String groupId, String actionId) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(webElement, driver, wait);
        buttonContainer.callActionById(groupId, actionId);
    }

    public void waitToClose() {
        wait.until(ExpectedConditions.invisibilityOf(this.webElement));
    }

    public void rolloutById(String id) {
        if (isElementPresent(webElement, By.xpath("//div[contains(@class, 'collapsedRollout')]/div[@" + CSSUtils.TEST_ID + "='" + id + "']"))) {
            webElement.findElement(By.xpath(String.format(BY_DATA_TEST_ID_PATTERN, id) + "/span")).click();
        }
    }

    public void skipAndAccept(String acceptButtonId) {
        int stepsNumber = countNumberOfSteps();
        if (stepsNumber > 1) {
            for (int i = 1; i < stepsNumber; i++) {
                clickNext();
                DelayUtils.waitForLoadBars(wait, webElement);
            }
        }
        clickButtonById(acceptButtonId);
    }

    public int countNumberOfSteps() {
        if (isStepsPresent()) {
            DelayUtils.waitForNestedElements(wait, webElement, WIZARD_STEPS_XPATH);
            List<WebElement> steps = webElement.findElements(By.xpath(WIZARD_STEPS_XPATH));
            return steps.size();
        }
        return 1;
    }

    private void clickButton(String xpath) {
        DelayUtils.waitForNestedElements(wait, webElement, xpath);
        WebElement button = webElement.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        Actions actions = new Actions(driver);
        actions.moveToElement(button).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(button));
        actions.click(button).build().perform();
    }

    private boolean isStepsPresent() {
        List<WebElement> steps = this.webElement.findElements(By.xpath("//div[@class='simple-progress-bar']"));
        return !steps.isEmpty();
    }
}
