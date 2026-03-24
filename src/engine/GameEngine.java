package engine;

public class GameEngine {
    public GameState Scottsville = new GameState();
    public GameClock Time = new GameClock(Scottsville);

    public void advance() {
        while (Scottsville.getSimulationStatus() == GameStatus.RUNNING) {
            tick();
            try {
                Thread.sleep(Scottsville.getCurrentSimulationSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void tick() {
        Time.advance();
    }




}
