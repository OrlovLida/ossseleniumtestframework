package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.listwidget.ListGroup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;

public class ChooseConfigurationWizard {

    public static final String CHOOSE_CONFIG_ID = "chooseConfig";
    private static final String WIZARD_ID = "configuration_chooser";
    private static final String APPLY_BUTTON_ID = "configuration_chooser_apply_button";
    private static final String CANCEL_BUTTON_ID = "configuration_chooser_cancel_button";
    private static final String DOWNLOAD_BUTTON_ID = "configuration_chooser_apply_button";
    private static final String DELETE_BUTTON_XPATH = "//a[@class='CommonButton btn btn-danger btn-md']";
    private final WebDriver driver;
    private final WebDriverWait wait;

    private ChooseConfigurationWizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static ChooseConfigurationWizard create(WebDriver driver, WebDriverWait wait) {
        return new ChooseConfigurationWizard(driver, wait);
    }

    public ChooseConfigurationWizard chooseConfiguration(String name) {
        ListGroup.create(driver, wait).selectItemByName(name);
        return this;
    }

    public ChooseConfigurationWizard deleteConfiguration(String name) {
        ListGroup.create(driver, wait).clickOnIcon(name);
        DelayUtils.waitForVisibility(wait, driver.findElement(By.xpath(DELETE_BUTTON_XPATH)));
        driver.findElement(By.xpath(DELETE_BUTTON_XPATH)).click();
        return this;
    }

    public void apply() {
        getWizard().clickButtonById(APPLY_BUTTON_ID);
    }

    public void download() {
        getWizard().clickButtonById(DOWNLOAD_BUTTON_ID);
    }

    public void cancel() {
        getWizard().clickButtonById(CANCEL_BUTTON_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

}
