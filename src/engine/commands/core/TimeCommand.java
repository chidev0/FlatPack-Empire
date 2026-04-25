package engine.commands.core;

import engine.GameClock;
import engine.commands.Command;
import engine.commands.CommandContext;
import exceptions.InvalidCommandArgsException;

public class TimeCommand implements Command {
    @Override
    public String name() {
        return "time";
    }

    @Override
    public String description() {
        return "Provides store time";
    }

    @Override
    public String execute(CommandContext context, String[] args) {
        // To do
        return "";
    }

    @Override
    public CommandContext parseArgs(String[] args) throws InvalidCommandArgsException {
        return new CommandContext(GameClock.getCurrentTime(true));
    }
}
