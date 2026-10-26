package engine;

import engine.commands.ApplicationStatus;
import engine.commands.CommandQueue;
import ui.ConsoleNarrator;

public class ApplicationRuntime {
    private GameEngine gameEngine;
    private GameState gameState;
    private CommandQueue queue;
    private ConsoleNarrator consoleNarrator;
    private ApplicationStatus applicationStatus;
    private boolean isInitializing;

    public ApplicationRuntime(GameEngine gameEngine, CommandQueue queue, ConsoleNarrator consoleNarrator, GameState gameState) {
        this.gameEngine = gameEngine;
        this.gameState = gameState;
        this.queue = queue;
        this.consoleNarrator = consoleNarrator;
        this.applicationStatus = ApplicationStatus.INITIALIZING;
        this.isInitializing = true;
    }

    public void run() {
        if (isInitializing) {
            this.applicationStatus = ApplicationStatus.RUNNING;
            this.isInitializing = false;
        }
        while (applicationStatus != ApplicationStatus.SHUTTING_DOWN) {
            if (gameState.getSimulationStatus() == GameStatus.RUNNING) {
            gameEngine.stepSimulation();
            }
            consoleNarrator.tick(gameState);
            queue.tick(gameState);
            try {
                Thread.sleep(gameState.getCurrentSimulationSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public void processInput() {

    }

    public void update() {
    }

    public void shutdown() {
        this.applicationStatus = ApplicationStatus.SHUTTING_DOWN;
    }

}
