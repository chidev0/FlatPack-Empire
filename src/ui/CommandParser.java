package ui;

import exceptions.InvalidCommandException;
import engine.commands.CommandQueue;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    String[] allowedCommands = {"buy", "info"};
    boolean commandFound = false;
    CommandQueue queue;

    public CommandParser(CommandQueue queue) {
        this.queue = queue;
    }

    public void parseAndExecute(String input) {
        String[] command = input.split(" ");
        for (String comm : allowedCommands) {
            if (comm.equals(command[0])) {
                System.out.println("Command entered matches " + comm );
                commandFound = true;
                try {
                    queue.add(command);
                    System.out.println("Successfully added to queue");
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        if (!commandFound) {
            throw new InvalidCommandException("Invalid");
        }
        commandFound = false;
    }
}
