package com.oss.framework.mainheader;

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
    private static final By NOTIFICATION_BY = By.xpath("//div[@class='notifications-button']");
    private static final By CLEAR_NOTIFICATION_BY = By.xpath("//a[@class='clear-action']");
    private static final By NOTIFICATION_BUTTON_BY = By.xpath("//div[@class='notifications-button']/button");
    private static final By NOTIFICATION_OPENED_BY = By.xpath("//div[@class='notifications__panel__wrapper']");
    private static final By EMPTY_NOTIFICATION_BY = By.xpath("//div[@class='notificationEmpty']");
    private static final By NOTIFICATION_LIST_BY = By.xpath("//div[@class='notificationContainer']/div[not(@class = 'notificationEmpty')]");
    private static final By NOTIFICATION_DETAILS_BY = By.xpath("(//a[@class='detailsLink'])[1]");
    private static final By DOWNLOAD_FILE_BY = By.xpath("//div[@class='notificationWrapper']//a[contains (text(), 'Download file')]");
    private static final String NOTIFICATION_LABEL_XPATH = ".//div[@class='notificationLabel']";
    private static final String NOTIFICATION_CONTAINER_XPATH = "//div[@class='notificationContainer']";
    private static final String NOTIFICATION_TEXT_CONTAINER_XPATH = "//div[@class='notificationTextContainer']";
    private static final String NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN = "/span[contains(text(), '%s') and contains(text(), '%s')]";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private Notifications(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static NotificationsInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, NOTIFICATION_BY);
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
        wait.until(ExpectedConditions.not(ExpectedConditions
                .attributeToBe(driver.findElement(By.xpath(NOTIFICATION_CONTAINER_XPATH + "/div")), "class", "notification progressNotification")));
        String notificationText =
                wait.until(ExpectedConditions
                        .visibilityOf(driver.findElement(By.xpath(NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + "/span"))))
                        .getText();
        clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION_BY));
        closeNotificationContainer();
        LOGGER.info("Notification finished with message: {}", notificationText);
        return notificationText;
    }

    @Override
    public void waitForNotification(String text, String notificationStatus) {
        openNotificationContainer();
        String notificationByTextAndStatus = String.format(NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN, text, notificationStatus);
        DelayUtils.waitByXPath(wait, NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + notificationByTextAndStatus);
        clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION_BY));
    }

    @Override
    public void clearAllNotification() {
        if (!isElementPresent(driver, EMPTY_NOTIFICATION_BY)) {
            openNotificationContainer();
            clickOnWebElement(driver.findElement(CLEAR_NOTIFICATION_BY));
        }
    }

    @Override
    public int countNotifications() {
        int amount = driver.findElements(NOTIFICATION_LIST_BY).size();
        LOGGER.info("The list of notifications includes {} elements", amount);
        return amount;
    }

    @Override
    public void openDetails(String text, String notificationStatus) {
        openNotificationContainer();
        String notificationByTextAndStatus = String.format(NOTIFICATION_BY_TEXT_AND_STATUS_PATTERN, text, notificationStatus);
        DelayUtils.waitByXPath(wait, NOTIFICATION_LABEL_XPATH + NOTIFICATION_TEXT_CONTAINER_XPATH + notificationByTextAndStatus);
        clickOnWebElement(driver.findElement(NOTIFICATION_DETAILS_BY));
        closeNotificationContainer();
    }

    @Override
    public void clickDownloadFile() {
        clickOnWebElement(driver.findElement(DOWNLOAD_FILE_BY));
    }

    private void openNotificationContainer() {
        if (!isElementPresent(driver, NOTIFICATION_OPENED_BY)) {
            clickOnWebElement(driver.findElement(NOTIFICATION_BUTTON_BY));
            DelayUtils.waitBy(wait, NOTIFICATION_OPENED_BY);
        }
    }

    private void closeNotificationContainer() {
        if (isElementPresent(driver, NOTIFICATION_OPENED_BY)) {
            clickOnWebElement(driver.findElement(NOTIFICATION_BUTTON_BY));
        }
    }

    private void clickOnWebElement(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click(webElement).build().perform();
    }
}
