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
    public static DropdownList create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement dropdownList = driver.findElement(By.className("portal"));
        return new DropdownList(driver, webDriverWait, dropdownList);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement dropdownList;

    private DropdownList(WebDriver driver, WebDriverWait wait, WebElement dropdownList) {
        this.driver = driver;
        this.wait = wait;
        this.dropdownList = dropdownList;
    }

    public void selectOption(String option) {
        DelayUtils.waitByElement(wait, dropdownList.findElement(By.xpath(".//div[text()='" + option + "']")));
        WebElement foundedElement =
                dropdownList.findElement(By.xpath(".//div[text()='" + option + "']"));
        foundedElement.click();
    }

    public void selectOptionContains(String option) {
        DelayUtils.waitByElement(wait, dropdownList.findElement(By.xpath(".//*[contains(text(), '" + option + "')]")));
        Actions action = new Actions(driver);
        WebElement foundedElement =
                dropdownList.findElement(By.xpath(".//*[contains(text(), '" + option + "')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionWithIconContains(String option) {
        DelayUtils.waitByElement(wait, dropdownList.findElement(By.xpath(".//a[contains(text(), '" + option + "')]")));
        Actions action = new Actions(driver);
        WebElement foundedElement =
                dropdownList.findElement(By.xpath(".//a[contains(text(), '" + option + "')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionWithId(String option) {
        Actions action = new Actions(driver);
        DelayUtils.waitByElement(wait, dropdownList
                .findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "='" + option + "'] | .//*[@id='" + option + "'] ")));
        WebElement foundedElement = dropdownList
                .findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "='" + option + "'] | .//*[@id='" + option + "'] "));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptions(List<String> options) {
        options.forEach(this::selectOption);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }
}
