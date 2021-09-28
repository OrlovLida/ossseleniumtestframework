package com.oss.framework.view;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Card {

    private static final Logger log = LoggerFactory.getLogger(Card.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement card;

    private static final String MAXIMIZE_CHART_BUTTON_XPATH = ".//a[@" + CSSUtils.TEST_ID + "='expand']";
    private static final String MINIMIZE_CHART_BUTTON_XPATH = ".//a[@" + CSSUtils.TEST_ID + "='collapse']";

    public static Card createCard(WebDriver driver, WebDriverWait wait, String windowId) {
        DelayUtils.waitByXPath(wait, ".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]");
        WebElement card = driver.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + windowId + "']"));
        return new Card(driver, wait, card);
    }

    private Card(WebDriver driver, WebDriverWait wait, WebElement card) {
        this.driver = driver;
        this.wait = wait;
        this.card = card;
    }

    public void maximizeCard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = card.findElement(By.xpath(MAXIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking maximize button");
    }

    public void minimizeCard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = card.findElement(By.xpath(MINIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking minimize button");
    }
}
