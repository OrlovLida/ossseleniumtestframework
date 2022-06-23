package com.oss.framework.components.search;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class FiltersSettings {
    private static final String FILTERS_SETTINGS_PANEL = ".filters-settings";
    private static final String FILTERS_BUTTONS_PATH =
            ".//div[@class='filters-settings-btn']//a | .//div[@class='filters-component-buttons']//a";
    private static final String SAVED_FILTERS_SELECTOR = ".filters-element-list > div.filters-element";
    private static final String ATTRIBUTE_PATH = ".//div[@class='custom-light-checkbox']";
    private static final String SAVED_FILTERS_TAB_SELECTOR = "div.filters-buttons-container > div:last-child";
    private static final String SAVED_FILTER_LABEL = ".//div[@class='filter-label']";
    private static final String NO_FILTERS = ".//div[@class='no-filters-text']";
    private static final String FAVORITE = "FAVOURITE";
    private static final String SAVE_LABEL = "Save";
    private static final String APPLY_LABEL = "Apply";
    private static final String STAR_ICON_XPATH = ".//div[@class='filters-element-icon']//i";
    private static final String SELECTED_ATTRIBUTE_PATH = ".//input[@checked]";
    private static final String CHECKBOX_CONTAINER_CSS = ".checkbox-container";
    private static final String FILTER_WITH_PROVIDED_NAME_DOESNT_EXIST_EXCEPTION = "Filter with provided name doesn't exist.";
    private static final String ARIA_LABEL = "aria-label";
    private static final String CANNOT_FIND_FILTER_WITH_NAME_EXCEPTION = "Cannot find filter with Name:  ";
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;
    
    private FiltersSettings(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }
    
    public static FiltersSettings create(WebDriver driver, WebDriverWait wait) {
        WebElement webElement = driver.findElement(By.cssSelector(FILTERS_SETTINGS_PANEL));
        return new FiltersSettings(driver, wait, webElement);
    }
    
    void markFavoriteFilter(String filterLabel) {
        SavedFilter theFilter = getFilterByLabel(filterLabel);
        theFilter.markFavorite();
    }
    
    List<SavedFilter> getFiltersList() {
        openSavedFilters();
        DelayUtils.waitByXPath(wait, NO_FILTERS + " | " + SAVED_FILTER_LABEL);
        return this.webElement.findElements(By.cssSelector(SAVED_FILTERS_SELECTOR)).stream()
                .map(savedFilter -> SavedFilter.create(driver, wait, savedFilter, webElement))
                .collect(Collectors.toList());
    }
    
    void selectFilterByLabel(String filterLabel) {
        getFilterByLabel(filterLabel).selectFilter();
        getSaveButton(APPLY_LABEL).ifPresent(WebElement::click);
    }
    
    void unselectAttributes(List<String> attributeIds) {
        List<Attribute> attributes = getAttributes(attributeIds);
        attributes.forEach(attribute -> {
            if (attribute.isSelected()) {
                attribute.toggleAttributes();
            }
        });
        getSaveButton(SAVE_LABEL).ifPresent(WebElement::click);
    }
    
    void selectAttributes(List<String> attributeIds) {
        List<Attribute> attributes = getAttributes(attributeIds);
        attributes.forEach(attribute -> {
            if (!attribute.isSelected()) {
                attribute.toggleAttributes();
            }
        });
        getSaveButton(SAVE_LABEL).ifPresent(WebElement::click);
    }
    
    private Predicate<WebElement> findByLabel(String label) {
        return element -> element.getText().contains(label);
    }
    
    private List<Attribute> getAllAttributes() {
        List<WebElement> attributes = this.webElement.findElements(By.xpath(ATTRIBUTE_PATH));
        return attributes.stream().map(attribute -> new Attribute(driver, attribute)).collect(Collectors.toList());
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
    
    private SavedFilter getFilterByLabel(String filterLabel) {
        return getFiltersList().stream().filter(filter -> filter.getFilterLabel().contains(filterLabel)).findFirst()
                .orElseThrow(() -> new RuntimeException(FILTER_WITH_PROVIDED_NAME_DOESNT_EXIST_EXCEPTION));
    }
    
    private List<Attribute> getAttributes(List<String> attributeIds) {
        return getAllAttributes().stream().filter(filter -> {
            String attributeId = filter.getAttributeId();
            return attributeIds.contains(attributeId);
        }).collect(Collectors.toList());
    }
    
    private static class Attribute {
        private final WebDriver driver;
        private final WebElement attributeElement;
        
        private Attribute(WebDriver driver, WebElement attributeElement) {
            this.driver = driver;
            this.attributeElement = attributeElement;
        }
        
        private boolean isSelected() {
            return !attributeElement.findElements(By.xpath(SELECTED_ATTRIBUTE_PATH)).isEmpty();
        }
        
        private String getAttributeId() {
            return attributeElement.getAttribute(CSSUtils.TEST_ID);
        }
        
        private void toggleAttributes() {
            WebElement checkbox = attributeElement.findElement(By.cssSelector(CHECKBOX_CONTAINER_CSS));
            WebElementUtils.clickWebElement(driver, checkbox);
        }
    }
    
    protected static class SavedFilter {
        private static final String FILTERS_ELEMENT_CLASS = "filters-element";
        private static final String FILTER_LABEL_CLASS = "filter-label";
        private static final String SUCCESS_CSS = ".success";
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final String filterName;
        private final WebElement parent;
        
        private SavedFilter(WebDriver driver, WebDriverWait wait, String filterName, WebElement parent) {
            this.driver = driver;
            this.wait = wait;
            this.filterName = filterName;
            this.parent = parent;
        }
        
        private static SavedFilter create(WebDriver driver, WebDriverWait wait, WebElement filter, WebElement parent) {
            String filterName = filter.findElement(By.className(FILTER_LABEL_CLASS)).getText();
            return new SavedFilter(driver, wait, filterName, parent);
        }
        
        public boolean isFavorite() {
            return getStar().getAttribute(ARIA_LABEL).equals(FAVORITE);
        }
        
        String getFilterLabel() {
            return filterName;
        }
        
        private WebElement getFilter() {
            return parent.findElements(By.className(FILTERS_ELEMENT_CLASS)).stream()
                    .filter(filter -> filter
                            .getText()
                            .equals(filterName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_FILTER_WITH_NAME_EXCEPTION + filterName));
        }
        
        private void markFavorite() {
            Actions action = new Actions(driver);
            if (!isFavorite()) {
                action.click(getStar()).pause(1000).build().perform();
                DelayUtils.waitBy(wait, By.cssSelector(SUCCESS_CSS));
                wait.until(ExpectedConditions.attributeToBe(getStar(), ARIA_LABEL, FAVORITE));
            }
        }
        
        private WebElement getStar() {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(STAR_ICON_XPATH)));
            return getFilter().findElement(By.xpath(STAR_ICON_XPATH));
        }
        
        private void selectFilter() {
            Actions action = new Actions(driver);
            action.moveToElement(getFilter()).click(getFilter()).build().perform();
        }
    }
}
