package com.oss.framework.listwidget;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treewidget.InlineMenu;

public class CommonList {

    private static final String LINK_XPATH = ".//div[contains(@class,'hyperlink placeholder')]";
    private static final String PLACE_HOLDERS_XPATH = ".//div[contains(@class,'placeholders')]";
    private static final String FAVOURITE_BUTTON_XPATH = ".//button[@class='favouriteButton favourite']";
    private static final String COLUMN_DATA_CLASS = "columnData";
    private static final String CHECK_CHECKBOX_XPATH = ".//i[contains(@class,'check')]";
    private static final String CATEGORY_LIST_XPATH = ".//li[@class='categoryListElement']";
    private static final String HEADERS_XPATH = ".//div[@class='header left']";
    private static final String LIST_ELEMENT_XPATH = ".//li[@class='listElement'] | .//li[@class='listElement rowSelected']";
    private static final String STAR_BUTTON_XPATH = ".//button[contains(@class, 'favourite')]";
    private static final String COLLAPSE_ICON_XPATH = ".//i[contains(@class,'chevron-up')]";
    private static final String EXPAND_ICON_XPATH = ".//i[contains(@class,'chevron-down')]";
    private static final String REMOVE_ACTION_ID = "remove_action";
    private static final String ACTION_CONTAINER_CLASS = "actionsContainer";
    private static final String CATEGORY_NAME_XPATH = "categoryLabel-text";
    private static final String SELECTED_ROW_CLASS = "rowSelected";
    private static final String NO_DATA_TEXT_XPATH = "//h3[contains(@class,'emptyResultsText')]";
    private static final String PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION = "Provided value doesn't exist";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView(true);";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String id;

    private CommonList(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        this.driver = driver;
        this.wait = wait;
        this.id = commonListAppId;
    }

