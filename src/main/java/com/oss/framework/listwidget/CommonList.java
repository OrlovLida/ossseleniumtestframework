package com.oss.framework.listwidget;

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
    protected final WebElement webelement;

    private final String CATEGORY_LIST_XPATH ="//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private final String ALL_LIST_ELEMENT_KEBABS_XPATH ="//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private final String ALL_CATEGORY_KEBABS_XPATH ="//div[@class='DropdownList']//div[@id='frameworkObjectButtonsGroup']";
    private final String KEBAB_ID="frameworkObjectButtonsGroup";
    private final String FAVORITE_BUTTON_XPATH=".//button[contains(@class, 'favourite')]";
    private final String EDIT_BUTTON_XPATH="//button[contains(@class, 'square')]";
    private final String CATEGORY_XPATH="//div[contains(@class,'categoryLabel')]";
    private final String LIST_ELEMENT_XPATH="//div[@class='text-wrapper']";
    private final String COLLAPSE_ICON_XPATH="//i[contains (@class, 'chevron-up')]";
    private final String EXPAND_ICON_XPATH="//i[contains (@class, 'chevron-down')]";
    private final String SHARE_ACTION_ID="share_action";
    private final String REMOVE_ACTION_ID="remove_action";
    private final String FAVORITE_ICON_XPATH = ".//i[contains(@class, 'star-o')]";

    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppID){
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@data-attributename, '"+ commonListAppID +"')]"));
        return new CommonList(driver, wait, commonListAppID);
    }

    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppID) {
            this.driver = driver;
            this.wait = wait;
            this.webelement = driver.findElement(By.xpath("//div[@data-attributename='" + commonListAppID + "']"));
        }

    public void expandListElementKebab(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        getListElementByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    public void expandCategoryKebab(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        getCategoryByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    public void deleteAllListElements(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> kebabs = driver.findElements(By.xpath(ALL_LIST_ELEMENT_KEBABS_XPATH));
        for (int i=kebabs.size(); i>0; i--){
            DelayUtils.waitForPageToLoad(driver,wait);
            kebabs.get(i-1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    public void deleteAllCategories(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> kebabs = driver.findElements(By.xpath(ALL_CATEGORY_KEBABS_XPATH));
        for (int i=kebabs.size(); i>1; i--){
            DelayUtils.waitForPageToLoad(driver,wait);
            kebabs.get(i-1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }
    public void expandAllCategories(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + EXPAND_ICON_XPATH));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
        }
    }

    public void collapseAllCategories(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + COLLAPSE_ICON_XPATH));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    public WebElement getListElementByName(String name){
        return driver.findElement(By.xpath(LIST_ELEMENT_XPATH + "[text()='"+ name +"']/../../../.."));
    }

    public WebElement getCategoryByName(String name) {
        return driver.findElement(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']/../.."));
    }

    public boolean isListElementVisible(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH + "[text()='"+ name +"']")).size()>0;
    }

    public boolean isCategoryVisible(String name){
        return driver.findElements(By.xpath(CATEGORY_XPATH + "[text()='" + name + "']")).size()>0;
    }

    public boolean isEditActionVisible(String name){
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH+"[text()='"+ name +"']/../../../.." + EDIT_BUTTON_XPATH)).size()>0;
    }

    public boolean isFavorite(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getListElementByName(name));
        return getFavoriteButtonByListElementName(name).findElements(By.xpath(FAVORITE_ICON_XPATH)).size()==0;
    }

    public int howManyListElements(){
        return driver.findElements(By.xpath(LIST_ELEMENT_XPATH + "/../../../../../li[@class='listElement']")).size();
    }

    public int howManyCategories(){
        return driver.findElements(By.xpath(CATEGORY_XPATH)).size();
    }

    public WebElement getEditButtonByListElementName(String name){
        return getListElementByName(name).findElement(By.xpath("."+EDIT_BUTTON_XPATH));
    }

    public WebElement getFavoriteButtonByListElementName(String name){
        return getListElementByName(name).findElement(By.xpath(FAVORITE_BUTTON_XPATH));
    }

    public void chooseShare(){
        DropdownList.create(driver,wait).selectOptionWithId(SHARE_ACTION_ID);
    }

    public void chooseDelete() {
        DropdownList.create(driver,wait).selectOptionWithId(REMOVE_ACTION_ID);
    }

    public void expandKebabByParent(WebElement parent){
        parent.findElement(By.xpath(".//*[@id='" + parent+ "']"));
    }

    }


