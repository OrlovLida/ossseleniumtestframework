package com.oss.framework.widgets.list;

import com.oss.framework.components.categorylist.CategoryList;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.InlineMenu;
import com.oss.framework.components.contextactions.InlineMenuInterface;
import com.oss.framework.components.contextactions.OldInlineMenu;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CommonList extends Widget {

    private static final String COMMON_LIST_CLASS = "CommonListApp";
    private static final String HEADERS_XPATH = ".//div[@class='header left']";
    private static final String LIST_ELEMENT_XPATH = ".//li[contains(@" + CSSUtils.TEST_ID + ", 'list-item')]";
    private static final String NO_DATA_TEXT_XPATH = ".//h3[contains(@class,'emptyResultsText')]";
    private static final String PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION = "Provided value doesn't exist";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView(true);";
    private static final String INLINE_MENU_XPATH = ".//div[@class='contextButtonMenu'] | .//div[@id='frameworkObjectButtonsGroup']";

    private CommonList(WebDriver driver, WebDriverWait webDriverWait, String commonListAppId) {
        super(driver, webDriverWait, commonListAppId);
    }

    public static CommonList create(WebDriver driver, WebDriverWait wait, String widgetId) {
        waitForWidget(wait, COMMON_LIST_CLASS);
        waitForWidgetById(wait, widgetId);
        return new CommonList(driver, wait, widgetId);
    }

    public void fullTextSearch(String value) {
        getAdvanceSearch().fullTextSearch(value);
    }

    public void callAction(String actionId) {
        ActionsContainer.createFromParent(webElement, driver, webDriverWait).callActionById(actionId);
    }

    public void callAction(String groupId, String actionId) {
        ActionsContainer.createFromParent(webElement, driver, webDriverWait).callActionById(groupId, actionId);
    }

    public boolean hasNoData() {
        return !webElement.findElements(By.xpath(NO_DATA_TEXT_XPATH)).isEmpty();
    }

    public void expandAllCategories() {
        DelayUtils.waitForElementToLoad(webDriverWait, webElement);
        List<CategoryList> categories = getCategories();
        categories.forEach(CategoryList::expandCategory);
    }

    public void collapseAllCategories() {
        List<CategoryList> categories = getCategories();
        categories.forEach(CategoryList::collapseCategory);
    }

    public void expandCategory(String name) {
        getCategory(name).expandCategory();
    }

    public boolean isCategoryDisplayed(String name) {
        return getCategories().stream().anyMatch(category -> category.getValue().equals(name));
    }

    public int countCategories() {
        return getCategories().size();
    }

    public CategoryList getCategory(String value) {
        return getCategories().stream().filter(category -> category.getValue().equals(value)).findFirst()
                .orElseThrow(() -> new NoSuchElementException(PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION));
    }

    public List<CategoryList> getCategories() {
        return CategoryList.create(driver, webDriverWait, id);
    }

    public List<String> getRowHeaders() {
        List<WebElement> headers = webElement.findElements(By.xpath(HEADERS_XPATH));
        if (!headers.isEmpty()) {
            ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, headers.get(headers.size() - 1));
        }
        return headers.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int countRows() {
        return getRows().size();
    }

    public List<Row> getRows() {
        List<String> headers = getRowHeaders();
        return webElement.findElements(By.xpath(LIST_ELEMENT_XPATH))
                .stream().map(row -> new Row(driver, webDriverWait, row, headers)).collect(Collectors.toList());
    }

    public boolean isRowDisplayed(String attributeName, String value) {
        return getRows().stream().anyMatch(row -> row.getValue(attributeName).contains(value));
    }

    public Row getRow(String attributeName, String value) {
        return getRows().stream().filter(row -> row.getValue(attributeName).equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION));
    }

    public Row getRowContains(String attributeName, String valueContains) {
        return getRows().stream().filter(row -> row.getValue(attributeName).contains(valueContains))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(PROVIDED_VALUE_DOESN_T_EXIST_EXCEPTION));
    }

    public void selectRow(int row) {
        getRows().get(row).selectRow();
    }

    private AdvancedSearch getAdvanceSearch() {
        return AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
    }

    public static class Row {
        private static final String CHECK_CHECKBOX_XPATH = ".//i[contains(@class,'check')]";
        private static final String COLUMN_DATA_CLASS = "list__cell_content";
        private static final String SELECTED_ROW_CLASS = "rowSelected";
        private static final String FAVOURITE_BUTTON_XPATH = ".//button[@class='favouriteButton favourite']";
        private static final String STAR_BUTTON_XPATH = ".//button[contains(@class, 'favourite')]";
        private static final String PLACE_HOLDERS_XPATH = ".//div[contains(@class,'placeholders')]";
        private static final String LINK_PATTERN = ".//div[contains(@class,'hyperlink placeholder')]//*[contains(text(),'%s')]";
        private static final String ICON_BY_ARIA_LABEL_PATTERN = ".//i[@aria-label='%s']";
        private static final String ICON_BY_ID_PATTERN = ".//button[@" + CSSUtils.TEST_ID + "= '%s']";

        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement rowElement;
        private final List<String> headers;

        private Row(WebDriver driver, WebDriverWait wait, WebElement row, List<String> headers) {
            this.driver = driver;
            this.wait = wait;
            this.rowElement = row;
            this.headers = headers;
        }

        public String getValue(String attributeName) {
            List<WebElement> rowCells = rowElement.findElements(By.className(COLUMN_DATA_CLASS));
            WebElement cell = rowCells.get(headers.indexOf(attributeName));
            if (!cell.findElements(By.xpath(CHECK_CHECKBOX_XPATH)).isEmpty()) {
                return cell.getAttribute("value");
            }
            return cell.getText();
        }

        public boolean isFavorite() {
            return !rowElement.findElements(By.xpath(FAVOURITE_BUTTON_XPATH)).isEmpty();
        }

        public void markFavorite() {
            if (!isFavorite()) {
                WebElementUtils.clickWebElement(driver, rowElement.findElement(By.xpath(STAR_BUTTON_XPATH)));
            }
        }

        public boolean isActionIconPresent(String actionId) {
            return !rowElement.findElements(By.xpath(String.format(ICON_BY_ID_PATTERN, actionId))).isEmpty();
        }

        public void callAction(String groupId, String actionId) {
            getInlineMenu().callAction(groupId, actionId);
        }

        public void callActionByLabel(String actionLabel) {
            getInlineMenu().callActionByLabel(actionLabel);
        }

        public void callActionByLabel(String groupLabel, String actionLabel) {
            getInlineMenu().callActionByLabel(groupLabel, actionLabel);
        }

        public void callActionIcon(String ariaLabel) {
            DelayUtils.waitForNestedElements(wait, rowElement, PLACE_HOLDERS_XPATH);
            WebElement placeholdersAndActions = rowElement.findElement(By.xpath(PLACE_HOLDERS_XPATH));
            WebElement icon = placeholdersAndActions.findElement(By.xpath(String.format(ICON_BY_ARIA_LABEL_PATTERN, ariaLabel)));
            DelayUtils.waitForClickability(wait, icon);
            WebElementUtils.clickWebElement(driver, icon);
        }

        public void callAction(String actionId) {
            DelayUtils.waitForElementToLoad(wait, rowElement);
            ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, rowElement);
            if (!rowElement.findElements(By.xpath(String.format(ICON_BY_ID_PATTERN, actionId))).isEmpty()) {
                WebElement button = rowElement.findElement(By.xpath(String.format(ICON_BY_ID_PATTERN, actionId)));
                WebElementUtils.clickWebElement(driver, button);
            } else {
                getInlineMenu().callAction(actionId);
            }
        }

        public void clickLink(String linkText) {
            DelayUtils.waitForElementToLoad(wait, rowElement);
            WebElement link = rowElement.findElement(By.xpath(String.format(LINK_PATTERN, linkText)));
            WebElementUtils.clickWebElement(driver, link);
        }

        public void selectRow() {
            if (!rowElement.getAttribute("class").contains(SELECTED_ROW_CLASS)) {
                rowElement.click();
            }
        }

        private InlineMenuInterface getInlineMenu() {
            DelayUtils.waitForPageToLoad(driver, wait);
            DelayUtils.waitForNestedElements(wait, rowElement, INLINE_MENU_XPATH);
            boolean isOldInlineMenu = !driver.findElements(By.className("contextButtonMenu")).isEmpty();
            if (isOldInlineMenu) {
                return OldInlineMenu.create(rowElement, driver, wait);
            } else {
                return InlineMenu.create(rowElement, driver, wait);
            }
        }
    }
}
