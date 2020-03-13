package com.oss;

import com.oss.framework.widgets.treewidget.InlineMenu;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.platform.HierarchyViewPage;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class HierarchyViewTest extends BaseTestCase {

    private static final String TREE_WIDGET_URL = String.format("%s/#/views/management/views/hierarchy-view/PhysicalDevice?perspective=LIVE",BASIC_URL);
    private HierarchyViewPage hierarchyViewPage;
    SoftAssert soft = new SoftAssert();

    @BeforeClass
    public void goToHierarchyViewPage() {hierarchyViewPage = homePage.goToHierarchyViewPage(TREE_WIDGET_URL);}

    @Test
    public void selectFirstNode() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget()
                .selectNode();
        Assertions.assertThat(treeWidget.isNodeSelected()).isTrue();
    }

    @Test
    public void expandNode() {
        TreeWidget treeWidget = hierarchyViewPage.getTreeWidget();
        int primaryExpNodesCount = treeWidget.getNodesWithExpandState("expanded").size();
        treeWidget.expandNode();
        int finalExpNodesCount = treeWidget.getNodesWithExpandState("expanded").size();
        Assertions.assertThat(primaryExpNodesCount).isEqualTo(finalExpNodesCount - 1);
    }

    //TODO: Below test need correction. Sth is wrong with getDescendantNodesWithExpandState("expanded") should return 3 elements instead of 19
    // finalExpNodesCount returns no such element exception
    @Test
    public void expandRootNodeWithAllDescendants() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget();
        int primaryExpNodesCount = treeWidget.getNodesWithExpandState("expanded").size();
        System.out.println(primaryExpNodesCount);
        treeWidget.selectExpandAllIcon();
        int expRootWithExpDescendantsCount = (treeWidget.getDescendantNodesWithExpandState("expanded").size())+1;
        System.out.println(expRootWithExpDescendantsCount);
        int finalExpNodesCount = treeWidget.getNodesWithExpandState("expanded").size();
        Assertions.assertThat(primaryExpNodesCount).isEqualTo(finalExpNodesCount-expRootWithExpDescendantsCount);
    }

    @Test
    public void selectRootNodeWithAllDescendants() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget()
                .selectExpandAllIcon()
                .selectRootCheckbox();
        Assert.assertTrue(treeWidget.areNodesSelected(), "Not all nodes are selected");
    }

    @Test
    public void displayInlineActions() {
        InlineMenu inlineMenu = hierarchyViewPage
                .getTreeWidget()
                .selectInlineActionsBtn();
        Assert.assertTrue(inlineMenu.isActionListDisplayed(), "Inline actions are not displayed");
    }

    @Test
    public void checkAvailableActionsForSelectedNode() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget()
                .selectNode();
        soft.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("EDIT"), "Edit button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("NAVIGATION"), "Navigation button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("OTHER"), "Other button is not displayed");
        soft.assertAll();
    }

    @Test
    public void checkAvailableActionsInDefaultView() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget();
        soft.assertTrue(treeWidget.isActionDisplayed("CREATE"), "Create button is not displayed");
        soft.assertTrue(treeWidget.isActionDisplayed("frameworkCustomButtonsGroup"), "Hamburger menu is not displayed");
        soft.assertAll();
    }

    @Test
    public void searchWithNotExistingData() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget()
                .performSearch("hjfkahfdadf");
        Assert.assertTrue(treeWidget.isTreeWidgetEmpty(), "Tree widget is not empty");
    }

    @Test
    public void searchWithExistingData() {
        TreeWidget treeWidget = hierarchyViewPage
                .getTreeWidget();
        String searchInput = treeWidget.getFirstNodeLabel();
                treeWidget.performSearch(searchInput);
        Assert.assertTrue(treeWidget.isSearchingCorrect(searchInput), "Searching is not correct");

    }
}
