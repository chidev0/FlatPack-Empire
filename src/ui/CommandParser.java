package ui;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    String[] allowedCommands = {"buy", "info"};
    boolean commandFound = false;
    public void parseAndExecute(String input) {
        String[] command = input.split(" ", 2);
        for (String comm : allowedCommands) {
            if (comm.equals(command[0])) {
                commandFound = true;
                // Code to execute command
            }
        }
        if (!commandFound) {
            throw new RuntimeException("Invalid Command");
        }
    }
}
