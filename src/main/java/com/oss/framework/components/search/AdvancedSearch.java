package com.oss.framework.components.search;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class AdvancedSearch {
    public static final String SEARCH_COMPONENT_CLASS = "advanced-search_search";
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
        WebElement search = this.webElement.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='search']"));
        return search.findElement(By.xpath(".//input"));
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
    
    private boolean hasTags() {
        List<WebElement> tags = this.webElement.findElements(By.className(TAGS_CLASS));
        return !tags.isEmpty();
    }
    
    private void clearTags() {
        if (hasTags()) {
            getTags().clear();
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
        this.webElement.findElement(By.xpath(SEARCH_PANEL_OPEN_BUTTON)).click();
        if (this.searchPanel == null) {
            DelayUtils.waitBy(this.wait, By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
            this.searchPanel = SearchPanel.create(this.driver, this.wait);
        }
    }
    
    public Input getComponent(String componentId, ComponentType componentType) {
        if (this.searchPanel == null) {
            DelayUtils.waitBy(this.wait, By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
            this.searchPanel = SearchPanel.create(this.driver, this.wait);
        }
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
    
    @Deprecated
    public void clickOnTagByLabel(String label) {
        this.webElement.findElement(By.xpath("//div[@class='" + TAGS_CLASS + "']//*[contains (text(), '" + label + "')]")).click();
    }
    
    public void closeTagByLabel(String label) {
        this.webElement.findElement(By.xpath(TAGS_ITEMS + "//*[contains (text(), '" + label + "')]/span[contains (@class, 'close')]"))
                .click();
    }
    
    public int howManyTagsIsVisible() {
        return this.webElement.findElements(By.xpath(TAGS_ITEMS)).size();
    }
    
    private static class Tags {
        private final WebElement webElement;
        
        private Tags(WebElement parentElement) {
            this.webElement = parentElement.findElement(By.className(TAGS_CLASS));
        }
        
        private List<String> getTags() {
            List<String> values = this.webElement.findElements(By.xpath(TAGS_ITEMS)).stream()
                    .map(WebElement::getText).collect(Collectors.toList());
            return values;
        }
        
        private void clear() {
            this.webElement.findElement(By.xpath(".//a")).click();
        }
    }
}
