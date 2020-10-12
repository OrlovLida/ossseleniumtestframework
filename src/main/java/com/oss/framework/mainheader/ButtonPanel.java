package com.oss.framework.mainheader;

import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ButtonPanel {

    private static final String BUTTON_PANEL_XPATH = "//div[@class='buttonPanel']";
    private static final String ICON_XPATH_PATTERN = ".//i[contains(@class,'%s')]";
    private static final String LAYOUT_ICON_ID = "layout";
    private static final String LAYOUT_EXPANDER_ICON_ID = "fa fa-chevron-down";
    private static final String SAVE_CONFIGURATION_ICON_ID = "fa fa-fw fa-floppy-o";
    private static final String CHOOSE_CONFIGURATION_ICON_ID = "fa fa-fw fa-cog";
    private static final String DOWNLOAD_CONFIGURATION_ICON_ID = "fa fa-fw fa-download";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private ButtonPanel(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static ButtonPanel create(WebDriver driver, WebDriverWait wait) {
        return new ButtonPanel(driver, wait);
    }

    private WebElement getButtonPanel() {
        DelayUtils.waitByXPath(wait, BUTTON_PANEL_XPATH);
        return driver.findElement(By.xpath(BUTTON_PANEL_XPATH));
    }

    private WebElement getButtonIcon(String iconId) {
        DelayUtils.waitByXPath(wait, String.format(ICON_XPATH_PATTERN, iconId));
        return getButtonPanel().findElement(By.xpath(String.format(ICON_XPATH_PATTERN, iconId)));
    }

    public void clickOnIcon(String iconId) {
        getButtonIcon(iconId).click();
    }

    private boolean isButtonDisplayed(String iconId) {
        DelayUtils.waitByXPath(wait, String.format(ICON_XPATH_PATTERN, iconId));
        return getButtonPanel().findElements(By.xpath(String.format(ICON_XPATH_PATTERN, iconId))).size() > 0;
    }

    public void expandLayoutMenu() {
        if (isButtonDisplayed(LAYOUT_EXPANDER_ICON_ID))
            clickOnIcon(LAYOUT_ICON_ID);
    }

    public SaveConfigurationWizard openSaveConfigurationWizard() {
        clickOnIcon(SAVE_CONFIGURATION_ICON_ID);
        return SaveConfigurationWizard.create(driver, wait);
    }

    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        clickOnIcon(CHOOSE_CONFIGURATION_ICON_ID);
        return ChooseConfigurationWizard.create(driver, wait);
    }

    public ChooseConfigurationWizard openDownloadConfigurationWizard() {
        clickOnIcon(DOWNLOAD_CONFIGURATION_ICON_ID);
        return ChooseConfigurationWizard.create(driver, wait);
    }

}
