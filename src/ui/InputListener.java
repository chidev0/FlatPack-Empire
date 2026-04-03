package ui;

import engine.GameState;
import engine.Tickable;
import exceptions.InvalidCommandException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputListener implements Runnable{
    private final BufferedReader commandListener = new BufferedReader(new InputStreamReader(System.in));
    private boolean waitingForResponse = false;
    private CommandParser parser;

    public InputListener(CommandParser parser) {
        this.parser = parser;
    }

    public void run() {
        while (true) {
            if (waitingForResponse) continue;
            String command = null;
            try {
                command = commandListener.readLine();
                waitingForResponse = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                parser.parseAndExecute(command);
            } catch (InvalidCommandException _) {
                System.out.println("Invalid Command");
            } finally {
                waitingForResponse = false;
            }
        }
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    public void setWaitingForResponse(boolean waitingForResponse) {
        this.waitingForResponse = waitingForResponse;
    }
}
