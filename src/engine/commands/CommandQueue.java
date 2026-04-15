package engine.commands;
import engine.GameState;
import engine.Tickable;
import structures.ArrayQueue;
import ui.UIState;

public class CommandQueue implements Tickable {

    private UIState uiState;
    private CommandDispatcher commandExecuter;

    public CommandQueue(UIState uiState) {
        this.uiState = uiState;
        this.commandExecuter = new CommandDispatcher(uiState);
    }

    private ArrayQueue<String[]> commandPipeline = new ArrayQueue<>();

    public void add(String[] command) {
        commandPipeline.enqueue(command);
    }

    public void executeCommand() {
        try {
            //System.out.println("Attempting to dequeue command");
            String[] command = commandPipeline.dequeue();
            //System.out.println("Command dequeued, attempting execute");
            commandExecuter.execute(command);
            //System.out.println("Executed command");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        }

    public void tick(GameState state) {
        if (!commandPipeline.isEmpty()) {
            try {
                executeCommand();
                //System.out.println("Command executed");
            } catch (RuntimeException e) {
                System.out.println("Ran into an error running that command.");
            }
        }
    }
}
