package com.oss.framework.components.alerts;

import java.util.List;
import java.util.NoSuchElementException;

public class Message {
    private static final String DANGER_MESSAGE_TYPE_CLASS = "danger";
    private static final String SUCCESS_MESSAGE_TYPE_CLASS = "success";
    private static final String WARNING_MESSAGE_TYPE_CLASS = "warning";
    private static final String INFO_MESSAGE_TYPE_CLASS = "info";
    private static final String ERROR = "error";
    private static final String CANNOT_MAP_TO_MESSAGE_TYPE_EXCEPTION = "Cannot map to message type.";

    private final String text;
    private final List<String> messageTypeClasses;

    private Message(String text, List<String> messageTypeClasses) {
        this.text = text;
        this.messageTypeClasses = messageTypeClasses;
    }

    public static Message create(String text, List<String> messageTypeClasses) {
        return new Message(text, messageTypeClasses);
    }

    public String getText() {
        return text;
    }

    public MessageType getMessageType() {
        return mapToMessageType();
    }

    private MessageType mapToMessageType() {
        for (String cssClass : messageTypeClasses) {
            if (cssClass.contains(WARNING_MESSAGE_TYPE_CLASS)) {
                return MessageType.WARNING;
            }
            if (cssClass.contains(DANGER_MESSAGE_TYPE_CLASS) || cssClass.contains(ERROR)) {
                return MessageType.DANGER;
            }
            if (cssClass.contains(INFO_MESSAGE_TYPE_CLASS)) {
                return MessageType.INFO;
            }
            if (cssClass.contains(SUCCESS_MESSAGE_TYPE_CLASS)) {
                return MessageType.SUCCESS;
            }
        }
        throw new NoSuchElementException(CANNOT_MAP_TO_MESSAGE_TYPE_EXCEPTION);
    }
}

