package engine;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private final GameState scottsville;
    private final GameClock time;
    List<Tickable> gameSystems = new ArrayList<>();

    public GameEngine(GameState gameState) {
        scottsville = gameState;
        this.time = new GameClock(scottsville);
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
        time.advance();
        for (Tickable i : gameSystems) {
            i.tick(scottsville);
        }
    }




}
