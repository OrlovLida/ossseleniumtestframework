package com.oss.framework.components.portals;

import com.oss.framework.listwidget.ListGroup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChooseConfigurationWizard {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String WIZARD_ID = "configuration_chooser";
    private static final String APPLY_BUTTON_ID = "configuration_chooser_apply_button";
    private static final String CANCEL_BUTTON_ID = "configuration_chooser_cancel_button";
    private static final String DOWNLOAD_BUTTON_ID = "configuration_chooser_apply_button";
    private static final String DELETE_BUTTON_XPATH = "//a[@class='CommonButton btn btn-danger btn-md']";

    private ChooseConfigurationWizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static ChooseConfigurationWizard create(WebDriver driver, WebDriverWait wait) {
        return new ChooseConfigurationWizard(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
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
        getWizard().clickActionById(APPLY_BUTTON_ID);
    }

    public void download() {
        getWizard().clickActionById(DOWNLOAD_BUTTON_ID);
    }

    public void cancel() {
        getWizard().clickActionById(CANCEL_BUTTON_ID);
    }

}
