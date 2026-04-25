package ui;

import engine.SimConfig;
import engine.commands.CommandParser;
import exceptions.InvalidCommandException;

import java.io.IOException;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class InputListener implements Runnable{
    private Terminal terminal = TerminalBuilder.builder().system(true).build();
    private NonBlockingReader reader = terminal.reader();
    private static boolean processingCommand = false;
    private CommandParser parser;
    private StringBuilder currentInput = new StringBuilder();
    private Attributes original;

    public InputListener(CommandParser parser) throws IOException {
        this.parser = parser;
    }

    public void run() {
        try {
            Attributes original = terminal.enterRawMode();
            while (true) {
                if (processingCommand) continue;
                try {
                    int ch = reader.read(SimConfig.SIMULATION_SPEED);
                    if (ch == -2) continue;
                    else if (ch == '\r' || ch == '\n') {
                        processingCommand = true;
                    } else if (ch == -1) break;
                    else if (ch == 127 || ch == 8) {
                        if (!currentInput.isEmpty()) {
                            currentInput.setLength(currentInput.length() - 1);
                        }
                    } else {
                        currentInput.append((char) ch);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (processingCommand) {
                        String command = currentInput.toString();
                        currentInput.setLength(0);
                        parser.parseAndExecute(command);
                    }
                } catch (InvalidCommandException _) {
                    System.out.println("Invalid Command");
                } finally {
                    processingCommand = false;
                }
            }

        } finally {
            terminal.setAttributes(original);
        }
        try {
            terminal.close();
        } catch (IOException ignored) {}
    }

    public static boolean isProcessingCommand() {
        return processingCommand;
    }

    public static void setProcessingCommand(boolean processingCommand) {
        InputListener.processingCommand = processingCommand;
       // System.out.println("Processing command");
    }

    public StringBuilder getCurrentInput() {
        return currentInput;
    }
}
