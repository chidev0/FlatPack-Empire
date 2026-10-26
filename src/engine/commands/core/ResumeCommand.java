package engine.commands.core;

import engine.GameState;
import engine.GameStatus;
import engine.commands.Command;
import engine.commands.CommandContext;

public class ResumeCommand implements Command {

    private GameState state;

    public ResumeCommand(GameState state) {
        this.state = state;
    }

    @Override
    public String name() { return "resume";}

    @Override
    public String description() {
        return "Resumes the simulation";
    }

    @Override
    public String execute(CommandContext context, String[] args) {
        if (context.getStatus() != GameStatus.RUNNING) {
            state.setSimulationStatus(GameStatus.RUNNING);
            return " ";
        } throw new RuntimeException("Game is already running");
    }

    @Override
    public CommandContext parseArgs(String[] args) {
        return new CommandContext(state.getSimulationStatus());
    }

}
