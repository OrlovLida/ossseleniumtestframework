package com.oss.framework.mainheader;

public interface NotificationsInterface {

    String waitAndGetFinishedNotificationText();

    void waitForSpecificNotification(String text, String notificationType);

    void clearAllNotification();

    int getAmountOfNotifications();
}
