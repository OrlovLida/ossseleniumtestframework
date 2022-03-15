package com.oss.framework.widgets.tabs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.widgetchooser.WidgetChooser;
import com.oss.framework.iaa.widgets.list.MessageListWidget;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.WidgetFactory;

public class TabsWidget extends Widget implements TabsInterface {

    public static final String TABS_WIDGET_CLASS = "tabsContainer";
    private static final String CHILD_TABS_XPATH = ".//a";
    private static final String ACTIVE_TAB_XPATH = ".//a[contains(@class,'active')]";
    private static final String ADD_TAB_ICON_XPATH = ".//i[@class ='OSSIcon fa fa-plus']";
    private static final String DROPDOWN_TAB_XPATH = ".//div[@class= 'dropdown-tab']";
    private static final String TABS_CONTAINER_XPATH = ".//div[contains(@class,'tabsContainerTabs')]";
    private static final String DRAGGABLE_ELEMENT_XPATH = ".//div[@class = 'btn-drag']";
    private static final String ACTIONS_CONTAINER_CSS = ".actionsContainer";
    private static final String WINDOW_TOOLBAR_CSS = ".windowToolbar";
    private static final String CONTEXT_ACTIONS_CSS = WINDOW_TOOLBAR_CSS + "," + ACTIONS_CONTAINER_CSS;
    private static final String TABS_PATTERN = "//div[@" + CSSUtils.TEST_ID + "= '%s']";
    private static final String TAB_BY_LABEL_PATTERN = ".//*[contains(text(),'%s')] | .//*[@class='tab-label'][contains(text(),'%s')]";
    private static final String TAB_BY_ID_PATTERN = ".//a[@id='%s']";
    private static final String ACTIVE_TAB_CONTENT = ".//div[@data-testid='%s']//div[contains(@class,'tabsContainerSingleContent active')]";
    private static final String REMOVE_TAB_XPATH = ".//*[@title='Remove tab']";

    private TabsWidget(WebDriver driver, WebDriverWait wait, String id) {
        super(driver, wait, id);
    }

    public static TabsWidget createById(WebDriver driver, WebDriverWait wait, String id) {
        Widget.waitForWidgetById(wait, id);
        return new TabsWidget(driver, wait, id);
    }

    public Widget getWidget(String widgetId, WidgetType widgetType) {
        return WidgetFactory.getWidget(widgetId, widgetType, driver, webDriverWait);
    }

    public String getTabLabel(int tab) {
        return getTabLabels().get(tab);
    }

    public String getActiveTabLabel() {
        return createTabs().findElement(By.xpath(ACTIVE_TAB_XPATH)).getText();
    }

    @Override
    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait, TABS_CONTAINER_XPATH);
        WebElement tabToSelect = getTab(String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToSelect));
        tabToSelect.click();
    }

    public void removeTab(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait, TABS_CONTAINER_XPATH);
        WebElement tabToRemove = getTab(String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToRemove));
        tabToRemove.findElement(By.xpath(REMOVE_TAB_XPATH)).click();

    }

    @Override
    public void selectTabById(String id) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement tabToSelect = getTab(String.format(TAB_BY_ID_PATTERN, id));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToSelect));
        tabToSelect.click();
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
    }

    @Override
    public void callActionByLabel(String groupLabel, String label) {
        getActionsInterface().callActionByLabel(groupLabel, label);
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        getActionsInterface().callActionById(groupId, actionId);
    }

    @Override
    public void callActionById(String id) {
        getActionsInterface().callActionById(id);
    }

    public void changeTabsOrder(String tabLabel, int position) {
        DragAndDrop.dragAndDrop(getDraggableElement(tabLabel), getDropElement(position), 60, 0, driver);
    }

    public WidgetChooser getWidgetChooser() {
        createTabs().findElement(By.xpath(ADD_TAB_ICON_XPATH)).click();
        return WidgetChooser.create(driver, webDriverWait);
    }

    public boolean isTabDisplayed(String tabLabel) {
        for (String label : getTabLabels()) {
            if (label.equals(tabLabel))
                return true;
        }
        return false;
    }

    private List<String> getTabLabels() {
        List<String> labels = Lists.newArrayList();
        for (WebElement element : createTabs().findElements(By.xpath(CHILD_TABS_XPATH))) {
            labels.add(element.getText());
        }
        return labels;
    }

    private WebElement createTabs() {
        if (this.id == null) {
            DelayUtils.waitBy(webDriverWait, By.className(TABS_WIDGET_CLASS));
            return driver.findElement(By.className(TABS_WIDGET_CLASS));
        }
        DelayUtils.waitByXPath(webDriverWait,
                String.format(TABS_PATTERN, id));
        return driver
                .findElement(By
                        .xpath(String.format(TABS_PATTERN, id)));
    }

    private ActionsInterface getActionsInterface() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), By.cssSelector(CONTEXT_ACTIONS_CSS));
        boolean isNewActionContainer = isElementPresent(driver, By.cssSelector("[" + CSSUtils.TEST_ID + "='" + id + "'] " + ACTIONS_CONTAINER_CSS));
        boolean isOldActionContainer = isElementPresent(driver, By.cssSelector("[" + CSSUtils.TEST_ID + "='" + id + "'] " + WINDOW_TOOLBAR_CSS));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(createTabs(), driver, webDriverWait);
        } else if (isOldActionContainer)
            return OldActionsContainer.createFromParent(driver, webDriverWait, createTabs());
        else {
            return ButtonContainer.createFromParent(createTabs(), driver, webDriverWait);
        }
    }

    private boolean isElementPresent(WebDriver driver, By by) {
        return WebElementUtils.isElementPresent(driver, by);
    }

    private boolean isMorePresent() {
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), TABS_CONTAINER_XPATH);
        List<WebElement> isMore = createTabs().findElements(By.xpath(DROPDOWN_TAB_XPATH));
        return !isMore.isEmpty();
    }

    private DragAndDrop.DraggableElement getDraggableElement(String tabLabel) {
        WebElement tab = getTabByLabel(tabLabel);
        Actions action = new Actions(driver);
        action.moveToElement(tab).perform();
        return new DragAndDrop.DraggableElement(tab.findElement(By.xpath(DRAGGABLE_ELEMENT_XPATH)));
    }

    private DragAndDrop.DropElement getDropElement(int position) {
        WebElement target = createTabs().findElements(By.xpath(CHILD_TABS_XPATH)).get(position);
        return new DragAndDrop.DropElement(target);
    }

    private WebElement getTabByLabel(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait,
                String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel));
        return createTabs().findElement(By
                .xpath(String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel) + "/.."));
    }

    @Override
    public MessageListWidget getMessageListWidget(String cardContainerId) {
        return MessageListWidget.createFromParent(getActiveTabContent(cardContainerId), driver, webDriverWait);
    }

    private WebElement getActiveTabContent(String cardContainerId) {
        return driver.findElement(By.xpath(String.format(ACTIVE_TAB_CONTENT, cardContainerId)));
    }

    private WebElement getTab(String xPathForTab) {
        if (isMorePresent()) {
            WebElement moreTab = createTabs().findElement(By.xpath(DROPDOWN_TAB_XPATH));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
            DelayUtils.waitByXPath(webDriverWait, xPathForTab);
            return driver.findElement(By.xpath(xPathForTab + "//ancestor::a"));
        } else {
            return createTabs().findElement(By.xpath(xPathForTab + "//ancestor::a"));
        }
    }
}
