package com.oss.framework.mainheader;

public interface NotificationsInterface {

    String getNotificationMessage();

    void waitForNotification(String text, String notificationStatus);

    void clearAllNotification();

    int countNotifications();

    void openDetails(String text, String notificationStatus);

    void clickDownloadFile();
}
