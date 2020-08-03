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

    public enum MessageType {
        DANGER, WARNING, SUCCESS, INFO
    }

    public static SystemMessageContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class, 'systemMessagesContainer')]");
        WebElement messageContainer = driver.findElement(By.xpath("//div[contains(@class, 'systemMessagesContainer')]"));
        return new SystemMessageContainer(driver, wait, messageContainer);
    }

    private SystemMessageContainer(WebDriver driver, WebDriverWait wait, WebElement messageContainer) {
        this.driver = driver;
        this.wait = wait;
        this.messageContainer = messageContainer;
    }

    @Override
    public List<Message> getMessages() {
        DelayUtils.waitForNestedElements(wait, messageContainer, "//div[contains(@class,'systemMessageItem')]");
        List<WebElement> messageItems = messageContainer.findElements(By.xpath("//div[contains(@class,'systemMessageItem')]"));
        return messageItems.stream().map(this::toMessage).collect(Collectors.toList());
    }

    @Override
    public Optional<Message> getFirstMessage() {
        return getMessages().stream().findFirst();
    }

    private Message toMessage(WebElement messageItem) {
        String text = messageItem.findElement(By.xpath(".//p")).getText();
        List<String> allClasses = CSSUtils.getAllClasses(messageItem);
        return new Message(text, mapToMassageType(allClasses));
    }

    private MessageType mapToMassageType(List<String> classes) {
        for (String cssClass : classes) {
            switch (cssClass) {
                case "success": {
                    return MessageType.SUCCESS;
                }
                case "danger": {
                    return MessageType.DANGER;
                }
                case "info": {
                    return MessageType.INFO;
                }
                case "warning": {
                    return MessageType.WARNING;
                }
            }
        }
        throw new RuntimeException("Cannot map to message type");
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
