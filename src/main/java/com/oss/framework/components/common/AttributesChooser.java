package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;

public class AttributesChooser {

    private static final String X_PATH_ID = "//div[@id='attributes-management']";
    private static final String APPLY_BUTTON_XPATH = ".//a[contains(@class,'btn-primary')]";
    private static final String CANCEL_BUTTON_XPATH = ".//div[@class='management-basic-buttons']/a[contains(@class,'btn-flat')]";
    private static final String DEFAULT_BUTTON_XPATH = ".//div[@class='management-default-button']/a[contains(@class,'btn-flat')]";
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement attributesChooserElement;

    private AttributesChooser(WebDriver driver, WebDriverWait webDriverWait, WebElement attributesChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.attributesChooserElement = attributesChooser;
    }

    public static AttributesChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement attributesChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new AttributesChooser(driver, webDriverWait, attributesChooser);
    }

    public void enableAttributeByLabel(String attributeLabel, String... path) {
        String nodePath = getAttributePath(attributeLabel, path);
        Node attributeByLabelsPath = getAttributeByLabelsPath(nodePath);
        enableAttributesByLabel(attributeByLabelsPath);

    }

    public void disableAttributeByLabel(String attributeLabel, String... path) {
        String nodePath = getAttributePath(attributeLabel, path);
        Node attributeByLabelsPath = getAttributeByLabelsPath(nodePath);
        disableAttributesByLabel(attributeByLabelsPath);
    }

    private String getAttributePath(String attributeLabel, String... path) {
        StringBuilder level = new StringBuilder();
        for (String attributeCategory: path) {
            level.append(attributeCategory).append(".");
        }
        return level.append(attributeLabel).toString();
    }

    public void clickApply() {
        this.attributesChooserElement.findElement(By.xpath(APPLY_BUTTON_XPATH)).click();
    }

    public void clickCancel() {
        this.attributesChooserElement.findElement(By.xpath(CANCEL_BUTTON_XPATH)).click();
    }

    public void clickDefaultSettings() {
        this.attributesChooserElement.findElement(By.xpath(DEFAULT_BUTTON_XPATH)).click();
    }

    public void toggleAttributeByLabel(String attributeLabel, String... pathLabel) {
        String attributePath = getAttributePath(attributeLabel, pathLabel);
        toggleAttributeByPath(attributePath);
    }

    public void toggleAttributeByPath(String path) {
        getTreeComponent().toggleNodeByPath(path);
    }

    private TreeComponent getTreeComponent() {
        return TreeComponent.create(this.driver, this.webDriverWait, attributesChooserElement);
    }

    private void disableAttributesByLabel(Node attribute) {
        if (attribute.isToggled()) {
            attribute.toggleNode();
        }
    }

    private void enableAttributesByLabel(Node attribute) {
        if (!attribute.isToggled()) {
            attribute.toggleNode();
        }
    }

    private Node getAttributeByLabelsPath(String attributeLabel) {
        return getTreeComponent().getNodeByLabelsPath(attributeLabel);

    }
}
