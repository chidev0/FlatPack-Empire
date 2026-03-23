package models;

import engine.GameState;
import upgrades.UpgradeType;
import engine.SimConfig;

public class Upgrade {
public void upgrade(UpgradeType model) {
    if (model == UpgradeType.TRUCK) {
    GameState.setCurrentTruckTier(GameState.getCurrentTruckTier() + 1);
    return;
    } else if (model == UpgradeType.CHECKOUT_LANE) {
        GameState.setCurrentCheckoutLanes(GameState.getCurrentCheckoutLanes() + 1);
        return;
    }
    throw new RuntimeException("Illegal UpgradeType");
}
}
