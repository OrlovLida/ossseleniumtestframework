package com.oss.framework.navigation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.PopupV2;
import com.oss.framework.utils.DelayUtils;

public class CategoryWizard extends PopupV2 {

    private static final String NAME_FIELD_ID_IN_CATEGORY_POPUP = "category-popup--input1";
    private static final String DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP = "category-popup--input2";
    private static final String ADMINISTRATION_PANEL_ICON_FULL_XPATH = "//div[@class='popupBackground']//i[@class='OSSIcon ossfont-Administration-Panel']";
    private static final String ANTENNA_ICON_FULL_XPATH = "//div[@class='popupBackground']//i[@class='OSSIcon ossfont-Antenna']";
    private static final String AI_CONTROL_DESK_ICON_FULL_XPATH = "//div[@class='popupBackground']//i[@class='OSSIcon ossfont-AI-Control-Desk']";
    private static final String ASSET_MANAGEMENT_ICON_FULL_XPATH = "//div[@class='popupBackground']//i[@class='OSSIcon ossfont-Asset-Management']";
    private static final String SAVE_BUTTON_FULL_XPATH = "//div[@class='popupBackground']//a[@class='CommonButton btn btn-primary btn-md']";

    private CategoryWizard(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static CategoryWizard create(WebDriver driver, WebDriverWait wait) {
        return new CategoryWizard(driver, wait);
    }

    public Input setNameValue(String name) {
        return setComponentValue(NAME_FIELD_ID_IN_CATEGORY_POPUP, name, Input.ComponentType.TEXT_FIELD);
    }

    public Input setDescriptionValue(String description) {
        return setComponentValue(DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP, description, Input.ComponentType.TEXT_AREA);
    }

    public void deleteNameValue() {
        deleteComponentValue(NAME_FIELD_ID_IN_CATEGORY_POPUP, Input.ComponentType.TEXT_FIELD);
    }

    public void deleteDescriptionValue() {
        deleteComponentValue(DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP, Input.ComponentType.TEXT_AREA);
    }

    public void clickOnAntennaIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(ANTENNA_ICON_FULL_XPATH)).click();
    }

    public void clickOnAdministrationPanelIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(ADMINISTRATION_PANEL_ICON_FULL_XPATH)).click();
    }

    public void clickOnAIControlDeskIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(AI_CONTROL_DESK_ICON_FULL_XPATH)).click();
    }

    public void clickOnAssetManagementIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(ASSET_MANAGEMENT_ICON_FULL_XPATH)).click();
    }

    public void clickOnSaveButton() {
        driver.findElement(By.xpath(SAVE_BUTTON_FULL_XPATH)).click();
    }
}
