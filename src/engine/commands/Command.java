package engine.commands;

import exceptions.InvalidCommandArgsException;

public interface Command {
     String name();
     String description();
     String execute(CommandContext context, String[] args);
     CommandContext parseArgs(String[] args) throws InvalidCommandArgsException;
}
