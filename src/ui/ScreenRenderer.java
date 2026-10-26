package ui;

import core.CheckoutManager;
import core.DamagesManager;
import core.DayManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import engine.TaskScheduler;
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
    private TaskScheduler taskScheduler;
    private StringBuilder frame;

    public ScreenRenderer(GameState state, UIState uiState, InventoryManager manager, EventBuffer eventBuffer, DamagesManager damagesManager, InputListener inputListener, CheckoutManager checkoutManager, DayManager dayManager, TaskScheduler taskScheduler) {
        this.state = state;
        this.uiState = uiState;
        this.manager = manager;
        this.eventBuffer = eventBuffer;
        this.damagesManager = damagesManager;
        this.inputListener = inputListener;
        this.checkoutManager = checkoutManager;
        this.dayManager = dayManager;
        this.taskScheduler = taskScheduler;
    }


    public void renderFrame() {
        frame = new StringBuilder();
        buildHeader(frame);
        buildSummaryPanel(frame);
        buildActivePanel(frame);
        buildRecentEventsPanel(frame);
        buildPromptLine(frame);
        String body = frame.toString().replace("\n", "\033[K\n");

        System.out.print("\033[H" + body + "\033[J");
        System.out.flush();
    }

    public void buildHeader(StringBuilder frame) {
        frame.append("=".repeat(57));
        frame.append("\n");
        frame.append(Printer.centerPad("FLATPACK EMPIRE - STORE DASHBOARD - DAY " + state.getCurrentDay() + " - " + GameClock.getCurrentTime(true), 62));
        frame.append("\n");
        frame.append("=".repeat(57));
        frame.append("\n");
        frame.append("\n");
    }

    public void buildSummaryPanel(StringBuilder frame) {
        frame.append("\uD83D\uDCB0 Balance: $" + state.getCURRENT_BALANCE() + " ".repeat(14 - state.getCURRENT_BALANCE().toString().length()) + "| \uD83D\uDCE6 Inventory: " + manager.getInventorySnapshot().size() + " items");
        frame.append("\n");
        frame.append("🚚 Truck Tier " + state.getCurrentTruckTier() + " (" + state.getTruckStatus() + ")" + " ".repeat(9 - state.getTruckStatus().length()));
        frame.append("| \uD83D\uDD28 Tasks:   " + taskScheduler.totalScheduled());
        frame.append("\n");
        frame.append("\uD83D\uDED2 Customers in Store: " + dayManager.getCurrentDay().getCustomersInStore());
        frame.append(" ".repeat(4 - String.valueOf(state.getCurrentCustomers()).length()) + "| \uD83E\uDDD1\u200D\uD83D\uDCBC Checkout Lanes Open: 1/5");
        frame.append("\n");
        frame.append("-".repeat(57));
        frame.append("\n");
        frame.append("\n");
    }

    public void buildActivePanel(StringBuilder frame) {
        ActiveViewContext activeView = uiState.getActiveViewContext();
        if (activeView != null) {
            if (activeView.getViewMode() == ViewMode.INVENTORY_VIEW) {
                buildInventoryPanel(frame, activeView);
            } else if (activeView.getViewMode() == ViewMode.LANE_VIEW) {
                buildLanePanel(frame, activeView);
            }
        } else {
            frame.append("WIP");
        }
        frame.append("\n");
        frame.append("-".repeat(57));
        frame.append("\n");
    }

    public void buildRecentEventsPanel(StringBuilder frame) {
        eventBuffer.updateRecentEventsPanel();
        frame.append("[INTERNAL MEMO - WAREHOUSE DIVISION]");
        frame.append("\n");
        frame.append(eventBuffer.getEventOne());
        frame.append("\n");
        frame.append(eventBuffer.getEventTwo());
        frame.append("\n");
        frame.append(eventBuffer.getEventThree());
        frame.append("\n");
        frame.append("Current Mood of Store: WIP");
        frame.append("\n");
    }

    public void buildPromptLine(StringBuilder frame) {
        frame.append("=".repeat(57));
        frame.append("\n");
        frame.append("Enter Command > ");
        frame.append(inputListener.getCurrentInput());

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
            activePanel.append("\nCurrent total: $" + laneSnapshot.getTransactionTotal());
            activePanel.append("\n Ticks until Next Scan: " + laneSnapshot.getTicksUntilNextScan());
        }


    }
}
