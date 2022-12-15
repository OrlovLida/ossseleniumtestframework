package com.oss.framework.widgets.editconfigurationpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;

public class EditConfigurationPanel {

    private static final String EDIT_CONFIGURATION_PAGE_CSS = ".edit-configuration-page";
    private static final String ACCEPT_BUTTON_ID_PATTERN = "configuration-header-submit-button-%s";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String cardId;
    private final WebElement configurationPanel;

    private EditConfigurationPanel(WebDriver driver, WebDriverWait wait, String cardId, WebElement configurationPanel) {
        this.driver = driver;
        this.wait = wait;
        this.cardId = cardId;
        this.configurationPanel = configurationPanel;
    }

    public static EditConfigurationPanel create(WebDriver driver, WebDriverWait wait, String cardId) {
        WebElement card = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(cardId)));
        WebElement editConfigurationPage = card.findElement(By.cssSelector(EDIT_CONFIGURATION_PAGE_CSS));
        return new EditConfigurationPanel(driver, wait, cardId, editConfigurationPage);
    }

    public void setComponentValue(String value, String componentId) {
        Input input = ComponentFactory.createFromParent(componentId, driver, wait, configurationPanel);
        input.setSingleStringValue(value);
    }

    public void clickAccept() {
        driver.findElement(By.cssSelector(String.format(ACCEPT_BUTTON_ID_PATTERN, cardId)));
    }

}
