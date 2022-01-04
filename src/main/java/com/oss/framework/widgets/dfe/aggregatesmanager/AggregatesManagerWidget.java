package com.oss.framework.widgets.dfe.aggregatesmanager;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.MultiCombobox;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class AggregatesManagerWidget extends Widget {

    private static final String ADD_BTN_PATH = "//button[@class='btn btn-primary btn-add-aggregate']";
    private static final String AGGREGATES_MANAGER_PATH = "//div[@class='AggregatesManagerContainer']";

    private AggregatesManagerWidget(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webElement, webDriverWait);
    }

    public static AggregatesManagerWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, AGGREGATES_MANAGER_PATH);
        WebElement webElement = driver.findElement(By.xpath(AGGREGATES_MANAGER_PATH));

        return new AggregatesManagerWidget(driver, wait, webElement);
    }

    public List<AggregateSingleConfiguration> getAggregateConfigurations() {
        return webElement.findElements(By.xpath(AggregateSingleConfiguration.AGGREGATE_COMPONENT_PATH))
                .stream()
                .map(element -> AggregateSingleConfiguration.create(driver, webDriverWait, element))
                .collect(Collectors.toList());
    }

    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
        DelayUtils.waitByXPath(webDriverWait, AggregateSingleConfiguration.AGGREGATE_FORM_PATH);
    }

    public static class AggregateSingleConfiguration {

        public static final String AGGREGATE_FORM_PATH = "//div[@class='AggregateForm']";
        public static final String AGGREGATE_COMPONENT_PATH = "//section[@class='AggregateComponentContainer']";
        private static final String AGGREGATE_HEADER_PATH = "//div[@class='AggregateHeader']";
        private static final String INPUT_NAME_ID = "nameId";
        private static final String INPUT_TABLE_BASE_NAME_ID = "tableBaseNameId";
        private static final String INPUT_DIMENSIONS_PATH = "factColumnsId";

        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement webElement;

        private AggregateSingleConfiguration(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
            this.driver = driver;
            this.wait = webDriverWait;
            this.webElement = webElement;
        }

        public static AggregateSingleConfiguration create(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
            return new AggregateSingleConfiguration(driver, webDriverWait, webElement);
        }

        public boolean isExpanded() {
            return !webElement.findElements(By.xpath(AGGREGATE_FORM_PATH)).isEmpty();
        }

        public void expand() {
            if (!isExpanded()) {
                webElement.findElement(By.xpath(AGGREGATE_HEADER_PATH)).click();
            }
        }

        public TextField getNameInput() {
            return (TextField) getComponent(INPUT_NAME_ID, Input.ComponentType.TEXT_FIELD);
        }

        public TextField getBaseTableNameInput() {
            return (TextField) getComponent(INPUT_TABLE_BASE_NAME_ID, Input.ComponentType.TEXT_FIELD);
        }

        public MultiCombobox getDimensionsInput() {
            return (MultiCombobox) getComponent(INPUT_DIMENSIONS_PATH, Input.ComponentType.MULTI_COMBOBOX);
        }

        public String getName() {
            return getNameInput().getStringValue();
        }

        public void setName(String value) {
            getNameInput().setSingleStringValue(value);
        }

        public String getBaseTableName() {
            return getBaseTableNameInput().getStringValue();
        }

        public void setBaseTableName(String value) {
            getBaseTableNameInput().setSingleStringValue(value);
        }

        public Data getDimensions() {
            return getDimensionsInput().getValue();
        }

        public void setDimensions(String value) {
            getDimensionsInput().setSingleStringValue(value);
        }

        private Input getComponent(String componentId, Input.ComponentType componentType) {
            return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        }

    }

}
