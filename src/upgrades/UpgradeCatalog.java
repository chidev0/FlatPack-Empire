package upgrades;

import models.Upgrade;

import java.math.BigDecimal;

public class UpgradeCatalog {
    public static Upgrade truckTierOne = new Upgrade("Truck Tier One", UpgradeType.TRUCK, BigDecimal.valueOf(0), UpgradePrerequisite.NONE, true, 15, 50);
    public static Upgrade truckTierTwo = new Upgrade("Truck Tier Two", UpgradeType.TRUCK, BigDecimal.valueOf(10500.00), UpgradePrerequisite.TRUCK_TIER_ONE, false, 7.5, 100);

}
