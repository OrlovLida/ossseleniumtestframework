/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.alerts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */

public class SystemMessageContainer implements SystemMessageInterface {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement messageContainer;

    private static final String PATH_TO_CLOSEBUTTON = ".//div[contains(@class,'closeButton')]";
    private static final String PATH_TO_SYSTEM_MESSAGE_CONTAINER = "//div[contains(@class, 'systemMessagesContainer')]";
    private static final String PATH_TO_SYSTEM_MESSAGE_ITEM = "//div[contains(@class,'systemMessageItem')]";
    private static final String DANGER_MESSAGE_TYPE_CLASS = "danger";
    private static final String SUCCESS_MESSAGE_TYPE_CLASS = "success";
    private static final String WARNING_MESSAGE_TYPE_CLASS = "warning";
    private static final String INFO_MESSAGE_TYPE_CLASS = "info";
    private static final String CANNOT_MAP_TO_MESSAGE_EXCEPTION = "Cannot map to message type";

    public enum MessageType {
        DANGER, WARNING, SUCCESS, INFO
    }

    public static SystemMessageInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, PATH_TO_SYSTEM_MESSAGE_CONTAINER);
        WebElement messageContainer = driver.findElement(By.xpath(PATH_TO_SYSTEM_MESSAGE_CONTAINER));
        return new SystemMessageContainer(driver, wait, messageContainer);
    }

    private SystemMessageContainer(WebDriver driver, WebDriverWait wait, WebElement messageContainer) {
        this.driver = driver;
        this.wait = wait;
        this.messageContainer = messageContainer;
    }

    @Override
    public List<Message> getMessages() {
        DelayUtils.waitForNestedElements(wait, messageContainer, PATH_TO_SYSTEM_MESSAGE_ITEM);
        List<WebElement> messageItems = messageContainer.findElements(By.xpath(PATH_TO_SYSTEM_MESSAGE_ITEM));
        return messageItems.stream().map(this::toMessage).collect(Collectors.toList());
    }

    @Override
    public Optional<Message> getFirstMessage() {
        return getMessages().stream().findFirst();
    }

    @Override
    public void close() {
        Actions builder = new Actions(driver);
        builder.moveToElement(messageContainer).build().perform();
        DelayUtils.waitForNestedElements(wait, messageContainer, PATH_TO_CLOSEBUTTON);
        builder.click(messageContainer.findElement(By.xpath(PATH_TO_CLOSEBUTTON))).build().perform();
    }

    private Message toMessage(WebElement messageItem) {
        String text = messageItem.findElement(By.xpath(".//p | .//a")).getText();
        List<String> allClasses = CSSUtils.getAllClasses(messageItem);
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
            }
        }
        throw new RuntimeException(CANNOT_MAP_TO_MESSAGE_EXCEPTION);
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

    @Override
    public void clickMessageLink() {
        DelayUtils.waitForNestedElements(wait, messageContainer, "//div[contains(@class,'systemMessageItem')]");
        messageContainer.findElement(By.xpath(".//a[contains(@href, '#/')]")).click();
    }

    @Override
    public void waitForMessageDisappear() {
        DelayUtils.waitForElementDisappear(wait, driver.findElement(By.xpath(PATH_TO_SYSTEM_MESSAGE_ITEM)));
    }
}
