package com.oss.framework.components.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

public class Notifications implements NotificationsInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifications.class);
    private static final String PROGRESS_NOTIFICATION_CLASS = "notification progressNotification";
    private static final String NOTIFICATION_LABEL_XPATH = ".//div[@class='notificationLabel']";
    private static final String NOTIFICATION_CONTAINER_XPATH = "//div[@class='notificationContainer']";
    private static final String NOTIFICATION_TEXT_CONTAINER_XPATH = "//div[@class='notificationTextContainer']";
    private static final String NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN = "/span[contains(text(), '%s') and contains(text(), '%s')]";
    private static final By NOTIFICATION = By.xpath("//div[@class='notifications-button']");
    private static final By CLEAR_NOTIFICATION = By.xpath("//a[@class='clear-action']");
    private static final By NOTIFICATION_BUTTON = By.xpath("//div[@class='notifications-button']/button");
    private static final By NOTIFICATION_OPENED = By.xpath("//div[@class='notifications__panel__wrapper']");
    private static final By EMPTY_NOTIFICATION = By.xpath("//div[@class='notificationEmpty']");
    private static final By NOTIFICATION_LIST = By.xpath("//div[@class='notificationContainer']/div[not(@class = 'notificationEmpty')]");
    private static final By NOTIFICATION_DETAILS = By.xpath("(//a[@class='detailsLink'])[1]");
    private static final By DOWNLOAD_FILE = By.xpath("//div[@class='notificationWrapper']//a[contains (text(), 'Download file')]");
    private static final By NOTIFICATION_IN_PROGRESS = By.xpath(NOTIFICATION_CONTAINER_XPATH + "/div[@class='" + PROGRESS_NOTIFICATION_CLASS + "']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    private Notifications(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static NotificationsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, NOTIFICATION);
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
    public String getNotificationMessage() {
        openNotificationContainer();
        DelayUtils.waitByXPath(wait, NOTIFICATION_LABEL_XPATH);
        if (isElementPresent(driver, NOTIFICATION_IN_PROGRESS)) {
            wait.until(ExpectedConditions.not(ExpectedConditions
                    .attributeToBe(driver.findElement(By.xpath(NOTIFICATION_CONTAINER_XPATH + "/div")), "class", PROGRESS_NOTIFICATION_CLASS)));
        }
        String notificationText =
                wait.until(ExpectedConditions
                        .visibilityOf(driver.findElement(By.xpath(NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + "/span"))))
                        .getText();
        clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION));
        closeNotificationContainer();
        LOGGER.info("Notification finished with message: {}", notificationText);
        return notificationText;
    }

    @Override
    public void waitForNotification(String text, String notificationStatus) {
        openNotificationContainer();
        String notificationByTextAndStatus = String.format(NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN, text, notificationStatus);
        DelayUtils.waitByXPath(wait, NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + notificationByTextAndStatus);
        clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION));
    }

    @Override
    public void clearAllNotification() {
        if (!isElementPresent(driver, EMPTY_NOTIFICATION)) {
            openNotificationContainer();
            clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION));
        }
    }

    @Override
    public int countNotifications() {
        int amount = driver.findElements(NOTIFICATION_LIST).size();
        LOGGER.info("The list of notifications includes {} elements", amount);
        return amount;
    }

    @Override
    public void openDetails(String text, String notificationStatus) {
        openNotificationContainer();
        String notificationByTextAndStatus = String.format(NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN, text, notificationStatus);
        DelayUtils.waitByXPath(wait, NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + notificationByTextAndStatus);
        clickOnWebElement(driver.findElement(NOTIFICATION_DETAILS));
        closeNotificationContainer();
    }

    @Override
    public void clickDownloadFile() {
        clickOnWebElement(driver.findElement(DOWNLOAD_FILE));
    }

    private void openNotificationContainer() {
        if (!isElementPresent(driver, NOTIFICATION_OPENED)) {
            clickOnWebElement(driver.findElement(NOTIFICATION_BUTTON));
            DelayUtils.waitBy(wait, NOTIFICATION_OPENED);
        }
    }

    private void closeNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_OPENED)) {
            clickOnWebElement(driver.findElement(NOTIFICATION_BUTTON));
        }
    }

    private void clickOnWebElement(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click(webElement).build().perform();
    }
}
