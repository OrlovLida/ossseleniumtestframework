package com.oss.framework.iaa.widgets.dpe.contextaction;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class ContextActionPanel {

    private static final String GRAPH_LOCATOR_PATH = "//*[starts-with(@class,'multipleChart ')]";
    private static final String CONTEXT_ACTION_PANEL_ID = "context-action-panel";
    private static final String CONTEXT_ACTIONS_BUTTON_XPATH = ".//button[@data-testid='context-action-panel']";

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
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement((By.xpath(GRAPH_LOCATOR_PATH)))).build().perform();
        WebElement contextActionPanel = driver.findElement(By.xpath("//*[@" + CSSUtils.TEST_ID + "='" + CONTEXT_ACTION_PANEL_ID + "']"));

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

    private void clickOnGroup(String groupId) {
        if (!isPanelOpen()) {
            clickOnPanel();
        }
        List<WebElement> actions = contextActionPanelElement.findElements(By.xpath(".//div[@class='btn-chart' or @class='xdr-views-container']"));
        WebElement group = actions.stream().filter(a -> a.findElement(By.xpath(".//button"))
                .getAttribute(CSSUtils.TEST_ID)
                .equals(groupId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cant find groupId: " + groupId));
        group.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnAction(String actionLabel) {
        List<WebElement> linksList = contextActionPanelElement.findElements(By.className("xdr-view-link"));
        WebElement action;

        if (!linksList.isEmpty()) {
            action = linksList.stream().filter(a -> a.findElement(By.xpath(".//a"))
                    .getText()
                    .equals(actionLabel))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Link with text label " + actionLabel + " doesn't exist"));
            action.click();
        } else {
            action = contextActionPanelElement.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "='" + actionLabel + "']"));
            action.click();
            Actions actions = new Actions(driver);
            WebElement graph = driver.findElement(By.xpath(GRAPH_LOCATOR_PATH));
            actions.moveToElement(graph).click(graph).build().perform();
        }
    }

    private void chooseColor(String colorRGB) {
        WebElement colorPalette = contextActionPanelElement.findElement(By.className("color-picker"));
        WebElement color = colorPalette.findElement(By.xpath("//*[@style='background-color: " + colorRGB + ";']"));
        color.click();
    }

    private boolean isPanelOpen() {
        return !contextActionPanelElement.findElements(By.xpath(".//i[contains(@class,'chevron-left')]")).isEmpty();
    }
}