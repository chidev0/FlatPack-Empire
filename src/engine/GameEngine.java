package engine;

import core.DamagesManager;
import core.InventoryManager;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private final GameState scottsville;
    private final InventoryManager manager;
    private final DamagesManager damages;
    private final GameClock time;
    List<Tickable> gameSystems = new ArrayList<>();
    List<Tickable> pausedSystems = new ArrayList<>();

    public GameEngine(GameState gameState, InventoryManager manager, DamagesManager damages, GameClock time) {
        scottsville = gameState;
        this.manager = manager;
        this.damages = damages;
        this.time = time;
    }

    public void registerSystem(Tickable system) {
        gameSystems.add(system);
    }

    // Debugging method for Pausing system
    public boolean pauseSystem(Tickable system) {
        for (int i = 0; i < gameSystems.size(); i++){
            if (gameSystems.get(i) == system) {
                pausedSystems.add(system);
                gameSystems.remove(system);
                return true;
            }
        }
        return false;
    }

    // Debugging method for resuming system
    public boolean resumeSystem(Tickable system) {
        for (int i = 0; i < pausedSystems.size(); i++){
            if (pausedSystems.get(i) == system) {
                gameSystems.add(system);
                pausedSystems.remove(system);
                return true;
            }
        }
        return false;
    }

    public void advance() {
        while (scottsville.getSimulationStatus() == GameStatus.RUNNING) {
            tick();
            try {
                Thread.sleep(scottsville.getCurrentSimulationSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void tick() {
        time.advance();
        for (Tickable i : gameSystems) {
            i.tick(scottsville);
        }
    }




}
