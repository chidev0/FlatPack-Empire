package engine.commands;

import engine.commands.core.TimeCommand;
import engine.commands.store.InfoCommand;
import ui.UIState;

public class CommandBootstrapper {
    private static UIState uiState;

    public CommandBootstrapper(UIState uiState) {
        CommandBootstrapper.uiState = uiState;
    }

    public static CommandRegistry createDefaultRegistry() {
        CommandRegistry registry = new CommandRegistry();
        registry.add(new TimeCommand());
        registry.add(new InfoCommand(uiState));
        return registry;
    }
}
