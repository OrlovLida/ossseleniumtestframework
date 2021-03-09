package com.oss.framework.components.dfe.aggegatesmanager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.MultiCombobox;
import com.oss.framework.components.inputs.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregateSingleConfiguration {

    public static final String AGGREGATE_FORM_PATH = "//div[@class='AggregateForm']";
    private static final String AGGREGATE_HEADER_PATH = "//div[@class='AggregateHeader']";

    private static final String INPUT_NAME_ID = "nameId";
    private static final String INPUT_TABLE_BASE_NAME_ID = "tableBaseNameId";
    private static final String INPUT_DIMENSIONS_PATH = "factColumnsId";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public AggregateSingleConfiguration(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public Boolean isExpanded(){
        return !webElement.findElements(By.xpath(AGGREGATE_FORM_PATH)).isEmpty();
    }

    public void expand(){
        if(!isExpanded()){
            webElement.findElement(By.xpath(AGGREGATE_HEADER_PATH)).click();
        }
    }

    public TextField getNameInput(){
        return (TextField) getComponent(INPUT_NAME_ID, Input.ComponentType.TEXT_FIELD);
    }

    public TextField getBaseTableNameInput(){
        return (TextField) getComponent(INPUT_TABLE_BASE_NAME_ID, Input.ComponentType.TEXT_FIELD);
    }

    public MultiCombobox getDimensionsInput(){
        return (MultiCombobox) getComponent(INPUT_DIMENSIONS_PATH, Input.ComponentType.MULTI_COMBOBOX);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

}
