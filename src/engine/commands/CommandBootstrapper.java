package engine.commands;

import core.CheckoutManager;
import engine.GameState;
import engine.commands.core.PauseCommand;
import engine.commands.core.ResumeCommand;
import engine.commands.core.TimeCommand;
import engine.commands.store.InfoCommand;
import engine.commands.store.LaneCommand;
import ui.EventBuffer;
import ui.UIState;

public class CommandBootstrapper {
    private static UIState uiState;
    private static GameState gameState;
    private static CheckoutManager checkoutManager;
    private static EventBuffer eventBuffer;

    public CommandBootstrapper(UIState uiState, GameState gameState, CheckoutManager checkoutManager, EventBuffer eventBuffer) {
        CommandBootstrapper.uiState = uiState;
        CommandBootstrapper.gameState = gameState;
        CommandBootstrapper.eventBuffer = eventBuffer;
        CommandBootstrapper.checkoutManager = checkoutManager;
    }

    public static CommandRegistry createDefaultRegistry() {
        CommandRegistry registry = new CommandRegistry();
        registry.add(new TimeCommand());
        registry.add(new InfoCommand(uiState));
        registry.add(new LaneCommand(uiState, gameState, checkoutManager, eventBuffer));
        registry.add(new PauseCommand(gameState));
        registry.add(new ResumeCommand(gameState));
        return registry;
    }
}
