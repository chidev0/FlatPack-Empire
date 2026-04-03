package engine.commands;
import engine.GameState;
import engine.Tickable;
import structures.ArrayStack;

public class CommandQueue implements Tickable {
    private ArrayStack<String[]> commandQueue = new ArrayStack<>();
    private Command handler = new Command();

    public void add(String[] command) {
        commandQueue.push(command);
    }

    public void executeCommand() {
        String[] command = commandQueue.pop();
        handler.execute(command);
    }

    public void tick(GameState state) {
        if (!commandQueue.isEmpty()) {
            executeCommand();
        }
    }
}
