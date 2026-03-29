package upgrades;

import engine.GameState;
import models.Product;
import structures.ArrayStack;

public class UpgradeManager {

    GameState GameState;

    public UpgradeManager(GameState GameState) {
        this.GameState = GameState;
    }

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

    public void setTruckCapacity() {}

    public void upgradeTruck(ArrayStack<Product> truck, int tier) {

    }

    public static int getTruckCapacity() {
        if (UpgradeCatalog.truckTierTwo.isUnlocked()) {
            return UpgradeCatalog.truckTierTwo.getCapacity();
        }
        return UpgradeCatalog.truckTierOne.getCapacity();
    }

    public void setTruckDamageChance() {}

    public static double getTruckDamageChance() {
        if (UpgradeCatalog.truckTierTwo.isUnlocked()) {
            return UpgradeCatalog.truckTierTwo.getDamageChance();
        }
        return UpgradeCatalog.truckTierOne.getDamageChance();
    }

    public static int getTruckTier() {
        if (UpgradeCatalog.truckTierTwo.isUnlocked()) {
            return 2;
        }
        return 1;
    }
}
