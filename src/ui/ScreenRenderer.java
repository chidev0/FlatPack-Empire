package ui;

import core.CheckoutManager;
import core.DamagesManager;
import core.DayManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import models.CheckoutLane;
import models.CheckoutLaneSnapshot;
import models.Product;
import products.ProductCatalog;

public class ScreenRenderer {
    private GameState state;
    private UIState uiState;
    private InventoryManager manager;
    private EventBuffer eventBuffer;
    private DamagesManager damagesManager;
    private InputListener inputListener;
    private CheckoutManager checkoutManager;
    private DayManager dayManager;

    public ScreenRenderer(GameState state, UIState uiState, InventoryManager manager, EventBuffer eventBuffer, DamagesManager damagesManager, InputListener inputListener, CheckoutManager checkoutManager, DayManager dayManager) {
        this.state = state;
        this.uiState = uiState;
        this.manager = manager;
        this.eventBuffer = eventBuffer;
        this.damagesManager = damagesManager;
        this.inputListener = inputListener;
        this.checkoutManager = checkoutManager;
        this.dayManager = dayManager;
    }


    public void renderFrame() {
        buildHeader();
        buildSummaryPanel();
        buildActivePanel();
        buildRecentEventsPanel();
        buildPromptLine();
    }

    public void buildHeader() {
        StringBuilder header = new StringBuilder();
        header.append("=".repeat(57));
        header.append("\n");
        header.append(Printer.centerPad("FLATPACK EMPIRE - STORE DASHBOARD - DAY " + state.getCurrentDay() + " - " + GameClock.getCurrentTime(true), 62));
        header.append("\n");
        header.append("=".repeat(57));
        header.append("\n");
        System.out.println(header.toString());
    }

    public void buildSummaryPanel() {
        StringBuilder summaryPanel = new StringBuilder();
        summaryPanel.append("\uD83D\uDCB0 Balance: $" + state.getCURRENT_BALANCE() + " ".repeat(14 - state.getCURRENT_BALANCE().toString().length()) + "| \uD83D\uDCE6 Inventory: " + manager.getInventorySnapshot().size() + " items");
        summaryPanel.append("\n");
        summaryPanel.append("🚚 Truck Tier " + state.getCurrentTruckTier() + " (" + state.getTruckStatus() + ")" + " ".repeat(9 - state.getTruckStatus().length()));
        summaryPanel.append("| \uD83D\uDD28 Damages: " + damagesManager.size());
        summaryPanel.append("\n");
        summaryPanel.append("\uD83D\uDED2 Customers in Store: " + dayManager.getCurrentDay().getCustomersInStore());
        summaryPanel.append(" ".repeat(4 - String.valueOf(state.getCurrentCustomers()).length()) + "| \uD83E\uDDD1\u200D\uD83D\uDCBC Checkout Lanes Open: 1/5");
        summaryPanel.append("\n");
        summaryPanel.append("-".repeat(57));
        summaryPanel.append("\n");
        System.out.println(summaryPanel.toString());
    }

    public void buildActivePanel() {
        StringBuilder activePanel = new StringBuilder();
        ActiveViewContext activeView = uiState.getActiveViewContext();
        if (activeView != null) {
            if (activeView.getViewMode() == ViewMode.INVENTORY_VIEW) {
                buildInventoryPanel(activePanel, activeView);
            } else if (activeView.getViewMode() == ViewMode.LANE_VIEW) {
                buildLanePanel(activePanel, activeView);
            }
        } else {
            activePanel.append("WIP");
        }
        activePanel.append("\n");
        activePanel.append("-".repeat(57));
        System.out.println(activePanel.toString());
    }

    public void buildRecentEventsPanel() {
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

    public void buildPromptLine() {
        StringBuilder promptPanel = new StringBuilder();
        promptPanel.append("=".repeat(57));
        promptPanel.append("\n");
        promptPanel.append("Enter Command > ");
        if (!inputListener.getCurrentInput().isEmpty()) promptPanel.append(inputListener.getCurrentInput().toString());
        System.out.println(promptPanel.toString());

    }

    public static void buildInventoryPanel(StringBuilder activePanel, ActiveViewContext activeViewContext) {
        Product p = ProductCatalog.productLookup(activeViewContext.getSelectedProductSku());
        activePanel.append("ACTIVE VIEW: PRODUCT INFO - " + p.getProduct() + " " + p.getType() + " (" + p.getColor() + ")");
        activePanel.append("\n");
        activePanel.append(p.getDescription());
        activePanel.append("\n");
        activePanel.append("\n");
        activePanel.append("Color: " + p.getColor());
        activePanel.append("\n");
        activePanel.append("Cost: $" + p.getPrice());
        activePanel.append("\n");
    }

    public void buildLanePanel(StringBuilder activePanel, ActiveViewContext activeViewContext) {
        CheckoutLaneSnapshot laneSnapshot = checkoutManager.buildLaneSnapshot(activeViewContext.getSelectedLaneSnapshot().getLaneNumber());
        activePanel.append("ACTIVE VIEW: LANE VIEW (" + laneSnapshot.getLaneNumber() + ")");
        activePanel.append("\n");
        if (!laneSnapshot.isLaneProcessing()) {
            activePanel.append("Lane is currently empty");
        } else {
            activePanel.append("Processing customer");
            activePanel.append("\n" + laneSnapshot.getItemsProcessed() + " / " + laneSnapshot.getItemsRemaining());
            activePanel.append("Current total: $" + laneSnapshot.getTransactionTotal());
            activePanel.append("Ticks until Next Scan: " + laneSnapshot.getTicksUntilNextScan());
        }


    }
}
