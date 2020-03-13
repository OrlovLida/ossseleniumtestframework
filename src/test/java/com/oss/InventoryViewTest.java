package com.oss;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Multimap;
import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.ColumnsManagement;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tablewidget.TableWidget.Row;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.platform.InventoryViewPage;

public class InventoryViewTest extends BaseTestCase {

    private static final String TABLE_WIDGET_URL = String.format("%s/#/views/management/views/inventory-view/Location" +
            "?perspective=LIVE", BASIC_URL);
    private InventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = homePage.goToInventoryViewPage(TABLE_WIDGET_URL);
    }

    @Test
    public void searchByType() {
        //given
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        //when
        tableWidget.setFilterContains("type", ComponentType.COMBOBOX, "PoP");
        tableWidget.confirmFilter();
        Multimap<String,String> filterValues = tableWidget.getAppliedFilters();

        //then
        Assertions.assertThat(filterValues.keys()).hasSize(1);
        Assertions.assertThat(Lists.newArrayList(filterValues.get("Type")).get(0)).startsWith("PoP");
    }

    @Test
    public void selectFirstRow() {
        //given
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        //when
        tableWidget.selectFirstRow();
        List<Row> selectedRows = tableWidget.getSelectedVisibleRows();

        //then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isEqualTo(1);
    }

    @Test
    public void clickOnCreate() {
        //given
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        //when
        tableWidget.callAction("CREATE", "CreateLocationWizardAction");

        //then
        //TODO: add assertion
    }

    @Test
    public void editRow() {
        //given
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        //when
        tableWidget.selectFirstRow();
        tableWidget.callAction("EDIT", "UpdateLocationWizardAction");

        //then
        //TODO: add assertions
    }

    @Test
    public void assignIpAddress() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        tableWidget.selectFirstRow();
        tableWidget.callAction("CREATE", "AssignIPv6SubnetToLocation");

        //TODO: add assertions

    }

    @Test
    public void clickNavigation() {
        //given
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        //when
        tableWidget.selectFirstRow();
        tableWidget.callAction("NAVIGATION", "OpenInventoryView");

        //then
        //TODO: add assertions
    }

    @Test
    public void resizeColumn() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        Assertions.assertThat(defaultSize).isEqualTo(200);

        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

        int newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
    }

    @Test
    public void removeSecondColumn() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstHeader = tableWidget.getFirstColumnLabel();
        String secondHeader = tableWidget.getActiveColumnLabel(1);
        String thirdHeader = tableWidget.getActiveColumnLabel(2);

        columnsManagement.toggleColumn(secondHeader);
        columnsManagement.clickApply();

        String newFirstHeader = tableWidget.getFirstColumnLabel();
        String newSecondHeader = tableWidget.getActiveColumnLabel(1);

        Assert.assertEquals(firstHeader, newFirstHeader);
        Assert.assertEquals(thirdHeader, newSecondHeader);
    }

    @Test
    public void addFirstUnselectedColumn() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstUnselectedColumnName = columnsManagement.getFirstUnselectedColumnLabel();
        Assertions.assertThat(tableWidget.getActiveColumns()).doesNotContain(firstUnselectedColumnName);

        columnsManagement.toggleColumn(firstUnselectedColumnName);
        columnsManagement.clickApply();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.scrollHorizontally(1000);
        List<String> columnLabels = tableWidget.getActiveColumns();
        Assert.assertEquals(columnLabels.get(columnLabels.size() - 1), firstUnselectedColumnName);
    }

    @Test
    public void expandLabel() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstRowID = tableWidget.getTableCells().get(0).getText();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.resizeFirstColumn(-145);
        tableWidget.clickOnFirstExpander();
        Assert.assertEquals(firstRowID, tableWidget.getExpandedText());
    }

    @Test
    public void changeColumnsOrder() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        String firstHeader = tableWidget.getFirstColumnLabel();
        String secondHeader = tableWidget.getActiveColumnLabel(1);

        columnsManagement.changeColumnPosition("ID", -120, 0);
        columnsManagement.clickApply();

        String newFirstHeader = tableWidget.getFirstColumnLabel();
        String newSecondHeader = tableWidget.getActiveColumnLabel(1);
        Assert.assertEquals(firstHeader, newSecondHeader);
        Assert.assertEquals(secondHeader, newFirstHeader);
    }

    @Test
    public void checkDefaultSettingsOfColumnsManagement() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String defaultFirstLabel = columnsManagement.getColumnLabels().get(0);
        String defaultSecondLabel = columnsManagement.getColumnLabels().get(1);
        boolean defaultModelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        boolean defaultDescriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        columnsManagement.toggleColumn("Model ID");
        columnsManagement.toggleColumn("Description");
        boolean modelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        boolean descriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        Assert.assertEquals(defaultModelIDChbxStatus, !modelIDChbxStatus);
        Assert.assertEquals(defaultDescriptionChbxStatus, !descriptionChbxStatus);

        columnsManagement.changeColumnPosition("ID", -120, 0);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstLabel = columnsManagement.getColumnLabels().get(0);
        String secondLabel = columnsManagement.getColumnLabels().get(1);
        Assert.assertEquals(firstLabel, defaultSecondLabel);
        Assert.assertEquals(secondLabel, defaultFirstLabel);

        columnsManagement.clickApply();
        String firstHeader = tableWidget.getActiveColumnLabel(0);
        Assert.assertEquals(firstHeader, defaultSecondLabel);

        columnsManagement = tableWidget.getColumnsManagement();
        columnsManagement.clickDefaultSettings();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        firstHeader = tableWidget.getActiveColumnLabel(0);
        Assert.assertEquals(firstHeader, defaultFirstLabel);

        columnsManagement=tableWidget.getColumnsManagement();
        firstLabel = columnsManagement.getColumnLabels().get(0);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        secondLabel = columnsManagement.getColumnLabels().get(1);
        modelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        descriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        Assert.assertEquals(firstLabel, defaultFirstLabel);
        Assert.assertEquals(secondLabel, defaultSecondLabel);
        Assert.assertEquals(defaultModelIDChbxStatus, modelIDChbxStatus);
        Assert.assertEquals(defaultDescriptionChbxStatus, descriptionChbxStatus);
    }

    @Test
    public void checkPagination() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        Assert.assertEquals(tableWidget.isFirstPageActive(), true);
        Assert.assertEquals(tableWidget.isPreviousPageClicable(), false);
        if (tableWidget.isMoreThanOnePage()) {
            tableWidget.clickNextPage();
            DelayUtils.sleep(500);
            Assert.assertEquals(tableWidget.getLabelOfActivePageBtn(), "2");
            tableWidget.clickPreviousPage();
            DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
            Assert.assertEquals(tableWidget.isFirstPageActive(), true);
            tableWidget.clickLastPage();
        }
        Assert.assertEquals(tableWidget.isNextPageClicable(), false);
    }

    @Test
    public void checkHorizontalScroller() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.scrollHorizontally(1000);
        List<String> headers = tableWidget.getActiveColumns();
        Assert.assertEquals(headers.get(headers.size() - 1), "Calculated Parents");
        tableWidget.scrollHorizontally(-1000);
        headers = tableWidget.getActiveColumns();
        Assert.assertEquals(headers.get(0), "ID");
    }

    @Test
    public void checkVerticalScroller() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.scrollVertically(1000);
        String lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
        String lastIdOnThePage = tableWidget.getValueFromRowWithID("ID", lastVisibleID);
        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), true);
        tableWidget.scrollVertically(-1000);
        lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), false);
    }

    @Test
    public void findByText() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String secondID = tableWidget.getValueFromNthRow("ID", "2");
        tableWidget.typeIntoSearch(secondID);
        DelayUtils.sleep(500);
        String newFirstID = tableWidget.getValueFromNthRow("ID", "1");
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(tableWidget.getNumberOfAllRowsInTable(), "1");
    }

    @Test
    public void clickExport() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(750);
        String numberOfExports = homePage.getNumberOfNotifications();
        tableWidget.clickOnKebabMenu();
        tableWidget.clickOnAction("Export");
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String newNumberOfExports = homePage.getNumberOfNotifications();
        Assert.assertEquals(Integer.valueOf(newNumberOfExports), Integer.valueOf(1 + Integer.valueOf(numberOfExports)));
    }

    @Test
    public void changeTab() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();

        tableWidget.selectFirstRow();
        TabsWidget tabsWidget = inventoryViewPage.getTabsWidget();

        String activeTabLabel = tabsWidget.getActiveTabLabel();
        String secondTabLabel = tabsWidget.getTabLabel(1);
        Assertions.assertThat(activeTabLabel).isNotEqualTo(secondTabLabel);

        tabsWidget.clickOnTab(secondTabLabel);
        String newActiveLabel = tabsWidget.getActiveTabLabel();
        Assertions.assertThat(newActiveLabel).isEqualTo(secondTabLabel);
    }

    @Test
    public void clickRefresh() {
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        DelayUtils.sleep(500);
        tableWidget.clickOnKebabMenu();
        tableWidget.clickOnAction("Refresh");
        Assert.assertTrue(inventoryViewPage.isLoadBarDisplayed());
    }

    @Test
    public void wizardTest() {
        //TODO: problem with click Next and then Cancel button
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
//        tableWidget.takeAction("Create", "Location");
        inventoryViewPage.waitForComponent("//div[contains(@id,'CREATE')]");
        driver.findElement(By.id("CREATE")).click();
        inventoryViewPage.waitForComponent("(//a[contains(text(),'Create Location')])[1]");
        driver.findElement(By.xpath("(//a[contains(text(),'Create Location')])[1]")).click();
        DelayUtils.sleep(600);
        Wizard locationWizard = inventoryViewPage.getWizard();
        Input type = locationWizard.getComponent("type", Input.ComponentType.COMBOBOX);
        type.setSingleStringValue("Building");
        DelayUtils.sleep(1000);
        locationWizard.clickNext();
        DelayUtils.sleep(2000);
        locationWizard.cancel(); //something is NO YES
        DelayUtils.sleep(4000);
    }

    @Test
    public void changeTabAndSelectFirstRow() { //
        //TODO: uwzględnić notatki z AdvancedSearch.java, nie działa dopóki nie bedzei widgetID - wtedy poprawiać
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        tableWidget.selectFirstRow();

        TabsWidget tabsWidget = inventoryViewPage.getTabsWidget();
        tabsWidget.clickOnTab("Locations");

        DelayUtils.sleep(500);
//        tabsWidget
//                .getCurrentTabTable()
//                .clickFirstRow();
        DelayUtils.sleep(50);
    }

    @Test
    public void checkDisplayingOfPropertyValue(){
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        String idNumberFromTableWidget = tableWidget.getValueFromNthRow("ID", "1");
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue("id");
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);
    }

    @Test
    public void setProperties(){
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        PropertiesFilter propertiesFilter = inventoryViewPage.getPropertiesFilter();
        String defaultFirstPropertyLabel = propertyPanel.getNthPropertyLabel(1);
        String defaultSecondPropertyLabel = propertyPanel.getNthPropertyLabel(2);
        propertiesFilter.clickOnFilterIcon();
        boolean defaultChbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        propertiesFilter.clickCheckbox(defaultFirstPropertyLabel);
        boolean chbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        Assert.assertEquals(defaultChbxStatusForFirstLabel, !chbxStatusForFirstLabel);
        propertiesFilter.clickOnSave();
        String firstLabel = propertyPanel.getNthPropertyLabel(1);
        Assert.assertEquals(firstLabel, defaultSecondPropertyLabel);
        propertiesFilter.clickOnFilterIcon();
        propertiesFilter.clickCheckbox(defaultFirstPropertyLabel);
        chbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        Assert.assertEquals(defaultChbxStatusForFirstLabel, chbxStatusForFirstLabel);
        propertiesFilter.clickOnSave();
        firstLabel = propertyPanel.getNthPropertyLabel(1);
        Assert.assertEquals(firstLabel, defaultFirstPropertyLabel);
    }

    @Test
    public void searchProperty(){
        TableWidget tableWidget = inventoryViewPage.getTableWidget();
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        PropertiesFilter propertiesFilter = inventoryViewPage.getPropertiesFilter();
        String defaultFirstPropertyLabel = propertyPanel.getNthPropertyLabel(1);
        String defaultSecondPropertyLabel = propertyPanel.getNthPropertyLabel(2);
        propertiesFilter.clickOnFilterIcon();
        String defaultFirstPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(1);
        String defaultSecondPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(2);
        Assert.assertEquals(defaultFirstPropertyLabel,defaultFirstPropertyLabelOnPopup);
        Assert.assertEquals(defaultSecondPropertyLabel,defaultSecondPropertyLabelOnPopup);
        propertiesFilter.typeIntoSearch(defaultSecondPropertyLabelOnPopup);
        String firstPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(1);
        Assert.assertEquals(firstPropertyLabelOnPopup, defaultSecondPropertyLabelOnPopup);
    }
}
