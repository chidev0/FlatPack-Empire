package engine;

import engine.commands.ApplicationStatus;
import engine.commands.CommandQueue;
import ui.ConsoleNarrator;
import ui.MainMenuRenderer;

public class ApplicationRuntime {
    private GameEngine gameEngine;
    private GameState gameState;
    private CommandQueue queue;
    private ConsoleNarrator consoleNarrator;
    private ApplicationStatus applicationStatus;
    private boolean isInitializing;
    private MainMenuRenderer mainMenu;

    public ApplicationRuntime(GameEngine gameEngine, CommandQueue queue, ConsoleNarrator consoleNarrator, GameState gameState, MainMenuRenderer mainMenu) {
        this.gameEngine = gameEngine;
        this.gameState = gameState;
        this.queue = queue;
        this.consoleNarrator = consoleNarrator;
        this.applicationStatus = ApplicationStatus.INITIALIZING;
        this.isInitializing = true;
        this.mainMenu = mainMenu;
    }

    public void run() {
        if (isInitializing) {
            this.applicationStatus = ApplicationStatus.MAIN_MENU;
            mainMenu.load();

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
