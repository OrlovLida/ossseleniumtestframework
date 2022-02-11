package com.oss.framework.components.portals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class DropdownList {

    public static final String PORTAL_CLASS = "portal";
    private static final String INPUT_XPATH = ".//div[@class='search-cont']//input";
    private static final String BY_ID_PATTERN = "//*[@" + CSSUtils.TEST_ID + "='%s'] | //*[@id='%s']";
    private static final String BY_TEXT_PATTERN = ".//*[text()='%s']";
    private static final String BY_TEXT_CONTAINS_PATTERN = ".//*[contains(text(), '%s')]";

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
        return new DropdownList(driver, webDriverWait, dropdownList);
    }

    public void selectOption(String optionLabel) {
        DelayUtils.waitByElement(wait, dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_PATTERN, optionLabel))));
        WebElement foundedElement =
                dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_PATTERN, optionLabel)));
        foundedElement.click();
    }

    public void selectOptionContains(String optionLabel) {
        DelayUtils.waitByElement(wait, dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_CONTAINS_PATTERN, optionLabel))));
        Actions action = new Actions(driver);
        WebElement foundedElement =
                dropdownListElement.findElement(By.xpath(String.format(BY_TEXT_CONTAINS_PATTERN, optionLabel)));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionById(String optionId) {
        Actions action = new Actions(driver);
        DelayUtils.waitByXPath(wait, String.format(BY_ID_PATTERN, optionId, optionId));
        WebElement foundedElement = dropdownListElement
                .findElement(By.xpath(String.format(BY_ID_PATTERN, optionId, optionId)));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptions(List<String> optionsId) {
        optionsId.forEach(this::selectOptionById);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }

    public void search(String value) {
        clear();
        Actions action = new Actions(driver);
        WebElement input = dropdownListElement.findElement(By.xpath(INPUT_XPATH));
        action.moveToElement(input).click().build().perform();
        input.sendKeys(value);
    }

    public void clear() {
        Actions action = new Actions(driver);
        WebElement input = dropdownListElement.findElement(By.xpath(INPUT_XPATH));
        action.moveToElement(input).click().build().perform();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
