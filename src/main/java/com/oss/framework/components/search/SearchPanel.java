package com.oss.framework.components.search;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;

import java.util.List;
import java.util.stream.Collectors;

public class SearchPanel {
    private static final String ADVANCED_SEARCH_PANEL_CLASS = "advanced-search_panel";
    private static final String FILTERS_SETTINGS_PANEL_CLASS = "filters-settings as-component";
    private static final String FILTERS_PATH = ".//div[@class='filter_wrapper']";
    private static final String INPUT_LABEL_PATH = ".//span[@class='md-input-label-text']";
    private static final String NEW_COMBOBOX_LABEL_PATH = ".//*[@class='oss-input__input-label']";

    private static final String APPLY_BTN_PATH = ".//a[text()='Apply']";
    private static final String CANCEL_BTN_PATH = ".//a[text()='Cancel']";
    private static final String BTN_TOGGLE_FILTERS_PATH = ".//button[@class='btn-toggle-filters']";

    private static final String SAVE_BUTTONS_DROPDOWN_PATH = ".//div[@" + CSSUtils.TEST_ID + "='save-buttons-dropdown']";
    private static final String SAVE_AS_NEW_FILTER_BTN_PATH = ".//a[@" + CSSUtils.TEST_ID + "='save_as_new_filter']";
    private static final String SAVE_AS_NEW_FILTER_FORM_PATH = ".//div[@" + CSSUtils.TEST_ID + "='save_as_new_filter_form']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static SearchPanel create(WebDriver driver, WebDriverWait wait) {
        return new SearchPanel(driver, wait);
    }

    private SearchPanel(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
    }

    public void openFiltersSettings() {
        WebElement btnToggleFilters = this.webElement.findElement(By.xpath(BTN_TOGGLE_FILTERS_PATH));
        btnToggleFilters.click();
    }

    private FiltersSettings getFiltersSettings() {
        DelayUtils.waitBy(this.wait, By.xpath(".//*[@class='" + FILTERS_SETTINGS_PANEL_CLASS + "']"));
        return FiltersSettings.create(this.driver, this.wait);
    }

    public void applyFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(APPLY_BTN_PATH)))).click();
    }

    public void cancel() {
        this.webElement.findElement(By.xpath(CANCEL_BTN_PATH)).click();
    }

    public List<String> getAllVisibleFilters() {
        return this.webElement
                .findElements(By.xpath(FILTERS_PATH)).stream()
                .map(filter -> filter.findElement(By.xpath(INPUT_LABEL_PATH + " | " + NEW_COMBOBOX_LABEL_PATH)).getText())
                .collect(Collectors.toList());
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

    public void markFilterAsFavByLabel(String label) {
        getFiltersSettings().markFilterAsFavByLabel(label);
    }

    public void choseSavedFilterByLabel(String label) {
        getFiltersSettings().choseFilterByLabel(label);
    }

    public void saveAsNewFilter(String name) {
        this.webElement.findElement(By.xpath(SAVE_BUTTONS_DROPDOWN_PATH)).click();
        driver.findElement(By.xpath(SAVE_AS_NEW_FILTER_BTN_PATH)).click();

        WebElement form = driver.findElement(By.xpath(SAVE_AS_NEW_FILTER_FORM_PATH));
        form.findElement(By.xpath(".//input")).sendKeys(name);
        form.findElement(By.xpath(".//a[text()='Save']")).click();

        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void toggleAttributes(List<String> attributeIds) {
        getFiltersSettings().toggleAttributes(attributeIds);
    }
}
