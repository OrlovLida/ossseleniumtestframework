package com.oss.framework.iaa.widgets.dpe.contextaction;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class ContextActionPanel {

    private static final String GRAPH_LOCATOR_PATH = "//*[starts-with(@class,'multipleChart ')]";
    private static final String CONTEXT_ACTION_PANEL_ID = "context-action-panel";
    private static final String CONTEXT_ACTIONS_BUTTON_XPATH = ".//button[@data-testid='context-action-panel']";
    private static final String ACTIONS_XPATH = ".//div[@class='btn-chart' or @class='xdr-views-container']";
    private static final String COLOR_XPATH = "//*[@style='background-color: %s;']";
    private static final String XDR_LINK_CLASS = "xdr-view-link";
    private static final String BUTTON_XPATH = ".//button";
    private static final String COLOR_PICKER_CLASS = "color-picker";
    private static final String SHOW_HIDE_CHART_ACTIONS_XPATH = ".//i[contains(@class,'chevron-left')]";
    private static final String TITLE_ATTRIBUTE = "title";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement contextActionPanelElement;

    private ContextActionPanel(WebDriver driver, WebDriverWait wait, WebElement contextActionPanelElement) {
        this.driver = driver;
        this.wait = wait;
        this.contextActionPanelElement = contextActionPanelElement;
    }

    public static ContextActionPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElementUtils.moveToElement(driver, driver.findElement((By.xpath(GRAPH_LOCATOR_PATH))));
        WebElement contextActionPanel = driver.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + CONTEXT_ACTION_PANEL_ID + "']"));

        return new ContextActionPanel(driver, wait, contextActionPanel);
    }

    public void clickOnPanel() {
        contextActionPanelElement.findElement(By.xpath(CONTEXT_ACTIONS_BUTTON_XPATH)).click();
    }

    public void callAction(String groupId) {
        clickOnGroup(groupId);
    }

    public void callAction(String groupId, String actionLabel) {
        clickOnGroup(groupId);
        clickOnAction(actionLabel);
    }

    public void callAction(String groupId, String actionClass, String color) {
        clickOnGroup(groupId);
        WebElement action = contextActionPanelElement.findElement(By.className(actionClass));
        action.click();
        chooseColor(color);
    }

    public String getButtonTitle(String buttonId) {
        return contextActionPanelElement.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + buttonId + "']"))
                .getAttribute(TITLE_ATTRIBUTE);
    }

    private void clickOnGroup(String groupId) {
        if (!isPanelOpen()) {
            clickOnPanel();
        }
        List<WebElement> actions = contextActionPanelElement.findElements(By.xpath(ACTIONS_XPATH));
        WebElement group = actions.stream().filter(a -> a.findElement(By.xpath(BUTTON_XPATH))
                        .getAttribute(CSSUtils.TEST_ID)
                        .equals(groupId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cant find groupId: " + groupId));
        group.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnAction(String actionLabel) {
        List<WebElement> linksList = contextActionPanelElement.findElements(By.className(XDR_LINK_CLASS));
        WebElement action;

        if (!linksList.isEmpty()) {
            action = linksList.stream().filter(a -> a.findElement(By.xpath(".//a"))
                    .getText()
                    .equals(actionLabel))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Link with text label " + actionLabel + " doesn't exist"));
            action.click();
        } else {
            action = contextActionPanelElement.findElement(By.xpath(BUTTON_XPATH + "[@" + CSSUtils.TEST_ID + "='" + actionLabel + "']"));
            action.click();
            Actions actions = new Actions(driver);
            WebElement graph = driver.findElement(By.xpath(GRAPH_LOCATOR_PATH));
            actions.moveToElement(graph).click(graph).build().perform();
        }
    }

    private void chooseColor(String colorRGB) {
        WebElementUtils.clickWebElement(driver, getColorPalette().findElement(By.xpath(String.format(COLOR_XPATH, colorRGB))));
    }

    private WebElement getColorPalette() {
        return contextActionPanelElement.findElement(By.className(COLOR_PICKER_CLASS));
    }

    private boolean isPanelOpen() {
        return WebElementUtils.isElementPresent(contextActionPanelElement, By.xpath(SHOW_HIDE_CHART_ACTIONS_XPATH));
    }
}