package com.oss.framework.components.alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class GlobalNotificationContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalNotificationContainer.class);
    private static final String NOTIFICATION_CONTAINER_CLASS = "globalNotificationContainer";
    private static final String LI_CSS = "li";
    private static final String ERROR_TYPE_CLASS = "errorType";
    private static final String NO_NOTIFICATION_EXCEPTION = "No notification is present.";

    private final List<WebElement> notificationContainers;
    private final WebDriver driver;

    private GlobalNotificationContainer(WebDriver driver, List<WebElement> notificationContainers) {
        this.notificationContainers = notificationContainers;
        this.driver = driver;
    }

    public static GlobalNotificationContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPresence(wait, By.className(NOTIFICATION_CONTAINER_CLASS));
        List<WebElement> notificationContainers = driver.findElements(By.className(NOTIFICATION_CONTAINER_CLASS));
        return new GlobalNotificationContainer(driver, notificationContainers);
    }

    public static GlobalNotificationContainer createFromParent(WebDriver driver, WebElement parent, WebDriverWait wait) {
        DelayUtils.waitForNestedElements(wait, parent, By.className(NOTIFICATION_CONTAINER_CLASS));
        List<WebElement> notificationContainers = parent.findElements(By.className(NOTIFICATION_CONTAINER_CLASS));
        return new GlobalNotificationContainer(driver, notificationContainers);
    }

    /**
     * @deprecated method will be removed in 4.0.x release, please use method createFromParent(WebDriver driver, WebElement parent, WebDriverWait wait)
     */
    @Deprecated
    public static GlobalNotificationContainer createFromParent(WebElement parent, WebDriverWait wait) {
        DelayUtils.waitForNestedElements(wait, parent, By.className(NOTIFICATION_CONTAINER_CLASS));
        List<WebElement> notificationContainers = parent.findElements(By.className(NOTIFICATION_CONTAINER_CLASS));
        return new GlobalNotificationContainer(null, notificationContainers);
    }

    public NotificationInformation getNotificationInformation() {
        return getNotificationInformations().stream().findFirst().orElseThrow(() -> new NoSuchElementException(NO_NOTIFICATION_EXCEPTION));
    }

    public List<NotificationInformation> getNotificationInformations() {
        List<NotificationInformation> notificationInformations = new ArrayList<>(Collections.emptyList());
        List<WebElement> getNotificationsList = getNotificationsList();
        if (!getNotificationsList.isEmpty()) {
            for (WebElement notificationContainerWithMessage : getNotificationsList) {
                notificationInformations.add(new NotificationInformation(driver, notificationContainerWithMessage));
            }
        }
        return notificationInformations;
    }

    public boolean isErrorNotificationPresent() {
        List<NotificationInformation> notificationInformations = getNotificationInformations();
        if (!notificationInformations.isEmpty()) {
            return isErrorPresent(notificationInformations);
        }
        return false;
    }

    private boolean isErrorPresent(List<NotificationInformation> notificationInformations) {
        for (NotificationInformation notificationInformation : notificationInformations) {
            if (notificationInformation.getType().equals(ERROR_TYPE_CLASS)) {
                LOGGER.error(notificationInformation.getMessage());
                return true;
            }
        }
        return false;
    }

    private List<WebElement> getNotificationsList() {
        List<WebElement> notificationContainersWithMessage = new ArrayList<>(Collections.emptyList());
        for (WebElement notificationContainer : notificationContainers) {
            if (WebElementUtils.isElementPresent(notificationContainer, By.cssSelector(LI_CSS))) {
                notificationContainersWithMessage.add(notificationContainer);
            }
        }
        return notificationContainersWithMessage;
    }

    public static class NotificationInformation {

        private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
        private static final String MESSAGE_CLASS = "message-text";
        private static final String CLASS = "class";
        private static final String CLOSE_BUTTON_CSS = ".delete-button";
        private static final String ERROR_MESSAGES = "Driver is null, use static method create/createFromParent with parameter WebDrive";

        private final WebElement notification;
        private final WebDriver driver;

        private NotificationInformation(WebDriver driver, WebElement notificationContainer) {
            this.notification = notificationContainer.findElement(By.cssSelector(LI_CSS));
            this.driver = driver;
        }

        public String getType() {
            return notification.getAttribute(CLASS);
        }

        public String getMessage() {
            return notification.findElement(By.className(MESSAGE_CLASS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }

        public void close() {
            Preconditions.checkArgument(driver != null, ERROR_MESSAGES);
            WebElementUtils.clickWebElement(driver, notification.findElement(By.cssSelector(CLOSE_BUTTON_CSS)));
        }
    }
}
