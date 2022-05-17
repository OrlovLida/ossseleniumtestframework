package com.oss.framework.components.layout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class Card {

    private static final Logger log = LoggerFactory.getLogger(Card.class);
    private static final String MAXIMIZE_CHART_BUTTON_XPATH = ".//*[contains(@" + CSSUtils.TEST_ID + ", 'maximize')]";
    private static final String MINIMIZE_CHART_BUTTON_XPATH = ".//*[contains(@" + CSSUtils.TEST_ID + ", 'minimize')]";
    private static final String CARD_CONTAINER_NAME_PATTERN = ".//div[contains(@class, 'simple-card-container')]//*[contains(text(), '%s')]";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement cardElement;

    private Card(WebDriver driver, WebDriverWait wait, WebElement cardElement) {
        this.driver = driver;
        this.wait = wait;
        this.cardElement = cardElement;
    }

    public static Card createCard(WebDriver driver, WebDriverWait wait, String windowId) {
        DelayUtils.waitByXPath(wait, ".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]");
        WebElement card = driver.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + windowId + "']"));
        return new Card(driver, wait, card);
    }

    public static Card createCardByName(WebDriver driver, WebDriverWait wait, String cardName) {
        DelayUtils.waitByXPath(wait, String.format(CARD_CONTAINER_NAME_PATTERN, cardName));
        WebElement card = driver.findElement(By.xpath(String.format(CARD_CONTAINER_NAME_PATTERN, cardName)));
        return new Card(driver, wait, card);
    }

    public void maximizeCard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = cardElement.findElement(By.xpath(MAXIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking maximize button");
    }

    public void minimizeCard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = cardElement.findElement(By.xpath(MINIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking minimize button");
    }

    public boolean isCardMaximized() {
        return !cardElement.findElements(By.xpath(MINIMIZE_CHART_BUTTON_XPATH)).isEmpty();
    }

    public void clickContextAction(String actionLabel) {
        getOldActionsContainer().callActionByLabel(actionLabel);
    }

    public void clickContextActionById(String actionId) {
        getOldActionsContainer().callActionById(actionId);
    }

    private OldActionsContainer getOldActionsContainer() {
        return OldActionsContainer.createFromParent(driver, wait, cardElement);
    }
}
