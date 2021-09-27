package com.oss.framework.view;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
    private static final String SEARCH_TOOLBAR_BUTTON_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-button']";
    private static final String CLOSE_SEARCH_BUTTON_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-clean-button']";
    private static final String SEARCH_RESULTS_XPATH = "//*[starts-with(@class, 'resultsPopup')]";

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

    public void maximizeCard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = card.findElement(By.xpath(MAXIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking maximize button");
    }

    public void minimizeCard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement resizeButton = card.findElement(By.xpath(MINIMIZE_CHART_BUTTON_XPATH));
        resizeButton.click();
        log.debug("Clicking minimize button");
    }

    public void searchToolbar(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement searchButton = card.findElement(By.xpath(SEARCH_TOOLBAR_BUTTON_XPATH));
        searchButton.click();
        log.debug("Clicking search button");
    }

    public void closeSearchToolbar(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement closeButton = card.findElement(By.xpath(CLOSE_SEARCH_BUTTON_XPATH));
        closeButton.click();
        log.debug("Clicking close search button");
    }

    public void selectFirstSearchResult() {
        String firstResultXpath = SEARCH_RESULTS_XPATH + "/ol/li[1]";
        DelayUtils.waitByXPath(wait, firstResultXpath);
        WebElement firstResult = card.findElement(By.xpath(firstResultXpath));
        Actions action = new Actions(driver);
        action.moveToElement(firstResult)
                .click(firstResult)
                .build()
                .perform();
    }
}
