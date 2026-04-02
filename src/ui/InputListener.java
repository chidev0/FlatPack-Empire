package ui;

import engine.GameState;
import engine.Tickable;
import java.util.Scanner;

public class InputListener {
    private final Scanner commandListener = new Scanner(System.in);
    private static boolean waitingForResponse = false;
    private CommandParser parser;

    public InputListener(CommandParser parser) {
        this.parser = parser;
    }

    public void nextInput() {
        if (waitingForResponse) return;
        String command = commandListener.nextLine();
        waitingForResponse = true;
        parser.parseAndExecute(command);
    }

    public static boolean isWaitingForResponse() {
        return waitingForResponse;
    }
}
