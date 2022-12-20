package com.oss.framework.components.alerts;

import com.oss.framework.components.alerts.SystemMessageContainer.Message;

import java.util.List;
import java.util.Optional;

public interface SystemMessageInterface {

    List<Message> getMessages();

    Optional<Message> getFirstMessage();

    void close();

    void clickMessageLink();

    void waitForMessageDisappear();

}
