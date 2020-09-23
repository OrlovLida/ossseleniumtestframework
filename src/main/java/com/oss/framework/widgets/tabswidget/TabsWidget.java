package com.oss.framework.widgets.tabswidget;

import com.google.common.collect.Maps;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.TableWidget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabsWidget implements TabsInterface {

    public static final String TABS_WIDGET_CLASS = "tabsContainer";

    private final String tabs = ".//a";
    private final String activeTab = ".//a[contains(@class,'active')]";
    private final Map<String, TableWidget> widgets = Maps.newHashMap();


    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;
    protected final String id;

    private TabsWidget(WebDriver driver, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(TABS_WIDGET_CLASS));
        this.webDriverWait = webDriverWait;
        this.id =null;
    }
    private TabsWidget(WebDriver driver, WebDriverWait wait, String id) {
        this.driver = driver;
        this.webDriverWait = wait;
        this.webElement = null;
        this.id = id;
    }

    public static TabsWidget create(WebDriver driver, WebDriverWait webDriverWait) {
        return new TabsWidget(driver, webDriverWait);
    }
    public static TabsWidget createById(WebDriver driver, WebDriverWait wait, String id) {
        return new TabsWidget(driver, wait, id);
    }

    private WebElement createTabs() {
        if(this.webElement != null) {
            return this.webElement;
        }
        DelayUtils.waitByXPath(webDriverWait, "//div[contains(@class,'"+TABS_WIDGET_CLASS+"') and contains(@data-attributename, "+id+")]");
        WebElement widget = driver.findElement(By.xpath("//div[contains(@class,'"+TABS_WIDGET_CLASS+"') and contains(@data-attributename,"+id+")]"));
        return widget;
    }
@Deprecated
    public static void waitForTabsContainer(WebDriverWait wait, String ContainerPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ContainerPath)));
    }
    @Deprecated
    public List<WebElement> getTabs() {
        return this.webElement.findElements(By.xpath(tabs));
    }

    public List<String> getTabLabels() {
        List<String> labels = new ArrayList<String>();
        for (WebElement element : this.webElement.findElements(By.xpath(tabs))) {
            labels.add(element.getText());
        }
        return labels;
    }

    //numeration starts from 0
    public String getTabLabel(int tab) {
        return getTabLabels().get(tab);
    }

    public String getActiveTabLabel() {
        return this.webElement.findElement(By.xpath(activeTab)).getText();
    }

@Deprecated
    public void clickOnTab(String tabName) {
        int index = getTabLabels().indexOf(tabName);
        getTabs().get(index).click();
        //if(tabName != "Properties") {
        //    if(tabTable!=null){tabTable = null;}
        //    Widget.waitForWidget(new WebDriverWait(driver, 50), "");
        //    tabTable = TableWidget.create(driver, "");
        //}
    }

@Deprecated
    public TableWidget getTableWidget(String tabName) {
        if (widgets.containsKey(tabName)) {
            return widgets.get(tabName);
        }
        if (tabName != "Properties") {
            Widget.waitForWidget(new WebDriverWait(this.driver, 11), tabName);
            TableWidget tableWidget = TableWidget.create(this.driver, tabName, this.webDriverWait);
            widgets.put(tabName, tableWidget);
            return tableWidget;
        } else {
            return null;
        }
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), ".//div[contains(@class,'tabsContainerTabs')]");
        WebElement allTabs = createTabs().findElement(By.xpath(".//div[contains(@class,'tabsContainerTabs')]"));
        if(isMoreVisible()){
            WebElement moreTab = createTabs().findElement(By.xpath("//div[@class= 'dropdown-tab']"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(webDriverWait, ".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'"+tabLabel+"')]");
            WebElement tab = driver.findElement(By.xpath(".//a[contains(text(),'" + tabLabel + "')]  | .//div[@class='tab-label'][contains(text(),'"+ tabLabel+"')]"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }
        else {
            DelayUtils.waitForNestedElements(webDriverWait, allTabs, ".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'"+tabLabel+"')]");
            WebElement tab = allTabs.findElement(By.xpath(".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'"+tabLabel+"')]"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }

    }

    @Override
    public void selectTabById(String id) {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        if (isMoreVisible()) {
            WebElement moreTab = createTabs().findElement(By.xpath("//div[@class= 'dropdown-tab']"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(webDriverWait, ".//a[@id='" + id + "']");
            WebElement tab = driver.findElement(By.xpath(".//a[@id='" + id + "']"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }
        else {
            WebElement tab = createTabs().findElement(By.xpath(".//a[@id='" + id + "']"));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
        }

    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actionsContainer = OldActionsContainer.createFromParent(driver, webDriverWait, createTabs());
        actionsContainer.callActionByLabel(actionLabel);

    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        throw new RuntimeException("Method not implemented for the old tabs");

    }

    @Override
    public void callActionById(String groupLabel, String id) {

    }

    @Override
    public void callActionById(String id) {
        ActionsInterface actionsContainer = OldActionsContainer.createFromParent(driver, webDriverWait, createTabs());
        actionsContainer.callActionById(id);
    }

    @Override
    public boolean isNoData(String id) {
        return false;
    }
    private boolean isMoreVisible(){
        DelayUtils.waitForNestedElements(webDriverWait,createTabs(),"//div[@class= 'tabsContainerTabs']");
        List<WebElement> isMore = createTabs().findElements(By.xpath("//div[@class= 'dropdown-tab']"));
        return !isMore.isEmpty();
    }
}
