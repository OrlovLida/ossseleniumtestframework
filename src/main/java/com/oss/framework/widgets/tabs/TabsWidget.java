package com.oss.framework.widgets.tabs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private static final String CHILD_TABS_CSS = ".tabsContainerTabBtn";
    private static final String ACTIVE_TAB_XPATH = ".//*[contains(@class,'active')]";
    private static final String ADD_TAB_ICON_XPATH = ".//i[@class ='OSSIcon fa fa-plus']";
    private static final String DROPDOWN_TAB_XPATH = ".//div[contains(@class, 'dropdown-tab')]";
    private static final String TABS_CONTAINER_XPATH = ".//div[contains(@class,'tabsContainerTabs')]";
    private static final String ACTIONS_CONTAINER_CSS = ".actionsContainer";
    private static final String WINDOW_TOOLBAR_CSS = ".windowToolbar";
    private static final String CONTEXT_ACTIONS_CSS = WINDOW_TOOLBAR_CSS + "," + ACTIONS_CONTAINER_CSS;
    private static final String TABS_PATTERN = "//div[@" + CSSUtils.TEST_ID + "= '%s']";
    private static final String TAB_BY_LABEL_PATTERN = ".//*[contains(text(),'%s')] | .//*[@class='tab-label'][contains(text(),'%s')]";
    private static final String TAB_BY_ID_PATTERN = ".//a[@id='%s'] | .//*[@data-testid='%s']";
    private static final String ACTIVE_TAB_CONTENT = ".//div[@data-testid='%s']//div[contains(@class,'tabsContainerSingleContent active')]";
    private static final String REMOVE_TAB_XPATH = ".//*[@title='Remove tab']";
    private static final String ANCESTOR_PATTERN = "(%s)//ancestor::a";
    private static final String BUTTON_PATTERN = ".//button[text()='%s']";
    private static final String TABS_HEADER_CSS = ".tabsContainerHeader";
    private static final String TOOLBAR_CONTENT_CSS = ".toolbarComponent";
    private static final String HEADER_ACTION_CSS =
            TABS_HEADER_CSS + " " + TOOLBAR_CONTENT_CSS + "," + TABS_HEADER_CSS + " " + ACTIONS_CONTAINER_CSS;
    private static final String TAB_MORE_DROPDOWN_CSS = ".tabsContainerTabBtnDropdown";
    private static final String TEXT_CONTENT = "textContent";
    private static final String DRAG_CSS = ".btn-drag";
    private static final String ANCESTOR_TO_TAB_CONTAINER_XPATH = ".//ancestor::*[contains(@class,'tabsContainerTabBtn')]";

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

    public String getActiveTabId() {
        return createTabs().findElement(By.xpath(ACTIVE_TAB_XPATH)).getAttribute(CSSUtils.TEST_ID);
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
        DelayUtils.waitByXPath(webDriverWait, TABS_CONTAINER_XPATH);
        WebElement tabToSelect = getTab(String.format(TAB_BY_ID_PATTERN, id, id));
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

    @Override
    public boolean isButtonPresent(String text) {
        return isElementPresent(createTabs(), By.xpath(String.format(BUTTON_PATTERN, text)));
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

    public List<String> getTabLabels() {
        List<String> tabsLabel = createTabs().findElements(By.cssSelector(CHILD_TABS_CSS)).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        if (isMorePresent()) {
            clickMoreTab();
            List<String> tabsDropdown = driver.findElements(By.cssSelector(TAB_MORE_DROPDOWN_CSS)).stream()
                    .map(tab -> tab.getAttribute(TEXT_CONTENT))
                    .collect(Collectors.toList());
            tabsLabel.addAll(tabsDropdown);
        }
        return tabsLabel;
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

    public ActionsInterface getActionsInterface() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), By.cssSelector(CONTEXT_ACTIONS_CSS));
        boolean isHeaderActionPresent = isElementPresent(createTabs(),
                By.cssSelector(HEADER_ACTION_CSS));
        if (isHeaderActionPresent) {
            return getActionsInterface(createTabs().findElement(By.cssSelector(TABS_HEADER_CSS)));
        }
        return getActionsInterface(createTabs());
    }

    private ActionsInterface getActionsInterface(WebElement parent) {
        boolean isNewActionContainer = isElementPresent(parent, By.cssSelector(ACTIONS_CONTAINER_CSS));
        boolean isOldActionContainer = isElementPresent(parent, By.cssSelector(WINDOW_TOOLBAR_CSS));
        if (isNewActionContainer) {
            return ActionsContainer.createFromParent(parent, driver, webDriverWait);
        } else if (isOldActionContainer)
            return OldActionsContainer.createFromParent(driver, webDriverWait, parent);
        else {
            return ButtonContainer.createFromParent(parent, driver, webDriverWait);
        }
    }

    private boolean isElementPresent(WebElement webElement, By by) {
        return WebElementUtils.isElementPresent(webElement, by);
    }

    private boolean isMorePresent() {
        DelayUtils.waitForNestedElements(webDriverWait, createTabs(), TABS_CONTAINER_XPATH);
        List<WebElement> isMore = createTabs()
                .findElement(By.cssSelector(TABS_HEADER_CSS))
                .findElements(By.xpath(DROPDOWN_TAB_XPATH));
        return !isMore.isEmpty();
    }

    private DragAndDrop.DraggableElement getDraggableElement(String tabLabel) {
        WebElement tab = getTabByLabel(tabLabel);
        Actions action = new Actions(driver);
        action.moveToElement(tab).perform();
        return new DragAndDrop.DraggableElement(tab.findElement(By.cssSelector(DRAG_CSS)));
    }

    private DragAndDrop.DropElement getDropElement(int position) {
        WebElement target = createTabs().findElements(By.cssSelector(DRAG_CSS)).get(position);
        return new DragAndDrop.DropElement(target);
    }

    private WebElement getTabByLabel(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait,
                String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel));
        return createTabs().findElement(By
                .xpath(String.format(TAB_BY_LABEL_PATTERN, tabLabel, tabLabel))).findElement(By.xpath(ANCESTOR_TO_TAB_CONTAINER_XPATH));
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
            clickMoreTab();
            DelayUtils.waitByXPath(webDriverWait, xPathForTab);
            return driver.findElement(By.xpath(String.format(ANCESTOR_PATTERN, xPathForTab)));
        } else {
            return createTabs().findElement(By.xpath(String.format(ANCESTOR_PATTERN, xPathForTab)));
        }
    }

    private void clickMoreTab() {
        if (!WebElementUtils.isElementPresent(driver, By.cssSelector(TAB_MORE_DROPDOWN_CSS))) {
            WebElement moreTab = createTabs().findElement(By.xpath(DROPDOWN_TAB_XPATH));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(moreTab));
            moreTab.click();
        }
        DelayUtils.waitForPresence(webDriverWait, By.cssSelector(TAB_MORE_DROPDOWN_CSS));
    }
}
