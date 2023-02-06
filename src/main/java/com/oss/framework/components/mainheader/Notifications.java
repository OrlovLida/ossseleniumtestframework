package com.oss.framework.components.mainheader;

import com.google.common.base.Preconditions;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class Notifications implements NotificationsInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifications.class);
    private static final String PROGRESS_NOTIFICATION_CLASS = "notification progressNotification";
    private static final String NOTIFICATION_LABEL_CSS_SELECTOR = ".notificationLabel";
    private static final By NOTIFICATION_CONTAINER = By.cssSelector(".notificationContainer");
    private static final String NOTIFICATION_TEXT_CONTAINER_CSS_SELECTOR = ".notificationTextContainer";
    private static final String NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN = ".//span[contains(text(), '%s') and contains(text(), '%s')]";
    private static final By NOTIFICATION = By.cssSelector(".notifications-button");
    private static final By CLOSE_BUTTON = By.cssSelector(".closeButton");
    private static final By CLEAR_NOTIFICATION = By.cssSelector(".clear-action");
    private static final By NOTIFICATION_BUTTON = By.xpath("//div[@class='notifications-button']/button");
    private static final By NOTIFICATION_OPENED = By.xpath("//div[@class='notifications__panel__wrapper']");
    private static final By EMPTY_NOTIFICATION = By.cssSelector(".notificationEmpty");
    private static final By NOTIFICATION_LIST = By.cssSelector(".notification:not(.notificationEmpty)");
    private static final By NOTIFICATION_DETAILS = By.xpath("(.//a[@class='detailsLink'])[1]");
    private static final By DOWNLOAD_FILE = By.xpath(".//a[contains (text(), 'Download file')]");
    private static final String SUCCESS_NOTIFICATION_TYPE_CLASS = "success";
    private static final String FAILED_NOTIFICATION_TYPE_CLASS = "danger";
    private static final String WARNING_NOTIFICATION_TYPE_CLASS = "warning";
    private static final String INFO_NOTIFICATION_TYPE_CLASS = "info";
    private static final String DOWNLOAD_NOTIFICATION_TYPE_CLASS = "fa-download";
    private static final String CANNOT_MAP_TO_NOTIFICATION_TYPE_EXCEPTION = "Following classes cannot be mapped to any Notification Type:";
    private static final String INVALID_MESSAGE_TYPE_EXCEPTION = "Cannot perform download file operation, because of invalid notification type.";
    private static final String NO_NOTIFICATION_EXCEPTION = "No notifications found in Notifications Container.";
    private static final String NOTIFICATION_WITHOUT_DETAILS_EXCEPTION = "Notification does not contain Details button.";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private WebElement getNotificationsContainer() {
        return driver.findElement(NOTIFICATION_CONTAINER);
    }

    private Notifications(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static NotificationsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, NOTIFICATION);
        return new Notifications(driver, wait);
    }

    private static boolean isElementPresent(WebDriver driver, By by) {
        return WebElementUtils.isElementPresent(driver, by);
    }

    @Override
    public String getNotificationMessage() {
        Notification notification = getFirstNotification().orElseThrow(() -> new NoSuchElementException(NO_NOTIFICATION_EXCEPTION));
        notification.close();
        closeNotificationContainer();
        LOGGER.info("Notification finished with message: {}", notification.getText());
        return notification.getText();
    }

    @Override
    public void waitForNotification(String text, String notificationStatus) {
        getNotification(text, notificationStatus).close();
        closeNotificationContainer();
    }

    @Override
    public void clearAllNotification() {
        openNotificationContainer();
        if (!WebElementUtils.isElementPresent(getNotificationsContainer(), EMPTY_NOTIFICATION)) {
            clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION));
        }
        closeNotificationContainer();
    }

    @Override
    public int countNotifications() {
        openNotificationContainer();
        int amount = getNotificationsContainer().findElements(NOTIFICATION_LIST).size();
        LOGGER.info("The list of notifications includes {} elements", amount);
        closeNotificationContainer();
        return amount;
    }

    @Override
    public void openDetails(String text, String notificationStatus) {
        getNotification(text, notificationStatus).openDetails();
    }

    @Override
    public void clickDownloadFile() {
        getFirstNotification().orElseThrow(() -> new NoSuchElementException(NO_NOTIFICATION_EXCEPTION)).clickDownloadFile();
        closeNotificationContainer();
    }

    private void openNotificationContainer() {
        if (!isElementPresent(driver, NOTIFICATION_OPENED)) {
            WebElementUtils.clickWithRetry(driver, driver.findElement(NOTIFICATION_BUTTON), NOTIFICATION_OPENED);
        }
    }

    public void closeNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_OPENED)) {
            clickOnWebElement(driver.findElement(NOTIFICATION_BUTTON));
        }
    }

    private void clickOnWebElement(WebElement webElement) {
        WebElementUtils.clickWebElement(driver, webElement);
    }

    private NotificationType mapToNotificationType(List<String> classes) {
        for (String cssClass : classes) {
            switch (cssClass) {
                case SUCCESS_NOTIFICATION_TYPE_CLASS: {
                    return NotificationType.SUCCESS;
                }
                case FAILED_NOTIFICATION_TYPE_CLASS: {
                    return NotificationType.FAILED;
                }
                case WARNING_NOTIFICATION_TYPE_CLASS: {
                    return NotificationType.WARNING;
                }
                case INFO_NOTIFICATION_TYPE_CLASS: {
                    return NotificationType.INFO;
                }
                case DOWNLOAD_NOTIFICATION_TYPE_CLASS: {
                    return NotificationType.DOWNLOAD;
                }
                default:
            }
        }
        throw new IllegalArgumentException(CANNOT_MAP_TO_NOTIFICATION_TYPE_EXCEPTION + classes);
    }

    private Notification toNotification(WebElement notificationElement) {
        if (notificationElement.getAttribute("class").contains(PROGRESS_NOTIFICATION_CLASS)) {
            wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(
                    notificationElement, "class", PROGRESS_NOTIFICATION_CLASS)));
        }
        return new Notification(wait.until(ExpectedConditions.visibilityOf(notificationElement.findElement(
                By.cssSelector(NOTIFICATION_LABEL_CSS_SELECTOR + " " + NOTIFICATION_TEXT_CONTAINER_CSS_SELECTOR + " span")
        ))).getText(),
                mapToNotificationType(CSSUtils.getAllClasses(
                        notificationElement.findElement(By.xpath(".//i")))), notificationElement);
    }

    public List<Notification> getNotifications() {
        openNotificationContainer();
        List<WebElement> notifications = getNotificationsContainer().findElements(NOTIFICATION_LIST);
        return notifications.stream().map(this::toNotification).collect(Collectors.toList());
    }

    public Optional<Notification> getFirstNotification() {
        return getNotifications().stream().findFirst();
    }

    public Notification getNotification(String text, String notificationStatus) {
        openNotificationContainer();
        String notificationByTextAndStatus = String.format(NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN, text, notificationStatus);
        DelayUtils.waitBy(wait, By.cssSelector(NOTIFICATION_LABEL_CSS_SELECTOR + " " + NOTIFICATION_TEXT_CONTAINER_CSS_SELECTOR));
        DelayUtils.waitForNestedElements(wait, getNotificationsContainer(), notificationByTextAndStatus);
        return getNotifications().stream().filter(notification ->
                        notification.getText().contains(text) && notification.getText().contains(notificationStatus))
                .findFirst().orElseThrow(() -> new NoSuchElementException(NO_NOTIFICATION_EXCEPTION));
    }

    public enum NotificationType {
        FAILED, WARNING, SUCCESS, INFO, DOWNLOAD
    }

    public class Notification {
        private final String text;
        private final NotificationType type;
        private final WebElement notificationElement;

        private Notification(String text, NotificationType type, WebElement notificationElement) {
            this.text = text;
            this.type = type;
            this.notificationElement = notificationElement;
        }

        public String getText() {
            return text;
        }

        public NotificationType getType() {
            return type;
        }

        public void close() {
            clickOnWebElement(notificationElement.findElement(CLOSE_BUTTON));
        }

        public void clickDownloadFile() {
            Preconditions.checkState(type.equals(NotificationType.DOWNLOAD), INVALID_MESSAGE_TYPE_EXCEPTION);
            clickOnWebElement(notificationElement.findElement(DOWNLOAD_FILE));
        }

        public void openDetails() {
            Preconditions.checkState(WebElementUtils.isElementPresent(notificationElement, NOTIFICATION_DETAILS),
                    NOTIFICATION_WITHOUT_DETAILS_EXCEPTION);
            clickOnWebElement(notificationElement.findElement(NOTIFICATION_DETAILS));
            closeNotificationContainer();
        }
    }
}
