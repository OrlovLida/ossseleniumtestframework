package com.oss.framework.components.portals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static java.lang.Thread.sleep;

public class DropdownList {
    private final WebDriver driver;

    public DropdownList(WebDriver driver) {
        this.driver = driver;
    }

    public void selectOption(String option) {
            Actions action = new Actions(driver);
            WebElement foundedElement =
                    driver.findElement(By.xpath("//div[@class='CustomSelectList-data']//div[text()='"+option+"']"));
            action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptionContains(String option) {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                driver.findElement(By.xpath("//div[@class='CustomSelectList-data']//div[contains(text(), '"+option+"')]"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void selectOptions(List<String> options) {
        options.forEach(this::selectOption);
    }

    public void selectOptionsContains(List<String> options) {
        options.forEach(this::selectOptionContains);
    }
}
