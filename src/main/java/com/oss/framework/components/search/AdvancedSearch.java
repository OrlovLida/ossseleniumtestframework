package com.oss.framework.components.search;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class AdvancedSearch {
    public static final String SEARCH_COMPONENT_CLASS = "advanced-search_search";
    private static final String QUICK_FILTERS_ID = "quick_filters";
    private static final String TAGS_CLASS = "tagsWidgetDisplay";
    private static final String TAGS_ITEMS = ".md-input-value";
    private static final String FILTERS_SETTINGS_XPATH = "//div[contains(@class,'filters-settings')]";
    private static final String FILTERS_SETTINGS_CLASS = "filters-settings";
    private static final String SEARCH_PANEL_OPEN_BUTTON = ".//button[@class='button-filters-panel']";
    private static final String TAG_CLOSE_BUTTON_PATH = ".//span[@class='md-input-close']";
    private static final String TAGS_SEPARATOR = ": ";
    private static final String ADVANCED_SEARCH_CSS = ".advanced-search_panel";
    private static final String FILTERS_BOX_CSS = ".filters-box";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public AdvancedSearch(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.className(SEARCH_COMPONENT_CLASS));
    }

    private AdvancedSearch(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static AdvancedSearch createByClass(WebDriver driver, WebDriverWait wait, String className) {
        DelayUtils.waitByXPath(wait, "//*[contains(@class,'" + className + "')]");
        WebElement webElement = driver.findElement(By.className(className));
        return new AdvancedSearch(driver, wait, webElement);
    }

    public static AdvancedSearch createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitBy(wait, By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, id)));
        WebElement webElement = driver.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, id)));
        return new AdvancedSearch(driver, wait, webElement);
    }

    public static AdvancedSearch createByWidgetId(WebDriver driver, WebDriverWait wait, String widgetId) {
        DelayUtils.waitByXPath(wait, "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']");
        WebElement widget = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        return new AdvancedSearch(driver, wait, widget);
    }

    public void fullTextSearch(String text) {
        clearFullText();
        getFullTextSearch().sendKeys(text);
    }

    public Multimap<String, String> getAppliedFilters() {
        return parseTags(getTags().getTags());
    }

    public void clearFullText() {
        WebElement fullTextSearch = getFullTextSearch();
        fullTextSearch.sendKeys(Keys.CONTROL + "a");
        fullTextSearch.sendKeys(Keys.DELETE);
    }

    public void clearAllFilters() {
        clearFullText();
        clearTags();
    }

    public void clearFilter(String filterLabel) {
        clearTag(filterLabel);
    }

    public SearchPanel openSearchPanel() {
        if (isFilterBoxVisible()) {
            return getFiltersBox();
        } else {
            if (!isSearchPanelOpen()) {
                WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(SEARCH_PANEL_OPEN_BUTTON)));
            }
            return getSearchPanel();
        }
    }

    public void markFavoriteFilter(String label) {
        openFiltersSettings().markFavoriteFilter(label);
    }

    public void selectAttributes(List<String> attributeIds) {
        openFiltersSettings().selectAttributes(attributeIds);
    }

    public void unselectAttributes(List<String> attributeIds) {
        openFiltersSettings().unselectAttributes(attributeIds);
    }

    public void selectSavedFilterByLabel(String label) {
        openFiltersSettings().selectFilterByLabel(label);
    }

    public void saveAsNewFilter(String name) {
        openSearchPanel().saveAsNewFilter(name);
    }

    public void saveFilter() {
        openSearchPanel().saveFilter();
    }

    public void clickClearAll() {
        openSearchPanel().clickClearAll();
    }

    public void backToDefault() {
        openSearchPanel().clickBackToDefault();
    }

    public void changeAttributesOrder(String attributeId, int position) {
        openSearchPanel().changeAttributesOrder(attributeId, position);
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        return openSearchPanel().getComponent(componentId, componentType);
    }

    public Input getComponent(String componentId) {
        return openSearchPanel().getComponent(componentId);
    }

    public void setFilter(String componentId, ComponentType componentType, String value) {
        getComponent(componentId, componentType).setSingleStringValue(value);
    }

    public void setFilter(String componentId, String value) {
        getComponent(componentId).setSingleStringValue(value);
    }

    public void clickApply() {
        getSearchPanel().applyFilter();
    }

    public void clickCancel() {
        getSearchPanel().cancel();
    }

    public List<String> getAllVisibleFilters() {
        return getSearchPanel().getAllVisibleFilters();
    }

    public List<String> getSavedFilters() {
        return openFiltersSettings().getFiltersList().stream().map(FiltersSettings.SavedFilter::getFilterLabel)
                .collect(Collectors.toList());
    }

    public List<String> getFavoriteFilters() {
        return openFiltersSettings().getFiltersList().stream().filter(FiltersSettings.SavedFilter::isFavorite)
                .map(FiltersSettings.SavedFilter::getFilterLabel).collect(Collectors.toList());
    }

    public boolean isSavedFiltersPresent() {
        return openFiltersSettings().isSavedFiltersPresent();
    }

    public int getTagsNumber() {
        return this.webElement.findElements(By.cssSelector(TAGS_ITEMS)).size();
    }

    private WebElement getFullTextSearch() {
        DelayUtils.waitForNestedElements(wait, webElement, ".//div[@" + CSSUtils.TEST_ID + "='search']");
        WebElement search = webElement.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='search']"));
        return search.findElement(By.xpath(".//input"));
    }

    private Input getQuickFilter() {
        return ComponentFactory.create(QUICK_FILTERS_ID, ComponentType.MULTI_COMBOBOX, this.driver, this.wait);
    }

    public void setQuickFilter(String name) {
        Input quickFilter = getQuickFilter();
        quickFilter.setValue(Data.createSingleData(name));
    }

    private Tags getTags() {
        DelayUtils.waitBy(this.wait, By.className(TAGS_CLASS));
        return new Tags(this.webElement);
    }

    private boolean hasTags() {
        List<WebElement> tags = this.webElement.findElements(By.className(TAGS_CLASS));
        return !tags.isEmpty();
    }

    private void clearTag(String filterLabel) {
        if (hasTags()) {
            getTags().clear(filterLabel);
        }
    }

    private void clearTags() {
        if (hasTags()) {
            getTags().clearAll();
        }
    }

    private Multimap<String, String> parseTags(List<String> tags) {
        Multimap<String, String> values = HashMultimap.create();
        for (String tag : tags) {
            String[] parts = tag.split(TAGS_SEPARATOR);
            values.put(parts[0], parts[1]);
        }
        return values;
    }

    private FiltersSettings openFiltersSettings() {
        if (!isFiltersSettingsOpen()) {
            openSearchPanel().openFiltersSettings();
        }
        return getFiltersSettings();
    }

    private FiltersSettings getFiltersSettings() {
        DelayUtils.waitBy(this.wait, By.xpath(FILTERS_SETTINGS_XPATH));
        return FiltersSettings.create(this.driver, this.wait);
    }

    private SearchPanel getSearchPanel() {
        DelayUtils.waitBy(this.wait, By.cssSelector(ADVANCED_SEARCH_CSS));
        WebElement searchPanel = driver.findElement(By.cssSelector(ADVANCED_SEARCH_CSS));
        DelayUtils.waitForSpinners(wait, searchPanel);
        return SearchPanel.create(this.driver, this.wait);
    }

    private SearchPanel getFiltersBox() {
        DelayUtils.waitBy(this.wait, By.cssSelector(FILTERS_BOX_CSS));
        WebElement filtersBox = driver.findElement(By.cssSelector(FILTERS_BOX_CSS));
        DelayUtils.waitForSpinners(wait, filtersBox);
        return SearchPanel.createFilterBox(this.driver, this.wait);
    }

    private boolean isFiltersSettingsOpen() {
        return !driver.findElements(By.className(FILTERS_SETTINGS_CLASS)).isEmpty();
    }

    private boolean isSearchPanelOpen() {
        return WebElementUtils.isElementPresent(driver, By.cssSelector(ADVANCED_SEARCH_CSS));
    }

    private boolean isFilterBoxVisible() {
        return WebElementUtils.isElementPresent(webElement, By.cssSelector(FILTERS_BOX_CSS));
    }

    private static class Tags {
        private final WebElement webElement;

        private Tags(WebElement parentElement) {
            this.webElement = parentElement.findElement(By.className(TAGS_CLASS));
        }

        private List<WebElement> getTagsWebElement() {
            return this.webElement.findElements(By.cssSelector(TAGS_ITEMS));
        }

        private List<String> getTags() {
            return getTagsWebElement().stream().map(WebElement::getText).collect(Collectors.toList());
        }

        private void clear(String filterName) {
            Optional<WebElement> tag = getTagsWebElement().stream().filter(e -> e.getText().startsWith(filterName)).findFirst();
            if (tag.isPresent()) {
                WebElement closeButton = tag.get().findElement(By.xpath(TAG_CLOSE_BUTTON_PATH));
                closeButton.click();
            }
        }

        private void clearAll() {
            this.webElement.findElement(By.xpath(".//a")).click();
        }
    }
}
