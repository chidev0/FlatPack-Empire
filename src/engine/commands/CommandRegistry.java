package engine.commands;

import engine.commands.core.TimeCommand;
import exceptions.InvalidCommandException;

import java.util.ArrayList;

public class CommandRegistry {
     ArrayList<Command> commandList = new ArrayList<>();


    public void add(Command command) {
        commandList.add(command);
    }

    public boolean commandFound(String commandName) {
        for (Command command : commandList) {
            if (commandName.equalsIgnoreCase(command.name())) {
                return true;
            }
        }
        return false;
    }

    public Command getCommand(String name) {
        for (Command command : commandList) {
            if (command.name().equals(name)) {
                return command;
            }
        }
        throw new InvalidCommandException("Registry failed to fetch command");
    }
}
