package engine;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private GameState scottsville;
    private GameClock Time = new GameClock(scottsville);
    List<Tickable> gameSystems = new ArrayList<>();

    public GameEngine(GameState gameState) {
        this.scottsville = gameState;
    }

    public void registerSystem(Tickable system) {
        gameSystems.add(system);
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
        Time.advance();
        for (Tickable i : gameSystems) {
            i.tick(scottsville);
        }
    }




}
