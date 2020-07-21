package com.oss.framework.components.notifications;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class Notifications implements NotificationsInterface {
    private static final By CLEAR_NOTIFICATION = By.xpath("//a[@class='clear-action']");
    private static final By NOTIFICATION_BUTTON = By.xpath("//div[@class='notificationHoveringContainer']");
    private static final By NOTIFICATION_OPENED = By.xpath("//a[@class='clicked notificationType badge']");
    private static final By NOTIFICATION_CLOSED = By.xpath("//a[@class='notificationType badge']");
    private static final By EMPTY_NOTIFICATION = By.xpath("//div[@class='notificationEmpty']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    private Notifications(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static NotificationsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[@class='toolbarWidget globalNotification']");
        return new Notifications(driver, wait);
    }

    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public String waitAndGetFinishedNotificationText() {
        openNotificationContainer();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(".//div[@class='notificationLabel']"))));
        wait.until(ExpectedConditions.not(ExpectedConditions
                        .attributeToBe(driver.findElement(By.xpath("//div[@class='notificationContainer']/div")), "class", "notification progressNotification")));
        WebElement windowContent = driver.findElement(By.xpath(".//div[@class='notificationLabel']"));
        WebElement message = windowContent.findElement(By.xpath("//div[@class='notificationTextContainer']/span"));
        driver.findElement(CLEAR_NOTIFICATION).click();
        return message.getText();
    }

    @Override
    public void clearAllNotification() {
        if (!isElementPresent(driver, EMPTY_NOTIFICATION)) {
            openNotificationContainer();
            driver.findElement(CLEAR_NOTIFICATION).click();
        }
    }

    private void openNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_CLOSED)) {
            driver.findElement(NOTIFICATION_BUTTON).click();
            DelayUtils.waitBy(wait, NOTIFICATION_OPENED);
        }
    }

    private void closeNotificationContainter() {
        if (isElementPresent(driver, NOTIFICATION_OPENED)) {
            driver.findElement(NOTIFICATION_BUTTON).click();
            DelayUtils.waitBy(wait, NOTIFICATION_CLOSED);
        }
    }
}
