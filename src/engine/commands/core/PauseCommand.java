package engine.commands.core;

import engine.GameState;
import engine.GameStatus;
import engine.commands.Command;
import engine.commands.CommandContext;

public class PauseCommand implements Command {

    private GameState state;

    public PauseCommand(GameState state) {
        this.state = state;
    }

    @Override
    public String name() { return "pause";}

    @Override
    public String description() {
        return "Pauses the simulation";
    }

    @Override
    public String execute(CommandContext context, String[] args) {
        if (context.getStatus() != GameStatus.PAUSED) {
            state.setSimulationStatus(GameStatus.PAUSED);
            return " ";
        } throw new RuntimeException("Game is already paused");
    }

    @Override
    public CommandContext parseArgs(String[] args) {
        return new CommandContext(state.getSimulationStatus());
    }

}
