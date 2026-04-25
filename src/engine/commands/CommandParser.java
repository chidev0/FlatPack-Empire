package engine.commands;

import exceptions.InvalidCommandException;
import ui.EventBuffer;

public class CommandParser {
    CommandQueue queue;
    EventBuffer eventBuffer;
    CommandRegistry commandRegistry;

    public CommandParser(CommandQueue queue, EventBuffer eventBuffer, CommandRegistry registry) {
        this.eventBuffer = eventBuffer;
        this.queue = queue;
        this.commandRegistry = registry;
    }

    public void parseAndExecute(String input) {
        String[] commandArray = input.split(" ");
        String commandName = commandArray[0];
        if (commandRegistry.commandFound(commandName)) {
            try {
                // eventBuffer.enqueueEvent("Added " +commandName + "to the command queue" );
                queue.add(commandArray);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new InvalidCommandException("Invalid");
        }
    }
}
