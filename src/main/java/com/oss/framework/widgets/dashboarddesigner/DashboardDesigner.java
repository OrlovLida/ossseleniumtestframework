package com.oss.framework.widgets.dashboarddesigner;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.Card;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class DashboardDesigner {

    private static final String DASHBOARD_DESIGNER_CSS = ".dashboard-designer__view-container";
    private static final String SIMPLE_CARD_CONTAINER_CSS = ".simple-card-container";
    private static final float GRID_PADDING = 32f;
    private static final int COLUMNS_NUMBER_IN_GRID = 24;
    private static final String GRID_CONTAINER = ".grid-container";
    private static final String DESIGN_WORKSPACE = ".react-grid-layout,.info-component";
    private static final float ROW_NUMBER_IN_GRID = 12f;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement designerElement;

    public DashboardDesigner(WebDriver driver, WebDriverWait wait, WebElement designerElement) {
        this.driver = driver;
        this.wait = wait;
        this.designerElement = designerElement;
    }

    public static DashboardDesigner create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPresence(wait, By.cssSelector(DASHBOARD_DESIGNER_CSS));
        WebElement designerElement = driver.findElement(By.cssSelector(DASHBOARD_DESIGNER_CSS));
        return new DashboardDesigner(driver, wait, designerElement);
    }

    public void callAction(String actionId) {
        ActionsContainer.createFromParent(designerElement, driver, wait).callActionById(actionId);
    }

    public List<Card> getCards() {
        DelayUtils.waitForNestedElements(wait, designerElement, By.cssSelector(DESIGN_WORKSPACE));
        List<WebElement> cards = designerElement.findElements(By.cssSelector(SIMPLE_CARD_CONTAINER_CSS));
        return cards.stream()
                .map(card -> card.getAttribute(CSSUtils.TEST_ID))
                .map(card -> Card.createCard(driver, wait, card))
                .collect(Collectors.toList());
    }

    /**
     * JavaDoc
     * xSize - number of columns (grid is divided on 24 columns)
     * ySize - number of rows (grid is divided on 12 rows)
     */
    public void resizeCard(Card card, int xSize, int ySize) {
        int offsetX = getOffsetX(card, xSize);
        int offsetY = getOffsetY(card, ySize);
        card.resizeCard(offsetX, offsetY);
    }

    public void changeCardOrder(Card card, int xOffset, int yOffset) {
        card.changeCardOrder(xOffset, yOffset);
    }

    private int getOffsetX(Card card, int xSize) {
        return calculateSizeX(xSize) - card.getWidthCard();
    }

    private int getFrameWidthSize() {
        return CSSUtils.getWidthValue(designerElement.findElement(By.cssSelector(GRID_CONTAINER)));
    }

    private int getFrameHeightSize() {
        return CSSUtils.getHeightValue(designerElement.findElement(By.cssSelector(GRID_CONTAINER)));
    }

    private int getOffsetY(Card card, int ySize) {
        return calculateSizeY(ySize) - card.getHeightCard();
    }

    private int calculateSizeY(int ySize) {
        float rowHeight = getFrameHeightSize() / ROW_NUMBER_IN_GRID;
        return (int) (rowHeight * ySize);
    }

    private int calculateSizeX(int xSize) {
        double grid = getFrameWidthSize() - GRID_PADDING;
        double column = (grid / COLUMNS_NUMBER_IN_GRID);
        return (int) Math.ceil(column * xSize);
    }

}
