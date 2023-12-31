package com.oss.framework.components.layout;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;

public class Card {

    private static final Logger log = LoggerFactory.getLogger(Card.class);
    private static final String MAXIMIZE_CHART_BUTTON_XPATH = ".//*[contains(@" + CSSUtils.TEST_ID + ", 'maximize')]";
    private static final String MINIMIZE_CHART_BUTTON_XPATH = ".//*[contains(@" + CSSUtils.TEST_ID + ", 'minimize')]";
    private static final String CARD_CONTAINER_NAME_PATTERN = ".//div[contains(@class, 'simple-card-container')]//*[contains(text(), '%s')]";
    private static final String ACTIONS_CONTAINER_CSS = ".actionsContainer";
    private static final String WINDOW_TOOLBAR_CSS = ".windowToolbar";
    private static final String CONTEXT_ACTIONS_CSS = WINDOW_TOOLBAR_CSS + "," + ACTIONS_CONTAINER_CSS;
    private static final String CARD_HEADER_LABEL_CSS = ".card-header__label";
    private static final String RESIZABLE_HANDLE_CSS = ".react-resizable-handle";
    private static final String GO_TO_PARENT_XPATH = "..";
    private static final String CARD_HEADER_LABEL_CLASS = "card-header__label";

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
        WebElementUtils.clickWebElement(driver, resizeButton);
        log.debug("Clicking maximize button");
    }

    public void minimizeCard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = cardElement.findElement(By.xpath(MINIMIZE_CHART_BUTTON_XPATH));
        WebElementUtils.clickWebElement(driver, resizeButton);
        log.debug("Clicking minimize button");
    }

    public boolean isCardMaximized() {
        return !cardElement.findElements(By.xpath(MINIMIZE_CHART_BUTTON_XPATH)).isEmpty();
    }

    public void callAction(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
    }

    public void callActionById(String actionId) {
        getActionsInterface().callActionById(actionId);
    }

    public void callActionById(String groupId, String actionId) {
        getActionsInterface().callActionById(groupId, actionId);
    }

    public String getCardName() {
        return cardElement.findElement(By.cssSelector(CARD_HEADER_LABEL_CSS)).getText();
    }

    public String getCardId() {
        return cardElement.getAttribute(CSSUtils.TEST_ID);
    }

    public void resizeCard(int xOffset, int yOffset) {
        DragAndDrop.dragAndDrop(getResizableHandle(), xOffset, yOffset, driver);
    }

    public void changeCardOrder(int xOffset, int yOffset) {
        DragAndDrop.dragAndDrop(getHeader(), xOffset, yOffset, driver);
    }

    public int getWidthCard() {
        return CSSUtils.getWidthValue(cardElement);
    }

    public int getHeightCard() {
        return CSSUtils.getHeightValue(cardElement);
    }

    private DragAndDrop.DraggableElement getHeader() {
        WebElement element = cardElement.findElement(By.className(CARD_HEADER_LABEL_CLASS));
        return new DragAndDrop.DraggableElement(element);
    }

    private DragAndDrop.DraggableElement getResizableHandle() {
        WebElementUtils.moveToElement(driver, cardElement);
        WebElement resizableHandle = cardElement.findElement(By.xpath(GO_TO_PARENT_XPATH)).findElement(By.cssSelector(RESIZABLE_HANDLE_CSS));
        return new DragAndDrop.DraggableElement(resizableHandle);
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForPresence(wait, By.cssSelector(CONTEXT_ACTIONS_CSS));
        boolean isNewActionContainer = isElementPresent(cardElement, By.cssSelector(ACTIONS_CONTAINER_CSS));
        boolean isOldActionContainer = isElementPresent(cardElement, By.cssSelector(WINDOW_TOOLBAR_CSS));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(cardElement, driver, wait);
        } else if (isOldActionContainer)
            return OldActionsContainer.createFromParent(driver, wait, cardElement);
        else {
            return ButtonContainer.createFromParent(cardElement, driver, wait);
        }
    }

    private boolean isElementPresent(WebElement webElement, By by) {
        return WebElementUtils.isElementPresent(webElement, by);
    }
}
