package com.oss.framework.listwidget;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class CommonList {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String id;

    private static final String CATEGORY_LIST_XPATH = "//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private static final String ALL_LIST_ELEMENT_KEBABS_XPATH = "//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private static final String ALL_CATEGORY_KEBABS_XPATH = "//div[@class='DropdownList']//div[@id='frameworkObjectButtonsGroup']";
    private static final String KEBAB_ID = "frameworkObjectButtonsGroup";
    private static final String FAVORITE_BUTTON_XPATH = ".//button[contains(@class, 'favourite')]";
    private static final String EDIT_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Edit')]";
    private static final String DELETE_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Delete')]";
    private static final String CATEGORY_XPATH = "//div[@class='categoryLabel-text']";
    private static final String LIST_ELEMENT_XPATH = ".//div[@class='text-wrapper']";
    private static final String COLLAPSE_ICON_XPATH = "//i[contains (@class, 'chevron-up')]";
    private static final String EXPAND_ICON_XPATH = "//i[contains (@class, 'chevron-down')]";
    private static final String SHARE_ACTION_ID = "share_action";
    private static final String REMOVE_ACTION_ID = "remove_action";
    private static final String FAVORITE_ICON_XPATH = ".//i[contains(@class, 'star-o')]";

    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@"+ CSSUtils.TEST_ID +", '" + commonListAppId + "')]"));
        return new CommonList(driver, wait, commonListAppId);
    }

    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        this.driver = driver;
        this.wait = wait;
        this.id = commonListAppId;
    }

    private WebElement getCommonList() {
        DelayUtils.waitByXPath(wait, "//div[contains(@"+ CSSUtils.TEST_ID +", '" + id + "')]");
        return driver.findElement(By.xpath("//div[@"+ CSSUtils.TEST_ID +"='" + id + "']"));
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

    public void clickOnCategoryByName(String name) {
        getCategoryByName(name).click();
    }

    public boolean isListElementVisible(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getCommonList().findElements(By.xpath(LIST_ELEMENT_XPATH + "[text()='" + name + "']")).isEmpty();
    }

    public boolean isCategoryVisible(String name) {
        return !getCommonList().findElements(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']")).isEmpty();
    }

    public boolean isEditActionVisible(String name) {
        return !getCommonList().findElements(By.xpath(LIST_ELEMENT_XPATH + "[text()='" + name + "']/../../../.." + EDIT_BUTTON_XPATH)).isEmpty();
    }

    public boolean isFavorite(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getListElementByName(name));
        return getFavoriteButtonByListElementName(name).findElements(By.xpath(FAVORITE_ICON_XPATH)).size() == 0;
    }

    public int howManyListElements() {
        return getCommonList().findElements(By.xpath(LIST_ELEMENT_XPATH + "/../../../../../li[@class='listElement']")).size();
    }

    public int howManyCategories() {
        return getCommonList().findElements(By.xpath(CATEGORY_XPATH)).size();
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
        List<WebElement> noData = this.driver.findElements(By.xpath("//div[@"+ CSSUtils.TEST_ID +"='" + id + "']//h3[contains(@class,'emptyResultsText')]"));
        return !noData.isEmpty();
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private WebElement getListElementByName(String name) {
        if (isElementPresent(getCommonList(), By.xpath((LIST_ELEMENT_XPATH + "[text()='" + name + "']/../../../..")))) {
            return getCommonList().findElement(By.xpath(LIST_ELEMENT_XPATH + "[text()='" + name + "']/../../../.."));
        } else {
            return getCommonList().findElement(By.xpath(LIST_ELEMENT_XPATH + "[contains(text(),'" + name + "')]/../../../.."));
        }
    }

    private WebElement getCategoryByName(String name) {
        return getCommonList().findElement(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']/../.."));
    }

    private WebElement getEditButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath("." + EDIT_BUTTON_XPATH));
    }

    private WebElement getFavoriteButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath(FAVORITE_BUTTON_XPATH));
    }
}


