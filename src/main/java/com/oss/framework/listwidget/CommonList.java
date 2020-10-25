package com.oss.framework.listwidget;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CommonList {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String id;

    private final String CATEGORY_LIST_XPATH = "//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private final String ALL_LIST_ELEMENT_KEBABS_XPATH = "//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private final String ALL_CATEGORY_KEBABS_XPATH = "//div[@class='DropdownList']//div[@id='frameworkObjectButtonsGroup']";
    private final String KEBAB_ID = "frameworkObjectButtonsGroup";
    private final String FAVORITE_BUTTON_XPATH = ".//button[contains(@class, 'favourite')]";
    private final String EDIT_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Edit')]";
    private final String DELETE_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Delete')]";
    private final String CATEGORY_XPATH = "//div[@class='categoryLabel']";
    private final String LIST_ELEMENT_XPATH = "//div[@class='text-wrapper']";
    private final String COLLAPSE_ICON_XPATH = "//i[contains (@class, 'chevron-up')]";
    private final String EXPAND_ICON_XPATH = "//i[contains (@class, 'chevron-down')]";
    private final String SHARE_ACTION_ID = "share_action";
    private final String REMOVE_ACTION_ID = "remove_action";
    private final String FAVORITE_ICON_XPATH = ".//i[contains(@class, 'star-o')]";

    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@data-attributename, '" + commonListAppId + "')]"));
        return new CommonList(driver, wait, commonListAppId);
    }

    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        this.driver = driver;
        this.wait = wait;
        this.id = commonListAppId;
    }

    private WebElement getCommonList() {
        DelayUtils.waitByXPath(wait, "//div[contains(@data-attributename, '" + id + "')]");
        return driver.findElement(By.xpath("//div[@data-attributename='" + id + "']"));
    }

    public void expandListElementKebab(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getListElementByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    public void expandCategoryKebab(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCategoryByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    public void deleteAllListElements() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> kebabs = getCommonList().findElements(By.xpath(ALL_LIST_ELEMENT_KEBABS_XPATH));
        for (int i = kebabs.size(); i > 0; i--) {
            DelayUtils.waitForPageToLoad(driver, wait);
            kebabs.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver, wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }

    public void deleteAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> kebabs = getCommonList().findElements(By.xpath(ALL_CATEGORY_KEBABS_XPATH));
        for (int i = kebabs.size(); i > 1; i--) {
            DelayUtils.waitForPageToLoad(driver, wait);
            kebabs.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver, wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }

    public void expandAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> categoryLists = getCommonList().findElements(By.xpath(CATEGORY_LIST_XPATH + EXPAND_ICON_XPATH));
        for (int i = categoryLists.size(); i > 0; i--) {
            categoryLists.get(i - 1).click();
        }
    }

    public void collapseAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> categoryLists = getCommonList().findElements(By.xpath(CATEGORY_LIST_XPATH + COLLAPSE_ICON_XPATH));
        for (int i = categoryLists.size(); i > 0; i--) {
            categoryLists.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }

    private WebElement getListElementByName(String name) {
        return driver.findElement(By.xpath(LIST_ELEMENT_XPATH + "[contains(text(),'" + name + "')]/../../../.."));
    }

    private WebElement getCategoryByName(String name) {
        return driver.findElement(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']/../.."));
    }

    public void clickOnCategoryByName(String name) {
        getCategoryByName(name).click();
    }

    public boolean isListElementVisible(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH + "[text()='" + name + "']")).size() > 0;
    }

    public boolean isCategoryVisible(String name) {
        return driver.findElements(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']")).size() > 0;
    }

    public boolean isEditActionVisible(String name) {
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH + "[text()='" + name + "']/../../../.." + EDIT_BUTTON_XPATH)).size() > 0;
    }

    public boolean isFavorite(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getListElementByName(name));
        return getFavoriteButtonByListElementName(name).findElements(By.xpath(FAVORITE_ICON_XPATH)).size() == 0;
    }

    public int howManyListElements() {
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH + "/../../../../../li[@class='listElement']")).size();
    }

    public int howManyCategories() {
        return getCommonList().findElements(By.xpath(CATEGORY_XPATH)).size();
    }

    private WebElement getEditButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath("." + EDIT_BUTTON_XPATH));
    }

    public void clickOnEditButtonByListElementName(String name) {
        getEditButtonByListElementName(name).click();
    }

    private WebElement getDeleteButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath("." + DELETE_BUTTON_XPATH));
    }

    public void clickOnDeleteButtonByListElementName(String name) {
        getDeleteButtonByListElementName(name).click();
    }

    private WebElement getFavoriteButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath(FAVORITE_BUTTON_XPATH));
    }

    public void clickOnFavoriteButtonByListElementName(String name) {
        getFavoriteButtonByListElementName(name).click();
    }

    public void chooseShare() {
        DropdownList.create(driver, wait).selectOptionWithId(SHARE_ACTION_ID);
    }

    public void chooseDelete() {
        DropdownList.create(driver, wait).selectOptionWithId(REMOVE_ACTION_ID);
    }

    public ActionsContainer getActionsContainer() {
        return ActionsContainer.createFromParent(getCommonList().findElement(By.xpath("//div[@class='OssWindow']")), driver, wait);
    }

    public boolean isNoData() {
        List<WebElement> noData = this.driver.findElements(By.xpath("//div[@data-attributename='" + id + "']//h3[contains(@class,'emptyResultsText')]"));
        return !noData.isEmpty();
    }

}


