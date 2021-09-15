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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
        StringBuilder level = new StringBuilder();
        for (String attributeCategory: path) {
            expandAttributeCategoryByLabel(attributeCategory);
            level = level.append(attributeCategory).append(".");
        }
        String labelPath = level.append(attributeLabel).toString();
        enableAttributesByLabel(labelPath);
        return this;
    }
    
    public AttributesChooser disableAttributeByLabel(String attributeLabel, String... path) {
        
        for (String attributeCategory: path) {
            expandAttributeCategoryByLabel(attributeCategory);
        }
        disableAttributesByLabel(attributeLabel);
        
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
    
    public void expandAttributeCategoryByLabel(String attributeCategoryLabel) {
        Node node = findAttributeCategoryByLabel(attributeCategoryLabel);
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
        
    }
    
    public void toggleAttributeByPath(String path) {
        getTreeComponent().toggleNodeByPath(path);
    }
    
    private TreeComponent getTreeComponent() {
        return TreeComponent.create(this.driver, this.webDriverWait, attributesChooser);
    }
    
    private AttributesChooser disableAttributesByLabel(String... attributeLabels) {
        for (String attributeLabel: attributeLabels) {
            if (isAttributeSelectedByLabel(attributeLabel)) {
                toggleAttributeByLabel(attributeLabel);
            }
        }
        return this;
    }
    
    private AttributesChooser enableAttributesByLabel(String... columnLabels) {
        for (String columnLabel: columnLabels) {
            if (!isAttributeSelectedByLabel(columnLabel)) {
                toggleAttributeByLabel(columnLabel);
            }
        }
        return this;
    }
    
    private boolean isAttributeSelectedByLabel(String attributeLabel) {
        Node node = findAttributeByLabel(attributeLabel);
        return node.isToggled();
    }
    
    private Node findAttributeByLabel(String attributeLabel) {
        return getTreeComponent().getNodeByLabelsPath(attributeLabel);
//        List<Node> nodes = getTreeComponent().getVisibleNodes();
//        return getNodeByLabel(nodes, attributeLabel);
    }
    
    private Node findAttributeCategoryByLabel(String attributeCategoryLabel) {
        // List<Node> nodes = getTreeComponent().getVisibleNodes();
        return getTreeComponent().getNodeByLabelsPath(attributeCategoryLabel);
        // return getNodeByLabel(nodes, attributeCategoryLabel);
    }
    
    private Node getNodeByLabel(List<Node> nodes, String label) {
        Node node = nodes.stream().filter(n -> n.getLabel().equals(label))
                .findFirst().orElseThrow(() -> new RuntimeException("Cant find node " + label));
        return node;
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
