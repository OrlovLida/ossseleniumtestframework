package com.oss.framework.components.portals;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class DropdownList {

    private static String XPATH_DROPDOWN_LIST = "//div[@class='portal']";

    public static DropdownList create(WebDriver driver, WebDriverWait webDriverWait) {
        return new DropdownList(driver, webDriverWait);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;

    private DropdownList(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void selectOption(String option) {
        DelayUtils.waitByXPath(wait, XPATH_DROPDOWN_LIST + "//div[text()='" + option + "']");
        WebElement foundedElement =
                driver.findElement(By.xpath(XPATH_DROPDOWN_LIST + "//div[text()='" + option + "']"));
        foundedElement.click();
    }

    public void selectOptionContains(String option) {
        DelayUtils.waitByXPath(wait, XPATH_DROPDOWN_LIST + "//div[contains(text(), '" + option + "')]");
        Actions action = new Actions(driver);
        WebElement foundedElement =
                driver.findElement(By.xpath(XPATH_DROPDOWN_LIST + "//div[contains(text(), '" + option + "')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionWithIconContains(String option) {
        Actions action = new Actions(driver);
        DelayUtils.waitByXPath(wait, XPATH_DROPDOWN_LIST + "//a[contains(text(), '" + option + "')]");
        WebElement foundedElement =
                driver.findElement(By.xpath(XPATH_DROPDOWN_LIST + "//a[contains(text(), '" + option + "')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionWithId(String option) {
        Actions action = new Actions(driver);
        DelayUtils.waitByXPath(wait, XPATH_DROPDOWN_LIST + "//a[contains(@"+ CSSUtils.TEST_ID +", '" + option + "')]");
        WebElement foundedElement =
                driver.findElement(By.xpath(XPATH_DROPDOWN_LIST + "//a[contains(@"+ CSSUtils.TEST_ID +", '" + option + "')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptions(List<String> options) {
        options.forEach(this::selectOption);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }
}
