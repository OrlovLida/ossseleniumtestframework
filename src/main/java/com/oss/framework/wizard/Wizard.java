package com.oss.framework.wizard;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class Wizard {

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
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
    private static final String ANCESTOR_XPATH = ".//ancestor::div[contains(@class,'card-shadow')]";
    private static final String CARD_HEADER_LABEL_CSS = ".card-header__label";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;
    private final String wizardId;

    private Wizard(WebDriver driver, WebDriverWait wait, WebElement webElement, String wizardId) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
        this.wizardId = wizardId;
    }

    public static Wizard createByComponentId(WebDriver driver, WebDriverWait wait, String wizardId) {
        Widget.waitForWidgetById(wait, wizardId);
        WebElement webElement = driver.findElement(By.xpath(String.format(BY_DATA_TEST_ID_PATTERN, wizardId)));
        return new Wizard(driver, wait, webElement, wizardId);
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        return WebElementUtils.isElementPresent(webElement, by);
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.createFromParent(componentId, componentType, this.driver, this.wait, this.webElement);
    }

    public Input getComponent(String componentId) {
        return ComponentFactory.createFromParent(componentId, this.driver, this.wait, this.webElement);
    }

    public Input setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(BY_DATA_TEST_ID_PATTERN, componentId));
        Input input = getComponent(componentId, componentType);
        input.setSingleStringValue(value);
        return input;
    }

    public Input setComponentValue(String componentId, String value) {
        DelayUtils.waitForNestedElements(wait, webElement, String.format(BY_DATA_TEST_ID_PATTERN, componentId));
        Input input = getComponent(componentId);
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
        if (isElementPresent(webElement,
                By.xpath("//div[contains(@class, 'collapsedRollout')]/div[@" + CSSUtils.TEST_ID + "='" + id + "']"))) {
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

    public TableComponent getTableComponent() {
        return TableComponent.create(driver, wait, wizardId);
    }

    public TableComponent getTableComponent(String tableComponentId) {
        return TableComponent.createById(driver, wait, tableComponentId);
    }

    public String getWizardName() {
        return webElement.findElement(By.xpath(ANCESTOR_XPATH)).findElement(By.cssSelector(CARD_HEADER_LABEL_CSS)).getText();
    }

    private void clickButton(String xpath) {
        DelayUtils.waitForNestedElements(wait, webElement, xpath);
        WebElement button = webElement.findElement(By.xpath(xpath));
        WebElementUtils.clickWebElement(driver, button);
    }

    public String getButtonLabel(String id) {
        String xpath = String.format(BY_DATA_TEST_ID_PATTERN, id);
        DelayUtils.waitForNestedElements(wait, webElement, xpath);
        WebElement button = webElement.findElement(By.xpath(xpath));
        WebElementUtils.moveToElement(driver, button);
        return button.getAttribute(TEXT_CONTENT_ATTRIBUTE);
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

    public List<String> getWizardStepsTitles() {
        if (isStepsPresent())
            return getWizardSteps().stream().map(WebElement::getText).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    public boolean isNextStepPresent() {
        List<WebElement> steps = getWizardSteps();
        WebElement currentStep = getCurrentStep();
        return (steps.size() > steps.indexOf(currentStep) + 1);
    }

    public boolean isElementPresentById(String id) {
        return isElementPresent(webElement, By.xpath(".//*[@" + CSSUtils.TEST_ID + "='" + id + "']"));
    }

    public void waitForWizardToLoad() {
        DelayUtils.waitForElementToLoad(wait, webElement);
    }

}
