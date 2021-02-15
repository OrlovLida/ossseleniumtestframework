package com.oss.framework.mainheader;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Notifications implements NotificationsInterface {
    private static final By CLEAR_NOTIFICATION = By.xpath("//a[@class='clear-action']");
    private static final By NOTIFICATION_BUTTON = By.xpath("//div[@class='notificationHoveringContainer']");
    private static final By NOTIFICATION_OPENED = By.xpath("//a[(@class='clicked badge') or (@class = 'clicked notificationType badge')]");
    private static final By NOTIFICATION_CLOSED = By.xpath("//a[(@class= 'badge') or (@class= 'notificationType badge')]");
    private static final By EMPTY_NOTIFICATION = By.xpath("//div[@class='notificationEmpty']");
    private static final By NOTIFICATION_LIST = By.xpath("//div[@class='notificationContainer']/div");
    private static final By NOTIFICATION_DETAILS = By.xpath("(//a[@class='detailsLink'])[1]");

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
        String notificationText =
                wait.until(ExpectedConditions
                        .visibilityOf(driver.findElement(By.xpath(".//div[@class='notificationLabel']//div[@class='notificationTextContainer']/span"))))
                        .getText();
        driver.findElement(CLEAR_NOTIFICATION).click();
        closeNotificationContainer();
        return notificationText;
    }

    @Override
    public void waitForSpecificNotification(String text, String notificationStatus) {
        openNotificationContainer();
        DelayUtils.waitByXPath(wait, ".//div[@class='notificationLabel']//div[@class='notificationTextContainer']/span[contains(text(), '" + text + "') and contains(text(), '" + notificationStatus + "')]");
        driver.findElement(CLEAR_NOTIFICATION).click();
    }

    @Override
    public void clearAllNotification() {
        if (!isElementPresent(driver, EMPTY_NOTIFICATION)) {
            openNotificationContainer();
            driver.findElement(CLEAR_NOTIFICATION).click();
        }
    }

    @Override
    public int getAmountOfNotifications() {
        return driver.findElements(NOTIFICATION_LIST).size();
    }

    private void openNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_CLOSED)) {
            driver.findElement(NOTIFICATION_BUTTON).click();
            DelayUtils.waitBy(wait, NOTIFICATION_OPENED);
        }
    }

    private void closeNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_OPENED)) {
            driver.findElement(NOTIFICATION_BUTTON).click();
            DelayUtils.waitBy(wait, NOTIFICATION_CLOSED);
        }
    }

    public void openDetailsForSpecificNotification(String text, String notificationStatus) {
        openNotificationContainer();
        DelayUtils.waitByXPath(wait, ".//div[@class='notificationLabel']//div[@class='notificationTextContainer']/span[contains(text(), '" + text + "') and contains(text(), '" + notificationStatus + "')]");
        driver.findElement(NOTIFICATION_DETAILS).click();
    }
}
