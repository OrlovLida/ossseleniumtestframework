package com.oss.framework.components.portals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;

import static java.lang.Thread.sleep;

public class DropdownList {

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
        WebElement foundedElement =
                driver.findElement(By.xpath("//div[@class='portal']//div[text()='"+option+"']"));
        foundedElement.click();
    }

    public void selectOptionContains(String option) {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                driver.findElement(By.xpath("//div[@class='portal']//div[contains(text(), '"+option+"')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptions(List<String> options) {
        options.forEach(this::selectOption);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }
}
