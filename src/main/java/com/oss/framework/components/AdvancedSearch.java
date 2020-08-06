package com.oss.framework.components;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedSearch {
    public static final String SEARCH_COMPONENT_CLASS = "advanced-search_search";
    private static final String TAGS_CLASS = "tagsWidgetDisplay";
    private static final String TAGS_ITEMS = ".//span[@class='md-input-value']";
    private static final String ADVANCED_SEARCH_PANEL_CLASS = "advanced-search_panel";

    private static final String SEARCH_PANEL_OPEN_BUTTON = ".//button[@class='button-filters-panel']";
    private static final String APPLY_BTN_PATH =".//a[text()='Apply']";
    private static final String CANCEL_BTN_PATH =".//a[text()='Cancel']";

    private static final String TAGS_SEPARATOR = " : ";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private SearchPanel searchPanel;
    private Tags tags;

    public AdvancedSearch(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.className(SEARCH_COMPONENT_CLASS));
    }

    public Multimap<String, String> getAppliedFilters() {
        if(tags == null) {
            DelayUtils.waitBy(this.wait, By.className(TAGS_CLASS));
            tags = new Tags(this.webElement);
        }
        return parseTags(tags.getTags());
    }

    private Multimap<String, String> parseTags(List<String> tags) {
        Multimap<String, String> values = HashMultimap.create();
        for(String tag : tags) {
            String[] parts = tag.split(TAGS_SEPARATOR);
            values.put(parts[0], parts[1]);
        }
        return values;
    }

    public void openSearchPanel() {
        this.webElement.findElement(By.xpath(SEARCH_PANEL_OPEN_BUTTON)).click();
        if(this.searchPanel == null) {
            DelayUtils.waitBy(this.wait, By.className(ADVANCED_SEARCH_PANEL_CLASS));
            this. searchPanel = new SearchPanel(this.driver, this.wait);
        }
    }

    public Input getComponent(String componentId, ComponentType componentType){
        if(this.searchPanel == null) {
            //openSearchPanel();
            DelayUtils.waitBy(this.wait, By.className(ADVANCED_SEARCH_PANEL_CLASS));
            this. searchPanel = new SearchPanel(this.driver, this.wait);
        }
        return this.searchPanel.getComponent(componentId, componentType);
    }

    public void clickApply(){
        this.searchPanel.applyFilter();
        this.searchPanel = null;
    }

    public void clickCancel() {
        this.searchPanel.cancel();
        this.searchPanel = null;
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
    }

    private static class SearchPanel {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement webElement;
        private final Map<String, Input> components = Maps.newHashMap();

        private SearchPanel(WebDriver driver, WebDriverWait wait) {
            this.driver = driver;
            this.wait = wait;
            this.webElement = this.driver.findElement(By.className(ADVANCED_SEARCH_PANEL_CLASS));
        }

        private void applyFilter() {
            this.webElement.findElement(By.xpath(APPLY_BTN_PATH)).click();
        }

        private void cancel() {
            this.webElement.findElement(By.xpath(CANCEL_BTN_PATH)).click();
        }

        private Input getComponent(String componentId, ComponentType componentType){
            if(components.containsKey(componentId)) {
                return components.get(componentId);
            }

            Input input = ComponentFactory.createFromParent(componentId, componentType, this.driver, this.wait, this.webElement);
            components.put(componentId, input);
            return input;
        }

    }

}
