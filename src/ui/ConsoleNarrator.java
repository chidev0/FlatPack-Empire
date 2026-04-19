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
    private static ScreenRenderer screenRenderer;

    public ConsoleNarrator(GameState state, InventoryManager manager, DamagesManager damagesManager, ScreenRenderer screenRenderer) {
        ConsoleNarrator.state = state;
        ConsoleNarrator.manager = manager;
        ConsoleNarrator.damagesManager = damagesManager;
        ConsoleNarrator.screenRenderer = screenRenderer;

    }


    public static String WelcomeMessageLine1 = ConsoleStyle.BLUE + "Welcome to FlatPack Empire - V4.0 Pre-Alpha" + ConsoleStyle.RESET;
    public static String WelcomeMessageLine2 = "The warehouse is yours. Try not to set it on " + ConsoleStyle.RED + ConsoleStyle.BOLD + "FIRE" + ConsoleStyle.RESET;

    public static void bootSequence() {
        Printer.clearConsole();
        Printer.typeThenPause(ConsoleNarrator.WelcomeMessageLine1, 40, 500);
        Printer.typeThenPause(WelcomeMessageLine2, 40, 500);
    }


    public static void updateDashboard(){
        dashboard.replace(294,294, "" + state.getCurrentCustomers());
    }

    public void tick(GameState state){
        Printer.clearConsole();
        screenRenderer.renderFrame();
    }
}
