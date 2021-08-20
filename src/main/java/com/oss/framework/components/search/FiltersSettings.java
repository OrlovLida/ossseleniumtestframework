package com.oss.framework.components.search;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FiltersSettings {
    private static final Logger log = LoggerFactory.getLogger(FiltersSettings.class);
    private static final String FILTERS_SETTINGS_PANEL = "filters-settings as-component";
    private static final String FILTERS_BUTTONS_PATH = ".//div[@class='filters-settings-btn']//a | .//div[@class='filters-component-buttons']//a";
    private static final String SAVED_FILTERS_SELECTOR = ".filters-element-list > div.filters-element";
    private static final String ATTRIBUTE_PATH = ".//div[@class='custom-light-checkbox']";
    private static final String SAVED_FILTERS_TAB_SELECTOR = "div.filters-buttons-container > div:last-child";

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

    private List<WebElement> getAttributes() {
        return this.webElement.findElements(By.xpath(ATTRIBUTE_PATH));
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

    private List<WebElement> getFiltersList() {
        openSavedFilters();

        DelayUtils.waitByXPath(wait, ".//div[@class='filter-label']");
        return this.webElement.findElements(By.cssSelector(SAVED_FILTERS_SELECTOR));
    }

    private Optional<WebElement> getFilterByLabel(String filterLabel) {
        return getFiltersList().stream().filter(filter -> {
            String label = filter.findElement(By.xpath(".//div[@class='filter-label']")).getText();
            return label.contains(filterLabel);
        }).findFirst();
    }

    public void markFilterAsFavByLabel(String filterLabel) {
        Optional<WebElement> theFilter = getFilterByLabel(filterLabel);
        if (theFilter.isPresent()){
            WebElement filter = theFilter.get();
            WebElement icon = filter.findElement(By.xpath(".//div[@class='filters-element-icon']//i"));
            if(CSSUtils.getAttributeValue("aria-label", icon).equals("STAR")) {
                icon.click();
            }
            log.info("Filter is already mark as favorite");
        }
    }

    public void choseFilterByLabel(String filterLabel) {
        getFilterByLabel(filterLabel).ifPresent(WebElement::click);
        getSaveButton(APPLY_LABEL).ifPresent(WebElement::click);
    }

    public void toggleAttributes(List<String> attributeIds) {
        List<WebElement> attribute = getAttributes().stream().filter(filter -> {
            String attributeId = filter.getAttribute(CSSUtils.TEST_ID);
            return attributeIds.contains(attributeId);
        }).collect(Collectors.toList());
        attribute.forEach(element -> element.findElement(By.xpath(".//input")).click());
        getSaveButton(SAVE_LABEl).ifPresent(WebElement::click);
    }

}
