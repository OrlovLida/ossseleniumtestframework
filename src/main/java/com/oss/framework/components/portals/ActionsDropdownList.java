package com.oss.framework.components.portals;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ActionsDropdownList {

    private final String actions = ".//a";

    protected final WebDriver driver;
    protected final WebElement webElement;

    public ActionsDropdownList (WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className("actionsList"));
    }

    public static ActionsDropdownList create(WebDriver driver) {
        return new ActionsDropdownList(driver);
    }

    public List<WebElement> getActions(){
        return this.webElement.findElements(By.xpath(actions));
    }

    public List<String> getActionLabels(){
        List<String> labels = new ArrayList<String>();
        for(WebElement element : this.webElement.findElements(By.xpath(actions))){
            labels.add(element.getText());
        }
        return labels;
    }

    public void clickOnAction(String actionName) {
        int index = getActionLabels().indexOf(actionName);
        getActions().get(index).click();
    }
}
