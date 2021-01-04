package com.oss.framework.components.portals;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateCategoryPopup extends PopupV2 {

    private static final String NAME_FIELD_ID_IN_CATEGORY_POPUP = "category-popup--input1";
    private static final String DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP = "category-popup--input2";
    private static final String SECOND_ICON_FULL_XPATH = "/html/body/div[8]/div/div/div[2]/div/div[1]/div/div[4]/div/span[2]";
    private static final String FIRST_ICON_FULL_XPATH = "/html/body/div[8]/div/div/div[2]/div/div[1]/div/div[4]/div/span[1]";
    private static final String SAVE_BUTTON_FULL_XPATH = "/html/body/div[8]/div/div/div[2]/div/div[1]/div/div[5]/a[2]";

    public CreateCategoryPopup(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public Input setNameValue(String name) {
        return setComponentValue(NAME_FIELD_ID_IN_CATEGORY_POPUP, name, Input.ComponentType.TEXT_FIELD);
    }

    public Input setDescriptionValue(String description) {
        return setComponentValue(DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP, description, Input.ComponentType.TEXT_AREA);
    }

    public void clickOnFirstIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(FIRST_ICON_FULL_XPATH)).click();
    }

    public void clickOnSecondIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.findElement(By.xpath(SECOND_ICON_FULL_XPATH)).click();
    }

    public void clickOnSaveButton() {
        driver.findElement(By.xpath(SAVE_BUTTON_FULL_XPATH)).click();
    }
}
