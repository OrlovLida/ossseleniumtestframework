package com.oss.framework.alerts;

import java.util.List;
import java.util.Optional;

import com.oss.framework.alerts.SystemMessageContainer.Message;

public interface SystemMessageInterface {

    List<Message> getMessages();

    Optional<Message> getFirstMessage();

    void close();

    void clickMessageLink();
    
}
