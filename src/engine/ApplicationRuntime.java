package engine;

import engine.commands.ApplicationStatus;
import engine.commands.CommandQueue;
import ui.ConsoleNarrator;
import ui.InputListener;
import ui.MainMenuRenderer;
import ui.ScreenRenderer;

public class ApplicationRuntime {
    private GameEngine gameEngine;
    private GameState gameState;
    private CommandQueue queue;
    private ScreenRenderer screenRenderer;
    private ApplicationStatus applicationStatus;
    private boolean isInitializing;
    private MainMenuRenderer mainMenu;
    private Boolean awaitingInput;
    private InputListener inputListener;

    public ApplicationRuntime(GameEngine gameEngine, CommandQueue queue, ScreenRenderer screenRenderer, GameState gameState, MainMenuRenderer mainMenu, InputListener inputListener) {
        this.gameEngine = gameEngine;
        this.gameState = gameState;
        this.queue = queue;
        this.screenRenderer = screenRenderer;
        this.applicationStatus = ApplicationStatus.INITIALIZING;
        this.isInitializing = true;
        this.mainMenu = mainMenu;
        this.awaitingInput = false;
        this.inputListener = inputListener;
    }

    public void run() {
        while (isInitializing) {
            if (!awaitingInput) {
                this.applicationStatus = ApplicationStatus.MAIN_MENU;
                mainMenu.load();
                this.awaitingInput = true;
            } if (inputListener.getAwaitScreenRender()) {
                this.isInitializing = false;
            }
        }
        while (applicationStatus != ApplicationStatus.SHUTTING_DOWN) {
            if (gameState.getSimulationStatus() == GameStatus.RUNNING) {
            gameEngine.stepSimulation();
            }
            screenRenderer.renderFrame();
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

    public void transitionGameState() {
        this.isInitializing = false;
    }

}
