package com.oss.framework.wizard;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class Wizard {

    private static final String OSS_WINDOW_XPATH = "//div[contains(@class,'OssWindow')]";
    private static final String NEXT_BUTTON_XPATH = ".//button[text()='Next']";
    private static final String NEXT_STEP_XPATH = ".//button[text()='Next Step']";
    private static final String ACCEPT_BUTTON_XPATH = ".//button[text()='Accept']";
    private static final String CANCEL_BUTTON_XPATH = ".//button[text()='Cancel']";
    private static final String OK_BUTTON_XPATH = ".//a[text()='OK']";
    private static final String UPDATE_BUTTON_XPATH = ".//a[text()='Update']";
    private static final String SAVE_BUTTON_XPATH = ".//a[text()='Save']";
    private static final String PROCEED_BUTTON_XPATH = ".//a[text()='Proceed']";
    private static final String BY_TEXT_PATTERN = "//*[text()='%s']";
    private static final String BY_DATA_TEST_ID_PATTERN = "//*[@" + CSSUtils.TEST_ID + "='%s']";
    private static final String WIZARD_STEPS_CSS = ".simple-progress-bar-step";
    private static final String ACTIVE_STEP_CSS = ".simple-progress-bar-step--active";
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
        return WebElementUtils.isElementPresent(webElement, by);
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

    public void clickAcceptOldWizard() {
        DelayUtils.waitForNestedElements(wait, webElement, ACCEPT_BUTTON_XPATH);
        WebElement button = webElement.findElement(By.xpath(ACCEPT_BUTTON_XPATH));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        Actions actions = new Actions(driver);
        actions.moveToElement(button).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(button));
        actions.moveToElement(button).clickAndHold(button).perform();
        DelayUtils.sleep();
        actions.release().perform();
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
            return getWizardSteps().size();
        }
        return 1;
    }

    public TreeComponent getTreeComponent() {
        return TreeComponent.create(driver, wait, webElement);
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

    private List<WebElement> getWizardSteps() {
        DelayUtils.waitForNestedElements(wait, webElement, By.cssSelector(WIZARD_STEPS_CSS));
        return this.webElement.findElements(By.cssSelector(WIZARD_STEPS_CSS));
    }

    private WebElement getCurrentStep() {
        DelayUtils.waitForNestedElements(wait, webElement, By.cssSelector(WIZARD_STEPS_CSS));
        return this.webElement.findElement(By.cssSelector(ACTIVE_STEP_CSS));
    }

    public String getCurrentStepTitle() {
        return getCurrentStep().getText();
    }

    public boolean isNextStepPresent() {
        List<WebElement> steps = getWizardSteps();
        WebElement currentStep = getCurrentStep();
        return (steps.size() > steps.indexOf(currentStep) + 1);
    }

    public boolean isElementPresentById(String id) {
        return isElementPresent(webElement, By.xpath(".//*[@" + CSSUtils.TEST_ID + "='" + id + "']"));
    }

}
