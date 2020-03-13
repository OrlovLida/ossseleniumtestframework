package com.oss.framework.widgets.tabswidget;

import com.google.common.collect.Maps;
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

public class TabsWidget {

    public static final String TABS_WIDGET_CLASS = "tabsContainer";

    private final String tabs = ".//a";
    private final String activeTab = ".//a[contains(@class,'active')]";
    //private final String tabTablePath = "./div[@class='tabsContainerContent']";
    private final Map<String, TableWidget> widgets = Maps.newHashMap();


    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait webDriverWait;

    private TableWidget tabTable;

    private TabsWidget(WebDriver driver, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(TABS_WIDGET_CLASS));
        this.webDriverWait = webDriverWait;
    }

    public static TabsWidget create(WebDriver driver, WebDriverWait webDriverWait) {
        return new TabsWidget(driver, webDriverWait);
    }

    public static void waitForTabsContainer(WebDriverWait wait, String ContainerPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ContainerPath)));
    }

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

    public void clickOnNthTab(int n) {
        getTabs().get(n - 1).click();
        //if(n!=1) {
        //    if(tabTable!=null){tabTable = null;}
        //    Widget.waitForWidget(new WebDriverWait(driver, 50), "");
        //    tabTable = TableWidget.create(driver, "");
        //}
    }

    public void clickOnTab(String tabName) {
        int index = getTabLabels().indexOf(tabName);
        getTabs().get(index).click();
        //if(tabName != "Properties") {
        //    if(tabTable!=null){tabTable = null;}
        //    Widget.waitForWidget(new WebDriverWait(driver, 50), "");
        //    tabTable = TableWidget.create(driver, "");
        //}
    }

    //public TableWidget getCurrentTabTable(){
    //    return tabTable;
    //}

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
}
