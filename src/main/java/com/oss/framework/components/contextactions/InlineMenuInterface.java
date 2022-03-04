package com.oss.framework.components.contextactions;

public interface InlineMenuInterface {

    void callAction(String actionId);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String actionLabel);

    void callActionByLabel(String groupLabel, String actionLabel);

}
