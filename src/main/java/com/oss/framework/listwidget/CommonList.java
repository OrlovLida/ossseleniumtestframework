package com.oss.framework.listwidget;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class CommonList {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String id;
    
    private static final String CATEGORY_LIST_XPATH =
            "//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private static final String ALL_LIST_ELEMENT_KEBABS_XPATH = "//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private static final String ALL_CATEGORY_KEBABS_XPATH = "//div[@class='DropdownList']//div[@id='frameworkObjectButtonsGroup']";
    private static final String KEBAB_ID = "frameworkObjectButtonsGroup";
    private static final String FAVORITE_BUTTON_XPATH = ".//button[contains(@class, 'favourite')]";
    private static final String EDIT_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Edit')]";
    private static final String DELETE_BUTTON_XPATH = "//button[contains(@class, 'square')and contains(string(), 'Delete')]";
    private static final String CATEGORY_XPATH = "//div[@class='categoryLabel-text']";
    private static final String TEXT_WRAPPER_XPATH = ".//span[@class='long-text__wrapper']";
    private static final String ANCESTOR_LIST_ELEMENT_XPATH = "/ancestor::li[@class='listElement']";
    private static final String TEXT_EQUALS_XPATH = "[text()='%s']";
    private static final String TEXT_CONTAINS_XPATH = "[contains(text(),'%s')]";
    private static final String COLLAPSE_ICON_XPATH = "//i[contains (@class, 'chevron-up')]";
    private static final String EXPAND_ICON_XPATH = "//i[contains (@class, 'chevron-down')]";
    private static final String SHARE_ACTION_ID = "share_action";
    private static final String REMOVE_ACTION_ID = "remove_action";
    private static final String FAVORITE_ICON_XPATH = ".//i[contains(@class, 'star-o')]";
    
    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + commonListAppId + "')]"));
        return new CommonList(driver, wait, commonListAppId);
    }
    
    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        this.driver = driver;
        this.wait = wait;
        this.id = commonListAppId;
    }
    
    private WebElement getCommonList() {
        DelayUtils.waitByXPath(wait, "//div[contains(@" + CSSUtils.TEST_ID + ", '" + id + "')]");
        return driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + id + "']"));
    }
    
    public void callAction(String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsContainer.createFromParent(getCommonList(), driver, wait).callActionById(actionId);
    }
    
    public void callAction(String groupId, String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsContainer.createFromParent(getCommonList(), driver, wait).callActionById(groupId, actionId);
    }
    
    @Deprecated
    public void expandListElementKebab(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getListElementByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }
    
    @Deprecated
    public void expandCategoryKebab(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCategoryByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }
    
    public void deleteAllListElements() {
        DelayUtils.waitForPageToLoad(driver, wait);
        expandAllCategories();
        List<Row> rows = createRows();
       // int rowSize = rows.size();
        for (Row row : rows) {
            DelayUtils.waitForPageToLoad(driver, wait);
            row.callAction(ActionsContainer.KEBAB_GROUP_ID, REMOVE_ACTION_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }
    
    public void deleteAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        createCategories().forEach(category -> category.callAction(ActionsContainer.KEBAB_GROUP_ID, REMOVE_ACTION_ID));
    }
    
    public void expandAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<Category> categories = createCategories();
       categories.forEach(Category::expandCategory);
    }
    
    public void collapseAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> categoryLists = getCommonList().findElements(By.xpath(CATEGORY_LIST_XPATH + COLLAPSE_ICON_XPATH));
        for (int i = categoryLists.size(); i > 0; i--) {
            getCommonList().findElements(By.xpath(CATEGORY_LIST_XPATH + COLLAPSE_ICON_XPATH)).get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }
    
    public void clickOnCategoryByName(String name) {
        getCategory(name).selectCategory();
    }
    
    @Deprecated
    public boolean isListElementVisible(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getCommonList().findElements(By.xpath(TEXT_WRAPPER_XPATH + String.format(TEXT_EQUALS_XPATH, name))).isEmpty();
    }
    
    public boolean isRowVisible(String attributeName, String value) {
        List<String> rows = createRows().stream().map(row -> row.getValue(attributeName)).collect(Collectors.toList());
        return rows.contains(value);
        
    }
    
    public boolean isCategoryVisible(String name) {
       return createCategories().stream().map(category -> category.getValue().equals(name)).count() !=0;
    }
    
    @Deprecated
    public boolean isEditActionVisible(String name) {
        return !getCommonList()
                .findElements(By.xpath(
                        TEXT_WRAPPER_XPATH + String.format(TEXT_EQUALS_XPATH, name) + ANCESTOR_LIST_ELEMENT_XPATH + EDIT_BUTTON_XPATH))
                .isEmpty();
    }
    
    @Deprecated
    public boolean isFavorite(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getListElementByName(name));
        return getFavoriteButtonByListElementName(name).findElements(By.xpath(FAVORITE_ICON_XPATH)).size() == 0;
    }
    
    public int howManyListElements() {
        return createRows().size();
    }
    
    public int howManyCategories() {
        return createCategories().size();
    }
    
    @Deprecated
    public void clickOnEditButtonByListElementName(String name) {
        getEditButtonByListElementName(name).click();
    }
    
    @Deprecated
    private WebElement getDeleteButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath("." + DELETE_BUTTON_XPATH));
    }
    
    @Deprecated
    public void clickOnDeleteButtonByListElementName(String name) {
        getDeleteButtonByListElementName(name).click();
    }
    
    @Deprecated
    public void clickOnFavoriteButtonByListElementName(String name) {
        getFavoriteButtonByListElementName(name).click();
    }
    
    @Deprecated
    public void chooseShare() {
        DropdownList.create(driver, wait).selectOptionWithId(SHARE_ACTION_ID);
    }
    
    @Deprecated
    public void chooseDelete() {
        DropdownList.create(driver, wait).selectOptionWithId(REMOVE_ACTION_ID);
    }
    
    @Deprecated
    public ActionsContainer getActionsContainer() {
        return ActionsContainer.createFromParent(getCommonList().findElement(By.xpath("//div[@class='OssWindow']")), driver, wait);
    }
    
    public boolean isNoData() {
        List<WebElement> noData = this.driver
                .findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + id + "']//h3[contains(@class,'emptyResultsText')]"));
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
    
    @Deprecated
    private WebElement getListElementByName(String name) {
        String xpathWithTextEqual = TEXT_WRAPPER_XPATH + String.format(TEXT_EQUALS_XPATH, name) + ANCESTOR_LIST_ELEMENT_XPATH;
        if (isElementPresent(getCommonList(), By.xpath(xpathWithTextEqual))) {
            return getCommonList().findElement(By.xpath(xpathWithTextEqual));
        } else {
            return getCommonList()
                    .findElement(By.xpath(TEXT_WRAPPER_XPATH + String.format(TEXT_CONTAINS_XPATH, name) + ANCESTOR_LIST_ELEMENT_XPATH));
        }
    }
    
    @Deprecated
    private WebElement getCategoryByName(String name) {
        return getCommonList().findElement(By.xpath(CATEGORY_XPATH + String.format(TEXT_EQUALS_XPATH, name) + "/../.."));
    }
    
    @Deprecated
    private WebElement getEditButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath("." + EDIT_BUTTON_XPATH));
    }
    
    @Deprecated
    private WebElement getFavoriteButtonByListElementName(String name) {
        return getListElementByName(name).findElement(By.xpath(FAVORITE_BUTTON_XPATH));
    }
    
    public Category getCategory(String value) {
        return createCategories().stream().filter(category -> category.getValue().equals(value)).findFirst()
                .orElseThrow(() -> new RuntimeException("Provided List Category doesn't exist"));
    }
    
    public void selectRow(int row) {
        createRows().get(row).selectRow();
    }

    public List<Row> getAllRows() {
        return createRows();
    }

    public Row getRow(String attributeName, String value) {
        return createRows().stream().filter(row -> row.getValue(attributeName).equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Provided value doesn't exist"));
    }
    
    private List<Row> createRows() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> header = getCommonList().findElements(By.xpath(".//div[@class='header left']"));
        int size = header.size();
        if (size != 0) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", header.get(size - 1));
        }
        List<String> headers = header.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        
        return getCommonList().findElements(By.xpath(".//li[@class='listElement'] | .//li[@class='listElement rowSelected']"))
                .stream().map(row -> new Row(driver, wait, row, headers)).collect(Collectors.toList());
        
    }
    
    public List<Category> createCategories() {
        DelayUtils.waitForNestedElements(wait, getCommonList(), ".//li[@class='categoryListElement']");
        List<WebElement> categories = getCommonList().findElements(By.xpath(".//li[@class='categoryListElement']"));
        return categories.stream().map(category -> new Category(driver, wait, category)).collect(Collectors.toList());
    }
    public void fullTextSearch(String value){
        getAdvanceSearch().fullTextSearch(value);
    }

    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        openAdvancedSearch();
        setFilterContains(attributeId, componentType, value);
        getAdvanceSearch().clickApply();
    }
    private void openAdvancedSearch() {
        getAdvanceSearch().openSearchPanel();
    }

    private AdvancedSearch getAdvanceSearch(){
        return AdvancedSearch.createByWidgetId(driver,wait,id);
    }
    private void setFilterContains(String componentId, Input.ComponentType componentType, String value) {
        Input input = getAdvanceSearch().getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }
    
    public static class Row {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement row;
        private final List<String> headers;
        
        private Row(WebDriver driver, WebDriverWait wait, WebElement row, List<String> headers) {
            this.driver = driver;
            this.wait = wait;
            this.row = row;
            this.headers = headers;
        }
        
        public String getValue(String attributeName) {
            List<WebElement> columnData = row.findElements(By.className("columnData"));
            WebElement webElement = columnData.get(headers.indexOf(attributeName));
            if (!webElement.findElements(By.xpath(".//i[contains(@class,'check')]")).isEmpty()) {
                return "true";
            }
            return webElement.getText();
        }
        
        public void selectRow() {
            if (!row.getAttribute("class").contains("rowSelected")) {
                row.click();
            }
        }
        
        public boolean isFavorite() {
            return !row.findElements(By.xpath(".//button[@class='favouriteButton favourite']")).isEmpty();
        }
        
        public void setFavorite() {
            Actions action = new Actions(driver);
            if (!isFavorite()) {
                action.moveToElement(row.findElement(By.xpath(FAVORITE_BUTTON_XPATH))).click().build().perform();
            }
        }
        
        // add checking for other other actions
        public boolean isActionVisible(String actionId) {
            
            return !row.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty();
        }
        
        public void callAction(String groupId, String actionId) {
            Actions action = new Actions(driver);
            if (!row.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty()) {
                WebElement button = row.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']"));
                action.moveToElement(button).click().perform();
                return;
            }
            if (!row.findElements(By.className("actionsContainer")).isEmpty()) {
                ActionsContainer.createFromParent(row, driver, wait).callAction(groupId, actionId);
                
            }
        }

        public void callActionIcon(String ariaLabel) {
            String placeholdersXPath = ".//div[contains(@class,'placeholders')]";
            DelayUtils.waitForNestedElements(wait, row, placeholdersXPath);
            WebElement placeholdersAndActions = row.findElement(By.xpath(placeholdersXPath));
            WebElement icon = placeholdersAndActions.findElement(By.xpath(".//i[@aria-label='" + ariaLabel + "']"));
            DelayUtils.waitForClickability(wait, icon);
            Actions action = new Actions(driver);
            action.moveToElement(icon).click().build().perform();
        }

        public void callAction(String actionId) {
            callAction(null, actionId);
        }

        public void clickOnLink(String linkText) {
            String linkXpath = ".//div[contains(@class,'hyperlink placeholder')]";
            DelayUtils.waitForNestedElements(wait, row, linkXpath);
            WebElement rowWithLink = row.findElement(By.xpath(linkXpath));
            WebElement link = rowWithLink.findElement(By.xpath(String.format(".//*[contains(text(),'%s')]", linkText)));
            Actions action = new Actions(driver);
            action.moveToElement(link).click().build().perform();
        }
    }
    
    public static class Category {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement category;
        
        private Category(WebDriver driver, WebDriverWait wait, WebElement category) {
            this.driver = driver;
            this.wait = wait;
            this.category = category;
        }
        
        public String getValue() {
            return category.findElement(By.className("categoryLabel-text")).getText();
        }
        
        public void callAction(String groupId, String actionId) {
            Actions action = new Actions(driver);
            if (!category.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty()) {
                WebElement button = category.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']"));
                action.moveToElement(button).click().perform();
                return;
            }
            if (!category.findElements(By.className("actionsContainer")).isEmpty()) {
                ActionsContainer.createFromParent(category, driver, wait).callAction(groupId, actionId);
            }
        }
        
        public void callAction(String actionId) {
            Actions action = new Actions(driver);
            if (!category.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty()) {
                WebElement button = category.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']"));
                action.moveToElement(button).click().perform();
                return;
            }
            if (!category.findElements(By.className("actionsContainer")).isEmpty()) {
                ActionsContainer.createFromParent(category, driver, wait).callAction(actionId);
            }
        }
        
        public void expandCategory() {
            Actions actions = new Actions(driver);
            if (category.findElements(By.xpath(".//i[contains(@class,'chevron-up')]")).isEmpty()) {
                actions.moveToElement(category.findElement(By.xpath(".//i[contains(@class,'chevron-down')]"))).click().build().perform();
            }
            DelayUtils.waitForPageToLoad(driver,wait);
        }
        
        public void selectCategory() {
            Actions actions = new Actions(driver);
            actions.moveToElement(category).click().build().perform();
        }
        
    }
}
