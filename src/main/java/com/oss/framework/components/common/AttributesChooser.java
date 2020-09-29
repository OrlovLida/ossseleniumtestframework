package com.oss.framework.components.common;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;

public class AttributesChooser {

    private static String X_PATH_ID = "//div[@id='attributes-management']";

    public static AttributesChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement attributesChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new AttributesChooser(driver, webDriverWait, attributesChooser);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement attributesChooser;

    private AttributesChooser (WebDriver driver, WebDriverWait webDriverWait,  WebElement attributesChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.attributesChooser = attributesChooser;
    }

    public AttributesChooser disableColumnByLabel(String columnLabel) {
        if(isAttributeSelectedByLabel(columnLabel)) {
            toggleAttributeByLabel(columnLabel);
        }
        return this;
    }

    public AttributesChooser enableColumnByLabel(String columnLabel) {
        if(!isAttributeSelectedByLabel(columnLabel)) {
            toggleAttributeByLabel(columnLabel);
        }
        return this;
    }

    public List<Attribute> getAttributes() {
        return null;
    }

    public List<Attribute> getAttributes(String... path) {
        return null;
    }

    public List<Attribute> getAttributesByLabel(String... pathLabel) {
        return null;
    }

    public void toggleAttributeByLabel(String attributeLabel) {
        Node node = findAttributeByLabel(attributeLabel);
        node.toggleNode();
    }

    public void toggleAttributeByLabel(String attributeLabel, String... pathLabel) {

    }

    public void toggleAttribute(String attributeId) {

    }

    public void toggleAttribute(String attributeId, String... path) {

    }

    public boolean isAttributeSelectedByLabel(String attributeLabel) {
        Node node = findAttributeByLabel(attributeLabel);
        return node.isToggled();
    }

    private Node findAttributeByLabel(String attributeLabel) {
        List<Node> nodes = getTreeComponent().getNodes();
        Node node = nodes.stream().filter(n -> n.getLabel().equals(attributeLabel))
                .findFirst().orElseThrow(() -> new RuntimeException("Cant find node " + attributeLabel));
        return node;
    }

    private TreeComponent getTreeComponent() {
        return TreeComponent.create(this.driver, this.webDriverWait, attributesChooser);
    }

    public void clickApply() {
        this.attributesChooser.findElement(By.xpath(".//a[contains(@class,'btn-primary')]")).click();
    }

    public void clickCancel() {
        this.attributesChooser.findElement(By.xpath(".//div[@class='management-basic-buttons']/a[contains(@class,'btn-flat')]")).click();
    }

    public static class Attribute {
        private final String attributeId;
        private final String attributeLabel;
        private final boolean isSelected;

        public Attribute (String attributeId, String attributeLabel, boolean isSelected) {
            this.attributeId = attributeId;
            this.attributeLabel = attributeLabel;
            this.isSelected = isSelected;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public String getAttributeLabel() {
            return attributeLabel;
        }

        public boolean isSelected() {
            return isSelected;
        }
    }


}
