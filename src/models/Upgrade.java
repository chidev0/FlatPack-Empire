package models;

import upgrades.UpgradeType;
import engine.SimConfig;

public class Upgrade {
public void upgrade(UpgradeType model) {
    if (model == UpgradeType.TRUCK) {
    SimConfig.setCurrentTruckTier(SimConfig.CURRENT_TRUCK_TIER++);
    return;
    } else if (model == UpgradeType.CHECKOUT_LANE) {
        SimConfig.setCurrentCheckoutLanes(SimConfig.CURRENT_CASHIER_LANES++);
        return;
    }
    throw new RuntimeException("Illegal UpgradeType");
}
}
