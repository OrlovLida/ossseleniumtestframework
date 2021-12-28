package com.oss.framework.components.common;

import java.util.List;

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

    public static AttributesChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement attributesChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new AttributesChooser(driver, webDriverWait, attributesChooser);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement attributesChooser;

    private AttributesChooser(WebDriver driver, WebDriverWait webDriverWait, WebElement attributesChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.attributesChooser = attributesChooser;
    }

    public AttributesChooser enableAttributeByLabel(String attributeLabel, String... path) {
        int level = 0;
        for (String attributeCategory : path) {
            expandAttributeCategoryByLabel(attributeCategory, level);
            level++;
        }
        enableAttributesByLabel(level, attributeLabel);
        return this;
    }

    public AttributesChooser disableAttributeByLabel(String attributeLabel, String... path) {
        int level = 0;
        for (String attributeCategory : path) {
            expandAttributeCategoryByLabel(attributeCategory, level);
            level++;
        }
        disableAttributesByLabel(level, attributeLabel);
        return this;
    }

    public List<Attribute> getAttributes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Attribute> getAttributes(String... path) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Attribute> getAttributesByLabel(String... pathLabel) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void toggleAttributeByLabel(String attributeLabel, int level) {
        Node node = findAttributeByLabel(attributeLabel, level);
        node.toggleNode();
    }

    public void expandAttributeCategoryByLabel(String attributeCategoryLabel, int level) {
        Node node = findAttributeCategoryByLabel(attributeCategoryLabel, level);
        node.expandNode();
    }

    public void clickApply() {
        this.attributesChooser.findElement(By.xpath(APPLY_BUTTON_XPATH)).click();
    }

    public void clickCancel() {
        this.attributesChooser.findElement(By.xpath(CANCEL_BUTTON_XPATH)).click();
    }

    public void clickDefaultSettings() {
        this.attributesChooser.findElement(By.xpath(DEFAULT_BUTTON_XPATH)).click();
    }

    public void toggleAttributeByLabel(String attributeLabel, String... pathLabel) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void toggleAttributeByPath(String path) {
        getTreeComponent().toggleNodeByPath(path);
    }

    private TreeComponent getTreeComponent() {
        return TreeComponent.create(this.driver, this.webDriverWait, attributesChooser);
    }

    private AttributesChooser disableAttributesByLabel(int level, String... attributeLabels) {
        for (String attributeLabel : attributeLabels) {
            if (isAttributeSelectedByLabel(attributeLabel, level)) {
                toggleAttributeByLabel(attributeLabel, level);
            }
        }
        return this;
    }

    private AttributesChooser enableAttributesByLabel(int level, String... columnLabels) {
        for (String columnLabel : columnLabels) {
            if (!isAttributeSelectedByLabel(columnLabel, level)) {
                toggleAttributeByLabel(columnLabel, level);
            }
        }
        return this;
    }

    private boolean isAttributeSelectedByLabel(String attributeLabel, int level) {
        Node node = findAttributeByLabel(attributeLabel, level);
        return node.isToggled();
    }

    private Node findAttributeByLabel(String attributeLabel, int level) {
        List<Node> nodes = getTreeComponent().getNodes(level);
        return getNodeByLabel(nodes, attributeLabel);
    }

    private Node findAttributeCategoryByLabel(String attributeCategoryLabel, int level) {
        List<Node> nodes = getTreeComponent().getNodesWithExpander(level);
        return getNodeByLabel(nodes, attributeCategoryLabel);
    }

    private Node getNodeByLabel(List<Node> nodes, String label) {
        return nodes.stream().filter(n -> n.getLabel().equals(label))
                .findFirst().orElseThrow(() -> new RuntimeException("Cant find node " + label));
    }

    public static class Attribute {
        private final String attributeId;
        private final String attributeLabel;
        private final boolean isSelected;

        public Attribute(String attributeId, String attributeLabel, boolean isSelected) {
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
