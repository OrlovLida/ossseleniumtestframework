package com.oss.framework.components.alerts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class GlobalNotificationContainer {

    private static final String NOTIFICATION_CONTAINER_CLASS = "globalNotificationContainer";
    private static final String LI_CSS = "li";
    private static final String ERROR_TYPE_CLASS = "errorType";

    private WebElement notificationContainer;

    private GlobalNotificationContainer(WebElement notificationContainer) {
        this.notificationContainer = notificationContainer;
    }

    public static GlobalNotificationContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPresence(wait, By.className(NOTIFICATION_CONTAINER_CLASS));
        WebElement notificationContainer = driver.findElement(By.className(NOTIFICATION_CONTAINER_CLASS));
        return new GlobalNotificationContainer(notificationContainer);
    }

    public NotificationInformation getNotificationInformation() {
        return new NotificationInformation(notificationContainer);
    }

    public boolean isErrorNotificationPresent() {
        if (isNotificationPresent()) {
            return getNotificationInformation().getType().equals(ERROR_TYPE_CLASS);
        }
        return false;
    }

    private boolean isNotificationPresent() {
        return WebElementUtils.isElementPresent(notificationContainer, By.cssSelector(LI_CSS));
    }

    public static class NotificationInformation {

        private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
        private static final String MESSAGE_CLASS = "message-text";
        private static final String CLASS = "class";

        private final WebElement notification;

        private NotificationInformation(WebElement notificationContainer) {
            this.notification = notificationContainer.findElement(By.cssSelector(LI_CSS));
        }

        public String getType() {
            return notification.getAttribute(CLASS);
        }

        public String getMessage() {
            return notification.findElement(By.className(MESSAGE_CLASS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }
    }
}
