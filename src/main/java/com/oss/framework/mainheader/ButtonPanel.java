package com.oss.framework.mainheader;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ButtonPanel {

    private static final String XPATH_BUTTON_PANEL = "//div[@class='buttonPanel']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement buttonPanel;

    private ButtonPanel(WebDriver driver, WebDriverWait wait, WebElement buttonPanel) {
        this.driver = driver;
        this.wait = wait;
        this.buttonPanel = buttonPanel;
    }

    public static ButtonPanel create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, XPATH_BUTTON_PANEL);
        WebElement buttonPanel = driver.findElement(By.xpath(XPATH_BUTTON_PANEL));
        return new ButtonPanel(driver, wait, buttonPanel);
    }

    private WebElement getButtonIcon(String iconID) {
        DelayUtils.waitByXPath(wait, ".//i[contains(@class,'" + iconID + "')]");
        return this.buttonPanel.findElement(By.xpath(".//i[contains(@class,'" + iconID + "')]"));
    }

    public void clickOnIcon(String iconId) {
        getButtonIcon(iconId).click();
    }

    public boolean isButtonDisplayed(String iconId) {
        return this.buttonPanel.findElements(By.xpath(".//i[contains(@class,'" + iconId + "')]")).size()>0;
    }

}
