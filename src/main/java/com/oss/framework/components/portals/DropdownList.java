package com.oss.framework.components.portals;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class DropdownList {

    public static final String PORTAL_CLASS = "portal";
    private static final String INPUT_CSS = ".portal .dropdown__search input, .portal .search-cont input";
    private static final String BY_ID_PATTERN = ".portal [" + CSSUtils.TEST_ID + "='%s'],.portal #%s";
    private static final String BY_TEXT_PATTERN = "//div[starts-with(@class, 'portal')]//*[text()='%s']";
    private static final String BY_TITLE_PATTERN = ".portal [title='%s']";
    private static final String BY_TEXT_CONTAINS_PATTERN = "//div[starts-with(@class, 'portal')]//*[contains(text(), '%s')]";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
    private static final String DROPDOWN_ELEMENT_CSS = ".portal .list-item, .portal .dropdown-element";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement dropdownListElement;

    private DropdownList(WebDriver driver, WebDriverWait wait, WebElement dropdownListElement) {
        this.driver = driver;
        this.wait = wait;
        this.dropdownListElement = dropdownListElement;
    }

    public static DropdownList create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement dropdownList = driver.findElement(By.className(PORTAL_CLASS));
        DelayUtils.waitForElementToLoad(webDriverWait, dropdownList);
        return new DropdownList(driver, webDriverWait, dropdownList);
    }

    public void selectOption(String optionLabel) {
        DelayUtils.waitByElement(wait, dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_PATTERN, optionLabel))));
        WebElement foundedElement =
                driver.findElement(By.xpath(String.format(BY_TEXT_PATTERN, optionLabel)));
        foundedElement.click();
    }

    public void selectOptionByTitle(String title) {
        DelayUtils.waitByElement(wait, driver.findElement(By.cssSelector(String.format(BY_TITLE_PATTERN, title))));
        WebElement foundedElement =
                driver.findElement(By.cssSelector(String.format(BY_TITLE_PATTERN, title)));
        foundedElement.click();
    }

    public void selectOptionContains(String optionLabel) {
        DelayUtils.waitByElement(wait, dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_CONTAINS_PATTERN, optionLabel))));
        WebElement foundedElement =
                driver.findElement(By.xpath(String.format(BY_TEXT_CONTAINS_PATTERN, optionLabel)));
        WebElementUtils.clickWebElement(driver, foundedElement);
    }

    public Set<String> getOptions() {
        DelayUtils.waitByElement(wait, dropdownListElement);
        List<WebElement> visibleElements = driver.findElements(By.cssSelector(DROPDOWN_ELEMENT_CSS));
        Set<String> optionsLabels = new LinkedHashSet<>();

        for (WebElement option : visibleElements) {
            optionsLabels.add(option.getAttribute(TEXT_CONTENT_ATTRIBUTE));
        }

        String lastVisibleOption = visibleElements.get(visibleElements.size() - 1).getText();
        WebElementUtils.moveToElement(driver, visibleElements.get(visibleElements.size() - 1));
        visibleElements = driver.findElements(By.cssSelector(DROPDOWN_ELEMENT_CSS));
        String temporaryLastOption = visibleElements.get(visibleElements.size() - 1).getText();

        while (!lastVisibleOption.equals(temporaryLastOption)) {
            for (WebElement option : visibleElements) {
                optionsLabels.add(option.getAttribute(TEXT_CONTENT_ATTRIBUTE));
            }

            lastVisibleOption = visibleElements.get(visibleElements.size() - 1).getText();
            WebElementUtils.moveToElement(driver, visibleElements.get(visibleElements.size() - 1));
            visibleElements = driver.findElements(By.cssSelector(DROPDOWN_ELEMENT_CSS));
            temporaryLastOption = visibleElements.get(visibleElements.size() - 1).getText();
        }
        return optionsLabels;
    }

    public void selectOptionById(String optionId) {
        WebElement foundedElement = getOptionById(optionId);
        WebElementUtils.clickWebElement(driver, foundedElement);
    }

    public String getOptionLabel(String optionId) {
        return getOptionById(optionId).getAttribute(TEXT_CONTENT_ATTRIBUTE);
    }

    public void selectOptions(List<String> optionsId) {
        optionsId.forEach(this::selectOptionById);
    }

    public void selectOptionsByLabels(List<String> optionsLabels) {
        optionsLabels.forEach(this::selectOption);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }

    public void search(String value) {
        clear();
        WebElement input = driver.findElement(By.cssSelector(INPUT_CSS));
        WebElementUtils.clickWebElement(driver, input);
        input.sendKeys(value);
    }

    public void clear() {
        WebElement input = driver.findElement(By.cssSelector(INPUT_CSS));
        WebElementUtils.clickWebElement(driver, input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    private WebElement getOptionById(String optionId) {
        DelayUtils.waitBy(wait, By.cssSelector(String.format(BY_ID_PATTERN, optionId, optionId)));
        return driver.findElement(By.cssSelector(String.format(BY_ID_PATTERN, optionId, optionId)));
    }

}
