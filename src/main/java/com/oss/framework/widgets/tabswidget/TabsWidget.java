package com.oss.framework.widgets.tabswidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.oss.framework.components.common.WidgetChooser;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

public class TabsWidget implements TabsInterface {

    public static final String TABS_WIDGET_CLASS = "tabsContainer";

    private static final String TABS = ".//a";
    private static final String ACTIVE_TAB = ".//a[contains(@class,'active')]";
    private static final String ADD_TAB_ICON = ".//i[@class ='OSSIcon fa fa-plus']";
    private static final String SAVE_TAB_ICON = ".//i[@class ='OSSIcon fa fa-save']";
    private static final String DOWNLOAD_CONFIGURATION_ICON = ".//i[@class ='OSSIcon fa fa-download']";
    private static final String CHOOSE_CONFIGURATION_ICON = ".//i[@class ='OSSIcon fa fa-cog']";
    private static final String DROPDOWN_TAB = ".//div[@class= 'dropdown-tab']";
    private static final String TABS_CONTAINER_XPATH = ".//div[contains(@class,'tabsContainerTabs')]";

    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final String id;

    private TabsWidget(WebDriver driver, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.id = null;
    }

    private TabsWidget(WebDriver driver, WebDriverWait wait, String id) {
        this.driver = driver;
        this.webDriverWait = wait;
        this.id = id;
    }

    public static TabsWidget create(WebDriver driver, WebDriverWait webDriverWait) {
        return new TabsWidget(driver, webDriverWait);
    }

    public static TabsWidget createById(WebDriver driver, WebDriverWait wait, String id) {
        return new TabsWidget(driver, wait, id);
    }

    private WebElement createTabs() {
        if (this.id == null) {
            DelayUtils.waitByXPath(webDriverWait,
                    "//*[@class = '" + TABS_WIDGET_CLASS + "']");
            return driver.findElement(By.className(TABS_WIDGET_CLASS));
        }
        DelayUtils.waitByXPath(webDriverWait,
                "//div[contains(@class,'" + TABS_WIDGET_CLASS + "') and contains(@data-attributename, '" + id + "')]");
        return driver
                .findElement(By.xpath("//div[contains(@class,'" + TABS_WIDGET_CLASS + "') and contains(@data-attributename,'" + id + "')]"));
    }

    @Deprecated
    public static void waitForTabsContainer(WebDriverWait wait, String ContainerPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ContainerPath)));
    }

    @Deprecated
    public List<WebElement> getTabs() {
        return createTabs().findElements(By.xpath(TABS));
    }

    public List<String> getTabLabels() {
        List<String> labels = Lists.newArrayList();
        for (WebElement element : createTabs().findElements(By.xpath(TABS))) {
            labels.add(element.getText());
        }
        return labels;
    }

    // numeration starts from 0
    public String getTabLabel(int tab) {
        return getTabLabels().get(tab);
    }

    public String getActiveTabLabel() {
        return createTabs().findElement(By.xpath(ACTIVE_TAB)).getText();
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
//        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), TABS_CONTAINER_XPATH);
        DelayUtils.waitByXPath(webDriverWait, ".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'" + tabLabel + "')]");
        String xpath = ".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'" + tabLabel + "')]";
        WebElement tabToSelect = getTabToSelect(xpath);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToSelect));
        tabToSelect.click();
    }

    @Override
    public void selectTabById(String id) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement tabToSelect = getTabToSelect(".//a[@id='" + id + "']");
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToSelect));
        tabToSelect.click();
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

    @Override
    public void clickButtonByLabel(String label) {
        ActionsInterface buttonContainer = ButtonContainer.createFromParent(createTabs(), driver, webDriverWait);
        buttonContainer.callActionByLabel(label);
    }

    private boolean isMoreVisible() {
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), TABS_CONTAINER_XPATH);
        List<WebElement> isMore = createTabs().findElements(By.xpath(DROPDOWN_TAB));
        return !isMore.isEmpty();
    }

    public void changeTabsOrder(String tabLabel, int position) {
        changeTabsPosition(getTabByLabel(tabLabel), createTabs().findElements(By.xpath(TABS)).get(position));
    }

    private void changeTabsPosition(WebElement source, WebElement target) {
        Actions action = new Actions(driver);
        action.moveToElement(source).perform();
        WebElement sourceBtn = source.findElement(By.xpath(".//div[@class = 'btn-drag']"));
        DragAndDrop.dragAndDrop(sourceBtn, target, driver);
    }

    public WidgetChooser getWidgetChooser() {
        createTabs().findElement(By.xpath(ADD_TAB_ICON)).click();
        return WidgetChooser.create(driver, webDriverWait);
    }

    public boolean isTabVisible(String tabLabel) {
        for (String label : getTabLabels()) {
            if (label.equals(tabLabel))
                return true;
        }
        return false;
    }

    public SaveConfigurationWizard openSaveConfigurationWizard() {
        createTabs().findElement(By.xpath(SAVE_TAB_ICON)).click();
        return SaveConfigurationWizard.create(driver, webDriverWait);
    }

    public ChooseConfigurationWizard openDownloadConfigurationWizard() {
        createTabs().findElement(By.xpath(DOWNLOAD_CONFIGURATION_ICON)).click();
        return ChooseConfigurationWizard.create(driver, webDriverWait);
    }

    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        createTabs().findElement(By.xpath(CHOOSE_CONFIGURATION_ICON)).click();
        return ChooseConfigurationWizard.create(driver, webDriverWait);
    }

    private WebElement getTabByLabel(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait,
                ".//a[contains(text(),'" + tabLabel + "')] | .//div[@class='tab-label'][contains(text(),'" + tabLabel + "')]");
        return createTabs().findElement(By
                .xpath(".//a[contains(text(),'" + tabLabel + "')]  | .//div[@class='tab-label'][contains(text(),'" + tabLabel + "')]/.."));
    }

    private WebElement getTabToSelect(String xPathForTab) {
        if (isMoreVisible()) {
            WebElement moreTab = createTabs().findElement(By.xpath(DROPDOWN_TAB));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(webDriverWait, xPathForTab);
            return driver.findElement(By.xpath(xPathForTab));
        } else {
            return createTabs().findElement(By.xpath(xPathForTab));
        }
    }
}
