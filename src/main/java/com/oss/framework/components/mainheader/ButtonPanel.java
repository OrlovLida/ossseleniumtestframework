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
    private static final String ICON_XPATH_PATTERN = ".//*[contains(@"+ CSSUtils.TEST_ID +",'%s')]";
    private static final String SAVE_CONFIGURATION_ICON_ID = "fa fa-fw fa-floppy-o";
    private static final String CHOOSE_CONFIGURATION_ICON_ID = "fa fa-fw fa-cog";
    private static final String DOWNLOAD_CONFIGURATION_ICON_ID = "fa fa-fw fa-download";
    private static final String BUTTON_PANEL_CSS = "div.view-actions-container";
    private static final String HORIZONTAL_LAYOUT_BUTTON_CLASS = "ossfont-layout-horizontal";

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

    public void clickButton(String groupId, String actionId){
        clickButton(groupId);
        DropdownList.create(driver, wait).selectOptionWithId(actionId);
    }

    public boolean isHorizontalLayout() {
        return !webElement.findElements(By.className(HORIZONTAL_LAYOUT_BUTTON_CLASS)).isEmpty();
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
