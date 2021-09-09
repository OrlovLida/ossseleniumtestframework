package com.oss.framework.mainheader;

public interface NotificationsInterface {

    String waitAndGetFinishedNotificationText();

    void waitForSpecificNotification(String text, String notificationStatus);

    void clearAllNotification();

    int getAmountOfNotifications();

    void openDetailsForSpecificNotification(String text, String notificationStatus);

    void clickDownloadFile();
}
