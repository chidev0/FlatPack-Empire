package engine.commands;

import core.CheckoutManager;
import engine.GameClock;
import engine.commands.core.TimeCommand;
import exceptions.InvalidCommandArgsException;
import exceptions.InvalidCommandException;
import models.Product;
import products.ProductCatalog;
import ui.*;

public class CommandDispatcher {
    private UIState uiState;
    CommandRegistry commandRegistry;
    private EventBuffer eventBuffer;

    public CommandDispatcher(UIState uiState, CommandRegistry commandRegistry, EventBuffer eventBuffer) {
        this.uiState = uiState;
        this.commandRegistry = commandRegistry;
        this.eventBuffer = eventBuffer;

    }

    public void executeN(String[] args) {
        Command command = commandRegistry.getCommand(args[0]);
        try {
            CommandContext context = command.parseArgs(args);
            // eventBuffer.enqueueEvent("Built context for " + command.name() + " attempting execute.");
            command.execute(context, args);
            // eventBuffer.enqueueEvent("Command executed.");
        } catch (InvalidCommandArgsException e) {
            throw new RuntimeException(e);
        }
    }

}
