package ui;

import exceptions.InvalidCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputListener implements Runnable{
    private final BufferedReader commandListener = new BufferedReader(new InputStreamReader(System.in));
    private static boolean processingCommand = false;
    private CommandParser parser;

    public InputListener(CommandParser parser) {
        this.parser = parser;
    }

    public void run() {
        while (true) {
            if (processingCommand) continue;
            String command = null;
            try {
                command = commandListener.readLine();
                processingCommand = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                parser.parseAndExecute(command);
            } catch (InvalidCommandException _) {
                System.out.println("Invalid Command");
            } finally {
                processingCommand = false;
            }
        }
    }

    public static boolean isProcessingCommand() {
        return processingCommand;
    }

    public static void setProcessingCommand(boolean processingCommand) {
        InputListener.processingCommand = processingCommand;
       // System.out.println("Processing command");
    }
}
