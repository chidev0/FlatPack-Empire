package engine;

import core.DamagesManager;
import core.InventoryManager;

public interface Tickable {
    public void tick(GameState state);
}
