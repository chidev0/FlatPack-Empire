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
                commandFound = true;
                queue.add(command);
                break;
            }
        }
        if (!commandFound) {
            throw new InvalidCommandException("Invalid");
        }
    }
}
