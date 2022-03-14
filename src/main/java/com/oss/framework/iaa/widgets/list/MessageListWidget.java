package com.oss.framework.iaa.widgets.list;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;

public class MessageListWidget {

    private static final Logger log = LoggerFactory.getLogger(MessageListWidget.class);

    private static final String LIST_MESSAGE_ITEMS_XPATH = ".//div[contains(@class, 'im-message-list__message im-message-list__message')]";
    private static final String MESSAGE_LIST_WIDGET_XPATH = ".//div[contains(@class, 'messagelistwidget')]";
    private static final String DISABLED_BUTTON = ".//a[contains(@class, 'btn-md disabled') and contains(text(), '%s')]";
    private static final String BUTTONS_XPATH = ".//a[contains(@class, 'btn-md') and contains(text(), '%s')]";
    private static final String CHECKBOX_XPATH = ".//div[contains(@class, 'checkbox')]//label[contains(text(), '%s')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement msgListWidgetElement;

    private MessageListWidget(WebDriver driver, WebDriverWait wait, WebElement messageListWidget) {
        this.driver = driver;
        this.wait = wait;
        this.msgListWidgetElement = messageListWidget;
    }

    public static MessageListWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, MESSAGE_LIST_WIDGET_XPATH);
        WebElement messageListWidget = driver.findElement(By.xpath(MESSAGE_LIST_WIDGET_XPATH));

        return new MessageListWidget(driver, wait, messageListWidget);
    }

    public static MessageListWidget createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByElement(wait, parent);
        WebElement messageListWidget = parent.findElement(By.xpath(MESSAGE_LIST_WIDGET_XPATH));

        return new MessageListWidget(driver, wait, messageListWidget);
    }

    public boolean hasNoData() {
        return !msgListWidgetElement.findElements(By.className("im-message-list__no-data")).isEmpty();
    }

    public List<MessageItem> getMessageItems() {
        return msgListWidgetElement.findElements(By.xpath(LIST_MESSAGE_ITEMS_XPATH)).stream()
                .map(messageItem -> MessageItem.create(driver, wait, messageItem))
                .collect(Collectors.toList());
    }

    public int getMessagesNumber() {
        return getMessageItems().size();
    }

    public void clickButtonByLabel(String label) {
        if (!isButtonPresent(label)) {
            msgListWidgetElement.findElement(By.xpath(String.format(BUTTONS_XPATH, label))).click();
        } else {
            log.debug("Button is disabled");
        }
    }

    public void clickCheckboxByLabel(String checkboxLabel) {
        msgListWidgetElement.findElement(By.xpath(String.format(CHECKBOX_XPATH, checkboxLabel))).click();
    }

    public boolean isButtonPresent(String label) {
        return !msgListWidgetElement.findElements(By.xpath(String.format(DISABLED_BUTTON, label))).isEmpty();
    }

    public static class MessageItem {

        private static final String MESSAGE_BUTTONS_XPATH = ".//a[contains(@class, 'btn-xs') and contains(text(), '%s')]";
        private static final String KEBAB_XPATH = ".//a[contains(@class, 'btn-xs')]/i[@class='OSSIcon ossfont-kebab-menu']";
        private static final String BADGE_XPATH = ".//span[contains(@class, 'badge')]";
        private static final String MESSAGE_TEXT_XPATH = ".//div[@class='OSSRichText']";
        private static final String COMMENT_INFO_ROW_XPATH = ".//div[@class='im-message-list__message--comment__header__info__row']";
        private final WebElement messageElement;
        private final WebDriver driver;
        private final WebDriverWait wait;

        private MessageItem(WebDriver driver, WebDriverWait wait, WebElement messageItem) {
            this.driver = driver;
            this.wait = wait;
            this.messageElement = messageItem;
        }

        public static MessageItem create(WebDriver driver, WebDriverWait wait, WebElement messageItem) {
            return new MessageItem(driver, wait, messageItem);
        }

        public String getMessageType() {
            if (messageElement.getAttribute("class").contains("comment")) {
                return MessageType.COMMENT.toString();
            } else if (messageElement.getAttribute("class").contains("notification")) {
                return MessageType.NOTIFICATION.toString();
            }
            log.debug("Unknown type");
            return "Unknown type";
        }

        public void clickMessageAction(String actionLabel) {
            WebElement messageButton = messageElement.findElement(By.xpath(String.format(MESSAGE_BUTTONS_XPATH, actionLabel)));
            Actions actions = new Actions(driver);
            actions.moveToElement(messageButton).build().perform();
            actions.click(messageButton).perform();
        }

        public void clickForward() {
            clickOnKebabMenu();
            DropdownList.create(driver, wait).selectOption("Forward");
        }

        public String getBadgeText(int index) {
            if (getBadgesNumber() - 1 >= index) {
                return getBadges().get(index).getText();
            }
            log.debug("No budge with index: {} exist.", index);
            return "";
        }

        public int getBadgesNumber() {
            return getBadges().size();
        }

        public List<WebElement> getBadges() {
            return messageElement.findElements(By.xpath(BADGE_XPATH));
        }

        public String getMessageText() {
            return messageElement.findElement(By.xpath(MESSAGE_TEXT_XPATH)).getText();
        }

        public String getCommentType() {
            String infoRowText = messageElement.findElement(By.xpath(COMMENT_INFO_ROW_XPATH)).getText();

            if (getMessageType().equals("COMMENT")) {
                if (infoRowText.contains("source")) {
                    return "source";
                } else if (infoRowText.contains("external")) {
                    return "external";
                } else if (infoRowText.contains("internal")) {
                    return "internal";
                }
            }
            return "message is not a comment";
        }

        private void clickOnKebabMenu() {
            WebElement kebab = messageElement.findElement(By.xpath(KEBAB_XPATH));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", kebab);
            js.executeScript("arguments[0].click();", kebab);
        }

        public enum MessageType {
            COMMENT, NOTIFICATION
        }
    }
}
