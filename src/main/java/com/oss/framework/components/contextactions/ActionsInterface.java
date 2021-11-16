package com.oss.framework.components.contextactions;

public interface ActionsInterface {

    void callActionByLabel(String label);

    void callActionByLabel(String groupLabel, String actionLabel);

    void callActionById(String id);

    void callActionById(String groupId, String actionId);
}
