package com.oss.framework.components.search;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.TableWidget;

public class AdvancedSearch {
    public static final String SEARCH_COMPONENT_CLASS = "advanced-search_search";
    private static final String TAGS_CLASS = "tagsWidgetDisplay";
    private static final String TAGS_ITEMS = ".//span[@class='md-input-value']";
    private static final String ADVANCED_SEARCH_PANEL_CLASS = "advanced-search_panel";

    private static final String SEARCH_PANEL_OPEN_BUTTON = ".//button[@class='button-filters-panel']";
    private static final String APPLY_BTN_PATH = ".//a[text()='Apply']";
    private static final String ADD_BTN_PATH = ".//a[text()='Add']";
    private static final String CANCEL_BTN_PATH = ".//a[text()='Cancel']";

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

    //change id to data-attributeName after adding this attribute in web
    public static AdvancedSearch createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitByXPath(wait, "//*[@id='" + id + "']");
        WebElement webElement = driver.findElement(By.xpath("//*[@id='" + id + "']"));
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
        WebElement search = this.webElement.findElement(By.xpath(".//div[@data-attributename='search']"));
        search.findElement(By.xpath(".//input")).sendKeys(text);
    }

    public Multimap<String, String> getAppliedFilters() {
        if (tags == null) {
            DelayUtils.waitBy(this.wait, By.className(TAGS_CLASS));
            tags = new Tags(this.webElement);
        }
        return parseTags(tags.getTags());
    }

    private Multimap<String, String> parseTags(List<String> tags) {
        Multimap<String, String> values = HashMultimap.create();
        for (String tag : tags) {
            String[] parts = tag.split(TAGS_SEPARATOR);
            values.put(parts[0], parts[1]);
        }
        return values;
    }

    public void openSearchPanel() {
        this.webElement.findElement(By.xpath(SEARCH_PANEL_OPEN_BUTTON)).click();
        if (this.searchPanel == null) {
            DelayUtils.waitBy(this.wait, By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
            this.searchPanel = new SearchPanel(this.driver, this.wait);
        }
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        if (this.searchPanel == null) {
            //openSearchPanel();
            DelayUtils.waitBy(this.wait, By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
            this.searchPanel = new SearchPanel(this.driver, this.wait);
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

    public void clickOnTagByLabel(String label) {
        this.webElement.findElement(By.xpath("//div[@class='" + TAGS_CLASS + "']//*[contains (text(), '" + label + "')]")).click();
    }

    public void closeTagByLabel(String label) {
        this.webElement.findElement(By.xpath(TAGS_ITEMS + "//*[contains (text(), '" + label + "')]/span[contains (@class, 'close')]")).click();
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
    }

    private static class SearchPanel {
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement webElement;

        private SearchPanel(WebDriver driver, WebDriverWait wait) {
            this.driver = driver;
            this.wait = wait;
            this.webElement = this.driver.findElement(By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
        }

        private void applyFilter() {
            this.webElement.findElement(By.xpath(APPLY_BTN_PATH)).click();
        }

        private void cancel() {
            this.webElement.findElement(By.xpath(CANCEL_BTN_PATH)).click();
        }

        private Input getComponent(String componentId, ComponentType componentType) {
            return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        }
    }

    //TODO: move to the advanced search widget
    public TableWidget getTableWidget() {
        Widget.waitForWidget(wait, "right-side");
        return TableWidget.create(driver, "right-side", wait);
    }

}
