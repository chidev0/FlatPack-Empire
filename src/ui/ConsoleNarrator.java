package ui;

public class ConsoleNarrator {
    public static String WelcomeMessageLine1 = ConsoleStyle.BLUE + "Welcome to FlatPack Empire - V4.0 Pre-Alpha" + ConsoleStyle.RESET;
    public static String WelcomeMessageLine2 = "The warehouse is yours. Try not to set it on fire.";

    public static void bootSequence() {
        Printer.typeThenPause(ConsoleNarrator.WelcomeMessageLine1, 40, 500);
        Printer.typeThenPause(WelcomeMessageLine2, 40, 500);
        Printer.pressEnterToContinue();
    }
}
