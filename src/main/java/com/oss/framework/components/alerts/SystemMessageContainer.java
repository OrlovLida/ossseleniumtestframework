/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.alerts;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Gabriela Kasza
 */

public class SystemMessageContainer implements SystemMessageInterface {

    private static final Logger log = LoggerFactory.getLogger(SystemMessageContainer.class);
    private static final String CLOSE_SINGLE_MESSAGE_BUTTON = ".//*[contains(@class,'closeButton')]";
    private static final String CLOSE_MESSAGE_CONTAINER_BUTTON = ".//i[@aria-label='Close']";
    private static final String PATH_TO_SHOW_MESSAGES = ".//i[@aria-label='Show/Hide messages' and contains(@class, 'down')]";
    private static final String PATH_TO_SYSTEM_MESSAGE_CONTAINER = "//div[contains(@class, 'systemMessagesContainer')]";
    private static final String MESSAGE_XPATH = ".//p | .//a";
    private static final String SYSTEM_MESSAGE_ITEM_CSS = "div.systemMessageItem";
    private static final String SYSTEM_MESSAGE_ITEM_CLASS = "systemMessageItem";
    private static final String DANGER_MESSAGE_TYPE_CLASS = "danger";
    private static final String SUCCESS_MESSAGE_TYPE_CLASS = "success";
    private static final String WARNING_MESSAGE_TYPE_CLASS = "warning";
    private static final String INFO_MESSAGE_TYPE_CLASS = "info";
    private static final String CANNOT_MAP_TO_MESSAGE_EXCEPTION = "Cannot map to message type.";
    private static final String NO_MESSAGE_TEXT_EXCEPTION = "Cannot get text from system message.";
    private static final String TEXT_CONTAINER_CSS = ".textContainer";
    private static final String MESSAGE_LINK_URL_XPATH = ".//a[contains(@href, '#')]";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement messageContainer;

    private SystemMessageContainer(WebDriver driver, WebDriverWait wait, WebElement messageContainer) {
        this.driver = driver;
        this.wait = wait;
        this.messageContainer = messageContainer;
    }

