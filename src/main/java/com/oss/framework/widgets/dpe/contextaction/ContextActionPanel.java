package com.oss.framework.widgets.dpe.contextaction;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ContextActionPanel {

    private static final String GRAPH_LOCATOR_PATH = "//*[starts-with(@class,'multipleChart ')]";
    private static final String CONTEXT_ACTION_PANEL_ID = "context-action-panel";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement contextActionPanel;

    public static ContextActionPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement((By.xpath(GRAPH_LOCATOR_PATH)))).build().perform();
        WebElement contextActionPanel = driver.findElement(By.xpath("//*[@" + CSSUtils.TEST_ID + "='" + CONTEXT_ACTION_PANEL_ID + "']"));

        return new ContextActionPanel(driver, wait, contextActionPanel);
    }

    private ContextActionPanel(WebDriver driver, WebDriverWait wait, WebElement contextActionPanel) {
        this.driver = driver;
        this.wait = wait;
        this.contextActionPanel = contextActionPanel;
    }

    public void clickOnPanel() {
        contextActionPanel.click();
    }

    public void callAction(String groupId) {
        clickOnGroup(groupId);
    }

    public void callAction(String groupId, String actionLabel) {
        clickOnGroup(groupId);
        clickOnAction(actionLabel);
        Actions actions = new Actions(driver);
        WebElement graph = driver.findElement(By.xpath(GRAPH_LOCATOR_PATH));
        actions.moveToElement(graph).click(graph).build().perform();
    }

    public void callAction(String groupId, String actionClass, String color) {
        clickOnGroup(groupId);
        WebElement action = contextActionPanel.findElement(By.className(actionClass));
        action.click();
        chooseColor(color);
    }

    private void clickOnGroup(String groupId) {
        if (!isPanelOpen()) {
            clickOnPanel();
        }
        List<WebElement> actions = contextActionPanel.findElements(By.xpath(".//div[@class='btn-chart' or @class='xdr-views-container']"));
        WebElement group = actions.stream().filter(a -> a.findElement(By.xpath(".//button"))
                        .getAttribute(CSSUtils.TEST_ID)
                        .equals(groupId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cant find groupId: " + groupId));
        group.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnAction(String actionLabel) {
        List<WebElement> linksList = contextActionPanel.findElements(By.className("xdr-view-link"));
        WebElement action;

        if (!linksList.isEmpty()) {
            action = linksList.stream().filter(a -> a.findElement(By.xpath(".//a"))
                            .getText()
                            .equals(actionLabel))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Link with text label " + actionLabel + " doesn't exist"));
        } else {
            action = contextActionPanel.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "='" + actionLabel + "']"));
        }
        action.click();
    }

    private void chooseColor(String colorRGB) {
        WebElement colorPalette = contextActionPanel.findElement(By.className("color-picker"));
        WebElement color = colorPalette.findElement(By.xpath("//*[@style='background-color: " + colorRGB + ";']"));
        color.click();
    }

    private boolean isPanelOpen() {
        return !contextActionPanel.findElements(By.xpath(".//i[contains(@class,'chevron-left')]")).isEmpty();
    }
}