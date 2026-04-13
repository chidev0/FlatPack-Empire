package ui;

import java.io.Console;
import java.util.Scanner;

public class Printer {
    private static final Scanner scanner = new Scanner(System.in);

    public static void type(String text, int speedMs) {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            System.out.flush();
            try { Thread.sleep(speedMs); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println();
    }

    public static void type(String text) { type(text, 40); }

    public static void type(String color, String text, int speedMs) {
        type(color + text + ConsoleStyle.RESET, speedMs);
    }

    public static void pause(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public static void typeThenPause(String text, int typingSpeed, int pauseAfter) {
        type(text, typingSpeed);
        pause(pauseAfter);
    }

    public static void progressBar(String label, int current, int total, int barWidth, String fillColor) {
        int filled = (int)((double) current / total * barWidth);
        int empty  = barWidth - filled;
        String bar = ConsoleStyle.BOLD + fillColor + "█".repeat(filled)
                + ConsoleStyle.DIM + ConsoleStyle.WHITE + "░".repeat(empty) + ConsoleStyle.RESET;
        int percent = (int)((double) current / total * 100);
        System.out.print(ConsoleStyle.CR + "  " + label + " [" + bar + "] "
                + ConsoleStyle.BRIGHT_WHITE + current + "/" + total
                + ConsoleStyle.DIM + " " + percent + "%" + ConsoleStyle.RESET + "   ");
        System.out.flush();
    }

    public static void progressBarDone() { System.out.println(); }

    public static void liveStatus(String label, String value, String valueColor) {
        System.out.print(ConsoleStyle.CR + "  " + ConsoleStyle.DIM + label + ": " + ConsoleStyle.RESET
                + valueColor + ConsoleStyle.BOLD + value + ConsoleStyle.RESET + "                ");
        System.out.flush();
    }

    public static void box(String title, String[] lines, int width, String borderColor) {
        String b = borderColor, r = ConsoleStyle.RESET;
        int inner = width - 2;
        System.out.println(b + ConsoleStyle.DTL + ConsoleStyle.DH.repeat(inner) + ConsoleStyle.DTR + r);
        System.out.println(b + ConsoleStyle.DV + r + centerPad(ConsoleStyle.BOLD + title + r, inner) + b + ConsoleStyle.DV + r);
        System.out.println(b + ConsoleStyle.LJ + ConsoleStyle.DH.repeat(inner) + ConsoleStyle.RJ + r);
        for (String line : lines)
            System.out.println(b + ConsoleStyle.V + r + " " + padRight(line, inner - 1) + b + ConsoleStyle.V + r);
        System.out.println(b + ConsoleStyle.BL + ConsoleStyle.H.repeat(inner) + ConsoleStyle.BR + r);
    }

    public static void simpleBox(String[] lines, int width, String borderColor) {
        String b = borderColor, r = ConsoleStyle.RESET;
        int inner = width - 2;
        System.out.println(b + ConsoleStyle.TL + ConsoleStyle.H.repeat(inner) + ConsoleStyle.TR + r);
        for (String line : lines)
            System.out.println(b + ConsoleStyle.V + r + " " + padRight(line, inner - 1) + b + ConsoleStyle.V + r);
        System.out.println(b + ConsoleStyle.BL + ConsoleStyle.H.repeat(inner) + ConsoleStyle.BR + r);
    }

    public static void seperator(String label, int width, String color) {
        String b = color + ConsoleStyle.BOLD, r = ConsoleStyle.RESET;
        if (label == null || label.isEmpty()) { System.out.println(b + ConsoleStyle.H.repeat(width) + r); return; }
        String visibleLabel = " " + label + " ";
        int remaining = width - visibleLabel.length();
        int left = remaining/2, right = remaining - left;
        System.out.println(b + ConsoleStyle.H.repeat(left) + r + color + visibleLabel + r + b + ConsoleStyle.H.repeat(right) + r);
    }

    public static void blankLine() { System.out.println(); }

    public static String prompt(String promptText, String promptColor) {
        blankLine();
        type(promptColor + ConsoleStyle.BOLD + " " + promptText + ConsoleStyle.RESET, 55);
        System.out.print(" " + promptColor + ConsoleStyle.BOLD + "❯ " + ConsoleStyle.RESET);
        System.out.flush();
        String input = scanner.nextLine().trim();
        blankLine();
        return input;
    }

   public static void pressEnterToContinue() {
        System.out.print(" " + ConsoleStyle.DIM + "[ press enter to continue ]" + ConsoleStyle.RESET);
        System.out.flush();
        scanner.nextLine();
        blankLine();
   }

    public static boolean confirm(String question, String color) {
        String response = prompt(question + " (y/n", color);
        return response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes");
    }

    // INTERNAL HELPERS
    static String padRight(String s, int width) {
        String visible = s.replaceAll("\033\\[[;\\d]*m", "");
        int pad = Math.max(0, width - visible.length());
        return s + " ".repeat(pad);
    }

    static String centerPad(String s, int width) {
        String visible = s.replaceAll("\033\\[[;\\d]*m", "");
        int total = Math.max(0, width - visible.length());
        int left = total / 2, right = total - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

}
