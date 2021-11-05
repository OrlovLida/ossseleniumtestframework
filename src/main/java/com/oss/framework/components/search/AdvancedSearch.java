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
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class AdvancedSearch {
    public static final String SEARCH_COMPONENT_CLASS = "advanced-search_search";
    private static final String QUICK_FILTERS_ID = "quick_filters";
    private static final String TAGS_CLASS = "tagsWidgetDisplay";
    private static final String TAGS_ITEMS = ".//span[@class='md-input-value']";
    private static final String ADVANCED_SEARCH_PANEL_CLASS = "advanced-search_panel";
    
    private static final String SEARCH_PANEL_OPEN_BUTTON = ".//button[@class='button-filters-panel']";
    private static final String ADD_BTN_PATH = ".//a[text()='Add']";
    
    private static final String TAGS_SEPARATOR = ": ";
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;
    
    private SearchPanel searchPanel;
    private Tags tags;
    private FiltersSettings filtersSettings;
    
    public static AdvancedSearch createByClass(WebDriver driver, WebDriverWait wait, String className) {
        DelayUtils.waitByXPath(wait, "//*[@class='" + className + "']");
        WebElement webElement = driver.findElement(By.className(className));
        return new AdvancedSearch(driver, wait, webElement);
    }
    
    public static AdvancedSearch createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitByXPath(wait, "//*[@" + CSSUtils.TEST_ID + "='" + id + "']");
        WebElement webElement = driver.findElement(By.xpath("//*[@" + CSSUtils.TEST_ID + "='" + id + "']"));
        return new AdvancedSearch(driver, wait, webElement);
    }
    
    public static AdvancedSearch createByWidgetId(WebDriver driver, WebDriverWait wait, String widgetId) {
        DelayUtils.waitByXPath(wait, "//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']");
        WebElement widget = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + widgetId + "']"));
        return new AdvancedSearch(driver, wait, widget);
    }
    
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

    public void fullTextSearch(String text) {
        clearFullText();
        getFullTextSearch().sendKeys(text);
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
        for (String tag: tags) {
            String[] parts = tag.split(TAGS_SEPARATOR);
            values.put(parts[0], parts[1]);
        }
        return values;
    }
    
    public void openSearchPanel() {
        if (!isSearchPanelOpen()) {
            this.webElement.findElement(By.xpath(SEARCH_PANEL_OPEN_BUTTON)).click();
        }
        getSearchPanel();
    }
    
    private void openFiltersSettings() {
        if (!isFiltersSettingsOpen()) {
            openSearchPanel();
            searchPanel.openFiltersSettings();
        }
        getSearchPanel();
        getFiltersSettings();
    }

    private void getFiltersSettings() {
        DelayUtils.waitBy(this.wait, By.xpath("//div[contains(@class,'filters-settings')]"));
        this.filtersSettings = FiltersSettings.create(this.driver, this.wait);
    }

    private void getSearchPanel() {
        if (this.searchPanel == null) {
            DelayUtils.waitBy(this.wait, By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
            this.searchPanel = SearchPanel.create(this.driver, this.wait);
        }
    }

    private boolean isFiltersSettingsOpen() {
        return driver.findElements(By.className("filters-settings"))
                .size() > 0;
    }
    
    public void markFilterAsFavByLabel(String label) {
        openFiltersSettings();
        filtersSettings.markFilterAsFavByLabel(label);
    }
    
    public void selectAttributes(List<String> attributeIds) {
        openFiltersSettings();
        filtersSettings.selectAttributes(attributeIds);
    }
    
    public void unselectAttributes(List<String> attributeIds) {
        openFiltersSettings();
        filtersSettings.unselectAttributes(attributeIds);
    }
    
    public void choseSavedFilterByLabel(String label) {
        openFiltersSettings();
        filtersSettings.choseFilterByLabel(label);
    }
    
    public void saveAsNewFilter(String name) {
        openSearchPanel();
        this.searchPanel.saveAsNewFilter(name);
    }
    public void saveFilter(){
        this.searchPanel.saveFilter();
    }
    
    public Input getComponent(String componentId, ComponentType componentType) {
        getSearchPanel();
        return this.searchPanel.getComponent(componentId, componentType);
    }
    
    public void clickApply() {
        this.searchPanel.applyFilter();
        this.searchPanel = null;
    }
    
    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
    }
    
    public void clickCancel() {
        this.searchPanel.cancel();
        this.searchPanel = null;
    }
    
    public List<String> getAllVisibleFilters() {
        return this.searchPanel.getAllVisibleFilters();
    }
    
    public List<String> getSavedFilters() {
        openFiltersSettings();
        return filtersSettings.getFiltersList().stream().map(FiltersSettings.SavedFilter::getFilterLabel).collect(Collectors.toList());
    }
    
    public List<String> getFavoriteFilters() {
        openFiltersSettings();
        return filtersSettings.getFiltersList().stream().filter(FiltersSettings.SavedFilter::isFavorite)
                .map(FiltersSettings.SavedFilter::getFilterLabel).collect(Collectors.toList());
    }

    public void closeTagByLabel(String label) {
        this.webElement.findElement(By.xpath(TAGS_ITEMS + "//*[contains (text(), '" + label + "')]/span[contains (@class, 'close')]"))
                .click();
    }
    @Deprecated
    public int howManyTagsIsVisible() {
        return this.webElement.findElements(By.xpath(TAGS_ITEMS)).size();
    }
    
    private boolean isSearchPanelOpen() {
        return webElement.findElements(By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"))
                .size() > 0;
    }
    
    private static class Tags {
        private final WebElement webElement;
        
        private Tags(WebElement parentElement) {
            this.webElement = parentElement.findElement(By.className(TAGS_CLASS));
        }
        
        private List<WebElement> getTagsWebElement() {
            return this.webElement.findElements(By.xpath(TAGS_ITEMS));
        }
        
        private List<String> getTags() {
            List<String> values = getTagsWebElement().stream().map(WebElement::getText).collect(Collectors.toList());
            return values;
        }
        
        private void clear(String filterName) {
            Optional<WebElement> tag = getTagsWebElement().stream().filter(e -> e.getText().startsWith(filterName)).findFirst();
            if (tag.isPresent()) {
                WebElement closeButton = tag.get().findElement(By.xpath(".//span[@class='md-input-close']"));
                closeButton.click();
            }
        }
        
        private void clearAll() {
            this.webElement.findElement(By.xpath(".//a")).click();
        }
    }
}
