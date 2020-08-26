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

    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppID){
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@data-attributename, '"+ commonListAppID +"')]"));
        return new CommonList(driver, wait, commonListAppID);
    }

    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppID) {
            this.driver = driver;
            this.wait = wait;
            this.webelement = driver.findElement(By.xpath("//div[@data-attributename='" + commonListAppID + "']"));
        }

    public void expandListElementKebab(String filerName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getListElementByName(filerName).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    public void expandCategoryKebab(String folderName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getCategoryByName(folderName).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
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
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + "//i[contains (@class, 'chevron-down')]"));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
        }
    }

    public void collapseAllCategories(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + "//i[contains (@class, 'chevron-up')]"));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    public WebElement getListElementByName(String name){
        return driver.findElement(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']/../../../.."));
    }

    public WebElement getCategoryByName(String name) {
        return driver.findElement(By.xpath("//div[contains(@class,'categoryLabel') and text()='" + name + "']/../.."));
    }

    public boolean isListElementVisible(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.findElements(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']")).size()>0;
    }

    public boolean isCategoryVisible(String name){
        return driver.findElements(By.xpath("//div[contains(@class,'categoryLabel') and text()='" + name + "']")).size()>0;
    }

    public boolean isEditActionVisible(String name){
        return driver.findElements(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']/../../../..//button[contains(@class, 'square')]")).size()>0;
    }

    public boolean isFavorite(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getListElementByName(name));
        return getFavoriteButtonByListElementName(name).findElements(By.xpath(".//i[contains(@class, 'star-o')]")).size()==0;
    }

    public int howManyListElements(){
        return driver.findElements(By.xpath("//div[contains(@id,'name')]")).size();
    }

    public int howManyCategories(){
        return driver.findElements(By.xpath("//div[contains(@class,'categoryLabel')]")).size();
    }

    public WebElement getEditButtonByListElementName(String name){
        return getListElementByName(name).findElement(By.xpath(".//button[contains(@class, 'square')]"));
    }

    public WebElement getFavoriteButtonByListElementName(String name){
        return getListElementByName(name).findElement(By.xpath(".//button[contains(@class, 'favourite')]"));
    }

    public void chooseShare(){
        DropdownList.create(driver,wait).selectOptionWithId("share_action");
    }

    public void chooseDelete() {
        DropdownList.create(driver,wait).selectOptionWithId("remove_action");
    }

    public void expandKebabByParent(WebElement parent){
        parent.findElement(By.xpath(".//*[@id='" + parent+ "']"));
    }

    }


