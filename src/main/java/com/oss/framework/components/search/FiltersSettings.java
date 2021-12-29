package com.oss.framework.components.search;

import java.util.List;
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

public class FiltersSettings {
    private static final String FILTERS_SETTINGS_PANEL = "filters-settings as-component";
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
    private static final String STAR_ICON_PATH = ".//div[@class='filters-element-icon']//i";
    private static final String SELECTED_ATTRIBUTE_PATH = ".//input[@checked]";
    private static final String INPUT_PATH = ".//input";
    private static final String FILTER_WITH_PROVIDED_NAME_DOESNT_EXIST_EXCEPTION = "Filter with provided name doesn't exist.";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private FiltersSettings(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.xpath(".//*[@class='" + FILTERS_SETTINGS_PANEL + "']"));
    }

    public static FiltersSettings create(WebDriver driver, WebDriverWait wait) {
        return new FiltersSettings(driver, wait);
    }

    public List<SavedFilter> getFiltersList() {
        openSavedFilters();
        DelayUtils.waitByXPath(wait, NO_FILTERS + " | " + SAVED_FILTER_LABEL);
        return this.webElement.findElements(By.cssSelector(SAVED_FILTERS_SELECTOR)).stream()
                .map(savedFilter -> new SavedFilter(driver, wait, savedFilter))
                .collect(Collectors.toList());
    }

    public void markFilterAsFavByLabel(String filterLabel) {
        SavedFilter theFilter = getFilterByLabel(filterLabel);
        theFilter.markAsFavorite();
    }

    public void chooseFilterByLabel(String filterLabel) {
        getFilterByLabel(filterLabel).chooseFilter();
        getSaveButton(APPLY_LABEL).ifPresent(WebElement::click);
    }

    public void unselectAttributes(List<String> attributeIds) {
        List<Attribute> attributes = getAttributes(attributeIds);
        attributes.forEach(attribute -> {
            if (attribute.isSelected()) {
                attribute.toggleAttributes();
            }
        });
        getSaveButton(SAVE_LABEL).ifPresent(WebElement::click);
    }

    public void selectAttributes(List<String> attributeIds) {
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
        private final WebElement attributeElement;

        private Attribute(WebElement attributeElement) {
            this.attributeElement = attributeElement;
        }

        private boolean isSelected() {
            return !attributeElement.findElements(By.xpath(SELECTED_ATTRIBUTE_PATH)).isEmpty();
        }

        private String getAttributeId() {
            return attributeElement.getAttribute(CSSUtils.TEST_ID);
        }

        private void toggleAttributes() {
            attributeElement.findElement(By.xpath(INPUT_PATH)).click();
        }
    }

    protected static class SavedFilter {
        private final WebElement filter;
        private final WebDriver driver;
        private final WebDriverWait wait;

        private SavedFilter(WebDriver driver, WebDriverWait wait, WebElement filter) {
            this.driver = driver;
            this.filter = filter;
            this.wait = wait;
        }

        public boolean isFavorite() {
            return getStar().getAttribute("aria-label").equals(FAVORITE);
        }

        public String getFilterLabel() {
            return filter.getText();
        }

        private void markAsFavorite() {
            if (!isFavorite()) {
                getStar().click();
                wait.until(ExpectedConditions.attributeToBe(getStar(), "aria-label", FAVORITE));
            }
        }

        private WebElement getStar() {
            return filter.findElement(By.xpath(STAR_ICON_PATH));
        }

        private void chooseFilter() {
            Actions action = new Actions(driver);
            action.moveToElement(filter).click(filter).build().perform();

        }
    }
}
