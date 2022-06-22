package com.oss.framework.iaa.widgets.components;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class NotificationPreview {

    private static final String NOTIFICATION_PREVIEW_CLASS = "notification-preview";
    private static final String NOTIFICATION_PREVIEW_PATTERN = "[data-testid='%s']";
    private static final String RICH_TEXT_CLASS = "OSSRichText";
    private static final String CHANNEL_CLASS = "im-channel";
    private static final String ICON_XPATH = ".//i[contains(@class, 'OSSIcon')]";
    private static final String ARIA_LABEL_ATTRIBUTE = "aria-label";
    private static final String BADGE_CLASS = "badge";
    private static final String PROPERTY_CLASS = "property";
    private static final String ATTACHMENT_BUTTON_CLASS = "CommonButton";
    private static final String ATTACHMENTS_PROPERTY_PATTERN = "Attachments:%s";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement notificationPreviewElement;

    private NotificationPreview(WebDriver driver, WebDriverWait wait, WebElement notificationPreviewElement) {
        this.driver = driver;
        this.wait = wait;
        this.notificationPreviewElement = notificationPreviewElement;
    }

    /**
     * @deprecated (to remove with next release 1.1.x or 2.0.x because component now has an id)
     */
    @Deprecated
    public static NotificationPreview create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(NOTIFICATION_PREVIEW_CLASS));
        WebElement notificationPreview = driver.findElement(By.className(NOTIFICATION_PREVIEW_CLASS));
        return new NotificationPreview(driver, wait, notificationPreview);
    }

    public static NotificationPreview createById(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitBy(wait, By.cssSelector(String.format(NOTIFICATION_PREVIEW_PATTERN, componentId)));
        WebElement notificationPreview = driver.findElement(By.cssSelector(String.format(NOTIFICATION_PREVIEW_PATTERN, componentId)));
        return new NotificationPreview(driver, wait, notificationPreview);
    }

    public ActionsContainer getActionsContainer() {
        return ActionsContainer.createFromParent(notificationPreviewElement, driver, wait);
    }

    public String getText() {
        return notificationPreviewElement.findElement(By.className(RICH_TEXT_CLASS)).getText();
    }

    public String getChannelText() {
        return notificationPreviewElement.findElement(By.className(CHANNEL_CLASS)).getText();
    }

    public String getStatusLabel() {
        return notificationPreviewElement.findElement(By.xpath(ICON_XPATH)).getAttribute(ARIA_LABEL_ATTRIBUTE);
    }

    public boolean isNewBadgePresent() {
        return WebElementUtils.isElementPresent(notificationPreviewElement, By.className(BADGE_CLASS));
    }

    public List<String> getPropertiesText() {
        List<String> propertiesText = new ArrayList<>();
        for (WebElement property : getPropertiesList()) {
            propertiesText.add(property.getText().replace("\n", ""));
        }
        return propertiesText;
    }

    public void clickAttachment(String attachmentName) {
        if (getPropertiesText().contains(String.format(ATTACHMENTS_PROPERTY_PATTERN, attachmentName))) {
            WebElementUtils.clickWebElement(driver, getAttachmentButton());
        }
    }

    private List<WebElement> getPropertiesList() {
        return driver.findElements(By.className(PROPERTY_CLASS));
    }

    private WebElement getAttachmentButton() {
        return notificationPreviewElement.findElement(By.className(ATTACHMENT_BUTTON_CLASS));
    }
}