    public static CommonList create(WebDriver driver, WebDriverWait wait, String commonListAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + commonListAppId + "')]"));
        return new CommonList(driver, wait, commonListAppId);
    }

    public void callAction(String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsContainer.createFromParent(getCommonList(), driver, wait).callActionById(actionId);
    }

    public void callAction(String groupId, String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsContainer.createFromParent(getCommonList(), driver, wait).callActionById(groupId, actionId);
    }

    public void deleteAllListElements() {
        DelayUtils.waitForPageToLoad(driver, wait);
        expandAllCategories();
        List<Row> rows = createRows();
        for (Row row : rows) {
            DelayUtils.waitForPageToLoad(driver, wait);
            row.callAction(REMOVE_ACTION_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
    }

    public void deleteAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        createCategories().forEach(category -> category.callAction(REMOVE_ACTION_ID));
    }

    public void expandAllCategories() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<Category> categories = createCategories();
        categories.forEach(Category::expandCategory);
    }

    public void collapseAllCategories() {
        List<Category> categories = createCategories();
        categories.forEach(Category::collapseCategory);
    }

    public void clickOnCategoryByName(String name) {
        getCategory(name).selectCategory();
    }

    public boolean isRowVisible(String attributeName, String value) {
        List<String> rows = createRows().stream().map(row -> row.getValue(attributeName)).collect(Collectors.toList());
        return rows.contains(value);

    }

    public boolean isCategoryVisible(String name) {
        return createCategories().stream().map(category -> category.getValue().equals(name)).count() != 0;
    }

    public int howManyListElements() {
        return createRows().size();
    }

    public int howManyCategories() {
        return createCategories().size();
    }

    public boolean isNoData() {
        List<WebElement> noData = this.driver
                .findElements(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + id + "']" + NO_DATA_TEXT_XPATH));
        return !noData.isEmpty();
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
                .orElseThrow(() -> new RuntimeException(PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION));
    }

    public Row getRowContains(String attributeName, String valueContains) {
        return createRows().stream().filter(row -> row.getValue(attributeName).contains(valueContains))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION));
    }

    public List<Category> createCategories() {
        DelayUtils.waitForNestedElements(wait, getCommonList(), CATEGORY_LIST_XPATH);
        List<WebElement> categories = getCommonList().findElements(By.xpath(CATEGORY_LIST_XPATH));
        return categories.stream().map(category -> new Category(driver, wait, category)).collect(Collectors.toList());
    }

    public void fullTextSearch(String value) {
        getAdvanceSearch().fullTextSearch(value);
    }

    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        openAdvancedSearch();
        setFilterContains(attributeId, componentType, value);
        getAdvanceSearch().clickApply();
    }

    public List<String> getHeaders() {
        return getCommonList().findElements(By.xpath(HEADERS_XPATH)).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    private WebElement getCommonList() {
        DelayUtils.waitByXPath(wait, "//div[contains(@" + CSSUtils.TEST_ID + ", '" + id + "')]");
        return driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + id + "']"));
    }

    private List<Row> createRows() {
        DelayUtils.waitForPageToLoad(driver, wait);
        List<WebElement> header = getCommonList().findElements(By.xpath(HEADERS_XPATH));
        int size = header.size();
        if (size != 0) {
            ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, header.get(size - 1));
        }
        List<String> headers = header.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        return getCommonList().findElements(By.xpath(LIST_ELEMENT_XPATH))
                .stream().map(row -> new Row(driver, wait, row, headers)).collect(Collectors.toList());

    }

    private void openAdvancedSearch() {
        getAdvanceSearch().openSearchPanel();
    }

    private AdvancedSearch getAdvanceSearch() {
        return AdvancedSearch.createByWidgetId(driver, wait, id);
    }

    private void setFilterContains(String componentId, Input.ComponentType componentType, String value) {
        Input input = getAdvanceSearch().getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public static class Row {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement rowElement;
        private final List<String> headers;

        private Row(WebDriver driver, WebDriverWait wait, WebElement rowElement, List<String> headers) {
            this.driver = driver;
            this.wait = wait;
            this.rowElement = rowElement;
            this.headers = headers;
        }

        public String getValue(String attributeName) {
            List<WebElement> columnData = rowElement.findElements(By.className(COLUMN_DATA_CLASS));
            WebElement webElement = columnData.get(headers.indexOf(attributeName));
            if (!webElement.findElements(By.xpath(CHECK_CHECKBOX_XPATH)).isEmpty()) {
                return "true";
            }
            return webElement.getText();
        }

        public void selectRow() {
            if (!rowElement.getAttribute("class").contains(SELECTED_ROW_CLASS)) {
                rowElement.click();
            }
        }

        public boolean isFavorite() {
            return !rowElement.findElements(By.xpath(FAVOURITE_BUTTON_XPATH)).isEmpty();
        }

        public void setFavorite() {
            Actions action = new Actions(driver);
            if (!isFavorite()) {
                action.moveToElement(rowElement.findElement(By.xpath(STAR_BUTTON_XPATH))).click().build().perform();
            }
        }

        // add checking for other actions
        public boolean isActionVisible(String actionId) {
            return !rowElement.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty();
        }

        public void callAction(String groupId, String actionId) {
            if (!rowElement.findElements(By.className(ACTION_CONTAINER_CLASS)).isEmpty()) {
                InlineMenu.create(rowElement, driver, wait).callAction(groupId, actionId);
            }
        }

        public void callActionIcon(String ariaLabel) {
            DelayUtils.waitForNestedElements(wait, rowElement, PLACE_HOLDERS_XPATH);
            WebElement placeholdersAndActions = rowElement.findElement(By.xpath(PLACE_HOLDERS_XPATH));
            WebElement icon = placeholdersAndActions.findElement(By.xpath(".//i[@aria-label='" + ariaLabel + "']"));
            DelayUtils.waitForClickability(wait, icon);
            Actions action = new Actions(driver);
            action.moveToElement(icon).click().build().perform();
        }

        public void callAction(String actionId) {
            Actions action = new Actions(driver);
            ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, rowElement);
            if (!rowElement.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty()) {
                WebElement button = rowElement.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']"));
                action.moveToElement(button).click().perform();
                return;
            }
            if (!rowElement.findElements(By.className(ACTION_CONTAINER_CLASS)).isEmpty()) {
                InlineMenu.create(rowElement, driver, wait).callAction(actionId);
            }
        }

        public void clickOnLink(String linkText) {
            DelayUtils.waitForNestedElements(wait, rowElement, LINK_XPATH);
            WebElement rowWithLink = rowElement.findElement(By.xpath(LINK_XPATH));
            WebElement link = rowWithLink.findElement(By.xpath(String.format(".//*[contains(text(),'%s')]", linkText)));
            Actions action = new Actions(driver);
            action.moveToElement(link).click().build().perform();
        }
    }

    public static class Category {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement categoryElement;

        private Category(WebDriver driver, WebDriverWait wait, WebElement categoryElement) {
            this.driver = driver;
            this.wait = wait;
            this.categoryElement = categoryElement;
        }

        public String getValue() {
            return categoryElement.findElement(By.className(CATEGORY_NAME_XPATH)).getText();
        }

        public void callAction(String groupId, String actionId) {
            if (!categoryElement.findElements(By.className(ACTION_CONTAINER_CLASS)).isEmpty()) {
                InlineMenu.create(categoryElement, driver, wait).callAction(groupId, actionId);
            }
        }

        public void callAction(String actionId) {
            Actions action = new Actions(driver);
            if (!categoryElement.findElements(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']")).isEmpty()) {
                WebElement button = categoryElement.findElement(By.xpath(".//button[@" + CSSUtils.TEST_ID + "= '" + actionId + "']"));
                action.moveToElement(button).click().perform();
                return;
            }
            if (!categoryElement.findElements(By.className(ACTION_CONTAINER_CLASS)).isEmpty()) {
                InlineMenu.create(categoryElement, driver, wait).callAction(actionId);
            }
        }

        public void expandCategory() {
            Actions actions = new Actions(driver);
            if (!isExpanded()) {
                actions.moveToElement(categoryElement.findElement(By.xpath(EXPAND_ICON_XPATH))).click().build().perform();
            }
            DelayUtils.waitForPageToLoad(driver, wait);
        }

        public void collapseCategory() {
            Actions actions = new Actions(driver);
            if (isExpanded()) {
                actions.moveToElement(categoryElement.findElement(By.xpath(COLLAPSE_ICON_XPATH))).click().build().perform();
            }
            DelayUtils.waitForPageToLoad(driver, wait);
        }

        public void selectCategory() {
            Actions actions = new Actions(driver);
            actions.moveToElement(categoryElement).click().build().perform();
        }

        private boolean isExpanded() {
            return !categoryElement.findElements(By.xpath(COLLAPSE_ICON_XPATH)).isEmpty();
        }

    }
}
