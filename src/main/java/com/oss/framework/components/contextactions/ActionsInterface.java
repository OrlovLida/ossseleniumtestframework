package com.oss.framework.components.contextactions;

public interface ActionsInterface {

    void callAction(String actionId);

    void callActionByLabel(String label);

    void callAction(String groupId, String actionId);

    void callActionByLabel(String groupLabel, String actionLabel);

    void callActionById(String id);

    void callActionById(String groupId, String actionId);

}
