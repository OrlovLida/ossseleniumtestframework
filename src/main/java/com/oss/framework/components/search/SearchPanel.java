package com.oss.framework.components.search;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;

public class SearchPanel {
    private static final String FILTERS_PATH = ".//div[@class='filter_wrapper']";
    private static final String INPUT_LABEL_CSS = ".md-input-label-text";
    private static final String NEW_COMBOBOX_LABEL_PATH = ".oss-input__input-label";
    private static final String INPUT_PATH = ".//input";
    private static final String DRAGGABLE_ELEMENT_CSS = ".btn-drag";
    private static final String APPLY_BTN_PATH = ".//a[text()='Apply']";
    private static final String CANCEL_BTN_PATH = ".//a[text()='Cancel']";
    private static final String SAVE_BTN_PATH = ".//a[text()='Save']";
    private static final String BTN_TOGGLE_FILTERS_PATH = ".//button[@class='btn-toggle-filters']";
    private static final String SAVE_BUTTONS_DROPDOWN_PATH = ".//div[@" + CSSUtils.TEST_ID + "='save-buttons-dropdown']";
    private static final String SAVE_AS_NEW_FILTER_BTN_ID = "save_as_new_filter";
    private static final String SAVE_AS_NEW_FILTER_FORM_PATH = ".//div[@" + CSSUtils.TEST_ID + "='save_as_new_filter_form']";
    private static final String SAVE_FILTER_BTN_ID = "save_filter";
    private static final String ADVANCED_SEARCH_PANEL_CSS = ".advanced-search_panel";
    private static final String FILTER_BOX_CSS = ".filters-box";
    private static final String ATTRIBUTE_NAME_CSS = ".md-input .md-input-label-text";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private SearchPanel(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static SearchPanel createSearchPanel(WebDriver driver, WebDriverWait wait) {
        WebElement webElement =
                driver.findElement(By.cssSelector(ADVANCED_SEARCH_PANEL_CSS));
        return new SearchPanel(driver, wait, webElement);
    }

    public static SearchPanel createFilterBox(WebDriver driver, WebDriverWait wait) {
        WebElement webElement =
                driver.findElement(By.cssSelector(FILTER_BOX_CSS));
        return new SearchPanel(driver, wait, webElement);
    }

    public void openFiltersSettings() {
        WebElement btnToggleFilters = this.webElement.findElement(By.xpath(BTN_TOGGLE_FILTERS_PATH));
        btnToggleFilters.click();
    }

    public void applyFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(APPLY_BTN_PATH)))).click();
    }

    public void cancel() {
        this.webElement.findElement(By.xpath(CANCEL_BTN_PATH)).click();
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        return ComponentFactory.createFromParent(componentId, componentType, this.driver, this.wait, this.webElement);
    }

    public Input getComponent(String componentId) {
        return ComponentFactory.createFromParent(componentId, this.driver, this.wait, this.webElement);
    }

    List<String> getAllVisibleFilters() {
        return this.webElement
                .findElements(By.xpath(FILTERS_PATH)).stream()
                .map(filter -> filter.findElement(By.cssSelector(INPUT_LABEL_CSS + "," + NEW_COMBOBOX_LABEL_PATH)).getText())
                .collect(Collectors.toList());
    }

    void saveAsNewFilter(String name) {
        callSaveFilterAction(SAVE_AS_NEW_FILTER_BTN_ID);

        WebElement form = driver.findElement(By.xpath(SAVE_AS_NEW_FILTER_FORM_PATH));
        form.findElement(By.xpath(INPUT_PATH)).sendKeys(name);
        form.findElement(By.xpath(SAVE_BTN_PATH)).click();

        DelayUtils.waitForPageToLoad(driver, wait);
    }

    void saveFilter() {
        callSaveFilterAction(SAVE_FILTER_BTN_ID);
    }

    void clickClearAll() {
        webElement.findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "= 'clearAllButton']")).click();
    }

    void clickBackToDefault() {
        webElement.findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "= 'backToDefaultAllButton']")).click();
    }

    void changeASAttributeOrderById(String attributeId, int position) {
        DragAndDrop.dragAndDrop(getDraggableElement(attributeId), getDropElement(position), driver);
    }

    private DragAndDrop.DropElement getDropElement(int position) {
        WebElement target = this.webElement.findElements(By.cssSelector(ATTRIBUTE_NAME_CSS)).get(position);
        return new DragAndDrop.DropElement(target);
    }

    private DragAndDrop.DraggableElement getDraggableElement(String id) {
        WebElement source = getAttributeById(id).findElement(By.cssSelector(DRAGGABLE_ELEMENT_CSS));
        return new DragAndDrop.DraggableElement(source);
    }

    private WebElement getAttributeById(String id) {
        return this.webElement.findElement(By.id(id));
    }

    private void callSaveFilterAction(String actionId) {
        WebElement saveButton = this.webElement.findElement(By.xpath(SAVE_BUTTONS_DROPDOWN_PATH));
        WebElementUtils.clickWebElement(driver, saveButton);
        driver.findElement(By.xpath(".//a[@" + CSSUtils.TEST_ID + "='" + actionId + "']")).click();
    }

}
