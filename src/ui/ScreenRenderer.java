package ui;

import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import models.Product;

public class ScreenRenderer {
    private static GameState state;
    private static UIState uiState;
    private static InventoryManager manager;

    public ScreenRenderer(GameState state, UIState uiState, InventoryManager manager) {
    ScreenRenderer.state = state;
    ScreenRenderer.uiState = uiState;
    ScreenRenderer.manager = manager;
    }


    public static void renderFrame() {
        buildHeader();

    }

    public static void buildHeader() {
    StringBuilder header = new StringBuilder();
    header.append("=".repeat(57));
    header.append("\n");
    header.append(Printer.centerPad("FLATPACK EMPIRE - STORE DASHBOARD - DAY "  + state.getCurrentDay() + " - " + GameClock.getCurrentTime(), 62));
    header.append("\n");
    header.append("=".repeat(57));
    header.append("\n");
    System.out.println(header.toString());
    }

    public static void buildSummaryPanel() {}

    public static void buildActivePanel() {
        StringBuilder activePanel = new StringBuilder();
        ActiveViewContext activeView = uiState.getActiveViewContext();
        if (activeView.getViewMode() == ViewMode.INVENTORY_VIEW) {
            buildInventoryPanel(activePanel, activeView);
        }
        System.out.println(activePanel.toString());
    }

    public static void buildRecentEventsPanel() {}

    public static void buildPromptLine() {}

    public static void buildInventoryPanel(StringBuilder activePanel, ActiveViewContext activeViewContext) {
        Product p = manager.lookupProduct(activeViewContext.getSelectedProductSku());
        activePanel.append("Product Lookup: " + p.getProduct());
        activePanel.append("\n");
        activePanel.append("Product Type: " + p.getType());
        activePanel.append("\n");
        activePanel.append("Color: " + p.getColor());
        activePanel.append("\n");
        activePanel.append("Cost: $" + p.getPrice());
        activePanel.append("\n");
        activePanel.append("Description: " + p.getDescription());
    }




}
