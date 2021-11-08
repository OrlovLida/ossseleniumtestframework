package com.oss.framework.components.search;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class FiltersSettings {
    private static final Logger log = LoggerFactory.getLogger(FiltersSettings.class);
    private static final String FILTERS_SETTINGS_PANEL = "filters-settings as-component";
    private static final String FILTERS_BUTTONS_PATH =
            ".//div[@class='filters-settings-btn']//a | .//div[@class='filters-component-buttons']//a";
    private static final String SAVED_FILTERS_SELECTOR = ".filters-element-list > div.filters-element";
    private static final String ATTRIBUTE_PATH = ".//div[@class='custom-light-checkbox']";
    private static final String SAVED_FILTERS_TAB_SELECTOR = "div.filters-buttons-container > div:last-child";
    private static final String SAVED_FILTER_LABEL = ".//div[@class='filter-label']";
    private static final String NO_FILTERS = ".//div[@class='no-filters-text']";
    
    private static final String SAVE_LABEl = "Save";
    private static final String APPLY_LABEL = "Apply";
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;
    
    public static FiltersSettings create(WebDriver driver, WebDriverWait wait) {
        return new FiltersSettings(driver, wait);
    }
    
    private FiltersSettings(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.xpath(".//*[@class='" + FILTERS_SETTINGS_PANEL + "']"));
    }
    
    private Predicate<WebElement> findByLabel(String label) {
        return element -> element.getText().contains(label);
    }
    
    private List<Attribute> getAllAttributes() {
        List<WebElement> attributes = this.webElement.findElements(By.xpath(ATTRIBUTE_PATH));
        return attributes.stream().map(Attribute::new).collect(Collectors.toList());
    }
    
    private void openSavedFilters() {
        WebElement savedFiltersTab = this.webElement.findElement(By.cssSelector(SAVED_FILTERS_TAB_SELECTOR));
        savedFiltersTab.click();
    }
    
    private Optional<WebElement> getSaveButton(String label) {
        return this.webElement
                .findElements(By.xpath(FILTERS_BUTTONS_PATH))
                .stream()
                .filter(findByLabel(label))
                .findFirst();
    }
    
    public List<SavedFilter> getFiltersList() {
        openSavedFilters();
        DelayUtils.waitByXPath(wait, NO_FILTERS + " | " + SAVED_FILTER_LABEL);
        return this.webElement.findElements(By.cssSelector(SAVED_FILTERS_SELECTOR)).stream()
                .map(savedFilter -> new SavedFilter(driver, savedFilter))
                .collect(Collectors.toList());
    }
    
    private SavedFilter getFilterByLabel(String filterLabel) {
        return getFiltersList().stream().filter(filter -> filter.getFilterLabel().contains(filterLabel)).findFirst()
                .orElseThrow(() -> new RuntimeException("Filter with provided name doesn't exist."));
    }
    
    public void markFilterAsFavByLabel(String filterLabel) {
        SavedFilter theFilter = getFilterByLabel(filterLabel);
        theFilter.markAsFavorite();
    }
    
    public void chooseFilterByLabel(String filterLabel) {
        getFilterByLabel(filterLabel).chooseFilter();
        getSaveButton(APPLY_LABEL).ifPresent(WebElement::click);
    }
    
    private List<Attribute> getAttributes(List<String> attributeIds) {
        return getAllAttributes().stream().filter(filter -> {
            String attributeId = filter.getAttributeId();
            return attributeIds.contains(attributeId);
        }).collect(Collectors.toList());
    }
    
    public void unselectAttributes(List<String> attributeIds) {
        List<Attribute> attributes = getAttributes(attributeIds);
        attributes.forEach(attribute -> {
            if (attribute.isSelected()) {
                attribute.toggleAttributes();
            }
        });
        getSaveButton(SAVE_LABEl).ifPresent(WebElement::click);
    }
    
    public void selectAttributes(List<String> attributeIds) {
        List<Attribute> attributes = getAttributes(attributeIds);
        attributes.forEach(attribute -> {
            if (!attribute.isSelected()) {
                attribute.toggleAttributes();
            }
        });
        getSaveButton(SAVE_LABEl).ifPresent(WebElement::click);
    }
    
    private static class Attribute {
        private final WebElement attribute;
        
        private Attribute(WebElement attribute) {
            this.attribute = attribute;
        }
        
        private boolean isSelected() {
            return !attribute.findElements(By.xpath(".//input[@checked]")).isEmpty();
        }
        
        private String getAttributeId() {
            return attribute.getAttribute(CSSUtils.TEST_ID);
        }
        
        private void toggleAttributes() {
            attribute.findElement(By.xpath(".//input")).click();
        }
    }
    
    protected static class SavedFilter {
        private final WebElement filter;
        private final WebDriver driver;
        
        private SavedFilter(WebDriver driver, WebElement filter) {
            this.driver = driver;
            this.filter = filter;
        }
        
        public boolean isFavorite() {
            return getStar().getAttribute("aria-label").equals("FAVOURITE");
        }
        
        public String getFilterLabel() {
            return filter.getText();
        }
        
        private void markAsFavorite() {
            if (!isFavorite()) {
                getStar().click();
            }
        }
        
        private WebElement getStar() {
            return filter.findElement(By.xpath(".//div[@class='filters-element-icon']//i"));
        }
        
        private void chooseFilter() {
            Actions action = new Actions(driver);
            action.moveToElement(filter).click(filter).build().perform();
            
        }
    }
}
