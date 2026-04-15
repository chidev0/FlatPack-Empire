package ui;

import core.DamagesManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import engine.Tickable;

public class ConsoleNarrator implements Tickable {
    private static GameState state;
    private static InventoryManager manager;
    private static DamagesManager damagesManager;
    private static StringBuilder dashboard = new StringBuilder();

    public ConsoleNarrator(GameState state, InventoryManager manager, DamagesManager damagesManager) {
        ConsoleNarrator.state = state;
        ConsoleNarrator.manager = manager;
        ConsoleNarrator.damagesManager = damagesManager;

    }


    public static String WelcomeMessageLine1 = ConsoleStyle.BLUE + "Welcome to FlatPack Empire - V4.0 Pre-Alpha" + ConsoleStyle.RESET;
    public static String WelcomeMessageLine2 = "The warehouse is yours. Try not to set it on fire.";

    public static void bootSequence() {
        Printer.typeThenPause(ConsoleNarrator.WelcomeMessageLine1, 40, 500);
        Printer.typeThenPause(WelcomeMessageLine2, 40, 500);
        Printer.pressEnterToContinue();
    }

    // Old dashboard, being replaced by ScreenRenderer class
    public static void renderDashboard() {
        dashboard.append("=".repeat(57));
        dashboard.append("\n");
        dashboard.append(Printer.centerPad("FLATPACK EMPIRE - STORE DASHBOARD - DAY "  + state.getCurrentDay() + " - " + GameClock.getCurrentTime(), 62));
        dashboard.append("\n");
        dashboard.append("=".repeat(57));
        dashboard.append("\n");
        dashboard.append("\uD83D\uDCB0 Balance: $" + state.getCURRENT_BALANCE() + "      | \uD83D\uDCE6 Inventory: " + manager.getInventorySnapshot().size() + " items");
        dashboard.append("\n");
        dashboard.append("🚚 Truck Tier " + state.getCurrentTruckTier() + " (" + state.getTruckStatus() + ")   ");
        dashboard.append("| \uD83D\uDD28 Damages: " + damagesManager.size() );
        dashboard.append("\n");
        dashboard.append("\uD83D\uDED2 Customers in Store: " + state.getCurrentCustomers());
        dashboard.append("  | \uD83E\uDDD1\u200D\uD83D\uDCBC Checkout Lanes Open: 1/5");
        dashboard.append("\n");
        dashboard.append("-".repeat(57));
        dashboard.append("\n");
        dashboard.append("[INTERNAL MEMO - WAREHOUSE DIVISION]");
        dashboard.append("\n");
        dashboard.append("\n");
        dashboard.append(" > " +GameClock.getCurrentTime() + " - Another peaceful morning. No one has cried yet.");
        System.out.println(dashboard.toString());
        System.out.println("Position of customer: " + dashboard.indexOf("Store: "));
    }


    public static void updateDashboard(){
        dashboard.replace(294,294, "" + state.getCurrentCustomers());
    }

    public void tick(GameState state){
        Printer.clearConsole();
        dashboard.setLength(0);
        renderDashboard();
        updateDashboard();
    }
}
