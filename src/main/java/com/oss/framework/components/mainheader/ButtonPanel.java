package com.oss.framework.components.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class ButtonPanel {

    private static final String BUTTON_PANEL_XPATH = "//div[@class='view-actions-container']";
    private static final String ICON_XPATH_PATTERN = ".//*[contains(@" + CSSUtils.TEST_ID + ",'%s')]";
    private static final String BUTTON_PANEL_CSS = "div.view-actions-container";
    private static final String HORIZONTAL_50_50_LAYOUT_BUTTON_CLASS = "ossfont-layout-horizontal";
    private static final String HORIZONTAL_40_60_LAYOUT_BUTTON_CLASS = "ossfont-layout-horizontal-40-60";
    private static final String HORIZONTAL_60_40_LAYOUT_BUTTON_CLASS = "ossfont-layout-horizontal-60-40";
    private static final String HORIZONTAL_60_40_BUTTON_ID = "TWO_ROWS_60_40";
    private static final String HORIZONTAL_40_60_BUTTON_ID = "TWO_ROWS_40_60";
    private static final String HORIZONTAL_50_50_BUTTON_ID = "TWO_ROWS_50_50";
    private static final String VERTICAL_LAYOUT_BUTTON_CLASS = "ossfont-layout-vertical";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private ButtonPanel(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static ButtonPanel create(WebDriver driver, WebDriverWait wait) {
        WebElement buttonPanel = driver.findElement(By.cssSelector(BUTTON_PANEL_CSS));
        return new ButtonPanel(driver, wait, buttonPanel);
    }

    public void clickButton(String buttonId) {
        getButton(buttonId).click();
    }

    public void clickButton(String groupId, String actionId) {
        clickButton(groupId);
        DropdownList.create(driver, wait).selectOptionById(actionId);
    }

    public boolean isHorizontalLayout(String layoutId) {
        return !webElement.findElements(By.className(getHorizontalLayoutType(layoutId))).isEmpty();
    }

    private String getHorizontalLayoutType(String layoutId) {
        String layoutClass;
        switch (layoutId) {
            case HORIZONTAL_60_40_BUTTON_ID:
                layoutClass = HORIZONTAL_60_40_LAYOUT_BUTTON_CLASS;
                break;
            case HORIZONTAL_50_50_BUTTON_ID:
                layoutClass = HORIZONTAL_50_50_LAYOUT_BUTTON_CLASS;
                break;
            case HORIZONTAL_40_60_BUTTON_ID:
                layoutClass = HORIZONTAL_40_60_LAYOUT_BUTTON_CLASS;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + layoutId);
        }

        return layoutClass;

    }

    public boolean isVerticalLayout() {
        return !webElement.findElements(By.className(VERTICAL_LAYOUT_BUTTON_CLASS)).isEmpty();
    }

    private WebElement getButtonPanel() {
        DelayUtils.waitByXPath(wait, BUTTON_PANEL_XPATH);
        return webElement.findElement(By.xpath(BUTTON_PANEL_XPATH));
    }

    private WebElement getButton(String iconId) {
        DelayUtils.waitByXPath(wait, String.format(ICON_XPATH_PATTERN, iconId));
        return getButtonPanel().findElement(By.xpath(String.format(ICON_XPATH_PATTERN, iconId)));
    }

}
