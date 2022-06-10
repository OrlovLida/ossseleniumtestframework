package com.oss.framework.components.prompts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class Popup {

    private static final String POPUP_CSS_SELECTOR = ".popupContainer,.prompt-view";
    private static final String POPUP_TITLE_XPATH = ".//span[@class='popupTitle']";
    private static final String BUTTON_BY_LABEL_PATTERN = ".//a[contains(text(),'%s')]";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebElement webElement;

    private Popup(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.wait = wait;
        this.driver = driver;
        this.webElement = webElement;
    }

    public static Popup create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.cssSelector(POPUP_CSS_SELECTOR));
        WebElement webElement = driver.findElement(By.cssSelector(POPUP_CSS_SELECTOR));
        return new Popup(driver, wait, webElement);
    }

    public String getPopupTitle() {
        return this.webElement.findElement(By.xpath(POPUP_TITLE_XPATH)).getText();
    }

    public Input setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        DelayUtils.waitForNestedElements(wait, webElement, "//*[@" + CSSUtils.TEST_ID + "='" + componentId + "']");
        Input input = getComponent(componentId, componentType);
        input.setSingleStringValue(value);
        return input;
    }

    public void deleteComponentValue(String componentId, Input.ComponentType componentType) {
        DelayUtils.waitForNestedElements(wait, webElement, "//*[@" + CSSUtils.TEST_ID + "='" + componentId + "']");
        Input input = getComponent(componentId, componentType);
        input.clear();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

    public TreeComponent getTreeComponent() {
        return TreeComponent.create(driver, wait, webElement);
    }

    public void clickButtonByLabel(String label) {
        DelayUtils.waitForNestedElements(this.wait, this.webElement, String.format(BUTTON_BY_LABEL_PATTERN, label));
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.webElement.findElement(By.xpath(String.format(BUTTON_BY_LABEL_PATTERN, label)))));
        button.click();
        wait.until(ExpectedConditions.invisibilityOf(button));
    }
}