    public static SystemMessageContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPresence(wait, By.xpath(PATH_TO_SYSTEM_MESSAGE_CONTAINER));
        WebElement messageContainer = driver.findElement(By.xpath(PATH_TO_SYSTEM_MESSAGE_CONTAINER));
        Actions builder = new Actions(driver);
        builder.moveToElement(messageContainer).build().perform();
        return new SystemMessageContainer(driver, wait, messageContainer);
    }

    @Override
    public List<Message> getMessages() {
        log.debug("Starting getting messages");
        try {
            DelayUtils.waitBy(wait, By.className(SYSTEM_MESSAGE_ITEM_CLASS));
            expandSystemMessagesContainer();
            List<WebElement> messageItems = driver.findElements(By.cssSelector(SYSTEM_MESSAGE_ITEM_CSS));
            log.debug("Found {} system messages", messageItems.size());
            return messageItems.stream().map(this::toMessage).collect(Collectors.toList());
        } catch (TimeoutException e) {
            log.debug("No system message found");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Message> getFirstMessage() {
        return getMessages().stream().findFirst();
    }

    @Override
    public void close() {
        if (!messageContainer.findElements(By.xpath(CLOSE_MESSAGE_CONTAINER_BUTTON)).isEmpty()) {
            tryToClose(CLOSE_MESSAGE_CONTAINER_BUTTON);
        } else {
            tryToClose(CLOSE_SINGLE_MESSAGE_BUTTON);
        }
    }

    @Override
    public void clickMessageLink() {
        DelayUtils.waitForPresence(wait, By.cssSelector(SYSTEM_MESSAGE_ITEM_CSS));
        List<WebElement> webElements = messageContainer.findElements(By.xpath(MESSAGE_LINK_URL_XPATH));
        if (!webElements.isEmpty()) {
            webElements.get(0).click();
        }
    }

    public Optional<String> getLinkURL() {
        DelayUtils.waitForPresence(wait, By.cssSelector(SYSTEM_MESSAGE_ITEM_CSS));
        List<WebElement> webElements = messageContainer.findElements(By.xpath(MESSAGE_LINK_URL_XPATH));
        if (!webElements.isEmpty()) {
            return Optional.ofNullable(webElements.get(0).getAttribute("href"));
        } else
            return Optional.empty();
    }

    @Override
    public void waitForMessageDisappear() {
        DelayUtils.waitForElementDisappear(wait, driver.findElement(By.className(SYSTEM_MESSAGE_ITEM_CLASS)));
    }

    public List<String> getErrors() {
        log.debug("Checking errors");
        DelayUtils.waitForPageToLoad(driver, wait);
        expandSystemMessagesContainer();
        List<WebElement> messageItems = driver.findElements(By.cssSelector(SYSTEM_MESSAGE_ITEM_CSS));
        log.debug("Found {} system messages", messageItems.size());
        List<Message> messages = messageItems.stream().map(this::toMessage).collect(Collectors.toList()).stream()
                .filter(message -> message.getMessageType().equals(SystemMessageContainer.MessageType.DANGER)).collect(Collectors.toList());
        log.debug("Found {} error messages", messages.size());
        return messages.stream().map(Message::getText).collect(Collectors.toList());
    }

    public void expandSystemMessagesContainer() {
        if (!messageContainer.findElements(By.xpath(PATH_TO_SHOW_MESSAGES)).isEmpty()) {
            log.debug("Clicking show button in system message");
            Actions builder = new Actions(driver);
            builder.click(messageContainer.findElement(By.xpath(PATH_TO_SHOW_MESSAGES))).build().perform();
            DelayUtils.waitForPresence(wait, By.cssSelector(SYSTEM_MESSAGE_ITEM_CSS));
        }
    }

    private void tryToClose(String closeButtonXpath) {
        try {
            log.debug("Closing system message");
            Actions builder = new Actions(driver);
            builder.moveToElement(messageContainer).moveByOffset(100, 20).build().perform();
            DelayUtils.sleep(100);
            builder.moveToElement(messageContainer).build().perform();
            DelayUtils.waitForNestedElements(new WebDriverWait(driver, Duration.ofSeconds(5)), messageContainer, closeButtonXpath);
            builder.click(messageContainer.findElement(By.xpath(closeButtonXpath))).build().perform();
            log.debug("System message closed");
        } catch (NoSuchElementException | TimeoutException e) {
            log.warn("Cannot click close button in system message");
        }
    }

    private Message toMessage(WebElement messageItem) {
        DelayUtils.waitForPresence(wait, By.cssSelector(TEXT_CONTAINER_CSS));
        List<WebElement> messagesList = messageItem.findElements(By.xpath(MESSAGE_XPATH));
        List<String> allClasses = CSSUtils.getAllClasses(messageItem);
        if (!messagesList.isEmpty()) {
            log.debug("Message list contains {} message items.", messagesList.size());
            WebElement message =
                    messagesList.stream().findFirst().orElseThrow(() -> new java.util.NoSuchElementException(NO_MESSAGE_TEXT_EXCEPTION));
            String text = message.getText();
            return new Message(text, mapToMassageType(allClasses));
        }
        String text = messageItem.findElement(By.cssSelector(TEXT_CONTAINER_CSS)).getText();
        return new Message(text, mapToMassageType(allClasses));
    }

    private MessageType mapToMassageType(List<String> classes) {
        for (String cssClass : classes) {
            switch (cssClass) {
                case SUCCESS_MESSAGE_TYPE_CLASS: {
                    return MessageType.SUCCESS;
                }
                case DANGER_MESSAGE_TYPE_CLASS: {
                    return MessageType.DANGER;
                }
                case INFO_MESSAGE_TYPE_CLASS: {
                    return MessageType.INFO;
                }
                case WARNING_MESSAGE_TYPE_CLASS: {
                    return MessageType.WARNING;
                }
                default:
            }
        }
        throw new IllegalArgumentException(CANNOT_MAP_TO_MESSAGE_EXCEPTION);
    }

    public enum MessageType {
        DANGER, WARNING, SUCCESS, INFO
    }

    public static class Message {

        private final String text;
        private final MessageType messageType;

        private Message(String text, MessageType messageType) {
            this.text = text;
            this.messageType = messageType;
        }

        public String getText() {
            return text;
        }

        public MessageType getMessageType() {
            return messageType;
        }
    }
}
