package ui;

import core.DamagesManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import models.Product;

public class ScreenRenderer {
    private static GameState state;
    private static UIState uiState;
    private static InventoryManager manager;
    private static EventBuffer eventBuffer;
    private static DamagesManager damagesManager;

    public ScreenRenderer(GameState state, UIState uiState, InventoryManager manager, EventBuffer eventBuffer, DamagesManager damagesManager) {
    ScreenRenderer.state = state;
    ScreenRenderer.uiState = uiState;
    ScreenRenderer.manager = manager;
    ScreenRenderer.eventBuffer = eventBuffer;
    ScreenRenderer.damagesManager = damagesManager;
    }


    public static void renderFrame() {
        buildHeader();
        buildSummaryPanel();
        buildActivePanel();
        buildRecentEventsPanel();
        buildPromptLine();
    }

    public static void buildHeader() {
    StringBuilder header = new StringBuilder();
    header.append("=".repeat(57));
    header.append("\n");
    header.append(Printer.centerPad("FLATPACK EMPIRE - STORE DASHBOARD - DAY "  + state.getCurrentDay() + " - " + GameClock.getCurrentTime(true), 62));
    header.append("\n");
    header.append("=".repeat(57));
    header.append("\n");
    System.out.println(header.toString());
    }

    public static void buildSummaryPanel() {
        StringBuilder summaryPanel = new StringBuilder();
        summaryPanel.append("\uD83D\uDCB0 Balance: $" + state.getCURRENT_BALANCE() + " ".repeat(14 - state.getCURRENT_BALANCE().toString().length()) + "| \uD83D\uDCE6 Inventory: " + manager.getInventorySnapshot().size() + " items");
        summaryPanel.append("\n");
        summaryPanel.append("🚚 Truck Tier " + state.getCurrentTruckTier() + " (" + state.getTruckStatus() + ")" + " ".repeat(9 - state.getTruckStatus().length()));
        summaryPanel.append("| \uD83D\uDD28 Damages: " + damagesManager.size() );
        summaryPanel.append("\n");
        summaryPanel.append("\uD83D\uDED2 Customers in Store: " + state.getCurrentCustomers());
        summaryPanel.append(" ".repeat(4 - String.valueOf(state.getCurrentCustomers()).length()) + "| \uD83E\uDDD1\u200D\uD83D\uDCBC Checkout Lanes Open: 1/5");
        summaryPanel.append("\n");
        summaryPanel.append("-".repeat(57));
        summaryPanel.append("\n");
        System.out.println(summaryPanel.toString());
    }

    public static void buildActivePanel() {
        StringBuilder activePanel = new StringBuilder();
        ActiveViewContext activeView = uiState.getActiveViewContext();
        if (activeView != null) {
            if (activeView.getViewMode() == ViewMode.INVENTORY_VIEW) {
                buildInventoryPanel(activePanel, activeView);
            }
        }  else {
            activePanel.append("WIP");
        }
        activePanel.append("\n");
        activePanel.append("-".repeat(57));
        System.out.println(activePanel.toString());
    }

    public static void buildRecentEventsPanel() {
        StringBuilder eventPanel = new StringBuilder();
        eventBuffer.updateRecentEventsPanel();
        eventPanel.append("[INTERNAL MEMO - WAREHOUSE DIVISION]");
        eventPanel.append("\n");
        eventPanel.append(eventBuffer.getEventOne());
        eventPanel.append("\n");
        eventPanel.append(eventBuffer.getEventTwo());
        eventPanel.append("\n");
        eventPanel.append(eventBuffer.getEventThree());
        eventPanel.append("\n");
        eventPanel.append("Current Mood of Store: WIP");
        System.out.println(eventPanel.toString());
    }

    public static void buildPromptLine() {
        StringBuilder promptPanel = new StringBuilder();
        promptPanel.append("=".repeat(57));
        promptPanel.append("\n");
        promptPanel.append("Enter Command > ");
        System.out.println(promptPanel.toString());

    }

    public static void buildInventoryPanel(StringBuilder activePanel, ActiveViewContext activeViewContext) {
        Product p = manager.lookupProduct(activeViewContext.getSelectedProductSku());
        activePanel.append("ACTIVE VIEW: PRODUCT INFO - " + p.getProduct() + " " + p.getType() + " (" + p.getColor() +")");
        activePanel.append("\n");
        activePanel.append(p.getDescription());
        activePanel.append("\n");
        activePanel.append("\n");
        activePanel.append("Color: " + p.getColor());
        activePanel.append("\n");
        activePanel.append("Cost: $" + p.getPrice());
        activePanel.append("\n");
        activePanel.append("Description: " + p.getDescription());
    }




}
